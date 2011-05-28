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
	<head>
		<title><spring:message code="probe.jsp.title.servlet_source"/></title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='java_syntax.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='scroller.css'/>"/>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/areascroller.js'/>"></script>
	</head>

	<c:set var="navTabApps" value="active" scope="request"/>
	<c:set var="use_decorator" value="application" scope="request"/>
	<c:set var="appTabJSPs" value="active" scope="request"/>

	<body>

		<ul class="options">
			<li id="back">
				<a href="<c:url value='/app/jsp.htm'>
						<c:param name='webapp' value='${param.webapp}'/>
						</c:url>">
					<spring:message code="probe.jsp.viewsource.opt.back"/>
				</a>
			</li>
			<li id="viewJSP">
				<a href="<c:url value='/app/viewsource.htm'>
						<c:param name='webapp' value='${param.webapp}'/>
						<c:param name='source' value='${param.source}'/>
						</c:url>">
					<spring:message code="probe.jsp.servlet_source.opt.jsp"/>
				</a>
			</li>
			<li id="download">
				<a href="<c:url value='/app/downloadserv.htm'>
						<c:param name='webapp' value='${param.webapp}'/>
						<c:param name='source' value='${param.source}'/>
						</c:url>">
					<spring:message code="probe.jsp.follow.menu.download"/>
				</a>
			</li>

		</ul>
		<div class="embeddedBlockContainer">
			<h3><spring:message code="probe.jsp.servlet_source.h3.source"/></h3>

			<table id="resultsTable" cellspacing="0">
				<tr>
					<td id="left_scroller" class="scroller">&nbsp;</td>
					<td id="separator" width="1%" style="display: none;">&nbsp;</td>
					<td>
						<div class="scrollable_content" id="srccontent">
							<code>
								<c:out value="${content}" escapeXml="false"/>
							</code>
						</div>
					</td>
					<td id="right_scroller" class="scroller">&nbsp;</td>
				</tr>
			</table>
		</div>

		<script type="text/javascript">
			setupScrollers('srccontent');
		</script>

	</body>
</html>
