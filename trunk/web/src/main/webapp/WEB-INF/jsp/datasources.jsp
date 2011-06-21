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
		<title><spring:message code="probe.jsp.title.datasources"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<body>

		<c:set var="navTabDatasources" value="active" scope="request"/>

		<c:if test="${! empty errorMessage}">
			<div class="errorMessage">
				<p>
					${errorMessage}
				</p>
			</div>
		</c:if>

		<c:choose>
			<c:when test="${! empty privateResources || ! empty globalResources}">

				<ul class="options">
					<li id="groupByJdbcUrl">
						<a href="<c:url value='/datasourcegroups.htm' />">
							<spring:message code="probe.jsp.datasources.opt.groupByJdbcUrl"/>
						</a>
					</li>
					<li id="abbreviations">
						<a href="#">
							<spring:message code="probe.jsp.generic.abbreviations"/>
						</a>
					</li>
				</ul>

				<div class="blockContainer">

					<div id="help" class="helpMessage" style="display: none;">
						<div class="ajax_activity"></div>
					</div>

					<c:if test="${supportsGlobal}">
						<h3>
							<spring:message code="probe.jsp.datasources.h3.global"/>
						</h3>
						<c:choose>
							<c:when test="${not empty globalResources}">
								<c:set var="resources" value="${globalResources}" scope="request" />
								<c:set var="isGlobalResources" value="true" scope="request" />
								<jsp:include page="datasources_table.jsp" />
							</c:when>
							<c:otherwise>
								<p><spring:message code="probe.jsp.datasources.global.empty" /></p>
							</c:otherwise>
						</c:choose>
					</c:if>

					<c:if test="${supportsPrivate}">
						<h3>
							<spring:message code="probe.jsp.datasources.h3.app"/>
						</h3>
						<c:choose>
							<c:when test="${not empty privateResources}">
								<c:set var="resources" value="${privateResources}" scope="request" />
								<c:set var="isGlobalResources" value="false" scope="request" />
								<jsp:include page="datasources_table.jsp" />
							</c:when>
							<c:otherwise>
								<p><spring:message code="probe.jsp.datasources.app.empty" /></p>
							</c:otherwise>
						</c:choose>
					</c:if>

				</div>

				<script type="text/javascript">
					setupHelpToggle('<c:url value="/help/datasources.ajax"/>');
				</script>

			</c:when>
			<c:otherwise>
				<div class="infoMessage">
					<p>
						<spring:message code="probe.jsp.datasources.empty"/>
					</p>
				</div>
			</c:otherwise>
		</c:choose>
	</body>
</html>
