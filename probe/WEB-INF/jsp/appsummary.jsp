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
<%@ taglib uri='http://java.sun.com/jsp/jstl/fmt' prefix='fmt' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://www.jstripe.com/tags" prefix="js" %>

<%--
    Displays a summary of web application information

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
        <ul class="options">
            <li id="appSurfTo"><a href="${app.name}${app.name ne '/' ? '/' : ''}" target="_blank"><spring:message code="probe.jsp.app.summary.menu.goTo"/></a></li>
            <c:choose>
                <c:when test="${app.available}">
                    <li id="appStop"><a href="<c:url value="/app/stop_summary.htm"><c:param name="webapp" value="${param.webapp}"/></c:url>"><spring:message code="probe.jsp.app.summary.menu.stop"/></a></li>
                </c:when>
                <c:otherwise>
                    <li id="appStart"><a href="<c:url value="/app/start_summary.htm"><c:param name="webapp" value="${param.webapp}"/></c:url>"><spring:message code="probe.jsp.app.summary.menu.start"/></a></li>
                </c:otherwise>
            </c:choose>
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
                    <c:choose>
                        <c:when test="${app.available}">
                            <span class="bigOkValue"><spring:message code="probe.jsp.app.summary.status.up"/></span>
                        </c:when>
                        <c:otherwise>
                            <span class="bigErrorValue"><spring:message code="probe.jsp.app.summary.status.down"/></span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <h3><spring:message code="probe.jsp.app.summary.h3.static"/></h3>
            <div id="appInfo">
                <spring:message code="probe.jsp.app.summary.application"/>&nbsp;
                <span class="value"><a href="${app.name}${app.name ne '/' ? '/' : ''}" target="_blank">${app.name}</a></span>
                <spring:message code="probe.jsp.app.summary.docBase"/>&nbsp;<span class="value">${app.docBase}</span>
                <spring:message code="probe.jsp.app.summary.description"/>&nbsp;<span class="value">${app.displayName}</span>
                <spring:message code="probe.jsp.app.summary.servletVersion"/>&nbsp;<span class="value">${app.servletVersion}</span>
                <spring:message code="probe.jsp.app.summary.servletCount"/>&nbsp;<span class="value"><a href="<c:url value="/appservlets.htm"><c:param name="webapp" value="${app.name}"/></c:url>">${app.servletCount}</a></span>
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
                <c:choose>
                    <c:when test="${app.available}">
                        <table class="statsTable" cellpadding="0" cellspacing="0" width="90%">
                            <thead>
                            <tr>
                                <th class="leftMost">
                                    <spring:message code="probe.jsp.app.summary.sessionCount"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.serializable"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.sessionAttributeCount"/>
                                </th>
                                <c:if test="${param.size}">
                                    <th>
                                        <spring:message code="probe.jsp.app.summary.sessionSize"/>
                                    </th>
                                </c:if>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.contextAttributeCount"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.requestCount"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.errorCount"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.processingTime"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.minTime"/>
                                </th>
                                <th>
                                    <spring:message code="probe.jsp.app.summary.maxTime"/>
                                </th>
                                <c:if test="${! no_resources}">
                                    <th>
                                        <spring:message code="probe.jsp.app.summary.dataSourceUsageScore"/>
                                    </th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="leftMost">
                                    <a href="<c:url value="/sessions.htm?webapp=${app.name}&size=${param.size}"/>">${app.sessionCount}</a>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${app.serializable}">
                                            <span class="okValue"><spring:message code="probe.jsp.generic.yes"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="errorValue"><spring:message code="probe.jsp.generic.no"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                ${app.sessionAttributeCount}
                                </td>
                                <c:if test="${param.size}">
                                    <td>
                                        <js:volume value="${app.size}"/>
                                    </td>
                                </c:if>
                                <td>
                                    <a href="<c:url value="/appattributes.htm?webapp=${app.name}"/>">${app.contextAttributeCount}</a>
                                </td>
                                <td>
                                    <a href="<c:url value="/appservlets.htm?webapp=${app.name}"/>">${app.requestCount}</a>
                                </td>
                                <td>
                                ${app.errorCount}
                                </td>
                                <td>
                                    <js:duration value="${app.processingTime}"/>
                                </td>
                                <td>
                                ${app.minTime} ms.
                                </td>
                                <td>
                                ${app.maxTime} ms.
                                </td>
                                <c:if test="${! no_resources}">
                                    <td>
                                        <js:score value="${app.dataSourceUsageScore}" fullBlocks="8" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
                                            <a class="imglink" href="<c:url value="/resources.htm?webapp=${app.name}"/>"><img border="0"
                                                src="<c:url value="/css/classic/gifs/rb_{0}.gif"/>" alt="+"
                                                title="<spring:message code="probe.jsp.applications.jdbcUsage.title" arguments="${app.dataSourceUsageScore}"/>"/></a>
                                        </js:score>
                                        &nbsp;${app.dataSourceUsageScore}%
                                    </td>
                                </c:if>
                            </tr>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="warningMessage">
                            <p>This application is not running, runtime information is unavailable</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:otherwise>
    </c:choose>
</body>
</html>