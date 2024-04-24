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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class ViewContextXmlConfController.
 */
@Controller
public class ViewContextXmlConfController extends BaseViewXmlConfController {

  @RequestMapping(path = "/adm/viewcontextxml.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Value("view_xml_conf")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

  @Value("context.xml")
  @Override
  public void setDisplayTarget(String downloadTarget) {
    super.setDisplayTarget(downloadTarget);
  }

  @Value("/adm/downloadcontextxml.htm")
  @Override
  public void setDownloadUrl(String downloadUrl) {
    super.setDownloadUrl(downloadUrl);
  }

}
