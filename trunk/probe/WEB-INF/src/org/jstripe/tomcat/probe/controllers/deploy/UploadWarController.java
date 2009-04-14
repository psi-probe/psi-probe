/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers.deploy;

import org.apache.catalina.Context;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.jstripe.tomcat.probe.controllers.TomcatContainerController;
import org.jstripe.tomcat.probe.controllers.jsp.DisplayJspController;
import org.jstripe.tomcat.probe.model.jsp.Summary;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.util.Iterator;

/**
 * Uploads and installs web application from a .WAR.
 * <p/>
 * Author: Vlad Ilyushchenko
 */
public class UploadWarController extends TomcatContainerController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (FileUpload.isMultipartContent(new ServletRequestContext(request))) {

            File tmpWar = null;
            String contextName = null;
            boolean update = false;
            boolean compile = false;
            boolean discard = false;

            //
            // parse multipart request and extract the file
            //
            FileItemFactory factory = new DiskFileItemFactory(1048000, new File(System.getProperty("java.io.tmpdir")));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(-1);
            upload.setHeaderEncoding("UTF8");
            try {
                for (Iterator it = upload.parseRequest(request).iterator(); it.hasNext();) {
                    FileItem fi = (FileItem) it.next();
                    if (!fi.isFormField()) {
                        if (fi.getName() != null && fi.getName().length() > 0) {
                            tmpWar = new File(System.getProperty("java.io.tmpdir"), new File(fi.getName()).getName());
                            fi.write(tmpWar);
                        }
                    } else if ("context".equals(fi.getFieldName())) {
                        contextName = fi.getString();
                    } else if ("update".equals(fi.getFieldName()) && "yes".equals(fi.getString())) {
                        update = true;
                    } else if ("compile".equals(fi.getFieldName()) && "yes".equals(fi.getString())) {
                        compile = true;
                    } else if ("discard".equals(fi.getFieldName()) && "yes".equals(fi.getString())) {
                        discard = true;
                    }
                }
            } catch (Exception e) {
                logger.fatal("Could not process file upload", e);
                request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.deploy.war.uploadfailure",
                        new Object[]{e.getMessage()}));
                if (tmpWar != null && tmpWar.exists()) tmpWar.delete();
                tmpWar = null;
            }

            String errMsg = null;

            if (tmpWar != null) {
                try {
                    if (tmpWar.getName().endsWith(".war")) {

                        if (contextName == null || contextName.length() == 0) {
                            String warFileName = tmpWar.getName().replaceAll("\\.war$", "");
                            contextName = warFileName.equals("ROOT") ? "" : "/" + warFileName;
                        }

                        if (!contextName.startsWith("/")) contextName = "/" + contextName;
                        contextName = contextName.trim();

                        if (contextName.equals("/")) contextName = "";

                        if (update && getContainerWrapper().getTomcatContainer().findContext(contextName) != null) {
                            logger.debug("updating "+contextName + ": removing the old copy");
                            getContainerWrapper().getTomcatContainer().remove(contextName);
                        }

                        if (getContainerWrapper().getTomcatContainer().findContext(contextName) == null) {
                            //
                            // copy the .war to tomcat application base dir
                            //
                            File destWar = new File(getContainerWrapper().getTomcatContainer().getAppBase(),
                                    (contextName.length() == 0 ? "ROOT" : contextName) + ".war");

                            tmpWar.renameTo(destWar);

                            //
                            // let Tomcat know that the file is there
                            //
                            getContainerWrapper().getTomcatContainer().installWar(contextName, new URL("jar:file:" + destWar.getAbsolutePath() + "!/"));

                            request.setAttribute("successMessage", getMessageSourceAccessor().getMessage("probe.src.deploy.war.success",
                                    new Object[]{contextName}));

                            Context ctx = getContainerWrapper().getTomcatContainer().findContext(contextName);
                            if (ctx == null) {
                                errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.notinstalled", new Object[]{contextName});
                            } else {
                                if (discard) getContainerWrapper().getTomcatContainer().discardWorkDir(ctx);
                                if (compile) {
                                    Summary summary = new Summary();
                                    summary.setName(ctx.getName());
                                    getContainerWrapper().getTomcatContainer().listContextJsps(ctx, summary, true);
                                    request.getSession(true).setAttribute(DisplayJspController.SUMMARY_ATTRIBUTE, summary);
                                    //
                                    // pass the name of the newly deployed context to the presentation layer
                                    // using this name the presentation layer can render a url to view compilation details
                                    //
                                    request.setAttribute("compiled_app", contextName);
                                }
                            }

                        } else {
                            errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.alreadyExists", new Object[]{contextName});
                        }
                    } else {
                        errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.notWar.failure");
                    }
                } catch (Exception e) {
                    errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.failure", new Object[]{e.getMessage()});
                    logger.error("Tomcat throw an exception when trying to deploy", e);
                } finally {
                    if (errMsg != null) request.setAttribute("errorMessage", errMsg);
                    tmpWar.delete();
                }
            }
        }
        return new ModelAndView(new InternalResourceView(getViewName()));
    }
}
