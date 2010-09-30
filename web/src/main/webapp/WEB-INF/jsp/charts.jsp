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
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tags/probe.tld" prefix="probe" %>

<html>

	<head>
		<title><spring:message code="probe.jsp.title.charts"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
	</head>

	<c:set var="chartWidth" value="400"/>
	<c:set var="chartHeight" value="250"/>

	<c:url value="/chart.png" var="reqimg" scope="page">
		<c:param name="p" value="connector"/>
		<c:param name="xz" value="${chartWidth}"/>
		<c:param name="yz" value="${chartHeight}"/>
		<c:param name="l" value="false"/>
	</c:url>

	<c:url value="/chart.png" var="traffimg" scope="page">
		<c:param name="p" value="traffic"/>
		<c:param name="xz" value="${chartWidth}"/>
		<c:param name="yz" value="${chartHeight}"/>
		<c:param name="xl" value="Bytes"/>
		<c:param name="s1c" value="#95FE8B"/>
		<c:param name="s1o" value="#009406"/>
		<c:param name="s2c" value="#FDFB8B"/>
		<c:param name="s2o" value="#D9CB00"/>
		<c:param name="l" value="false"/>
	</c:url>

	<c:set var="navTabCharts" value="active" scope="request"/>

	<body>

		<div id="charts">
			<div class="shadow">
				<div class="info">
					<p><spring:message code="probe.jsp.charts.information"/></p>
				</div>
			</div>

			<c:forEach items="${names}" var="name">

				<%--
					create "reset connector" url
				--%>
				<c:url value="/app/connectorReset.htm" var="reset_url">
					<c:param name="cn" value="${name}"/>
				</c:url>

				<%--
					create  "remember group visibility" url
				--%>
				<c:url value="/remember.ajax" var="remember_url">
					<c:param name="cn" value="${name}"/>
				</c:url>

				<%--
					create style of the div based on user cookies
				--%>
				<c:choose>
					<c:when test="${cookie[name].value == 'off'}">
						<c:set var="style_collapse" value="display:none"/>
						<c:set var="style_expand" value=""/>
					</c:when>
					<c:otherwise>
						<c:set var="style_collapse" value=""/>
						<c:set var="style_expand" value="display:none"/>
					</c:otherwise>
				</c:choose>

				<div class="connectorChartHeader">
					<span class="headerTitle" onclick="togglePanel('chartdata-${name}', '${remember_url}')">
						<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='section.collapse.img'/>" alt="collapse" id="visible_chartdata-${name}" style="${style_collapse}"/>
						<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='section.expand.img'/>" alt="expand" id="invisible_chartdata-${name}" style="${style_expand}"/>
						${name}
					</span>
					<span class="actions">
						<a href="${reset_url}">
							<img border="0" src="${pageContext.request.contextPath}<spring:theme code='reset.gif'/>" alt="reset"/>
						</a>
					</span>
				</div>

				<div id="chartdata-${name}" style="${style_collapse}">
					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.charts.requests.title"/></dt>
							<dd class="image">
								<a href="<c:url value='/zoomchart.htm'/>?sp=${name}&p=connector"><img
										id="req-${name}"
										border="0" src="${reqimg}&sp=${name}"
										width="${chartWidth}"
										height="${chartHeight}"
										alt="+"/></a>
							</dd>
							<dd id="dd-req-${name}">
								<div class="ajax_activity"/>
							</dd>
						</dl>
					</div>

					<script type="text/javascript">
						new Ajax.ImgUpdater('req-${name}', ${probe:max(collectionPeriod, 5)});
						new Ajax.PeriodicalUpdater('dd-req-${name}', '<c:url value="/cnreqdetails.ajax"/>?cn=${name}', {frequency: 3});
					</script>

					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.charts.traffic.title"/></dt>
							<dd class="image">
								<a href="<c:url value='/zoomchart.htm'/>?sp=${name}&p=traffic"><img
										id="traf-${name}"
										border="0" src="${traffimg}&sp=${name}"
										width="${chartWidth}"
										height="${chartHeight}"
										alt="+"/></a>
							</dd>
							<dd id="dd-traf-${name}">
								<div class="ajax_activity"/>
							</dd>
						</dl>
					</div>

					<script type="text/javascript">
						new Ajax.ImgUpdater('traf-${name}', ${probe:max(collectionPeriod, 5)});
						new Ajax.PeriodicalUpdater('dd-traf-${name}', '<c:url value="/cntrafdetails.ajax"/>?cn=${name}', {frequency: 3});
					</script>
				</div>
			</c:forEach>
		</div>
	</body>
</html>
