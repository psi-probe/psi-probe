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

<%--
	Displays current traffic information.

	Author: Vlad Ilyushchenko
--%>

<html>
	<head>
		<title><spring:message code="probe.jsp.title.status"/></title>
	</head>

	<body>

		<c:set var="navTabStatus" value="active" scope="request"/>

		<c:forEach items="${pools}" var="pool" varStatus="poolStatus">

			<div class="poolInfo">
				<h3>${pool.name}</h3>

				<div class="threadInfo">
					<span class="name"><spring:message code="probe.jsp.status.currentThreadCount"/></span>&nbsp;${pool.currentThreadCount}
					<span class="name"><spring:message code="probe.jsp.status.currentThreadsBusy"/></span>&nbsp;${pool.currentThreadsBusy}
					<span class="name"><spring:message code="probe.jsp.status.maxThreads"/></span>&nbsp;${pool.maxThreads}
					<span class="name"><spring:message code="probe.jsp.status.maxSpareThreads"/></span>&nbsp;${pool.maxSpareThreads}
					<span class="name"><spring:message code="probe.jsp.status.minSpareThreads"/></span>&nbsp;${pool.minSpareThreads}
				</div>
			</div>
		</c:forEach>

	</body>
</html>
