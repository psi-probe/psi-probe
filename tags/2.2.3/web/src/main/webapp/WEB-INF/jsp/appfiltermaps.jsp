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
	Displays a list of web application filter mappings

	Author: Andy Shapoval
--%>

<html>

	<head>
		<title>
			<spring:message code="probe.jsp.title.app.filtermaps" arguments="${param.webapp}"/>
		</title>
	</head>

	<%--
		Make Tab #1 visually "active".
	--%>
	<c:set var="navTabApps" value="active" scope="request"/>
	<c:set var="use_decorator" value="application" scope="request"/>
	<c:set var="appTabFilters" value="active" scope="request"/>

	<body>

		<ul class="options">
			<li id="viewAppFilters">
				<a href="<c:url value='/appfilters.htm'>
						<c:param name='webapp' value='${param.webapp}'/>
						</c:url>">
					<spring:message code="probe.jsp.app.filtermaps.opt.defs"/>
				</a>
			</li>
		</ul>

		<div class="embeddedBlockContainer">
			<c:choose>
				<c:when test="${! empty filterMaps}">

					<h3><spring:message code="probe.jsp.app.filtermaps.h3.maps"/></h3>

					<display:table name="filterMaps" uid="fltr"
							class="genericTbl" cellspacing="0" cellpadding="0"
							requestURI="" defaultsort="1">
						<display:column sortProperty="url" sortable="true"
								titleKey="probe.jsp.app.filtermaps.col.url" class="leftmost">
							${fltr.url}&nbsp;
						</display:column>
						<display:column sortProperty="servletName" sortable="true"
								titleKey="probe.jsp.app.filtermaps.col.servletName">
							${fltr.servletName}&nbsp;
						</display:column>
						<display:column property="filterName" sortable="true"
								titleKey="probe.jsp.app.filtermaps.col.filterName"/>
						<display:column property="dispatcherMap" sortable="true"
								titleKey="probe.jsp.app.filtermaps.col.dispatcherMap"/>
						<display:column property="filterClass" sortable="true"
								titleKey="probe.jsp.app.filters.col.filterClass" maxLength="50"/>
					</display:table>
				</c:when>
				<c:otherwise>
					<div class="infoMessage">
						<p>
							<spring:message code="probe.jsp.app.filtermaps.empty"/>
						</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>
