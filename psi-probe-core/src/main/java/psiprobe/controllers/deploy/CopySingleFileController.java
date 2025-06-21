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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import psiprobe.controllers.AbstractTomcatContainerController;

/**
 * Uploads single file to a deployed context.
 */
@Controller
public class CopySingleFileController extends AbstractTomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(CopySingleFileController.class);

  @GetMapping("/adm/deployfile.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return new ModelAndView(new InternalResourceView(getViewName()));
  }

  @PostMapping("/adm/deployfile.htm")
  public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
      @RequestParam("context") String contextName, @RequestParam("where") String where,
      @RequestParam(value = "reload", required = false) String reloadParam,
      @RequestParam(value = "discard", required = false) String discardParam,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

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

    String errMsg = null;

    // If not multi-part content, exit
    if (file == null || file.isEmpty()) {
      errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notFile.failure")
          + " [null or empty]";
      request.setAttribute("errorMessage", errMsg);
      return new ModelAndView(new InternalResourceView(getViewName()));
    }

    // Save the uploaded file to a temporary location
    Path tmpPath = null;
    try {
      String fileName = file.getOriginalFilename();
      if (!Strings.isNullOrEmpty(fileName)) {
        fileName = FilenameUtils.getName(fileName);
        tmpPath = Path.of(System.getProperty("java.io.tmpdir"), fileName);
        file.transferTo(tmpPath);
      } else {
        errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notFile.failure")
            + " [null or empty]";
        request.setAttribute("errorMessage", errMsg);
        return new ModelAndView(new InternalResourceView(getViewName()));
      }
    } catch (IOException e) {
      logger.error("Could not process file upload", e);
      request.setAttribute("errorMessage", getMessageSourceAccessor()
          .getMessage("probe.src.deploy.file.uploadfailure", new Object[] {e.getMessage()}));
      if (tmpPath != null) {
        Files.delete(tmpPath);
      }
      return new ModelAndView(new InternalResourceView(getViewName()));
    }

    try {
      contextName = getContainerWrapper().getTomcatContainer().formatContextName(contextName);

      /*
       * pass the name of the newly deployed context to the presentation layer using this name the
       * presentation layer can render a url to view compilation details
       */
      String visibleContextName = contextName.isEmpty() ? "/" : contextName;
      request.setAttribute("contextName", visibleContextName);

      // Check if context is already deployed
      if (getContainerWrapper().getTomcatContainer().findContext(contextName) != null) {

        File destFile = Path.of(getContainerWrapper().getTomcatContainer().getAppBase().getPath(),
            contextName + where).toFile();

        // Checks if the destination path exists
        if (destFile.exists()) {
          if (!destFile.getAbsolutePath().contains("..")) {
            // Copy the file overwriting it if it already exists
            FileUtils.copyFileToDirectory(tmpPath.toFile(), destFile);

            request.setAttribute("successFile", Boolean.TRUE);
            // Logging action
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // get username logger
            String name = auth.getName();
            logger.info(getMessageSourceAccessor().getMessage("probe.src.log.copyfile"), name,
                contextName);
            Context context = getContainerWrapper().getTomcatContainer().findContext(contextName);
            // Checks if DISCARD "work" directory is selected
            if ("yes".equalsIgnoreCase(discardParam)) {
              getContainerWrapper().getTomcatContainer().discardWorkDir(context);
              logger.info(getMessageSourceAccessor().getMessage("probe.src.log.discardwork"), name,
                  contextName);
            }
            // Checks if RELOAD option is selected
            if ("yes".equalsIgnoreCase(reloadParam) && context != null) {
              context.reload();
              request.setAttribute("reloadContext", Boolean.TRUE);
              logger.info(getMessageSourceAccessor().getMessage("probe.src.log.reload"), name,
                  contextName);
            }
          } else {
            errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.pathNotValid");
          }
        } else {
          errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notPath");
        }
      } else {
        errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.notExists",
            new Object[] {visibleContextName});
      }
    } catch (IOException e) {
      errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.file.failure",
          new Object[] {e.getMessage()});
      logger.error("Tomcat threw an exception when trying to deploy", e);
    } finally {
      if (errMsg != null) {
        request.setAttribute("errorMessage", errMsg);
      }
      // File is copied so it will exist
      Files.delete(tmpPath);
    }
    return new ModelAndView(new InternalResourceView(getViewName()));
  }

  @Value("/adm/deploy.htm")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
