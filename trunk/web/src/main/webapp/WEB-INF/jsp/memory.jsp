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

<html>
	<head>
		<title><spring:message code="probe.jsp.title.memory"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
	</head>

	<c:set var="navTabSystem" value="active" scope="request"/>
	<c:set var="systemTabMemory" value="active" scope="request"/>
	<c:set var="use_decorator" value="system" scope="request"/>

	<c:set var="chartWidth" value="228"/>
	<c:set var="chartHeight" value="120"/>
	<c:set var="fullChartWidth" value="750"/>
	<c:set var="fullChartHeight" value="350"/>

	<body>

	<c:choose>
		<c:when test="${empty pools}">
			<div class="errorMessage">
				<p>
					<spring:message code="probe.jsp.memory.notAvailable"/>
				</p>
			</div>
		</c:when>
		<c:otherwise>
			<c:url value="/chart.png" var="fullChartBase">
				<c:param name="p" value="memory_usage"/>
				<c:param name="xz" value="${fullChartWidth}"/>
				<c:param name="yz" value="${fullChartHeight}"/>
			</c:url>

			<div class="memory">

				<ul class="options">
					<li id="adviseFin">
						<a href="<c:url value='/adm/advisegc.htm?fin=true'/>">
							<spring:message code="probe.jsp.memory.advise.finalization"/>
						</a>
					</li>
					<li id="adviseGC">
						<a href="<c:url value='/adm/advisegc.htm'/>">
							<spring:message code="probe.jsp.memory.advise.gc"/>
						</a>
					</li>
				</ul>

				<h3><spring:message code="probe.jsp.memory.h3.table"/></h3>

				<div id="memoryPools">
					<div class="ajax_activity"></div>
				</div>

				<h3><spring:message code="probe.jsp.memory.h3.charts"/></h3>

				<div id="memChartGroup">

					<c:forEach items="${pools}" var="pool" varStatus="status">

						<c:url value="/chart.png" var="chartUrl" scope="page">
							<c:param name="p" value="memory_usage"/>
							<c:param name="sp" value="${pool.name}"/>
							<c:param name="xz" value="${chartWidth}"/>
							<c:param name="yz" value="${chartHeight}"/>
							<c:param name="l" value="false"/>
						</c:url>

						<c:set var="cookie_name" value="mem_${pool.id}" scope="page"/>

						<c:choose>
							<c:when test="${cookie[probe:safeCookieName(cookie_name)].value == 'off'}">
								<c:set var="style" value="display:none"/>
							</c:when>
							<c:otherwise>
								<c:set var="style" value=""/>
							</c:otherwise>
						</c:choose>

						<div class="memoryChart" id="${pool.id}" style="${style}">
							<dl>
								<dt>
									${pool.name}
									<c:url var="toggleUrl" value="/remember.ajax?cn=mem_${pool.id}"/>
									<img onclick="togglePanel('${pool.id}', '${toggleUrl}')"
											src="${pageContext.request.contextPath}<spring:theme code='bullet_arrow_down.gif'/>" alt="+"/>
								</dt>
								<dd class="image"><img id="img_${pool.id}"
													src="<c:out value='${chartUrl}' escapeXml='false'/>" width="${chartWidth}" height="${chartHeight}" alt="+"
													onclick="zoomIn('${pool.name}')"/></dd>
							</dl>
						</div>

						<script type="text/javascript">
							new Ajax.ImgUpdater('img_${pool.id}', ${probe:max(collectionPeriod, 5)});
						</script>

					</c:forEach>
				</div>

				<div id="fullMemoryChart" style="display: none;">
					<img id="fullImg" class="clickable" src="${fullChartBase}&sp=Total" width="${fullChartWidth}" height="${fullChartHeight}" alt="-" onclick="zoomOut();"/>
				</div>
			</div>

			<script type="text/javascript">

				var fullImageUpdater;

				function zoomIn(newPool) {
					if (fullImageUpdater) {
						fullImageUpdater.stop();
					}
					Effect.DropOut('memChartGroup');
					Effect.Appear('fullMemoryChart');
					fullImageUpdater = new Ajax.ImgUpdater('fullImg', ${probe:max(collectionPeriod, 5)}, '<c:out value="${fullChartBase}" escapeXml="false"/>&sp=' + newPool + "&s1l=" + newPool);
				}

				function zoomOut() {
					Effect.DropOut('fullMemoryChart');
					Effect.Appear('memChartGroup');
					if (fullImageUpdater) {
						fullImageUpdater.stop();
						fullImageUpdater=null;
					}
				}

				new Ajax.PeriodicalUpdater('memoryPools', '<c:url value="/memory.ajax"/>?<%=request.getQueryString()%>', {frequency: 5});

			</script>
		</c:otherwise>
	</c:choose>

</body>
</html>
