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
		<p><spring:message code="probe.jsp.decorator.system.title"/></p>
	</div>
	<ul>
		<li>
			<a class="${systemTabOverview}" href="<c:url value='/sysinfo.htm'/>">
				<spring:message code="probe.jsp.decorator.system.overview"/>
			</a>
		</li>
		<li>
			<a class="${systemTabMemory}" href="<c:url value='/memory.htm'/>">
				<spring:message code="probe.jsp.decorator.system.memory"/>
			</a>
		</li>
		<li>
			<a class="${systemTabSysProps}" href="<c:url value='/sysprops.htm'/>">
				<spring:message code="probe.jsp.decorator.system.props"/>
			</a>
		</li>
		<li>
			<a class="${systemTabOsInfo}" href="<c:url value='/adm/osinfo.htm'/>">
				<spring:message code="probe.jsp.decorator.system.os"/>
			</a>
		</li>
		<li>
			<a class="${systemTabWrapper}" href="<c:url value='/wrapper.htm'/>">
				<spring:message code="probe.jsp.decorator.system.wrapper"/>
			</a>
		</li>
	</ul>
</div>

<div id="contentBody">
	<decorator:body/>
</div>

