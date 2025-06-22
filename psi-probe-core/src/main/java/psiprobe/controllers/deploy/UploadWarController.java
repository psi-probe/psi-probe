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
import java.time.Duration;

import org.apache.catalina.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.file.PathUtils;
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
import psiprobe.controllers.jsp.DisplayJspController;
import psiprobe.model.jsp.Summary;

/**
 * Uploads and installs web application from a .WAR.
 */
@Controller
public class UploadWarController extends AbstractTomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(UploadWarController.class);

  /** The Constant MAXSECONDS_WAITFOR_CONTEXT. */
  private static final int MAXSECONDS_WAITFOR_CONTEXT = 10;

  @GetMapping("/adm/war.htm")
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

  @PostMapping("/adm/war.htm")
  public ModelAndView handleUpload(@RequestParam("war") MultipartFile file,
      @RequestParam(value = "context", required = false) String contextName,
      @RequestParam(value = "update", required = false) String updateParam,
      @RequestParam(value = "compile", required = false) String compileParam,
      @RequestParam(value = "discard", required = false) String discardParam,
      HttpServletRequest request) throws Exception {

    String errMsg = null;

    // If not multi-part content, exit
    if (file == null || file.isEmpty()) {
      errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.notWar.failure")
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
        errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.notWar.failure")
            + " [null or empty]";
        request.setAttribute("errorMessage", errMsg);
        return new ModelAndView(new InternalResourceView(getViewName()));
      }
    } catch (IOException e) {
      logger.error("Could not process file upload", e);
      request.setAttribute("errorMessage", getMessageSourceAccessor()
          .getMessage("probe.src.deploy.war.uploadfailure", new Object[] {e.getMessage()}));
      // File is transferred so it will exist
      Files.delete(tmpPath);
      return new ModelAndView(new InternalResourceView(getViewName()));
    }

    if (!tmpPath.getFileName().toString().endsWith(".war")) {
      errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.notWar.failure") + " ["
          + tmpPath.getFileName() + "]";
      request.setAttribute("errorMessage", errMsg);
      return new ModelAndView(new InternalResourceView(getViewName()));
    }

    if (contextName == null || contextName.isEmpty()) {
      String warFileName = tmpPath.getFileName().toString().replaceAll("\\.war$", "");
      contextName = "/" + warFileName;
    }

    try {
      contextName = getContainerWrapper().getTomcatContainer().formatContextName(contextName);

      /*
       * pass the name of the newly deployed context to the presentation layer using this name the
       * presentation layer can render a url to view compilation details
       */
      String visibleContextName = contextName.isEmpty() ? "/" : contextName;
      request.setAttribute("contextName", visibleContextName);

      // Checks if UPDATE option is selected
      if ("yes".equals(updateParam)
          && getContainerWrapper().getTomcatContainer().findContext(contextName) != null) {
        if (contextName.matches("\\w*")) {
          logger.debug("updating {}: removing the old copy", contextName);
        }
        getContainerWrapper().getTomcatContainer().remove(contextName);
      }

      if (getContainerWrapper().getTomcatContainer().findContext(contextName) == null) {
        // move the .war to tomcat application base dir
        String destWarFilename =
            getContainerWrapper().getTomcatContainer().formatContextFilename(contextName);
        File destWar = Path.of(getContainerWrapper().getTomcatContainer().getAppBase().getPath(),
            destWarFilename + ".war").toFile();

        FileUtils.moveFile(tmpPath.toFile(), destWar);

        // let Tomcat know that the file is there
        getContainerWrapper().getTomcatContainer().installWar(contextName);

        Path destContext = Path
            .of(getContainerWrapper().getTomcatContainer().getAppBase().getPath(), destWarFilename);

        // Wait few seconds for creating context dir to avoid empty context
        PathUtils.waitFor(destContext, Duration.ofSeconds(MAXSECONDS_WAITFOR_CONTEXT));

        Context ctx = getContainerWrapper().getTomcatContainer().findContext(contextName);
        if (ctx == null) {
          errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.notinstalled",
              new Object[] {visibleContextName});
        } else {
          request.setAttribute("success", Boolean.TRUE);
          // Logging action
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          // get username logger
          String name = auth.getName();
          if (contextName.matches("\\w*")) {
            logger.info(getMessageSourceAccessor().getMessage("probe.src.log.deploywar"), name,
                contextName);
          }
          // Checks if DISCARD "work" directory is selected
          if ("yes".equals(discardParam)) {
            getContainerWrapper().getTomcatContainer().discardWorkDir(ctx);
            if (contextName.matches("\\w*")) {
              logger.info(getMessageSourceAccessor().getMessage("probe.src.log.discardwork"), name,
                  contextName);
            }
          }
          // Checks if COMPILE option is selected
          if ("yes".equals(compileParam)) {
            Summary summary = new Summary();
            summary.setName(ctx.getName());
            getContainerWrapper().getTomcatContainer().listContextJsps(ctx, summary, true);
            request.getSession(false).setAttribute(DisplayJspController.SUMMARY_ATTRIBUTE, summary);
            request.setAttribute("compileSuccess", Boolean.TRUE);
          }
        }
      } else {
        errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.alreadyExists",
            new Object[] {visibleContextName});
      }
    } catch (IOException e) {
      errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.failure",
          new Object[] {e.getMessage()});
      logger.error("Tomcat threw an exception when trying to deploy", e);
    } finally {
      if (errMsg != null) {
        request.setAttribute("errorMessage", errMsg);
      }
      // If war was not moved, delete it
      if (Files.exists(tmpPath)) {
        Files.delete(tmpPath);
      }
    }
    return new ModelAndView(new InternalResourceView(getViewName()));
  }

  @Value("/adm/deploy.htm")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
