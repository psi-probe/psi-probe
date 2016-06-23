/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.controllers.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import psiprobe.controllers.TomcatContainerController;

/**
 * Lets an user to copy a single file to a deployed context.
 * 
 * @author Sandra Pascual
 */
public class CopySingleFileController extends TomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(CopySingleFileController.class);

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    List<Context> apps;
    try {
      apps = getContainerWrapper().getTomcatContainer().findContexts();
    } catch (NullPointerException ex) {
      throw new IllegalStateException("No container found for your server: "
          + getServletContext().getServerInfo(), ex);
    }

    List applications = new ArrayList();
    for (Context appContext : apps) {
      // check if this is not the ROOT webapp
      if (appContext.getName() != null && appContext.getName().trim().length() > 0) {
        Map<String, String> app = new HashMap<String, String>();
        app.put("value", appContext.getName());
        app.put("label", appContext.getName());
        applications.add(app);
      }
    }
    request.setAttribute("apps", applications);

    if (FileUploadBase.isMultipartContent(new ServletRequestContext(request))) {

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
      upload.setHeaderEncoding("UTF8");
      try {
        List<FileItem> fileItems = upload.parseRequest(request);
        for (FileItem fi : fileItems) {
          if (!fi.isFormField()) {
            if (fi.getName() != null && fi.getName().length() > 0) {
              tmpFile =
                  new File(System.getProperty("java.io.tmpdir"),
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
        request.setAttribute(
            "errorMessage",
            getMessageSourceAccessor().getMessage("probe.src.deploy.file.uploadfailure",
                new Object[] {e.getMessage()}));
        if (tmpFile != null && tmpFile.exists()) {
          tmpFile.delete();
        }
        tmpFile = null;
      }

      String errMsg = null;

      if (tmpFile != null) {
        try {
          if (tmpFile.getName() != null && tmpFile.getName().trim().length() > 0) {

            contextName = getContainerWrapper().getTomcatContainer().formatContextName(contextName);

            String visibleContextName = "".equals(contextName) ? "/" : contextName;
            request.setAttribute("contextName", visibleContextName);

            // Check if context is already deployed
            if (getContainerWrapper().getTomcatContainer().findContext(contextName) != null) {

              File destFile =
                  new File(getContainerWrapper().getTomcatContainer().getAppBase(), contextName
                      + where);
              // Checks if the destination path exists

              if (destFile.exists()) {
                if (!destFile.getAbsolutePath().contains("..")) {
                  // Copy the file overwriting it if it
                  // already exists
                  FileUtils.copyFileToDirectory(tmpFile, destFile);

                  request.setAttribute("successFile", Boolean.TRUE);
                  Context context =
                      getContainerWrapper().getTomcatContainer().findContext(contextName);
                  // Checks if DISCARD "work" directory is
                  // selected
                  if (discard) {
                    getContainerWrapper().getTomcatContainer().discardWorkDir(context);
                  }
                  // Checks if RELOAD option is selected
                  if (reload) {

                    if (context != null) {
                      context.reload();
                      request.setAttribute("reloadContext", Boolean.TRUE);
                    }
                  }
                } else {
                  errMsg =
                      getMessageSourceAccessor().getMessage("probe.src.deploy.file.pathNotValid");
                }
              } else {
                errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notPath");
              }
            } else {
              errMsg =
                  getMessageSourceAccessor().getMessage("probe.src.deploy.file.notExists",
                      new Object[] {visibleContextName});
            }
          } else {
            errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notFile.failure");
          }
        } catch (Exception e) {
          errMsg =
              getMessageSourceAccessor().getMessage("probe.src.deploy.file.failure",
                  new Object[] {e.getMessage()});
          logger.error("Tomcat throw an exception when trying to deploy", e);
        } finally {
          if (errMsg != null) {
            request.setAttribute("errorMessage", errMsg);
          }
          tmpFile.delete();
        }
      }
    }
    return new ModelAndView(new InternalResourceView(getViewName()));
  }
}
