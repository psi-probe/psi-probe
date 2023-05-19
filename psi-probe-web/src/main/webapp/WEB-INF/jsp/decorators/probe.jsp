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
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>


<%-- Main site decorator. Face of the Probe. --%>

<!DOCTYPE html>
<html lang="${lang}">
	<head>
		<title>Probe - <decorator:title default="Tomcat management"/></title>
		<link type="image/gif" rel="shortcut icon" href="<c:url value='/css/favicon.gif'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='tables.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='main.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='mainnav.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='messages.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='tooltip.css'/>"/>
		<decorator:head/>
	</head>

	<body>

		<div id="caption">
			<ul id="top">
				<li id="logo"><a href="<c:url value='/index.htm'/>"><img src="<c:url value='/css/the-probe-logo.gif'/>"
																		alt="PSI Probe Logo"/></a></li>
				<li id="runtime">
					<spring:message code="probe.jsp.version" arguments="${version},<b>${hostname}</b>"/>,
					<span class="uptime"><spring:message code="probe.jsp.uptime"
														arguments="${uptime_days},${uptime_hours},${uptime_mins}"/></span></li>
				<li id="title"><decorator:title default="Probe"/></li>
			</ul>
		</div>

		<div id="navcontainer">
			<ul id="tabnav">
				<li>
					<a class="${navTabApps}" href="<c:url value='/index.htm'><c:param name='size'><c:out value='${param.size}' /></c:param></c:url>">
						<spring:message code="probe.jsp.menu.applications"/>
					</a>
				</li>
				<li>
					<a class="${navTabDatasources}" href="<c:url value='/datasources.htm'/>">
						<spring:message code="probe.jsp.menu.datasources"/>
					</a>
				</li>
				<li>
					<a class="${navTabDeploy}" href="<c:url value='/adm/deploy.htm'/>">
						<spring:message code="probe.jsp.menu.deployment"/>
					</a>
				</li>
				<li>
					<a class="${navTabLogs}" href="<c:url value='/logs/list.htm'/>">
						<spring:message code="probe.jsp.menu.logs"/>
					</a>
				</li>
				<li>
					<a class="${navTabThreads}" href="<c:url value='/threads.htm'/>">
						<spring:message code="probe.jsp.menu.threads"/>
					</a>
				</li>
				<li>
					<a class="${navTabCluster}" href="<c:url value='/cluster.htm'/>">
						<spring:message code="probe.jsp.menu.cluster"/>
					</a>
				</li>
				<li>
					<a class="${navTabSystem}" href="<c:url value='/sysinfo.htm'/>">
						<spring:message code="probe.jsp.menu.sysinfo"/>
					</a>
				</li>
				<li>
					<a class="${navTabConnectors}" href="<c:url value='/connectors.htm'/>">
						<spring:message code="probe.jsp.menu.connectors"/>
					</a>
				</li>
				<li>
					<a class="${navTabCertificates}" href="<c:url value='/certificates.htm'/>">
						<spring:message code="probe.jsp.menu.certificates"/>
					</a>
				</li>
				<li>
					<a class="${navTabQuickCheck}" href="<c:url value='/quickcheck.htm'/>">
						<spring:message code="probe.jsp.menu.quickcheck"/>
					</a>
				</li>
			</ul>
		</div>

		<c:choose>
			<c:when test="${! empty use_decorator}">
				<page:applyDecorator name="${use_decorator}">
					<decorator:body/>
				</page:applyDecorator>
			</c:when>
			<c:otherwise>
				<div id="mainBody">
					<decorator:body/>
				</div>
			</c:otherwise>
		</c:choose>

		<div id="footer">
			<ul>
				<li>
					<a href="<c:url value='/index.htm'/>">
						<spring:message code="probe.jsp.menu.applications"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/datasources.htm'/>">
						<spring:message code="probe.jsp.menu.datasources"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/adm/deploy.htm'/>">
						<spring:message code="probe.jsp.menu.deployment"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/logs/list.htm'/>">
						<spring:message code="probe.jsp.menu.logs"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/threads.htm'/>">
						<spring:message code="probe.jsp.menu.threads"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/cluster.htm'/>">
						<spring:message code="probe.jsp.menu.cluster"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/sysinfo.htm'/>">
						<spring:message code="probe.jsp.menu.sysinfo"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/connectors.htm'/>">
						<spring:message code="probe.jsp.menu.connectors"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/certificates.htm'/>">
						<spring:message code="probe.jsp.menu.certificates"/>
					</a>
				</li>
				<li class="last">
					<a href="<c:url value='/quickcheck.htm'/>">
						<spring:message code="probe.jsp.menu.quickcheck"/>
					</a>
				</li>
			</ul>
			<p>
				<spring:message code="probe.jsp.copyright"/>
				<br/>
				<spring:message code="probe.jsp.icons.credit"/>
			</p>
			<div id="locales">
				<a href="?<probe:addQueryParam param='lang' value='en'/>"><img
						src="<c:url value='/flags/gb.png'/>" alt="EN" /></a>
				<a href="?<probe:addQueryParam param='lang' value='ru'/>"><img
						src="<c:url value='/flags/ru.png'/>" alt="RU" /></a>
				<a href="?<probe:addQueryParam param='lang' value='ja'/>"><img
						src="<c:url value='/flags/jp.png'/>" alt="JP" /></a>
				<a href="?<probe:addQueryParam param='lang' value='it'/>"><img
						src="<c:url value='/flags/it.png'/>" alt="IT" /></a>
				<a href="?<probe:addQueryParam param='lang' value='de'/>"><img
						src="<c:url value='/flags/de.png'/>" alt="DE" /></a>
				<a href="?<probe:addQueryParam param='lang' value='es'/>"><img
						src="<c:url value='/flags/es.png'/>" alt="ES" /></a>
				<a href="?<probe:addQueryParam param='lang' value='fr'/>"><img
						src="<c:url value='/flags/fr.png'/>" alt="FR" /></a>
				<a href="?<probe:addQueryParam param='lang' value='pt_br'/>"><img
						src="<c:url value='/flags/br.png'/>" alt="BR" /></a>
				<a href="?<probe:addQueryParam param='lang' value='zh_cn'/>"><img
						src="<c:url value='/flags/cn.png'/>" alt="CN" /></a>
			</div>
			<p>
				<spring:message code="probe.jsp.i18n.credit"/>
			</p>
		</div>

	</body>
</html>
