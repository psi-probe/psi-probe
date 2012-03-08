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

<%--
	Probe "home" page. Displays list of web applications.
	It is assumed that command by the name "apps" is created by the controller.

	Author: Vlad Ilyushchenko
--%>

<html>

	<head>
		<title><spring:message code="probe.jsp.title.applications"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/effects.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<body>

	<c:set var="navTabApps" value="active" scope="request"/>

	<script type="text/javascript">
		function handleContextReload(idx, context) {
			var img = $('ri_'+idx);
			var status = $('rs_'+idx);
			var reload_url = '<c:url value="/app/reload.ajax"/>?webapp='+context;
			img.src='${pageContext.request.contextPath}<spring:theme code="animated_reset.gif"/>';
			status.update('wait...');
			new Ajax.Updater(status,reload_url, {
				method:'get',
				asynchronous:true,
				onComplete:function(response) {
					img.src='${pageContext.request.contextPath}<spring:theme code="reset.gif"/>';
					updateStatusClass(status, response.responseText);
				}
			});
			return false;
		}

		function toggleContext(idx, url, context) {
			var status = $('rs_'+idx);
			status.update('<img border="0" src="${pageContext.request.contextPath}<spring:theme code="progressbar_editnplace.gif"/>"/>');
			new Ajax.Updater(status, url+'?webapp='+context, {
				method:'get',
				asynchronous:true,
				onComplete:function(response) {
					updateStatusClass(status, response.responseText);
				}
			});
			return false;
		}

		function updateStatusClass(status, responseText) {
			if (responseText.include("<spring:message code='probe.jsp.applications.status.up'/>")) {
				status.addClassName('okValue').removeClassName('errorValue');
			} else if (responseText.include("<spring:message code='probe.jsp.applications.status.down'/>")) {
				status.addClassName('errorValue').removeClassName('okValue');
			}
		}

	</script>

	<ul class="options">
		<li id="allStats">
			<a href="<c:url value='/allappstats.htm'/>">
				<spring:message code="probe.jsp.title.allappstats"/>
			</a>
		</li>
		<c:choose>
			<c:when test="${param.size}">
				<li id="size">
					<a href="?<probe:toggle param='size'/>">
						<spring:message code="probe.jsp.applications.hidesize"/>
					</a>
				</li>
			</c:when>
			<c:otherwise>
				<li id="size">
					<a href="?<probe:toggle param='size'/>">
						<spring:message code="probe.jsp.applications.showsize"/>
					</a>
				</li>
			</c:otherwise>
		</c:choose>
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

		<c:if test="${! empty errorMessage}">
			<div class="errorMessage">
				<p>
					${errorMessage}
				</p>
			</div>
		</c:if>

		<c:if test="${! empty successMessage}">
			<div id="successMessage">
				${successMessage}
			</div>
		</c:if>

		<display:table class="genericTbl" name="apps" uid="app" cellspacing="0" requestURI="" defaultsort="1"
				defaultorder="ascending" cellpadding="0">

			<display:column class="leftMostIcon" title="&nbsp;">
				<c:set var="confirmMessage">
					<spring:message code="probe.jsp.applications.undeploy.confirm" arguments="${app.name}"/>
				</c:set>
				<a class="imglink" href="<c:url value='/adm/undeploy.htm'><c:param name='webapp' value='${app.name}'/></c:url>"
						onclick="return confirm('${confirmMessage}')">
					<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='remove.img'/>"
							alt="<spring:message code='probe.jsp.applications.alt.undeploy'/>"
							title="<spring:message code='probe.jsp.applications.title.undeploy' arguments='${app.name}'/>"/>
				</a>
			</display:column>

			<display:column sortable="true" sortProperty="name" titleKey="probe.jsp.applications.col.name">
				<a href="<c:url value='/appsummary.htm'><c:param name='webapp' value='${app.name}'/><c:param name='size' value='${param.size}'/></c:url>">
					${app.name}
				</a>
			</display:column>

			<display:column sortable="true" titleKey="probe.jsp.applications.col.status" sortProperty="available">
				<c:url var="toggleAppUrl" value="/app/toggle.ajax"/>
				<c:choose>
					<c:when test="${app.available}">
						<a onclick="return toggleContext('${app_rowNum}', '${toggleAppUrl}', '${app.name}');"
								href="<c:url value='/app/stop.htm'/>?webapp=${app.name}"
								title="<spring:message code='probe.jsp.applications.title.status.up' arguments='${app.name}'/>">
							<div class="okValue" id="rs_${app_rowNum}">
								<spring:message code="probe.jsp.applications.status.up"/>
							</div>
						</a>
					</c:when>
					<c:otherwise>
						<a onclick="return toggleContext('${app_rowNum}', '${toggleAppUrl}', '${app.name}');"
								href="<c:url value='/app/start.htm'/>?webapp=${app.name}"
								title="<spring:message code='probe.jsp.applications.status.down.title' arguments='${app.name}'/>">
							<div class="errorValue" id="rs_${app_rowNum}">
								<spring:message code="probe.jsp.applications.status.down"/>
							</div>
						</a>
					</c:otherwise>
				</c:choose>
			</display:column>

			<display:column title="&nbsp;">
				<a onclick="return handleContextReload('${app_rowNum}', '${app.name}');"
						class="imglink"
						href="<c:url value='/app/reload.htm'/>?webapp=${app.name}">
					<img id='ri_${app_rowNum}'
							border="0" src="${pageContext.request.contextPath}<spring:theme code='reset.gif'/>"
							alt="<spring:message code='probe.jsp.applications.alt.reload'/>"
							title="<spring:message code='probe.jsp.applications.title.reload' arguments='${app.name}'/>"/>
				</a>
			</display:column>

			<display:column titleKey="probe.jsp.applications.col.description">
				${app.displayName}&nbsp;
			</display:column>

			<display:column sortable="true" titleKey="probe.jsp.applications.col.requestCount" sortProperty="requestCount">
				<a href="<c:url value='/servlets.htm?webapp=${app.name}'/>">
					${app.requestCount}
				</a>
			</display:column>

			<display:column sortable="true" sortProperty="sessionCount"
					titleKey="probe.jsp.applications.col.sessionCount">
				<a href="<c:url value='/sessions.htm'><c:param name='webapp' value='${app.name}'/><c:param name='size' value='${param.size}'/></c:url>">
					${app.sessionCount}
				</a>
			</display:column>

			<display:column property="sessionAttributeCount" sortable="true"
					titleKey="probe.jsp.applications.col.sessionAttributeCount"/>

			<c:if test="${param.size}">
				<display:column sortProperty="size" sortable="true"
								titleKey="probe.jsp.applications.col.size" class="highlighted">
					<probe:volume value="${app.size}"/>
				</display:column>
			</c:if>

			<display:column sortable="true" sortProperty="contextAttributeCount"
					titleKey="probe.jsp.applications.col.contextAttributeCount">
				<a href="<c:url value='/appattributes.htm'><c:param name='webapp' value='${app.name}'/></c:url>">
					${app.contextAttributeCount}
				</a>
			</display:column>

			<display:column property="sessionTimeout" sortable="true" titleKey="probe.jsp.applications.col.sessionTimeout"/>

			<display:column titleKey="probe.jsp.applications.col.jsp">
				<a class="imglink" href="<c:url value='/app/jsp.htm'><c:param name='webapp' value='${app.name}'/></c:url>">
					<img border="0" src="${pageContext.request.contextPath}<spring:theme code='magnifier.png'/>" alt="<spring:message code='probe.jsp.applications.jsp.view'/>">
				</a>
			</display:column>

			<c:if test="${!no_resources}">
				<display:column sortable="true" sortProperty="dataSourceBusyScore"
						titleKey="probe.jsp.applications.col.jdbcUsage" class="score_wrapper">
					<div class="score_wrapper">
						<probe:score value="${app.dataSourceBusyScore}" value2="${app.dataSourceEstablishedScore - app.dataSourceBusyScore}" fullBlocks="10" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
							<a class="imglink" href="<c:url value='/resources.htm?webapp=${app.name}'/>"><img border="0"
																											src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+"
																											title="<spring:message code='probe.jsp.applications.jdbcUsage.title' arguments='${app.dataSourceBusyScore},${app.dataSourceEstablishedScore}'/>"/></a>
						</probe:score>
					</div>
				</display:column>
			</c:if>

			<display:column titleKey="probe.jsp.applications.col.distributable" sortable="true"
					sortProperty="distributable">
				<c:choose>
					<c:when test="${app.distributable}">
						<span class="okValue"><spring:message code="probe.jsp.generic.yes"/></span>
					</c:when>
					<c:otherwise>
						<span class="errorValue"><spring:message code="probe.jsp.generic.no"/></span>
					</c:otherwise>
				</c:choose>
			</display:column>

			<display:column titleKey="probe.jsp.applications.col.serializable" sortable="true" sortProperty="serializable">
				<c:choose>
					<c:when test="${app.serializable}">
						<span class="okValue"><spring:message code="probe.jsp.applications.serializable.yes"/></span>
					</c:when>
					<c:otherwise>
						<span class="errorValue"><spring:message code="probe.jsp.applications.serializable.no"/></span>
					</c:otherwise>
				</c:choose>
			</display:column>

		</display:table>

		<script type="text/javascript">
			setupHelpToggle('<c:url value="/help/applications.ajax"/>');
		</script>
	</div>
</body>
</html>
