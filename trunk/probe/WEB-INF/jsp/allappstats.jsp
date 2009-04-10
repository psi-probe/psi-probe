<%--
  ~ Licensed under the GPL License. You may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~   http://probe.jstripe.com/d/license.shtml
  ~
  ~  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
  ~  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
  ~  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <title>
        <spring:message code="probe.jsp.title.allappstats"/>
    </title>
</head>

<c:set var="navTabApps" value="active" scope="request"/>

<body>
    <script type="text/javascript" language="javascript" src="<c:url value="/js/prototype.js"/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value="/js/scriptaculous.js"/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value="/js/func.js"/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value="/js/behaviour.js"/>"></script>

    <div id="charts" class="embeddedBlockContainer">
        <h3><spring:message code="probe.jsp.allappstats.h3.charts"/></h3>

        <c:url value="/chart.png" var="total_avg_proc_time_url">
            <c:param name="p" value="total_avg_proc_time"/>
            <c:param name="xz" value="345"/>
            <c:param name="yz" value="250"/>
            <c:param name="s1c" value="#95FE8B"/>
            <c:param name="s1o" value="#009406"/>
            <c:param name="l" value="false"/>
        </c:url>

        <c:url value="/chart.png" var="total_avg_proc_time_url_full">
            <c:param name="p" value="total_avg_proc_time"/>
            <c:param name="xz" value="700"/>
            <c:param name="yz" value="320"/>
            <c:param name="s1c" value="#95FE8B"/>
            <c:param name="s1o" value="#009406"/>
            <c:param name="s1l">
                <spring:message code="probe.jsp.allappstats.charts.totalAvgProcTime.legend"/>
            </c:param>
        </c:url>

        <div id="chart_group">
            <div class="chartContainer">
                <dl>
                    <dt><spring:message code="probe.jsp.allappstats.charts.totalAvgProcTime.title"/></dt>
                    <dd class="image">
                        <img id="total_avg_proc_time_chart" border="0" src="${total_avg_proc_time_url}" alt="+"/>
                    </dd>
                </dl>
            </div>
        </div>

        <div id="full_chart" class="chartContainer" style="display: none;">
            <dl>
                <dt id="full_title"></dt>
                <dd class="image">
                    <img id="fullImg" border="0" src="${total_avg_proc_time_url_full}" alt="-"/>
                </dd>
            </dl>
        </div>
    </div>

    <script type="text/javascript">
        var imageUpdaters = new Array();
        var fullImageUpdater;

        function zoomIn(imgUrl, title) {
            if (fullImageUpdater) {
                fullImageUpdater.stop();
            }
            for (var i = 0; i < imageUpdaters.length; i++) {
                if (imageUpdaters[i]) {
                    imageUpdaters[i].stop();
                }
            }
            $('full_title').innerHTML = title;
            Effect.DropOut('chart_group');
            Effect.Appear('full_chart');
            fullImageUpdater = new Ajax.ImgUpdater("fullImg", 30, imgUrl);
        }

        function zoomOut() {
            Effect.DropOut('full_chart');
            Effect.Appear('chart_group');
            if (fullImageUpdater) {
                fullImageUpdater.stop();
                fullImageUpdater = null;
            }
            for (var i = 0; i < imageUpdaters.length; i++) {
                if (imageUpdaters[i]) {
                    imageUpdaters[i].start();
                }
            }
        }

        var rules = {
            '#total_avg_proc_time_chart': function(element) {
                element.onclick = function() {
                    zoomIn('${total_avg_proc_time_url_full}', '<spring:message code="probe.jsp.allappstats.charts.totalAvgProcTime.title"/>');
                }
            },
            '#full_chart': function(element) {
                element.onclick = function() {
                    zoomOut();
                }
            }
        }

        Behaviour.register(rules);

        imageUpdaters[0] = new Ajax.ImgUpdater("total_avg_proc_time_chart", 30);
    </script>
</body>
</html>