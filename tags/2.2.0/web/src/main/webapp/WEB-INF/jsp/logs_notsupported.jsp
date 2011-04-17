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


<html>
	<head><title>Not supported</title></head>

	<c:set var="navTabLogs" value="active" scope="request"/>

	<body>
		<div class="errorMessage">
			<p>
				<%
					pageContext.setAttribute("vmName", System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version") + " " +
							System.getProperty("java.vm.name"));
				%>
				<spring:message code="probe.jsp.logs_notSupported.message" arguments="${vmName}"/>
			</p>
		</div>
	</body>
</html>
