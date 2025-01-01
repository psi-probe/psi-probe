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
package psiprobe.controllers.jsp;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.catalina.Context;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.Utils;
import psiprobe.controllers.AbstractContextHandlerController;

/**
 * The Class ViewServletSourceController.
 */
@Controller
public class ViewServletSourceController extends AbstractContextHandlerController {

  @RequestMapping(path = "/app/viewservlet.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    String jspName = ServletRequestUtils.getStringParameter(request, "source");
    ServletContext sctx = context.getServletContext();
    ServletConfig scfg = (ServletConfig) context.findChild("jsp");
    Options opt = new EmbeddedServletOptions(scfg, sctx);
    String encoding = opt.getJavaEncoding();
    String content = null;

    if (jspName != null) {
      String servletName =
          getContainerWrapper().getTomcatContainer().getServletFileNameForJsp(context, jspName);

      if (servletName != null) {
        File servletFile = Path.of(servletName).toFile();
        if (servletFile.exists()) {
          try (InputStream fis = Files.newInputStream(servletFile.toPath())) {
            content = Utils.highlightStream(jspName, fis, "java", encoding);
          }
        }
      }
    }
    return new ModelAndView(getViewName(), "content", content);
  }

  @Value("view_servlet_source")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
