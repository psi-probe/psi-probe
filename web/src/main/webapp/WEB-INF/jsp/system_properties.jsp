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

<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html>
	<head>
		<title><spring:message code="probe.jsp.sysinfo.sysproperties.title"/></title>
	</head>

	<c:set var="navTabSystem" value="active" scope="request"/>
	<c:set var="systemTabSysProps" value="active" scope="request"/>
	<c:set var="use_decorator" value="system" scope="request"/>

	<body>

		<div id="systemProperties">
			<display:table name="systemInformation.systemPropertySet" class="genericTbl" cellspacing="0"
					requestURI="" defaultsort="1" defaultorder="ascending">
				<display:column property="key" titleKey="probe.jsp.sysinfo.col.name" sortable="true" class="leftmost"/>
				<display:column property="value" titleKey="probe.jsp.sysinfo.col.value" sortable="true"/>
			</display:table>
		</div>
	</body>
</html>
