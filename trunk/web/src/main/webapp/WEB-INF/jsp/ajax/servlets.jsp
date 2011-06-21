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

<%--
	An Ajax spippet that displays a list of servlets

	Author: Andy Shapoval, Vlad Ilyushchenko
--%>

<c:choose>
	<c:when test="${! empty servlets}">

		<h3><spring:message code="probe.jsp.servlets.h3.defs"/></h3>

		<display:table htmlId="servletTbl" name="servlets" uid="svlt"
				class="genericTbl" cellspacing="0" cellpadding="0"
				requestURI="" defaultsort="${empty param.webapp ? 6 : 5}" defaultorder="descending">
			<c:if test="${empty param.webapp}">
				<display:column sortProperty="applicationName" sortable="true"
						titleKey="probe.jsp.servlets.col.applicationName" class="leftmost">
					<a href="<c:url value='/appsummary.htm'><c:param name='webapp' value='${svlt.applicationName}'/></c:url>">${svlt.applicationName}</a>
				</display:column>
			</c:if>
			<display:column sortProperty="servletName" sortable="true"
					titleKey="probe.jsp.servlets.col.servletName" class="${! empty param.webapp ? 'leftmost' : ''}">
				<div class="servletName"><probe:out value="${svlt.servletName}" maxLength="40"/>
					<c:if test="${! empty svlt.mappings}">
						<span>
							(<probe:out maxLength="40"><c:forEach var="mp" items="${svlt.mappings}" varStatus="mpSt">${mp}<c:if test="${! mpSt.last}">,</c:if></c:forEach></probe:out>)
						</span>
					</c:if>
				</div>
				<div class="servletClass"><probe:out value="${svlt.servletClass}" maxLength="50"/></div>
			</display:column>
			<display:column sortProperty="available" sortable="true"
					titleKey="probe.jsp.servlets.col.available">
				<c:choose>
					<c:when test="${svlt.available}">
						<span class="okValue"><spring:message code="probe.jsp.generic.yes"/></span>
					</c:when>
					<c:otherwise>
						<span class="errorValue"><spring:message code="probe.jsp.generic.no"/></span>
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column sortable="true" sortProperty="loadOnStartup"
					titleKey="probe.jsp.servlets.col.loadOnStartup">
				<c:choose>
					<c:when test="${svlt.loadOnStartup == -1}">
						<span class="errorValue"><spring:message code="probe.jsp.generic.no"/></span>
					</c:when>
					<c:otherwise>
						${svlt.loadOnStartup}
					</c:otherwise>
				</c:choose>

			</display:column>
			<display:column sortable="true" sortProperty="loadTime"
					titleKey="probe.jsp.servlets.col.loadTime">
				${svlt.loadTime}
			</display:column>
			<display:column property="requestCount" sortable="true"
					titleKey="probe.jsp.servlets.col.requestCount"/>
			<display:column sortable="true" sortProperty="processingTime"
					titleKey="probe.jsp.servlets.col.processingTime">
				<probe:duration value="${svlt.processingTime}"/>
			</display:column>
			<display:column property="errorCount" sortable="true"
					titleKey="probe.jsp.servlets.col.errorCount"/>
			<display:column sortable="true" sortProperty="minTime"
					titleKey="probe.jsp.servlets.col.minTime">
				${svlt.minTime}
			</display:column>
			<display:column sortable="true" sortProperty="maxTime"
					titleKey="probe.jsp.servlets.col.maxTime">
				${svlt.maxTime}
			</display:column>
			<display:column sortProperty="singleThreaded" sortable="true"
					titleKey="probe.jsp.servlets.col.multiThreaded">
				<c:choose>
					<c:when test="${!svlt.singleThreaded}">
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
				<spring:message code="probe.jsp.servlets.empty"/>
			</p>
		</div>
	</c:otherwise>
</c:choose>
