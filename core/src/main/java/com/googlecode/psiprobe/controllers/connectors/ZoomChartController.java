package com.googlecode.psiprobe.controllers.connectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 * @author Mark Lewis
 */
public class ZoomChartController extends ParameterizableViewController {

    private long collectionPeriod;

    public long getCollectionPeriod() {
        return collectionPeriod;
    }

    public void setCollectionPeriod(long collectionPeriod) {
        this.collectionPeriod = collectionPeriod;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.handleRequestInternal(request, response)
                .addObject("collectionPeriod", new Long(getCollectionPeriod()));
    }

}
