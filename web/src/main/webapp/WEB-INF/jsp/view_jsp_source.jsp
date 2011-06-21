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
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>

<html>
	<head>
		<title><spring:message code="probe.jsp.title.viewsource" arguments="${param.source}"/></title>
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
		<c:set var="appTabJSPs" value="active" scope="request"/>

		<ul class="options">
			<li id="back">
				<a href="<c:url value='/app/jsp.htm'>
					<c:param name='webapp' value='${param.webapp}'/>
					</c:url>">
					<spring:message code="probe.jsp.viewsource.opt.back"/>
				</a>
			</li>
			<c:if test="${! empty content || ! empty highlightedContent}">
				<c:if test="${item.state == 2}">
					<li id="viewservlet">
						<a href="<c:url value='/app/viewservlet.htm'>
								<c:param name='webapp' value='${param.webapp}'/>
								<c:param name='source' value='${param.source}'/>
								</c:url>">
							<spring:message code="probe.jsp.viewsource.opt.viewServlet"/>
						</a>
					</li>
				</c:if>
				<li id="compilesingle">
					<a href="<c:url value='/app/recompile.htm'>
							<c:param name='webapp' value='${param.webapp}'/>
							<c:param name='source' value='${param.source}'/>
							<c:param name='view' value='/app/viewsource.htm'/>
							</c:url>">
						<spring:message code="probe.jsp.viewsource.opt.compile"/>
					</a>
				</li>
			</c:if>
		</ul>

		<c:choose>
			<c:when test="${empty content && empty highlightedContent}">
				<div class="errorMessage">
					<p>
						<spring:message code="probe.jsp.viewsource.notfound"/>
					</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="embeddedBlockContainer">
					<h3><spring:message code="probe.jsp.viewsource.h3.info"/></h3>

					<div class="shadow">
						<div class="info">
							<p><spring:message code="probe.jsp.viewsource.appname"/>&nbsp;<span class="value">${param.webapp}</span>
								<spring:message code="probe.jsp.viewsource.filename"/>&nbsp;<span class="value">${item.name}</span>
								<spring:message code="probe.jsp.viewsource.size"/>&nbsp;<span class="value"><probe:volume value="${item.size}"/></span>
								<spring:message code="probe.jsp.viewsource.lastmodified"/>&nbsp;<span class="value">${item.timestamp}</span>
								<spring:message code="probe.jsp.viewsource.encoding"/>&nbsp;<span class="value">${item.encoding}</span>
								<spring:message code="probe.jsp.viewsource.state"/>&nbsp;<span class="value">
									<c:choose>
										<c:when test="${item.state == 1}"><spring:message code="probe.jsp.jsps.status.outdated"/>
										</c:when>
										<c:when test="${item.state == 2}"><spring:message code="probe.jsp.jsps.status.compiled"/>
										</c:when>
										<c:otherwise><span class="fail"><spring:message code="probe.jsp.jsps.status.failed"/></span>
										</c:otherwise>
									</c:choose>
								</span>
							</p>
						</div>
					</div>

					<c:if test="${! empty item.exception.message}">
						<div class="errors">
							<p>${item.exception.message}</p>
						</div>
					</c:if>

					<h3><spring:message code="probe.jsp.viewsource.h3.source"/></h3>

					<table id="resultsTable" cellspacing="0">
						<tr>
							<td id="left_scroller" class="scroller">&nbsp;</td>
							<td width="1%">&nbsp;</td>
							<td>
								<div id="srccontent" class="scrollable_content">
									<code>
										<c:choose>
											<c:when test="${! empty highlightedContent}">
												<c:out value="${highlightedContent}" escapeXml="false"/>
											</c:when>
											<c:otherwise>
												${content}
											</c:otherwise>
										</c:choose>
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

			</c:otherwise>
		</c:choose>
	</body>
</html>
