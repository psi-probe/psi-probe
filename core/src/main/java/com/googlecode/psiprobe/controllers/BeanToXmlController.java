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

package com.googlecode.psiprobe.controllers;

import com.googlecode.psiprobe.model.TransportableModel;

import com.thoughtworks.xstream.XStream;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class BeanToXmlController.
 *
 * @author Vlad Ilyushchenko
 */
public class BeanToXmlController extends AbstractController {

  /** The xml marker. */
  private String xmlMarker = ".oxml";

  /**
   * Gets the xml marker.
   *
   * @return the xml marker
   */
  public String getXmlMarker() {
    return xmlMarker;
  }

  /**
   * Sets the xml marker.
   *
   * @param xmlMarker the new xml marker
   */
  public void setXmlMarker(String xmlMarker) {
    this.xmlMarker = xmlMarker;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String path = request.getServletPath();
    String internalPath = path.replaceAll(xmlMarker, "");

    Controller controller = (Controller) getApplicationContext().getBean(internalPath);
    if (controller != null) {
      ModelAndView modelAndView = controller.handleRequest(request, response);
      if (modelAndView.getModel() != null) {
        TransportableModel tm = new TransportableModel();
        tm.putAll(modelAndView.getModel());
        XStream stream = new XStream();
        stream.toXML(tm, response.getWriter());
      }
    }
    return null;
  }
}
