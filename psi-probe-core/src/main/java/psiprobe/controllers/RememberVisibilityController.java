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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import psiprobe.jsp.Functions;

/**
 * The Class RememberVisibilityController.
 */
@Controller
public class RememberVisibilityController extends AbstractController {

  /** The sdf. */
  private final SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss zz");

  @RequestMapping(path = "/remember.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String cookieName = ServletRequestUtils.getStringParameter(request, "cn");
    String state = ServletRequestUtils.getStringParameter(request, "state");
    if (cookieName != null && state != null) {
      cookieName = Functions.safeCookieName(cookieName);
      // expire the cookies at the current date + 10years (roughly, nevermind leap years)
      response.addHeader("Set-Cookie",
          cookieName + '=' + state + "; Expires="
              + sdf.format(new Date(System.currentTimeMillis() + 315360000000L))
              + "; Secure=true; HttpOnly=true");
    }
    return null;
  }
}
