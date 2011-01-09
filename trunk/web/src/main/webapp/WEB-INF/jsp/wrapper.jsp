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

<html>
	<head>
		<title><spring:message code="probe.jsp.title.wrapper"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<c:set var="navTabSystem" value="active" scope="request"/>
	<c:set var="systemTabWrapper" value="active" scope="request"/>
	<c:set var="use_decorator" value="system" scope="request"/>

	<body>
	<c:choose>
		<c:when test="${! wrapperInfo.controlledByWrapper}">
			<div class="errorMessage">
				<p><spring:message code="probe.jsp.wrapper.not_available"/></p>
			</div>
		</c:when>
		<c:otherwise>
			<ul class="options">
				<li id="thread_dump">
					<a href="#">
						<spring:message code="probe.jsp.wrapper.menu.thread_dump"/>
					</a>
				</li>
				<li id="stop_jvm">
					<a href="#">
						<spring:message code="probe.jsp.wrapper.menu.stop"/>
					</a>
				</li>
				<li id="restart_jvm">
					<a href="#">
						<spring:message code="probe.jsp.wrapper.menu.restart"/>
					</a>
				</li>
			</ul>

			<div id="msg" style="display: none;"></div>

			<h3><spring:message code="probe.jsp.wrapper.h3.info"/></h3>

			<div class="shadow" style="clear: none;">
				<div class="info">
					<p>
						<spring:message code="probe.jsp.wrapper.user"/>&nbsp;<span class="value">${wrapperInfo.user}</span>
						<spring:message code="probe.jsp.wrapper.interactive_user"/>&nbsp;<span class="value">${wrapperInfo.interactiveUser}</span>
						<spring:message code="probe.jsp.wrapper.java_pid"/>&nbsp;<span class="value">${wrapperInfo.jvmPid}</span>
						<spring:message code="probe.jsp.wrapper.pid"/>&nbsp;<span class="value">${wrapperInfo.wrapperPid}</span>
						<spring:message code="probe.jsp.wrapper.service"/>&nbsp;<span class="value">${wrapperInfo.launchedAsService}</span>
						<spring:message code="probe.jsp.wrapper.debug"/>&nbsp;<span class="value">${wrapperInfo.debugEnabled}</span>
						<spring:message code="probe.jsp.wrapper.version"/>&nbsp;<span class="value">${wrapperInfo.version}</span>
					</p>
				</div>
			</div>

			<h3><spring:message code="probe.jsp.wrapper.h3.props"/></h3>

			<div>
				<display:table name="wrapperInfo.properties" class="genericTbl" cellspacing="0"
						requestURI="" defaultsort="1" defaultorder="ascending">
					<display:column property="key" titleKey="probe.jsp.sysinfo.col.name" sortable="true" class="leftmost"/>
					<display:column property="value" titleKey="probe.jsp.sysinfo.col.value" sortable="true"/>
				</display:table>
			</div>

			<script type="text/javascript">

				function execute(url) {
					new Ajax.Updater('msg', url);
					Effect.Appear('msg');
					setTimeout('Effect.Fade(\'msg\')', 5000);
					return false;
				}

				var rules = {
					'#restart_jvm': function(element) {
						element.onclick = function() {
							return confirm('<spring:message code="probe.jsp.wrapper.confirm.restart"/>') &&
								execute('<c:url value="/adm/restartvm.ajax"/>');
						}
					},
					'#stop_jvm': function(element) {
						element.onclick = function() {
							return confirm('<spring:message code="probe.jsp.wrapper.confirm.stop"/>') &&
								execute('<c:url value="/adm/stopvm.ajax"/>');
						}
					},
					'#thread_dump': function(element) {
						element.onclick = function() {
							return execute('<c:url value="/adm/threaddump.ajax"/>');
						}

					}
				}
				Behaviour.register(rules);
			</script>
		</c:otherwise>
	</c:choose>
</body>
</html>
