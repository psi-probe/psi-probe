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

<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/tags/jstripe.tld" prefix="js" %>

<%--
    Simple tabular list of log files and their attributes. The page is further linked to
    log file viewer and file download controller.

    Author: Vlad Ilyushchenko.
--%>
<html>
<head>
    <title>
        <spring:message code="probe.jsp.title.logs"/>
    </title>
</head>

<c:set var="navTabLogs" value="active" scope="request"/>

<body>

<ul class="options">
    <c:choose>
        <c:when test="${param.apps}">
            <li id="showapps"><a href="?<js:toggle param="apps"/>"><spring:message code="probe.jsp.logs.hideapps"/></a></li>
        </c:when>
        <c:otherwise>
            <li id="showapps"><a href="?<js:toggle param="apps"/>"><spring:message code="probe.jsp.logs.showapps"/></a></li>
        </c:otherwise>
    </c:choose>
</ul>

<div class="blockContainer">
    <display:table name="logs" scope="session" class="genericTbl" cellspacing="0" uid="log" requestURI="">

        <c:choose>

            <c:when test="${param.apps}">

                <display:column titleKey="probe.jsp.logs.col.app" sortable="true" class="leftmost">
                    ${log.application.name}
                </display:column>

                <display:column titleKey="probe.jsp.logs.col.class" sortable="true" property="logClass"/>

            </c:when>

            <c:otherwise>
                <display:column titleKey="probe.jsp.logs.col.class" sortable="true" property="logClass" class="leftmost"/>
            </c:otherwise>

        </c:choose>

        <display:column titleKey="probe.jsp.logs.col.file" sort="true" sortProperty="file">
            <c:choose>
                <c:when test="${log.file == 'stdout'}">
                    <js:out value="${log.file}" maxLength="80" ellipsisRight="false"/>
                </c:when>
                <c:otherwise>
                    <a class="logfile" href="<c:url value="/logs/follow.htm"><c:param name="id" value="${log_rowNum}"/></c:url>">
                        <js:out value="${log.file}" maxLength="80" ellipsisRight="false"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </display:column>

        <display:column title="&nbsp;">
            <c:if test="${log.file != 'stdout'}">
                <a class="imglink" href="<c:url value="/logs/download"><c:param name="id" value="${log_rowNum}"/></c:url>"><img
                        class="lnk" src="${pageContext.request.contextPath}<spring:theme code="download.png"/>"
                        alt="<spring:message code="probe.jsp.logs.download.alt"/>"/></a>
            </c:if>
        </display:column>

        <display:column titleKey="probe.jsp.logs.col.size" sortable="true" sortProperty="size">
            <js:volume value="${log.size}"/>&nbsp;
        </display:column>

        <display:column titleKey="probe.jsp.logs.col.modified" sortable="true" sortProperty="lastModified">
            ${log.lastModified}&nbsp;
        </display:column>

        <display:column titleKey="probe.jsp.logs.col.type" sort="true" property="type"/>
    </display:table>
</div>
</body>
</html>