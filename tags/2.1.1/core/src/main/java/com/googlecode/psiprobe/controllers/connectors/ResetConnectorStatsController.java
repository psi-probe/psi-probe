package com.googlecode.psiprobe.controllers.connectors;

import com.googlecode.psiprobe.beans.stats.collectors.ConnectorStatsCollectorBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Mark Lewis
 */
public class ResetConnectorStatsController extends ParameterizableViewController {

    ConnectorStatsCollectorBean collectorBean;

    public ConnectorStatsCollectorBean getCollectorBean() {
        return collectorBean;
    }

    public void setCollectorBean(ConnectorStatsCollectorBean collectorBean) {
        this.collectorBean = collectorBean;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectorName = ServletRequestUtils.getRequiredStringParameter(request, "cn");
        collectorBean.reset(connectorName);
        return new ModelAndView(new RedirectView(request.getContextPath() + getViewName()));
    }

}
