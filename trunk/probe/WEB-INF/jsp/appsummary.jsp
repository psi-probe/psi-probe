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
<%@ taglib uri="http://www.jstripe.com/tags" prefix="js" %>

<%--
    Displays a web application information summary and application statistics charts

    Author: Andy Shapoval, Vlad Ilyushchenko
--%>

<html>
<head>
    <title>
        <spring:message code="probe.jsp.title.app.summary" arguments="${param.webapp}"/>
    </title>
</head>

<c:set var="navTabApps" value="active" scope="request"/>
<c:set var="use_decorator" value="application" scope="request"/>
<c:set var="appTabSummary" value="active" scope="request"/>

<body>
<c:choose>
    <c:when test="${empty app}">
        <div class="errorMessage">
            <p>
                <spring:message code="probe.jsp.app.summary.invalidApp"/>
            </p>
        </div>
    </c:when>
    <c:otherwise>
        <script type="text/javascript" language="javascript" src="<c:url value="/js/prototype.js"/>"></script>
        <script type="text/javascript" language="javascript" src="<c:url value="/js/scriptaculous.js"/>"></script>
        <script type="text/javascript" language="javascript" src="<c:url value="/js/func.js"/>"></script>
        <script type="text/javascript" language="javascript" src="<c:url value="/js/behaviour.js"/>"></script>

        <ul class="options">
            <li id="appSurfTo"><a href="${app.name}${app.name ne '/' ? '/' : ''}" target="_blank"><spring:message code="probe.jsp.app.summary.menu.goTo"/></a></li>
            <li id="appStop" ${app.available ? '' : 'style="display: none;"'}><a href="<c:url value="/app/stop_summary.htm"><c:param name="webapp" value="${param.webapp}"/></c:url>"><spring:message code="probe.jsp.app.summary.menu.stop"/></a></li>
            <li id="appStart" ${app.available ? 'style="display: none;"' : ''}><a href="<c:url value="/app/start_summary.htm"><c:param name="webapp" value="${param.webapp}"/></c:url>"><spring:message code="probe.jsp.app.summary.menu.start"/></a></li>
            <li id="appReload"><a href="<c:url value="/app/reload_summary.htm"><c:param name="webapp" value="${param.webapp}"/></c:url>"><spring:message code="probe.jsp.app.summary.menu.reload"/></a></li>
            <li id="appUndeploy"><a href="<c:url value="/adm/undeploy_summary.htm"><c:param name="webapp" value="${param.webapp}"/></c:url>"
                    onclick="return confirm('<spring:message code="probe.jsp.app.summary.undeploy.confirm" arguments="${param.webapp}"/>')"
                    ><spring:message code="probe.jsp.app.summary.menu.undeploy"/></a></li>
            <c:choose>
                <c:when test="${param.size}">
                    <li id="size" ><a href="?<js:toggle param="size"/>"><spring:message code="probe.jsp.applications.hidesize"/></a></li>
                </c:when>
                <c:otherwise>
                    <li id="size" ><a href="?<js:toggle param="size"/>"><spring:message code="probe.jsp.applications.showsize"/></a></li>
                </c:otherwise>
            </c:choose>
        </ul>

        <div class="embeddedBlockContainer">
            <c:if test="${! empty errorMessage}">
                <div class="errorMessage">
                    <p>
                        ${errorMessage}
                    </p>
                </div>
            </c:if>
            <div class="statusMessage">
                <p><spring:message code="probe.jsp.app.summary.status"/>&nbsp;
                    <span id="appStatusUp" class="bigOkValue" ${app.available ? '' : 'style="display: none;"'}><spring:message code="probe.jsp.app.summary.status.up"/></span>
                    <span id="appStatusDown" class="bigErrorValue" ${app.available ? 'style="display: none;"' : ''}><spring:message code="probe.jsp.app.summary.status.down"/></span>
                </p>
            </div>
            <h3><spring:message code="probe.jsp.app.summary.h3.static"/></h3>
            <div id="appInfo">
                <spring:message code="probe.jsp.app.summary.application"/>&nbsp;
                <span class="value"><a href="${app.name}${app.name ne '/' ? '/' : ''}" target="_blank">${app.name}</a></span>
                <spring:message code="probe.jsp.app.summary.docBase"/>&nbsp;<span class="value">${app.docBase}</span>
                <spring:message code="probe.jsp.app.summary.description"/>&nbsp;<span class="value">${app.displayName}</span>
                <spring:message code="probe.jsp.app.summary.servletVersion"/>&nbsp;<span class="value">${app.servletVersion}</span>
                <spring:message code="probe.jsp.app.summary.servletCount"/>&nbsp;<span class="value"><a href="<c:url value="/servlets.htm"><c:param name="webapp" value="${app.name}"/></c:url>"><span id="servletCount"></span></a></span>
                <spring:message code="probe.jsp.app.summary.sessionTimeout"/>&nbsp;<span class="value">${app.sessionTimeout} sec.</span>
                <spring:message code="probe.jsp.app.summary.distributable"/>
                <c:choose>
                    <c:when test="${app.distributable}">
                        <span class="okValue"><spring:message code="probe.jsp.generic.yes"/></span>
                    </c:when>
                    <c:otherwise>
                        <span class="errorValue"><spring:message code="probe.jsp.generic.no"/></span>
                    </c:otherwise>
                </c:choose>
            </div>
            <h3><spring:message code="probe.jsp.app.summary.h3.runtime"/></h3>
            <div id="runtimeAppInfo">
                <jsp:include page="/appruntimeinfo.ajax"/>
            </div>
        </div>

        <div id="charts" class="embeddedBlockContainer">
            <h3><spring:message code="probe.jsp.app.summary.h3.charts"/></h3>

            <spring:message code="probe.jsp.app.summary.charts.requests.title" var="req_title"/>

            <c:url value="/chart.png" var="req_url">
                <c:param name="p" value="app_req"/>
                <c:param name="sp" value="${param.webapp}"/>
                <c:param name="xz" value="350"/>
                <c:param name="yz" value="250"/>
                <c:param name="l" value="false"/>
            </c:url>

            <spring:message code="probe.jsp.app.summary.charts.requests.legend" var="req_legend"/>
            <spring:message code="probe.jsp.app.summary.charts.errors.legend" var="err_legend"/>

            <c:url value="/chart.png" var="req_url_full">
                <c:param name="p" value="app_req"/>
                <c:param name="sp" value="${param.webapp}"/>
                <c:param name="xz" value="700"/>
                <c:param name="yz" value="320"/>
                <c:param name="s1l" value="${req_legend}"/>
                <c:param name="s2l" value="${err_legend}"/>
            </c:url>

            <spring:message code="probe.jsp.app.summary.charts.avgProcTime.title" var="avg_proc_time_title"/>

            <c:url value="/chart.png" var="avg_proc_time_url">
                <c:param name="p" value="app_avg_proc_time"/>
                <c:param name="sp" value="${param.webapp}"/>
                <c:param name="xz" value="350"/>
                <c:param name="yz" value="250"/>
                <c:param name="s1c" value="#95FE8B"/>
                <c:param name="s1o" value="#009406"/>
                <c:param name="l" value="false"/>
            </c:url>

            <spring:message code="probe.jsp.app.summary.charts.avgProcTime.legend" var="avg_proc_time_legend"/>

            <c:url value="/chart.png" var="avg_proc_time_url_full">
                <c:param name="p" value="app_avg_proc_time"/>
                <c:param name="sp" value="${param.webapp}"/>
                <c:param name="xz" value="700"/>
                <c:param name="yz" value="320"/>
                <c:param name="s1c" value="#95FE8B"/>
                <c:param name="s1o" value="#009406"/>
                <c:param name="s1l" value="${avg_proc_time_legend}"/>
            </c:url>

            <div id="chart_group">
                <div class="chartContainer">
                    <dl>
                        <dt>${req_title}</dt>
                        <dd class="image">
                            <img id="req_chart" border="0" src="${req_url}" alt="+"/>
                        </dd>
                    </dl>
                </div>

                <div class="chartContainer">
                    <dl>
                        <dt>&nbsp;${avg_proc_time_title}</dt>
                        <dd class="image">
                            <img id="avg_proc_time_chart" border="0" src="${avg_proc_time_url}" alt="+"/>
                        </dd>
                    </dl>
                </div>
            </div>

            <div id="full_chart" class="chartContainer" style="display: none;">
                <dl>
                    <dt id="full_title"></dt>
                    <dd class="image">
                        <img id="fullImg" border="0" src="${avg_proc_time_url}" alt="-"/>
                    </dd>
                </dl>
            </div>
        </div>

        <%-- pereodical refreshing of runtime info --%>
        <script type="text/javascript">
            // updates static app info section with values that are actually collected by a runtime info request
            function updateStaticInfo() {
                $('servletCount').innerHTML = $('r_servletCount').innerHTML;
            }

            // changes visibility of markup items that depend on an application status
            function updateStatus() {
                if ($('r_appStatusUp')) {
                    Element.hide('appStart');
                    Element.show('appStop');
                    Element.hide('appStatusDown');
                    Element.show('appStatusUp');
                } else {
                    Element.hide('appStop');
                    Element.show('appStart');
                    Element.hide('appStatusUp');
                    Element.show('appStatusDown');
                }
            }

            // Unfortunately, Ajax.PeriodicalUpdater does not call a function specified in
            // onComplete option property after each request. Therefore, we'll manually execute
            // periodical requests and update the static portions of the page when a request
            // has been completed.
            var runtimeInfoTimer;
            var freq = 5 * 1000;

            function updateRuntimeInfo() {
                new Ajax.Updater("runtimeAppInfo",
                        "<c:url value="/appruntimeinfo.ajax?${pageContext.request.queryString}"/>",
                        {onComplete: function(req, obj) {
                            updateStatus();
                            updateStaticInfo();
                            if (runtimeInfoTimer) clearTimeout(runtimeInfoTimer);
                            runtimeInfoTimer = setTimeout('updateRuntimeInfo()', freq);
                        }});
            }

            updateStaticInfo();

            if (runtimeInfoTimer) clearTimeout(runtimeInfoTimer);
            runtimeInfoTimer = setTimeout('updateRuntimeInfo()', freq);
        </script>

        <%-- chart related functionality --%>
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
                var img = document.getElementById('fullImg');
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
                '#req_chart': function(element) {
                    element.onclick = function() {
                        zoomIn('${req_url_full}', '${req_title}');
                    }
                },
                '#avg_proc_time_chart': function(element) {
                    element.onclick = function() {
                        zoomIn('${avg_proc_time_url_full}', '${avg_proc_time_title}');
                    }
                },
                '#full_chart': function(element) {
                    element.onclick = function() {
                        zoomOut();
                    }
                }
            }

            Behaviour.register(rules);

            imageUpdaters[0] = new Ajax.ImgUpdater("req_chart", 30);
            imageUpdaters[1] = new Ajax.ImgUpdater("avg_proc_time_chart", 30);
        </script>
    </c:otherwise>
    </c:choose>
</body>
</html>