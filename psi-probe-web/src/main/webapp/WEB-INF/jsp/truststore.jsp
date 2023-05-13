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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title><spring:message code="probe.jsp.sysinfo.truststore.title"/></title>
	</head>

	<c:set var="navTabSystem" value="active" scope="request"/>
	<c:set var="systemTabTrustStore" value="active" scope="request"/>
	<c:set var="use_decorator" value="system" scope="request"/>

	<body>
		<c:choose>
			<c:when test="${ empty certificates}">
				<div class="errorMessage">
					<p>
						<spring:message code="probe.jsp.sysinfo.truststore.notAvailable"/>
					</p>
				</div>
			</c:when>
			<c:otherwise>
				<div id="systemProperties">
					<display:table name="certificates" class="genericTbl" cellspacing="0"
							requestURI="" defaultsort="2" defaultorder="ascending">
						<display:column property="alias" titleKey="probe.jsp.sysinfo.truststore.alias" sortable="true" class="leftmost"/>
						<display:column property="cn" titleKey="probe.jsp.sysinfo.truststore.cn" sortable="true" class="leftmost"/>
						<display:column property="expirationDate" titleKey="probe.jsp.sysinfo.truststore.expirationDate" sortable="true"/>
					</display:table>
				</div>
			</c:otherwise>
		</c:choose>
	</body>
</html>
