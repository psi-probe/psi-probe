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
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>

<html>

	<head>
		<title><spring:message code="probe.jsp.title.resources" arguments="${param.webapp}"/></title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='resources.css'/>"/>
	</head>

	<body>

		<c:set var="navTabApps" value="active" scope="request"/>
		<c:set var="use_decorator" value="application" scope="request"/>
		<c:set var="appTabResources" value="active" scope="request"/>


		<c:choose>
			<c:when test="${! empty resources}">
				<c:if test="${! empty requestScope.errorMessage}">
					<div class="erroMessage">
						<p>
							${requestScope.errorMessage}
						</p>
					</div>
				</c:if>
				<div id="resources">
					<display:table class="genericTbl" name="resources" uid="resource" cellspacing="0">
						<display:column class="leftmost" sortable="true" sortProperty="name"
								titleKey="probe.jsp.resources.col.name">
							<a href="<c:url value='/sql/datasourcetest.htm'/>?webapp=${resource.applicationName}&resource=${resource.name}">
								${resource.name}
							</a>
						</display:column>

						<display:column titleKey="probe.jsp.resources.col.type">
							${resource.type}<br/>
							<c:choose>
								<c:when test="${resource.dataSourceInfo != null}">
									<div class="dbConnAdditionalInfo">
										<spring:message code="probe.jsp.resources.info.title"/>
										<b>${resource.dataSourceInfo.jdbcURL}</b>
										<spring:message code="probe.jsp.resources.info.max"/>&nbsp;<b>${resource.dataSourceInfo.maxConnections}</b>
										<spring:message code="probe.jsp.resources.info.busy"/>&nbsp;<b>${resource.dataSourceInfo.busyConnections}</b>
										<spring:message code="probe.jsp.resources.info.established"/>&nbsp;<b>${resource.dataSourceInfo.establishedConnections}</b>
										<c:if test="${resource.dataSourceInfo.resettable}">
											<b>&nbsp;
												<a href="<c:url value='/app/resetds.htm'/>?webapp=${param.webapp}&resource=${resource.name}">
													<img border="0"
															src="${pageContext.request.contextPath}<spring:theme code='reset.gif'/>"
															alt="<spring:message code='probe.jsp.resources.info.reset.alt'/>"/>
												</a>
											</b>
										</c:if>
										<div class="score_wrapper">
											<probe:score value="${resource.dataSourceInfo.busyScore}" value2="${resource.dataSourceInfo.establishedScore - resource.dataSourceInfo.busyScore}" fullBlocks="10" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
												<img src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+"
														title="<spring:message code='probe.jsp.resources.info.connectionUsage.alt' arguments='${resource.dataSourceInfo.busyScore},${resource.dataSourceInfo.establishedScore}'/>"/>
											</probe:score>
										</div>
									</div>
								</c:when>
								<c:when test="${!resource.lookedUp}">
									<div class="dbConnMisconfigured">
										<img src="${pageContext.request.contextPath}<spring:theme code='exclamation.gif'/>"
												alt="<spring:message code='probe.jsp.resources.info.misconfigured.alt'/>"/>
										<spring:message code="probe.jsp.resources.info.misconfigured"/>
									</div>
								</c:when>
							</c:choose>
						</display:column>

						<display:column titleKey="probe.jsp.resources.col.linkTo">
							${resource.linkTo}&nbsp;
						</display:column>

						<display:column titleKey="probe.jsp.resources.col.auth">
							${resource.auth}&nbsp;
						</display:column>

					</display:table>
				</div>
			</c:when>
			<c:otherwise>
				<div class="infoMessage">
					<p>
					<spring:message code="probe.jsp.resources.empty"/>
					</p>
				</div>
			</c:otherwise>
		</c:choose>
	</body>
</html>
