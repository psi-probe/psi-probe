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
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>

<div class="shadow" style="clear: none;">
	<div class="info">
		<p>
			<spring:message code="probe.jsp.os.card.name"/>
			<span class="value">${runtime.osName}</span>
			<spring:message code="probe.jsp.os.card.version"/>
			<span class="value">${runtime.osVersion}</span>
			<spring:message code="probe.jsp.os.card.totalMemory"/>
			<span class="value"><probe:volume value="${runtime.totalPhysicalMemorySize}" fractions="2"/></span>
			<spring:message code="probe.jsp.os.card.freeMemory"/>
			<span class="value"><probe:volume value="${runtime.freePhysicalMemorySize}" fractions="2"/></span>
			<spring:message code="probe.jsp.os.card.committedVirtualMemory"/>
			<span class="value"><probe:volume value="${runtime.committedVirtualMemorySize}" fractions="2"/></span>
			<spring:message code="probe.jsp.os.card.totalSwap"/>
			<span class="value"><probe:volume value="${runtime.totalSwapSpaceSize}" fractions="2"/></span>
			<spring:message code="probe.jsp.os.card.freeSwap"/>
			<span class="value"><probe:volume value="${runtime.freeSwapSpaceSize}" fractions="2"/></span>
		</p>
	</div>
</div>
