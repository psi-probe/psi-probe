/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.controllers.sessions;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import com.googlecode.psiprobe.model.ApplicationSession;
import com.googlecode.psiprobe.model.Attribute;
import com.googlecode.psiprobe.model.SessionSearchInfo;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.tools.SecurityUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Creates the list of sessions for a particular web application or all web
 * applications if a webapp request parameter is not set.
 * 
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 */
public class ListSessionsController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
                && SecurityUtils.hasAttributeValueRole(getServletContext(), request);

        SessionSearchInfo searchInfo = new SessionSearchInfo();
        searchInfo.setSearchAction(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchAction", SessionSearchInfo.ACTION_NONE)));
        HttpSession sess = request.getSession();

        if (searchInfo.isApply()) {
            searchInfo.setSessionId(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchSessionId")));
            searchInfo.setLastIP(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchLastIP")));

            searchInfo.setAgeFrom(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchAgeFrom")));
            searchInfo.setAgeTo(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchAgeTo")));
            searchInfo.setIdleTimeFrom(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchIdleTimeFrom")));
            searchInfo.setIdleTimeTo(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchIdleTimeTo")));
            searchInfo.setAttrName(StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchAttrName")));
            if (sess != null) {
                sess.setAttribute(SessionSearchInfo.SESS_ATTR_NAME, searchInfo);
            }
        } else if (sess != null) {
            if (searchInfo.isClear()) {
                sess.removeAttribute(SessionSearchInfo.SESS_ATTR_NAME);
            } else {
                SessionSearchInfo ss = (SessionSearchInfo) sess.getAttribute(SessionSearchInfo.SESS_ATTR_NAME);
                if (ss != null) {
                    searchInfo = ss;
                }
            }
        }

        // context is not specified we'll retrieve all sessions of the container

        List ctxs;
        if (context == null) {
            ctxs = getContainerWrapper().getTomcatContainer().findContexts();
        } else {
            ctxs = new ArrayList();
            ctxs.add(context);
        }

        List sessionList = new ArrayList();
        for (Iterator it = ctxs.iterator(); it.hasNext();) {
            Context ctx = (Context) it.next();
            if (ctx != null && ctx.getManager() != null && (!searchInfo.isApply() || searchInfo.isUseSearch())) {
                Session[] sessions = ctx.getManager().findSessions();
                for (int i = 0; i < sessions.length; i++) {
                    Session session = sessions[i];
                    ApplicationSession appSession = ApplicationUtils.getApplicationSession(
                            session, calcSize, searchInfo.isUseAttr());
                    if (appSession != null && matchSession(appSession, searchInfo)) {
                        if (ctx.getName() != null) {
                            appSession.setApplicationName(ctx.getName().length() > 0 ? ctx.getName() : "/");
                        }
                        sessionList.add(appSession);
                    }
                }
            }
        }

        if (sessionList.isEmpty() && searchInfo.isApply()) {
            synchronized (sess) {
                populateSearchMessages(searchInfo);
            }
        }

        ModelAndView modelAndView = new ModelAndView(getViewName(), "sessions", sessionList);
        modelAndView.addObject("searchInfo", searchInfo);

        return modelAndView;
    }

    private void populateSearchMessages(SessionSearchInfo searchInfo) {
        MessageSourceAccessor msa = getMessageSourceAccessor();

        searchInfo.getErrorMessages().clear();

        if (searchInfo.isEmpty()) {
            searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.empty"));
        } else if (searchInfo.isValid()) {
            searchInfo.setInfoMessage(msa.getMessage("probe.src.sessions.search.results.empty"));
        } else {
            if (!searchInfo.isSessionIdValid()) {
                searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.sessionId", new Object[] {searchInfo.getSessionIdMsg()}));
            }
            if (!searchInfo.isAttrNameValid()) {
                for (Iterator i = searchInfo.getAttrNameMsgs().iterator(); i.hasNext();) {
                    String m = (String) i.next();
                    searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.attrName", new Object[] {m}));
                }
            }
            if (!searchInfo.isAgeFromValid()) {
                searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.ageFrom"));
            }
            if (!searchInfo.isAgeToValid()) {
                searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.ageTo"));
            }
            if (!searchInfo.isIdleTimeFromValid()) {
                searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.idleTimeFrom"));
            }
            if (!searchInfo.isIdleTimeToValid()) {
                searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.idleTimeTo"));
            }
            if (searchInfo.getErrorMessages().isEmpty()) {
                searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid"));
            }
        }
    }

    private boolean matchSession(ApplicationSession appSession, SessionSearchInfo searchInfo) {
        boolean sessionMatches = true;
        if (searchInfo.isUseSearch()) {
            if (searchInfo.isUseSessionId() && appSession.getId() != null) {
                sessionMatches = searchInfo.getSessionIdPattern().matcher(appSession.getId()).matches();
            }
            if (sessionMatches && searchInfo.isUseAgeFrom()) {
                sessionMatches = appSession.getAge() >= searchInfo.getAgeFromSec().longValue() * 1000;
            }
            if (sessionMatches && searchInfo.isUseAgeTo()) {
                sessionMatches = appSession.getAge() <= searchInfo.getAgeToSec().longValue() * 1000;
            }
            if (sessionMatches && searchInfo.isUseIdleTimeFrom()) {
                sessionMatches = appSession.getIdleTime() >= searchInfo.getIdleTimeFromSec().longValue() * 1000;
            }
            if (sessionMatches && searchInfo.isUseIdleTimeTo()) {
                sessionMatches = appSession.getIdleTime() <= searchInfo.getIdleTimeToSec().longValue() * 1000;
            }
            if (searchInfo.isUseLastIP() && appSession.getLastAccessedIP() != null) {
                sessionMatches = appSession.getLastAccessedIP().indexOf(searchInfo.getLastIP()) > -1;
            }

            if (sessionMatches && searchInfo.isUseAttrName()) {
                boolean attrMatches = false;
                List a = new ArrayList();
                a.addAll(searchInfo.getAttrNamePatterns());

                for (Iterator i = appSession.getAttributes().iterator(); i.hasNext();) {
                    String attrName = ((Attribute) i.next()).getName();

                    if (attrName != null) {
                        for (Iterator j = a.iterator(); j.hasNext();) {
                            Pattern p = (Pattern) j.next();
                            if (p.matcher(attrName).matches()) {
                                j.remove();
                            }
                        }

                        if (a.isEmpty()) {
                            attrMatches = true;
                            break;
                        }
                    }
                }

                sessionMatches = attrMatches;
            }
        }

        return sessionMatches;
    }

    protected boolean isContextOptional() {
        return true;
    }
}
