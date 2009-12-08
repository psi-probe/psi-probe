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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
    An Ajax snippet that displays application runtime information

    Author: Andy Shapoval, Vlad Ilyushchenko
--%>

<%-- a hidden servlet count value used to display the value in a static app info section --%>
<span id="r_servletCount" style="display: none;">${app.servletCount}</span>

<c:choose>
    <c:when test="${app.available}">
        <%-- a hidden flag that shows if the app is up to display the status in a static app info section --%>
        <span id="r_appStatusUp" style="display: none;">true</span>

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
                <th>
                    <spring:message code="probe.jsp.app.summary.avgTime"/>
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
                    <a href="<c:url value='/sessions.htm?webapp=${app.name}&size=${param.size}'/>">${app.sessionCount}</a>
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
                    <a href="<c:url value='/appattributes.htm?webapp=${app.name}'/>">${app.contextAttributeCount}</a>
                </td>
                <td>
                    <a href="<c:url value='/servlets.htm?webapp=${app.name}'/>">${app.requestCount}</a>
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
                <td>
                ${app.avgTime} ms.
                </td>
                <c:if test="${! no_resources}">
                    <td>
                        <js:score value="${app.dataSourceUsageScore}" fullBlocks="8" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
                            <a class="imglink" href="<c:url value="/resources.htm?webapp=${app.name}"/>"><img border="0"
                                src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+"
                                title="<spring:message code='probe.jsp.applications.jdbcUsage.title' arguments='${app.dataSourceUsageScore}'/>"/></a>
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
            <p><spring:message code="probe.jsp.app.summary.runtime.unavailable"/></p>
        </div>
    </c:otherwise>
</c:choose>
