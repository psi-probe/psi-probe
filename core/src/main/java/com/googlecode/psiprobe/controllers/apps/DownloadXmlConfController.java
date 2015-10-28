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

package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.Utils;
import com.googlecode.psiprobe.controllers.ContextHandlerController;

import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Downloads a deployment descriptor (web.xml) or a context descriptor (context.xml) of a web
 * application
 * 
 * @author Andy Shapoval
 */
public class DownloadXmlConfController extends ContextHandlerController {

  /** The Constant TARGET_WEB_XML. */
  public static final String TARGET_WEB_XML = "web.xml";
  
  /** The Constant TARGET_CONTEXT_XML. */
  public static final String TARGET_CONTEXT_XML = "context.xml";

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

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.controllers.ContextHandlerController#handleContext(java.lang.String, org.apache.catalina.Context, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
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
        logger.debug("File " + xmlPath + " of " + contextName + " application does not exists.");
      }
    } else {
      logger.debug("Cannot determine path to " + getDownloadTarget() + " file of " + contextName
          + " application.");
    }
    return null;
  }

}
