<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="${lang}" xml:lang="${lang}>
	<head>
		<title><spring:message code="probe.jsp.sysinfo.oshi.title"/></title>
	</head>

	<c:set var="navTabSystem" value="active" scope="request"/>
	<c:set var="systemTabOshi" value="active" scope="request"/>
	<c:set var="use_decorator" value="system" scope="request"/>

	<body>
		<div id="oshi">
			<h3><spring:message code="probe.jsp.sysinfo.oshi.title"/></h3>
			<table>
				<c:forEach items="${oshi}" var="oshi">
					<tr>
						<td style="white-space: pre"><c:out value="${oshi}" /></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>
