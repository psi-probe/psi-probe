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
package psiprobe.controllers.apps;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.Utils;
import psiprobe.controllers.AbstractContextHandlerController;

/**
 * Downloads a deployment descriptor (web.xml) or a context descriptor (context.xml) of a web
 * application.
 */
public class BaseDownloadXmlConfController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(BaseDownloadXmlConfController.class);

  /** The Constant TARGET_WEB_XML. */
  private static final String TARGET_WEB_XML = "web.xml";

  /** The Constant TARGET_CONTEXT_XML. */
  private static final String TARGET_CONTEXT_XML = "context.xml";

  /** Type of a configuration file to be downloaded. */
  private String downloadTarget;

  /**
   * Gets the download target.
   *
   * @return the download target
   */
  public String getDownloadTarget() {
    return downloadTarget;
  }

  /**
   * Sets the download target.
   *
   * @param downloadTarget the new download target
   */
  public void setDownloadTarget(String downloadTarget) {
    this.downloadTarget = downloadTarget;
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    if (downloadTarget == null) {
      throw new RuntimeException("Download target is not set for " + getClass().getName());
    }

    String xmlPath;

    if (TARGET_WEB_XML.equals(downloadTarget)) {
      ServletContext sctx = context.getServletContext();
      xmlPath = sctx.getRealPath("/WEB-INF/web.xml");
    } else if (TARGET_CONTEXT_XML.equals(downloadTarget)) {
      xmlPath = this.getContainerWrapper().getTomcatContainer().getConfigFile(context).getPath();
    } else {
      throw new RuntimeException("Unknown download target " + getDownloadTarget());
    }

    if (xmlPath != null) {
      File xmlFile = new File(xmlPath);
      if (xmlFile.exists()) {
        Utils.sendFile(request, response, xmlFile);
      } else {
        logger.debug("File {} of {} application does not exists.", xmlPath, contextName);
      }
    } else {
      logger.debug("Cannot determine path to {} file of {} application.", getDownloadTarget(),
          contextName);
    }
    return null;
  }

}
