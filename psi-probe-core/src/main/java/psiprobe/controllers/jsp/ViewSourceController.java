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

import java.io.InputStream;

import org.apache.catalina.Context;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.Utils;
import psiprobe.controllers.AbstractContextHandlerController;
import psiprobe.model.jsp.Item;
import psiprobe.model.jsp.Summary;

/**
 * The Class ViewSourceController.
 */
@Controller
public class ViewSourceController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ViewSourceController.class);

  @RequestMapping(path = "/app/viewsource.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    String jspName = ServletRequestUtils.getStringParameter(request, "source");
    boolean highlight = ServletRequestUtils.getBooleanParameter(request, "highlight", true);
    Summary summary = (Summary) (request.getSession(false) != null
        ? request.getSession(false).getAttribute(DisplayJspController.SUMMARY_ATTRIBUTE)
        : null);

    if (jspName != null && summary != null && contextName.equals(summary.getName())) {

      Item item = summary.getItems().get(jspName);

      if (item != null) {
        // replace "\" with "/"
        jspName = jspName.replace('\\', '/');

        // remove cheeky "../" from the path to avoid exploits
        while (jspName.contains("../")) {
          jspName = jspName.replace("../", "");
        }

        if (getContainerWrapper().getTomcatContainer().resourceExists(jspName, context)) {
          ServletContext sctx = context.getServletContext();
          ServletConfig scfg = (ServletConfig) context.findChild("jsp");
          Options opt = new EmbeddedServletOptions(scfg, sctx);
          String descriptorPageEncoding =
              opt.getJspConfig().findJspProperty(jspName).getPageEncoding();

          if (descriptorPageEncoding != null && descriptorPageEncoding.length() > 0) {
            item.setEncoding(descriptorPageEncoding);
          } else {

            /*
             * we have to read the JSP twice, once to figure out the content encoding the second
             * time to read the actual content using the correct encoding
             */
            try (InputStream encodedStream =
                getContainerWrapper().getTomcatContainer().getResourceStream(jspName, context)) {
              item.setEncoding(Utils.getJspEncoding(encodedStream));
            }
          }
          try (InputStream jspStream =
              getContainerWrapper().getTomcatContainer().getResourceStream(jspName, context)) {
            if (highlight) {
              request.setAttribute("highlightedContent",
                  Utils.highlightStream(jspName, jspStream, "xhtml", item.getEncoding()));
            } else {
              request.setAttribute("content", Utils.readStream(jspStream, item.getEncoding()));
            }
          }

        } else {
          logger.error("{} does not exist", jspName);
        }

        request.setAttribute("item", item);

      } else {
        logger.error("jsp name passed is not in the summary, ignored");
      }
    } else {
      if (jspName == null) {
        logger.error("Passed empty 'source' parameter");
      }
      if (summary == null) {
        logger.error("Session has expired or there is no summary");
      }
    }
    return new ModelAndView(getViewName());
  }

  @Value("view_jsp_source")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
