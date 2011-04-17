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
	Displays current thread pool information.

	Author: Vlad Ilyushchenko
--%>

<html>
	<head>
		<title><spring:message code="probe.jsp.title.threadpools"/></title>
	</head>

	<body>

		<c:set var="navTabThreads" value="active" scope="request"/>

		<ul class="options">
			<li id="threads">
				<a href="<c:url value='/threads.htm'/>">
					<spring:message code="probe.jsp.threadpools.menu.threads"/>
				</a>
			</li>
		</ul>

		<display:table name="pools" uid="pool" class="genericTbl" cellspacing="0" requestURI="" defaultsort="1" defaultorder="ascending">
			<display:column property="name"               sortable="true" titleKey="probe.jsp.threadpools.name"               style="white-space:nowrap;" class="leftmost"/>
			<display:column property="currentThreadCount" sortable="true" titleKey="probe.jsp.threadpools.currentThreadCount" style="white-space:nowrap;"/>
			<display:column property="currentThreadsBusy" sortable="true" titleKey="probe.jsp.threadpools.currentThreadsBusy" style="white-space:nowrap;"/>
			<display:column property="maxThreads"         sortable="true" titleKey="probe.jsp.threadpools.maxThreads"         style="white-space:nowrap;"/>
			<display:column property="maxSpareThreads"    sortable="true" titleKey="probe.jsp.threadpools.maxSpareThreads"    style="white-space:nowrap;"/>
			<display:column property="minSpareThreads"    sortable="true" titleKey="probe.jsp.threadpools.minSpareThreads"    style="white-space:nowrap;"/>
		</display:table>
	</body>
</html>
