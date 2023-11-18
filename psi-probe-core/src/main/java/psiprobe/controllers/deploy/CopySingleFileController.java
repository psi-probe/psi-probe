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
package psiprobe.controllers.deploy;

import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.openejb.loader.Files;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import psiprobe.controllers.AbstractTomcatContainerController;

/**
 * Lets an user to copy a single file to a deployed context.
 */
@Controller
public class CopySingleFileController extends AbstractTomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(CopySingleFileController.class);

  @RequestMapping(path = "/adm/deployfile.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    // If not multi-part content, exit
    if (!ServletFileUpload.isMultipartContent(request)) {
      return new ModelAndView(new InternalResourceView(getViewName()));
    }

    List<Context> apps;
    try {
      apps = getContainerWrapper().getTomcatContainer().findContexts();
    } catch (NullPointerException ex) {
      throw new IllegalStateException(
          "No container found for your server: " + getServletContext().getServerInfo(), ex);
    }

    List<Map<String, String>> applications = new ArrayList<>();
    for (Context appContext : apps) {
      // check if this is not the ROOT webapp
      if (!Strings.isNullOrEmpty(appContext.getName())) {
        Map<String, String> app = new HashMap<>();
        app.put("value", appContext.getName());
        app.put("label", appContext.getName());
        applications.add(app);
      }
    }
    request.setAttribute("apps", applications);

    File tmpFile = null;
    String contextName = null;
    String where = null;
    boolean reload = false;
    boolean discard = false;

    // parse multipart request and extract the file
    FileItemFactory factory =
        new DiskFileItemFactory(1048000, new File(System.getProperty("java.io.tmpdir")));
    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setSizeMax(-1);
    upload.setHeaderEncoding(StandardCharsets.UTF_8.name());
    try {
      List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(request));
      for (FileItem fi : fileItems) {
        if (!fi.isFormField()) {
          if (fi.getName() != null && fi.getName().length() > 0) {
            tmpFile = new File(System.getProperty("java.io.tmpdir"),
                FilenameUtils.getName(fi.getName()));
            fi.write(tmpFile);
          }
        } else if ("context".equals(fi.getFieldName())) {
          contextName = fi.getString();
        } else if ("where".equals(fi.getFieldName())) {
          where = fi.getString();
        } else if ("reload".equals(fi.getFieldName()) && "yes".equals(fi.getString())) {
          reload = true;
        } else if ("discard".equals(fi.getFieldName()) && "yes".equals(fi.getString())) {
          discard = true;
        }
      }
    } catch (Exception e) {
      logger.error("Could not process file upload", e);
      request.setAttribute("errorMessage", getMessageSourceAccessor()
          .getMessage("probe.src.deploy.file.uploadfailure", new Object[] {e.getMessage()}));
      Files.delete(tmpFile);
      tmpFile = null;
    }

    String errMsg = null;

    if (tmpFile == null) {
      return new ModelAndView(new InternalResourceView(getViewName()));
    }

    try {
      if (!Strings.isNullOrEmpty(tmpFile.getName())) {

        contextName = getContainerWrapper().getTomcatContainer().formatContextName(contextName);

        String visibleContextName = "".equals(contextName) ? "/" : contextName;
        request.setAttribute("contextName", visibleContextName);

        // Check if context is already deployed
        if (getContainerWrapper().getTomcatContainer().findContext(contextName) != null) {

          File destFile = new File(getContainerWrapper().getTomcatContainer().getAppBase(),
              contextName + where);

          // Checks if the destination path exists
          if (destFile.exists()) {
            if (!destFile.getAbsolutePath().contains("..")) {
              // Copy the file overwriting it if it already exists
              FileUtils.copyFileToDirectory(tmpFile, destFile);

              request.setAttribute("successFile", Boolean.TRUE);
              // Logging action
              Authentication auth = SecurityContextHolder.getContext().getAuthentication();
              // get username logger
              String name = auth.getName();
              logger.info(getMessageSourceAccessor().getMessage("probe.src.log.copyfile"), name,
                  contextName);
              Context context =
                  getContainerWrapper().getTomcatContainer().findContext(contextName);
              // Checks if DISCARD "work" directory is selected
              if (discard) {
                getContainerWrapper().getTomcatContainer().discardWorkDir(context);
                logger.info(getMessageSourceAccessor().getMessage("probe.src.log.discardwork"),
                    name, contextName);
              }
              // Checks if RELOAD option is selected
              if (reload && context != null) {
                context.reload();
                request.setAttribute("reloadContext", Boolean.TRUE);
                logger.info(getMessageSourceAccessor().getMessage("probe.src.log.reload"), name,
                    contextName);
              }
            } else {
              errMsg =
                  getMessageSourceAccessor().getMessage("probe.src.deploy.file.pathNotValid");
            }
          } else {
            errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notPath");
          }
        } else {
          errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notExists",
              new Object[] {visibleContextName});
        }
      } else {
        errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notFile.failure");
      }
    } catch (IOException e) {
      errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.failure",
          new Object[] {e.getMessage()});
      logger.error("Tomcat threw an exception when trying to deploy", e);
    } finally {
      if (errMsg != null) {
        request.setAttribute("errorMessage", errMsg);
      }
      Files.delete(tmpFile);
    }
    return new ModelAndView(new InternalResourceView(getViewName()));
  }

  @Value("/adm/deploy.htm")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
