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
package psiprobe.controllers;

import com.thoughtworks.xstream.XStream;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;

import psiprobe.model.TransportableModel;

/**
 * The Class BeanToXmlController.
 */
@org.springframework.stereotype.Controller
public class BeanToXmlController extends AbstractController {

  /** The xml marker. */
  private String xmlMarker;

  /** The xstream. */
  @Inject
  private XStream xstream;

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
  @Value(".oxml")
  public void setXmlMarker(String xmlMarker) {
    this.xmlMarker = xmlMarker;
  }

  @RequestMapping(path = "/*.oxml.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
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
        xstream.toXML(tm, response.getWriter());
      }
    }
    return null;
  }
}
