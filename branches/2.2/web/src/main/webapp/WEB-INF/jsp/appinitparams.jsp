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
	Displays a list of initialization parameters of a given application

	Author: Andy Shapoval
--%>

<html>

	<head>
		<title>
			<spring:message code="probe.jsp.title.app.initParams" arguments="${param.webapp}"/>
		</title>
	</head>

	<%--
		Make Tab #1 visually "active".
	--%>
	<c:set var="navTabApps" value="active" scope="request"/>
	<c:set var="use_decorator" value="application" scope="request"/>
	<c:set var="appTabInitParams" value="active" scope="request"/>

	<body>

		<div class="embeddedBlockContainer">
			<c:choose>
				<c:when test="${! empty appInitParams}">

					<h3><spring:message code="probe.jsp.app.initParams.h3.params"/></h3>

					<display:table htmlId="initParamTbl" name="appInitParams" uid="prm"
							class="genericTbl" cellspacing="0" cellpadding="0"
							requestURI="" defaultsort="2">
						<display:column class="leftMostIcon" title="&nbsp;" style="width:1px;">
							<c:choose>
								<c:when test="${prm.fromDeplDescr}">
									<img src="${pageContext.request.contextPath}<spring:theme code='deployment_descriptor.img'/>" alt="">
								</c:when>
								<c:otherwise>
									<img src="${pageContext.request.contextPath}<spring:theme code='context.img'/>" alt="">
								</c:otherwise>
							</c:choose>
						</display:column>
						<display:column property="name" sortable="true"
								titleKey="probe.jsp.app.initParams.col.name" maxLength="40"/>
						<display:column sortable="true" titleKey="probe.jsp.app.initParams.col.value">
							<c:choose>
								<c:when test="${allowedToViewValues}">
									<c:out value="${prm.value}" escapeXml="true"/>&nbsp;
								</c:when>
								<c:otherwise>
									**************
								</c:otherwise>
							</c:choose>
						</display:column>
						<display:column sortable="true"
								titleKey="probe.jsp.app.initParams.col.source">
							<c:choose>
								<c:when test="${prm.fromDeplDescr}">
									<spring:message code="probe.jsp.app.initParams.source.deplDescr"/>
								</c:when>
								<c:otherwise>
									<spring:message code="probe.jsp.app.initParams.source.context"/>
								</c:otherwise>
							</c:choose>
						</display:column>
					</display:table>
				</c:when>
				<c:otherwise>
					<div class="infoMessage">
						<p>
							<spring:message code="probe.jsp.app.initParams.empty"/>
						</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>
