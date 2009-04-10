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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%--
    Displays a list of web application servlet mappings

    Author: Andy Shapoval
--%>

<html>

<head>
    <title>
        <c:choose>
            <c:when test="${empty param.webapp}">
                <spring:message code="probe.jsp.title.all.servletmaps"/>
            </c:when>
            <c:otherwise>
                <spring:message code="probe.jsp.title.app.servletmaps" arguments="${param.webapp}"/>
            </c:otherwise>
        </c:choose>
    </title>
</head>

<%--
    Make Tab #1 visually "active".
--%>
<c:set var="navTabApps" value="active" scope="request"/>
<c:if test="${! empty param.webapp}">
    <c:set var="use_decorator" value="application" scope="request"/>
    <c:set var="appTabServlets" value="active" scope="request"/>
</c:if>

<body>

<ul class="options">
    <li id="viewAppServlets">
        <c:choose>
            <c:when test="${empty param.webapp}">
                <a href="<c:url value="/appservlets.htm"/>"><spring:message code="probe.jsp.app.servletmaps.opt.defs"/></a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/appservlets.htm">
                            <c:param name="webapp" value="${param.webapp}"/>
                        </c:url>"><spring:message code="probe.jsp.app.servletmaps.opt.defs"/></a>
            </c:otherwise>
        </c:choose>
    </li>
</ul>

<div class="embeddedBlockContainer">
    <c:choose>
        <c:when test="${! empty servletMaps}">

            <h3><spring:message code="probe.jsp.app.servletmaps.h3.maps"/></h3>

            <display:table name="servletMaps" id="svlt"
                           class="genericTbl" cellspacing="0" cellpadding="0"
                           requestURI="" defaultsort="1">
                <c:if test="${empty param.webapp}">
                    <display:column sortProperty="applicationName" sortable="true"
                                    titleKey="probe.jsp.app.servlets.col.applicationName" class="leftmost">
                        <a href="<c:url value="/appsummary.htm"><c:param name="webapp" value="${svlt.applicationName}"/></c:url>">${svlt.applicationName}</a>
                    </display:column>
                </c:if>
                <display:column property="url" sortable="true"
                                titleKey="probe.jsp.app.servletmaps.col.url" class="${! empty param.webapp ? 'leftmost' : ''}"/>
                <display:column property="servletName" sortable="true"
                                titleKey="probe.jsp.app.servletmaps.col.servletName" maxLength="40"/>
                <display:column property="servletClass" sortable="true"
                                titleKey="probe.jsp.app.servlets.col.servletClass" maxLength="50"/>
                <display:column sortProperty="available" sortable="true"
                                titleKey="probe.jsp.app.servlets.col.available">
                    <c:choose>
                        <c:when test="${svlt.available}">
                            <span class="okValue"><spring:message code="probe.jsp.generic.yes"/></span>
                        </c:when>
                        <c:otherwise>
                            <span class="errorValue"><spring:message code="probe.jsp.generic.no"/></span>
                        </c:otherwise>
                    </c:choose>
                </display:column>
            </display:table>
        </c:when>
        <c:otherwise>
            <div class="infoMessage">
                <p>
                    <c:choose>
                        <c:when test="${empty param.webapp}">
                            <spring:message code="probe.jsp.all.servletmaps.empty"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="probe.jsp.app.servletmaps.empty"/>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>