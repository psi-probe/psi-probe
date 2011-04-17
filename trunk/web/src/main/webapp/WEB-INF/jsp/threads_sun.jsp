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
		<title><spring:message code="probe.jsp.title.threads"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/effects.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/Tooltip.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
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

				<div id="ttdiv" class="tooltip" style="display: none;">
					<div class="tt_top">
						<span id="tt_title" style="display: none;"></span>
						<a id="ttdiv_close" href="#"><spring:message code="probe.jsp.tooltip.close"/></a>
					</div>

					<div class="tt_content" id="tt_content"></div>
				</div>

				<div id="help" class="helpMessage" style="display: none;">
					<div class="ajax_activity"></div>
				</div>

				<display:table name="threads" uid="th" class="genericTbl" cellspacing="0" requestURI="" defaultsort="4"
						defaultorder="ascending">

					<display:column class="leftMostIcon" title="&nbsp;" style="width:20px;">
						<c:set var="confirmMessage">
							<spring:message code="probe.jsp.threads.killmsg" arguments="${th.name}"/>
						</c:set>
						<a class="imglink"
								onclick="return confirm('${confirmMessage}')"
								href="<c:url value='/adm/kill.htm'>
								<c:param name='thread' value='${th.name}'/>
								</c:url>">
							<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='delete.png'/>"
									alt="<spring:message code='probe.jsp.threads.stop.alt'/>"/>
						</a>
					</display:column>


					<display:column property="id" sortable="true" sortProperty="id" titleKey="probe.jsp.threads.col.id"/>

					<display:column sortable="true" property="name" titleKey="probe.jsp.threads.col.name"/>

					<display:column titleKey="probe.jsp.threads.col.execPoint">
						<c:choose>
							<c:when test="${! empty th.executionPoint}">
								<a id="tt${th.id}">
									<c:set var="element" value="${th.executionPoint}"/>

									${element.className}.${element.methodName}
									(
									<c:choose>
										<c:when test="${!element.nativeMethod && element.lineNumber > 0}">
											${element.fileName}:${element.lineNumber}
										</c:when>
										<c:when test="${element.nativeMethod}">
											<spring:message code="probe.jsp.threadstack.native"/>
										</c:when>
										<c:otherwise>
											<spring:message code="probe.jsp.threadstack.unknown"/>
										</c:otherwise>
									</c:choose>
									)
								</a>
								<script type="text/javascript">
									addAjaxTooltip('tt${th.id}', 'ttdiv', '<c:url value="/app/threadstack.ajax"/>?id=${th.id}');
								</script>
							</c:when>
							<c:otherwise>
								<spring:message code="probe.jsp.threadstack.unavailable"/>
							</c:otherwise>
						</c:choose>

					</display:column>

					<display:column property="state" sortable="true" sortProperty="state" titleKey="probe.jsp.threads.col.state"/>
					<display:column property="inNative" sortable="true" sortProperty="inNative" titleKey="probe.jsp.threads.col.inNative"/>
					<display:column property="suspended" sortable="true" sortProperty="suspended" titleKey="probe.jsp.threads.col.suspended"/>
					<display:column property="waitedCount" sortable="true" sortProperty="waitedCount" titleKey="probe.jsp.threads.col.waitedCount"/>
					<display:column property="blockedCount" sortable="true" sortProperty="blockedCount" titleKey="probe.jsp.threads.col.blockedCount"/>
				</display:table>
			</div>
		</div>

		<script type="text/javascript">
			setupHelpToggle('<c:url value="/help/threads2.ajax"/>');
			var rules = {
				'#ttdiv_close': function(e) {
					e.onclick = function(e) {
						Effect.Fade('ttdiv');
						return false
					}
				}
			}
			Behaviour.register(rules);
		</script>

	</body>
</html>
