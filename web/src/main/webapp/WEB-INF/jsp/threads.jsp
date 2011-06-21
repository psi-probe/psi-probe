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

<html>
	<head>
		<title><spring:message code="probe.jsp.title.threads"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<c:set var="navTabThreads" value="active" scope="request"/>

	<body>

		<div>
			<ul class="options">
				<li id="pools">
					<a href="<c:url value='/threadpools.htm'/>">
						<spring:message code="probe.jsp.threads.menu.threadpools"/>
					</a>
				</li>
				<li id="abbreviations">
					<a href="#">
						<spring:message code="probe.jsp.generic.abbreviations"/>
					</a>
				</li>
			</ul>

			<div class="blockContainer">

				<div id="help" class="helpMessage" style="display: none;">
					<div class="ajax_activity"></div>
				</div>

				<display:table name="threads" uid="th" class="genericTbl" cellspacing="0" requestURI="" defaultsort="4"
						defaultorder="ascending">
					<display:column class="leftMostIcon" title="&nbsp;">
						<c:set var="confirmMessage">
							<spring:message code="probe.jsp.threads.killmsg" arguments="${th.name}"/>
						</c:set>
						<a class="imglink"
								onclick="return confirm('${confirmMessage}')"
								href="<c:url value='/adm/kill.htm'><c:param name='thread' value='${th.name}'/></c:url>">
							<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='delete.png'/>"
									alt="<spring:message code='probe.jsp.threads.stop.alt'/>"/>
						</a>
					</display:column>
					<display:column property="name" sortable="true" titleKey="probe.jsp.threads.col.name" maxLength="30" style="white-space:nowrap;"/>
					<display:column property="priority" sortable="true" titleKey="probe.jsp.threads.col.priority"/>
					<display:column sortable="true" sortProperty="appName" titleKey="probe.jsp.threads.col.application" style="white-space:nowrap;">
						${th.appName}&nbsp;
					</display:column>

					<display:column sortable="true" sortProperty="classLoader" titleKey="probe.jsp.threads.col.classLoader" style="white-space:nowrap;">
						<c:set var="clUrl">
							<c:url value="/cldetails.ajax?thread=${th.name}"/>
						</c:set>
						<span class="expandable" onclick="toggleAndReloadPanel('dd${th_rowNum}','${clUrl}')">
							<probe:out value="${th.classLoader}" maxLength="40" ellipsisRight="false"/>&nbsp;
						</span>

						<div id="dd${th_rowNum}" class="urlinfo" style="display: none;">
							<spring:message code="probe.jsp.threads.info.loading"/></div>
						</display:column>

					<display:column property="groupName" sortable="true" titleKey="probe.jsp.threads.col.groupName" maxLength="15" style="white-space:nowrap;"/>
					<display:column property="threadClass" sortable="true" titleKey="probe.jsp.threads.col.threadClass" maxLength="30" style="white-space:nowrap;"/>
					<display:column sortable="true" sortProperty="runnableClassName" titleKey="probe.jsp.threads.col.runnableClass" maxLength="30" style="white-space:nowrap;">
						${th.runnableClassName}&nbsp;
					</display:column>
					<display:column property="daemon" sortable="true" titleKey="probe.jsp.threads.col.daemon"/>
					<display:column property="interrupted" sortable="true" titleKey="probe.jsp.threads.col.interrupted"/>
				</display:table>
			</div>
		</div>

		<script type="text/javascript">
			setupHelpToggle('<c:url value="/help/threads.ajax"/>');
		</script>

	</body>
</html>
