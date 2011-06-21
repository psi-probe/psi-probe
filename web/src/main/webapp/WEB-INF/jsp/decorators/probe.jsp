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
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>


<%--
	Main site decorator. Face of the Probe.

	Author: Vlad Ilyushchenko
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="${lang}" xml:lang="${lang}">
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
					<a class="${navTabApps}" href="<c:url value='/index.htm?size=${param.size}'/>">
						<spring:message code="probe.jsp.menu.applications"/>
					</a>
				</li>
				<li>
					<a class="${navTabDatasources}" href="<c:url value='/datasources.htm'/>">
						<spring:message code="probe.jsp.menu.datasources"/>
					</a>
				</li>
				<li>
					<a class="${navTabDeploy}" href="<c:url value='/deploy.htm'/>">
						<spring:message code="probe.jsp.menu.deployment"/>
					</a>
				</li>
				<li>
					<a class="${navTabLogs}" href="<c:url value='/logs/index.htm'/>">
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
					<a class="${navTabQuickCheck}" href="<c:url value='/adm/quickcheck.htm'/>">
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
					<a href="<c:url value='/deploy.htm'/>">
						<spring:message code="probe.jsp.menu.deployment"/>
					</a>
				</li>
				<li>
					<a href="<c:url value='/logs/index.htm'/>">
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
				<li class="last">
					<a href="<c:url value='/adm/quickcheck.htm'/>">
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
						src="<c:url value='/flags/gb.gif'/>" alt="EN" /></a>
				<a href="?<probe:addQueryParam param='lang' value='ru'/>"><img
						src="<c:url value='/flags/ru.gif'/>" alt="RU" /></a>
				<a href="?<probe:addQueryParam param='lang' value='ja'/>"><img
						src="<c:url value='/flags/jp.gif'/>" alt="JP" /></a>
				<a href="?<probe:addQueryParam param='lang' value='it'/>"><img
						src="<c:url value='/flags/it.gif'/>" alt="IT" /></a>
				<a href="?<probe:addQueryParam param='lang' value='de'/>"><img
						src="<c:url value='/flags/de.gif'/>" alt="DE" /></a>
				<a href="?<probe:addQueryParam param='lang' value='es'/>"><img
						src="<c:url value='/flags/es.gif'/>" alt="ES" /></a>
				<a href="?<probe:addQueryParam param='lang' value='fr'/>"><img
						src="<c:url value='/flags/fr.gif'/>" alt="FR" /></a>
				<a href="?<probe:addQueryParam param='lang' value='pt_br'/>"><img
						src="<c:url value='/flags/br.gif'/>" alt="BR" /></a>
			</div>
			<p>
				<spring:message code="probe.jsp.i18n.credit"/>
			</p>
		</div>

	</body>
</html>