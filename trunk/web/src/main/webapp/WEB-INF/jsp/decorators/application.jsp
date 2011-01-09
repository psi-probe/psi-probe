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
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="verticalMenu">
	<div>
		<p><spring:message code="probe.jsp.app.nav.title" arguments="${param.webapp}"/></p>
	</div>
	<ul>
		<li>
			<a class="${appTabSummary}" href="<c:url value='/appsummary.htm'/>?webapp=${param.webapp}&size=${param.size}">
				<spring:message code="probe.jsp.app.nav.summary"/>
			</a>
		</li>
		<li>
			<a class="${appTabSessions}" href="<c:url value='/sessions.htm'/>?webapp=${param.webapp}&size=${param.size}">
				<spring:message code="probe.jsp.app.nav.sessions"/>
			</a>
		</li>
		<li>
			<a class="${appTabAttributes}" href="<c:url value='/appattributes.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.attributes"/>
			</a>
		</li>
		<li>
			<a class="${appTabJSPs}" href="<c:url value='/app/jsp.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.jsps"/>
			</a>
		</li>
		<li>
			<a class="${appTabResources}" href="<c:url value='/resources.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.resources"/>
			</a>
		</li>
		<li>
			<a class="${appTabContextDescriptor}" href="<c:url value='/adm/viewcontextxml.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.contextxml"/>
			</a>
		</li>
		<li>
			<a class="${appTabDeploymentDescriptor}" href="<c:url value='/app/viewwebxml.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.webxml"/>
			</a>
		</li>
		<li>
			<a class="${appTabServlets}" href="<c:url value='/servlets.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.servlets"/>
			</a>
		</li>
		<li>
			<a class="${appTabFilters}" href="<c:url value='/appfilters.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.filters"/>
			</a>
		</li>
		<li>
			<a class="${appTabInitParams}" href="<c:url value='/appinitparams.htm'/>?webapp=${param.webapp}">
				<spring:message code="probe.jsp.app.nav.initParams"/>
			</a>
		</li>
	</ul>
</div>

<div id="contentBody">
	<decorator:body/>
</div>

