<%--
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%--
	Displays a list of servlets of a particular web application or all web applications

	Author: Andy Shapoval
--%>

<html>

	<head>
		<title>
			<c:choose>
				<c:when test="${empty param.webapp}">
					<spring:message code="probe.jsp.title.servlets.all"/>
				</c:when>
				<c:otherwise>
					<spring:message code="probe.jsp.title.servlets.app" arguments="${param.webapp}"/>
				</c:otherwise>
			</c:choose>
		</title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='servlets.css'/>"/>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
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
			<li id="viewServletMaps">
				<c:url value="/servletmaps.htm" var="servletmaps" scope="page">
					<c:if test="${not empty param.webapp}">
						<c:param name="webapp" value="${param.webapp}"/>
					</c:if>
				</c:url>
				<a href="${servletmaps}">
					<spring:message code="probe.jsp.servlets.opt.maps"/>
				</a>
			</li>
			<c:if test="${not empty param.webapp}">
				<li id="viewAllServlets">
					<c:url value="/servlets.htm" var="allservlets" scope="page" />
					<a href="${allservlets}">
						<spring:message code="probe.jsp.servlets.opt.all"/>
					</a>
				</li>
			</c:if>
		</ul>

		<div id="servletListContainer" class="embeddedBlockContainer">
			<jsp:include page="/servlets.ajax"/>
		</div>

		<script type="text/javascript">
			new Ajax.PeriodicalUpdater('servletListContainer',
			'<c:url value="/servlets.ajax?${pageContext.request.queryString}"/>',
			{frequency: 5});
		</script>

	</body>
</html>
