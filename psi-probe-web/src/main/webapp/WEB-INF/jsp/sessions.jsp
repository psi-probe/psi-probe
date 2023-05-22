<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>

<%-- List of sessions view for a particular web application. It is assumed that the controller
 creates "session" command in the request. --%>

<!DOCTYPE html>
<html lang="${lang}">
	<head>
		<c:choose>
			<c:when test="${empty param.webapp}">
				<title><spring:message code="probe.jsp.title.sessions.all"/></title>
			</c:when>
			<c:otherwise>
				<title><spring:message htmlEscape="true" code="probe.jsp.title.sessions" arguments="${param.webapp}"/></title>
			</c:otherwise>
		</c:choose>
		<script src="<c:url value='/js/prototype.js'/>"></script>
		<script src="<c:url value='/js/scriptaculous/scriptaculous.js'/>"></script>
		<script src="<c:url value='/js/Tooltip.js'/>"></script>
		<script src="<c:url value='/js/func.js'/>"></script>
		<script src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<body>

		<%-- Make Tab #1 visually "active". --%>
		<c:set var="navTabApps" value="active" scope="request"/>
		<c:if test="${! empty param.webapp}">
			<c:set var="use_decorator" value="application" scope="request"/>
			<c:set var="appTabSessions" value="active" scope="request"/>
		</c:if>

		<div id="ttdiv" class="tooltip" style="display: none;">
			<div class="tt_top">
				<span id="tt_title" style="display: none;"></span>
				<a id="ttdiv_close" href="#"><spring:message code="probe.jsp.tooltip.close"/></a>
			</div>
			<div class="tt_content" id="tt_content"></div>
		</div>

		<form action="<c:url value='/app/expire_list.htm'/>"
				method="post" name="sessionForm"
				id="sessionForm">

			<c:if test="${! empty param.webapp}">
				<input type="hidden" name="webapp" value='<c:out value="${param.webapp}" />'/>
			</c:if>

			<ul class="options">
				<c:if test="${! empty sessions}">
					<li id="toggle">
						<a href="#" onclick="return inverse(sessionForm);">
							<spring:message code="probe.jsp.sessions.menu.toggle"/>
						</a>
					</li>
					<li id="delete">
						<a href="#" onclick="sessionForm.submit();">
							<spring:message code="probe.jsp.sessions.menu.expire"/>
						</a>
					</li>
				</c:if>
				<c:if test="${! empty param.webapp}">
					<li id="showAll">
						<a href="<c:url value='/sessions.htm'/>">
							<spring:message code="probe.jsp.sessions.opt.all"/>
						</a>
					</li>
				</c:if>
				<c:if test="${! empty sessions or searchInfo.apply}">
					<li id="showSearch" ${searchInfo.apply ? 'style="display: none;"' : ''}>
						<a href="#" onclick="showSearch();">
							<spring:message code="probe.jsp.sessions.menu.showSearch"/>
						</a>
					</li>
					<li id="clearSearch" ${! searchInfo.apply ? 'style="display: none;"' : ''}>
						<a href="#" onclick="clearSearch();">
							<spring:message code="probe.jsp.sessions.menu.clearSearch"/>
						</a>
					</li>
					<li id="applySearch" ${! searchInfo.apply ? 'style="display: none;"' : ''}>
						<a href="#" onclick="applySearch();">
							<spring:message code="probe.jsp.sessions.menu.applySearch"/>
						</a>
					</li>
				</c:if>
				<c:if test="${! empty sessions}">
					<c:choose>
						<c:when test="${param.size}">
							<li id="size">
								<a href="?<probe:toggle param='size'/>">
									<spring:message code="probe.jsp.hidesize"/>
								</a>
							</li>
						</c:when>
						<c:otherwise>
							<li id="size">
								<a href="?<probe:toggle param='size'/>">
									<spring:message code="probe.jsp.showsize"/>
								</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:if>
				<li id="abbreviations" ${! searchInfo.apply ? 'style="display: none;"' : ''}>
					<a href="#">
						<spring:message code="probe.jsp.sessions.menu.searchHelp"/>
					</a>
				</li>
			</ul>

			<div id="searchFormContainer" ${! searchInfo.apply ? 'style="display:none;"' : ''}>
				<div id="help" class="helpMessage" style="display: none;">
					<div class="ajax_activity"></div>
				</div>

				<h3><spring:message code="probe.jsp.sessions.search.h3"/></h3>
				<table id="searchFormTable">
					<tr>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.sessionId"/>
						</td>
						<td class="inputCell">
							<input id="searchSessionId" name="searchSessionId" type="text" value="${searchInfo.sessionId}" class="txtInput" size="40"/>
						</td>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.lastIp"/>
						</td>
						<td class="inputCell">
							<input name="searchLastIP" type="text" value="${searchInfo.lastIp}" class="txtInput" size="30"/>
						</td>
					</tr>
					<tr>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.idleTimeFrom"/>
						</td>
						<td class="inputCell">
							<input name="searchIdleTimeFrom" type="text" value="${searchInfo.idleTimeFrom}" class="txtInput" size="30"/>
						</td>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.idleTimeTo"/>
						</td>
						<td class="inputCell">
							<input name="searchIdleTimeTo" type="text" value="${searchInfo.idleTimeTo}" class="txtInput" size="30"/>
						</td>
					</tr>
					<tr>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.ageFrom"/>
						</td>
						<td class="inputCell">
							<input name="searchAgeFrom" type="text" value="${searchInfo.ageFrom}" class="txtInput" size="30"/>
						</td>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.ageTo"/>
						</td>
						<td class="inputCell">
							<input name="searchAgeTo" type="text" value="${searchInfo.ageTo}" class="txtInput" size="30"/>
						</td>
					</tr>
					<tr>
						<td class="labelCell">
							<spring:message code="probe.jsp.sessions.search.attrName"/>
						</td>
						<td class="inputCell">
							<input name="searchAttrName" type="text" value="${searchInfo.attrName}" class="txtInput" size="40"/>
						</td>
					</tr>
				</table>
			</div>

			<div class="embeddedBlockContainer">
				<c:if test="${searchInfo.apply}">
					<h3><spring:message code="probe.jsp.sessions.search.results.h3"/></h3>
				</c:if>

				<c:choose>
					<c:when test="${! empty sessions}">
						<c:if test="${! searchInfo.apply}">
							<h3><spring:message code="probe.jsp.sessions.h3"/></h3>
						</c:if>
						<display:table name="sessions" class="genericTbl" uid="session" style="border-spacing:0;border-collapse:separate;" pagesize="50"
								requestURI="">

							<display:column class="leftmost" title="&#160;">
								<input type="checkbox" name="sid_webapp" value="${session.id};${session.applicationName}"/>
							</display:column>

							<c:if test="${empty param.webapp}">
								<display:column sortProperty="applicationName" sortable="true"
										titleKey="probe.jsp.sessions.col.applicationName">
									<a href="<c:url value='/appsummary.htm'><c:param name='webapp' value='${session.applicationName}'/></c:url>">
										${session.applicationName}
									</a>&#160;
								</display:column>
							</c:if>

							<display:column titleKey="probe.jsp.sessions.col.id">
								<a href="<c:url value='/attributes.htm'><c:param name='webapp' value='${session.applicationName}' /><c:param name='sid' value='${session.id}' /><c:param name='size'><c:out value='${param.size}' /></c:param></c:url>">
									${session.id}
								</a>
							</display:column>

							<display:column titleKey="probe.jsp.sessions.col.lastIp" sortProperty="lastAccessedIp" sortable="true">
								<c:choose>
									<c:when test="${! empty session.lastAccessedIp}">
										<a id='ip_${session_rowNum}' href="#">${session.lastAccessedIp}</a>
										<script>
											addAjaxTooltip('ip_${session_rowNum}', 'ttdiv', '<c:url value="/whois.ajax?ip=${session.lastAccessedIp}"/>');
										</script>

									</c:when>
									<c:otherwise>
										<spring:message code="probe.jsp.sessions.unknown.ip"/>
									</c:otherwise>
								</c:choose>
							</display:column>

							<display:column title="&#160;" style="width:18px;">
								<c:choose>
									<c:when test="${! empty session.lastAccessedIpLocale.country && session.lastAccessedIpLocale.country != '**'}">
										<img border="0" src="<c:url value='/flags/${fn:toLowerCase(session.lastAccessedIpLocale.country)}.png'/>"
												alt="${session.lastAccessedIpLocale.country}"
												title="${session.lastAccessedIpLocale.displayCountry}"/>
									</c:when>
									<c:otherwise>
										&#160;
									</c:otherwise>
								</c:choose>
							</display:column>

							<display:column sortable="true" sortProperty="idleTime" titleKey="probe.jsp.sessions.col.idleTime">
								<probe:duration value="${session.idleTime}"/>
							</display:column>

							<display:column sortable="true" sortProperty="age" titleKey="probe.jsp.sessions.col.age">
								<probe:duration value="${session.age}"/>
							</display:column>

							<display:column property="expiryTime" sortable="true" nulls="false"
									titleKey="probe.jsp.sessions.col.expiryTime" style="white-space:nowrap;"/>
							<display:column property="objectCount" sortable="true"
									titleKey="probe.jsp.sessions.col.objectCount"/>

							<c:if test="${param.size}">
								<display:column sortProperty="size" sortable="true" titleKey="probe.jsp.sessions.col.size"
										class="highlighted">
									<probe:volume value="${session.size}"/>
								</display:column>
							</c:if>

							<display:column sortable="true" sortProperty="serializable"
									titleKey="probe.jsp.sessions.col.serializable">
								<c:choose>
									<c:when test="${session.serializable}">
										<span class="okValue"><spring:message code="probe.jsp.sessions.status.yes"/></span>
									</c:when>
									<c:otherwise>
										<span class="errorValue"><spring:message code="probe.jsp.sessions.status.no"/></span>
									</c:otherwise>
								</c:choose>
							</display:column>

						</display:table>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${searchInfo.apply}">
								<c:if test="${! empty searchInfo.infoMessage}">
									<div class="infoMessage">
										<p>${searchInfo.infoMessage}</p>
									</div>
								</c:if>
								<c:if test="${! empty searchInfo.errorMessages}">
									<div class="errorMessage">
										<c:forEach items="${searchInfo.errorMessages}" var="msg">
											<p>${msg}</p>
										</c:forEach>
									</div>
								</c:if>
							</c:when>
							<c:otherwise>
								<div class="infoMessage">
									<p>
										<spring:message code="probe.jsp.sessions.empty"/>
									</p>
								</div>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</form>

		<c:url value="/sessions.htm" var="urlApply">
			<c:param name="searchAction" value="apply"/>
			<c:param name="size">
				<c:out value="${param.size}"/>
			</c:param>
			<c:if test="${not empty param.webapp}">
				<c:param name="webapp" value="${param.webapp}"/>
			</c:if>
		</c:url>

		<c:url value="/sessions.htm" var="urlClear">
			<c:param name="searchAction" value="clear"/>
			<c:param name="size">
				<c:out value="${param.size}"/>
			</c:param>
			<c:if test="${not empty param.webapp}">
				<c:param name="webapp" value="${param.webapp}"/>
			</c:if>
		</c:url>

		<script>
			var rules = {
				'#ttdiv_close': function(e) {
					e.onclick = function(e) {
						Effect.Fade('ttdiv');
						return false
					}
				}
			}
			Behaviour.register(rules);

			setupHelpToggle('<c:url value="/help/sessionsearch.ajax"/>');

			function showSearch() {
				Element.hide('showSearch');
				Element.show('applySearch');
				Element.show('clearSearch');
				Element.show('abbreviations');
				Effect.Appear('searchFormContainer', {duration:0.15});
				$('searchSessionId').focus();
			}

			function applySearch() {
				$('sessionForm').action = '<c:out value="${urlApply}" />';
				$('sessionForm').submit();
			}

			function clearSearch() {
				$('sessionForm').action = '<c:out value="${urlClear}" />';
				$('sessionForm').submit();
			}
		</script>

	</body>
</html>
