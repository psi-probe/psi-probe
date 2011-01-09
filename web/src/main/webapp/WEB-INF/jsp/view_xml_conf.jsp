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
	Displays deployment descriptor (web.xml) or a context descriptor (context.xml) of a web application
	displayTarget model object denotes type of a file to be to be displayed

	Author: Andy Shapoval
--%>

<html>
	<head>
		<title><spring:message code="probe.jsp.title.app.viewXMLConf" arguments="${param.webapp},${fileDesc}"/></title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='syntax.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='scroller.css'/>"/>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/areascroller.js'/>"></script>
	</head>

	<body>
		<c:set var="navTabApps" value="active" scope="request"/>
		<c:set var="use_decorator" value="application" scope="request"/>
		<c:choose>
			<c:when test="${displayTarget eq 'web.xml'}">
				<c:set var="appTabDeploymentDescriptor" value="active" scope="request"/>
			</c:when>
			<c:when test="${displayTarget eq 'context.xml'}">
				<c:set var="appTabContextDescriptor" value="active" scope="request"/>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${empty content}">
				<div class="infoMessage">
					<p>
						<spring:message code="probe.jsp.app.viewXMLConf.notfound" arguments="${fileDesc}"/>
					</p>
				</div>
			</c:when>
			<c:otherwise>
				<ul class="options">
					<li id="download">
						<a href="<c:url value='${downloadUrl}'><c:param name='webapp' value='${param.webapp}'/></c:url>">
							<spring:message code="probe.jsp.follow.menu.download"/>
						</a>
					</li>
				</ul>

				<div class="embeddedBlockContainer">
					<h3><spring:message code="probe.jsp.app.viewXMLConf.h3.scr" arguments="${fileName}"/></h3>
					<table id="resultsTable" cellspacing="0">
						<tr>
							<td id="left_scroller" class="scroller">&nbsp;</td>
							<td width="1%">&nbsp;</td>
							<td>
								<div id="srccontent" class="scrollable_content">
									<code>
										<c:out value="${content}" escapeXml="false"/>
									</code>
								</div>
							</td>
							<td id="right_scroller" class="scroller">&nbsp;</td>
						</tr>
					</table>
					<script type="text/javascript">
						setupScrollers('srccontent');
					</script>
				</div>
			</c:otherwise>
		</c:choose>
	</body>
</html>
