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

<!--

	Displays a list of datasources grouped by JDBC URL with aggregated totals.
	Author: Andy Shapoval

-->

<html>
	<head>
		<title><spring:message code="probe.jsp.title.dataSourceGroups"/></title>
	</head>

	<body>

		<c:set var="navTabDatasources" value="active" scope="request"/>

		<ul class="options">
			<li id="back">
				<a href="<c:url value='/datasources.htm'/>">
					<spring:message code="probe.jsp.dataSourceGroups.menu.back"/>
				</a>
			</li>
		</ul>

		<c:choose>
			<c:when test="${! empty dataSourceGroups}">
				<div class="blockContainer">
					<div class="shadow">
						<div class="info">
							<p><spring:message code="probe.jsp.dataSourceGroups.information"/></p>
						</div>
					</div>

					<display:table class="genericTbl" cellspacing="0" name="dataSourceGroups" uid="dataSourceGroup" requestURI="">
						<display:column property="jdbcURL" class="leftmost" sortable="true" maxLength="50" nulls="true"
								titleKey="probe.jsp.dataSourceGroups.list.col.url"/>

						<display:column sortable="true" sortProperty="busyScore"
								titleKey="probe.jsp.dataSourceGroups.list.col.usage" class="score_wrapper">
							<div class="score_wrapper">
								<probe:score value="${dataSourceGroup.busyScore}" value2="${dataSourceGroup.establishedScore - dataSourceGroup.busyScore}" fullBlocks="10" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
									<img src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+"
											title="<spring:message code='probe.jsp.applications.jdbcUsage.title' arguments='${dataSourceGroup.busyScore},${dataSourceGroup.establishedScore}'/>"/>
								</probe:score>
							</div>
						</display:column>

						<display:column property="maxConnections" sortable="true"
								titleKey="probe.jsp.dataSourceGroups.list.col.max"/>

						<display:column property="establishedConnections" sortable="true"
								titleKey="probe.jsp.dataSourceGroups.list.col.established"/>

						<display:column property="busyConnections" sortable="true"
								titleKey="probe.jsp.dataSourceGroups.list.col.busy"/>

						<display:column property="dataSourceCount" sortable="true"
								titleKey="probe.jsp.dataSourceGroups.list.col.resourceCount"/>
					</display:table>
				</div>
			</c:when>
			<c:otherwise>
				<div class="errorMessage">
					<p>
						<spring:message code="probe.jsp.dataSourceGroups.empty"/>
					</p>
				</div>
			</c:otherwise>
		</c:choose>
	</body>
</html>
