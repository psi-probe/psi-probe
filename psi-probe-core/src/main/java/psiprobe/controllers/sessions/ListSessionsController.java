/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.sessions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractContextHandlerController;
import psiprobe.model.ApplicationSession;
import psiprobe.model.Attribute;
import psiprobe.model.SessionSearchInfo;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.SecurityUtils;

/**
 * Creates the list of sessions for a particular web application or all web applications if a webapp
 * request parameter is not set.
 */
@Controller
public class ListSessionsController extends AbstractContextHandlerController {

  @RequestMapping(path = "/sessions.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
        && SecurityUtils.hasAttributeValueRole(getServletContext(), request);

    SessionSearchInfo searchInfo = new SessionSearchInfo();
    searchInfo.setSearchAction(StringUtils.trimToNull(ServletRequestUtils
        .getStringParameter(request, "searchAction", SessionSearchInfo.ACTION_NONE)));
    HttpSession sess = request.getSession(false);

    if (searchInfo.isApply()) {
      searchInfo.setSessionId(StringUtils
          .trimToNull(ServletRequestUtils.getStringParameter(request, "searchSessionId")));
      searchInfo.setLastIp(
          StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchLastIP")));

      searchInfo.setAgeFrom(
          StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchAgeFrom")));
      searchInfo.setAgeTo(
          StringUtils.trimToNull(ServletRequestUtils.getStringParameter(request, "searchAgeTo")));
      searchInfo.setIdleTimeFrom(StringUtils
          .trimToNull(ServletRequestUtils.getStringParameter(request, "searchIdleTimeFrom")));
      searchInfo.setIdleTimeTo(StringUtils
          .trimToNull(ServletRequestUtils.getStringParameter(request, "searchIdleTimeTo")));
      searchInfo.setAttrName(StringUtils
          .trimToNull(ServletRequestUtils.getStringParameter(request, "searchAttrName")));
      if (sess != null) {
        sess.setAttribute(SessionSearchInfo.SESS_ATTR_NAME, searchInfo);
      }
    } else if (sess != null) {
      if (searchInfo.isClear()) {
        sess.removeAttribute(SessionSearchInfo.SESS_ATTR_NAME);
      } else {
        SessionSearchInfo ss =
            (SessionSearchInfo) sess.getAttribute(SessionSearchInfo.SESS_ATTR_NAME);
        if (ss != null) {
          searchInfo = ss;
        }
      }
    }

    // context is not specified we'll retrieve all sessions of the container

    List<Context> ctxs;
    if (context == null) {
      ctxs = getContainerWrapper().getTomcatContainer().findContexts();
    } else {
      ctxs = new ArrayList<>();
      ctxs.add(context);
    }

    List<ApplicationSession> sessionList = new ArrayList<>();
    for (Context ctx : ctxs) {
      if (ctx != null && ctx.getManager() != null
          && (!searchInfo.isApply() || searchInfo.isUseSearch())) {
        Session[] sessions = ctx.getManager().findSessions();
        for (Session session : sessions) {
          ApplicationSession appSession =
              ApplicationUtils.getApplicationSession(session, calcSize, searchInfo.isUseAttr());
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

  /**
   * Populate search messages.
   *
   * @param searchInfo the search info
   */
  private void populateSearchMessages(SessionSearchInfo searchInfo) {
    MessageSourceAccessor msa = getMessageSourceAccessor();

    searchInfo.getErrorMessages().clear();

    if (searchInfo.isEmpty()) {
      searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.empty"));
    } else if (searchInfo.isValid()) {
      searchInfo.setInfoMessage(msa.getMessage("probe.src.sessions.search.results.empty"));
    } else {
      if (!searchInfo.isSessionIdValid()) {
        searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.sessionId",
            new Object[] {searchInfo.getSessionIdMsg()}));
      }
      if (!searchInfo.isAttrNameValid()) {
        for (String message : searchInfo.getAttrNameMsgs()) {
          searchInfo.addErrorMessage(
              msa.getMessage("probe.src.sessions.search.invalid.attrName", new Object[] {message}));
        }
      }
      if (!searchInfo.isAgeFromValid()) {
        searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.ageFrom"));
      }
      if (!searchInfo.isAgeToValid()) {
        searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.ageTo"));
      }
      if (!searchInfo.isIdleTimeFromValid()) {
        searchInfo
            .addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.idleTimeFrom"));
      }
      if (!searchInfo.isIdleTimeToValid()) {
        searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid.idleTimeTo"));
      }
      if (searchInfo.getErrorMessages().isEmpty()) {
        searchInfo.addErrorMessage(msa.getMessage("probe.src.sessions.search.invalid"));
      }
    }
  }

  /**
   * Match session.
   *
   * @param appSession the app session
   * @param searchInfo the search info
   *
   * @return true, if successful
   */
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
        sessionMatches =
            appSession.getIdleTime() >= searchInfo.getIdleTimeFromSec().longValue() * 1000;
      }
      if (sessionMatches && searchInfo.isUseIdleTimeTo()) {
        sessionMatches =
            appSession.getIdleTime() <= searchInfo.getIdleTimeToSec().longValue() * 1000;
      }
      if (searchInfo.isUseLastIp() && appSession.getLastAccessedIp() != null) {
        sessionMatches = appSession.getLastAccessedIp().contains(searchInfo.getLastIp());
      }

      if (sessionMatches && searchInfo.isUseAttrName()) {
        boolean attrMatches = false;
        List<Pattern> namePatterns = new ArrayList<>(searchInfo.getAttrNamePatterns());
        for (Attribute attr : appSession.getAttributes()) {
          String attrName = attr.getName();

          if (attrName != null) {
            for (Iterator<Pattern> it = namePatterns.iterator(); it.hasNext();) {
              if (it.next().matcher(attrName).matches()) {
                it.remove();
              }
            }

            if (namePatterns.isEmpty()) {
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

  @Override
  protected boolean isContextOptional() {
    return true;
  }

  @Value("sessions")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
