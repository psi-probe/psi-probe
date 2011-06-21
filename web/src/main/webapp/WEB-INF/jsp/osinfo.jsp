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
		<title><spring:message code="probe.jsp.sysinfo.os.title"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<c:set var="navTabSystem" value="active" scope="request"/>
	<c:set var="systemTabOsInfo" value="active" scope="request"/>
	<c:set var="use_decorator" value="system" scope="request"/>

	<c:set var="chartWidth" value="260"/>
	<c:set var="chartHeight" value="140"/>
	<c:set var="fullChartWidth" value="650"/>
	<c:set var="fullChartHeight" value="320"/>

	<body>
		<c:choose>
			<c:when test="${empty runtime}">
				<div class="errorMessage">
					<p>
						<spring:message code="probe.jsp.memory.notAvailable"/>
					</p>
				</div>
			</c:when>
			<c:otherwise>

				<spring:message code="probe.jsp.os.chart.memory.legend.total" var="mem_total_legend"/>
				<spring:message code="probe.jsp.os.chart.memory.legend.jvm" var="jvm_legend"/>


				<c:url value="/chart.png" var="os_memory_url">
					<c:param name="p" value="os_memory"/>
					<c:param name="xz" value="${chartWidth}"/>
					<c:param name="yz" value="${chartHeight}"/>
					<c:param name="l" value="false"/>
				</c:url>

				<c:url value="/chart.png" var="os_memory_url_full">
					<c:param name="p" value="os_memory"/>
					<c:param name="xz" value="${fullChartWidth}"/>
					<c:param name="yz" value="${fullChartHeight}"/>
					<c:param name="s1l" value="${mem_total_legend}"/>
					<c:param name="s2l" value="${jvm_legend}"/>
				</c:url>

				<spring:message code="probe.jsp.os.chart.swap.legend" var="swap_legend"/>

				<c:url value="/chart.png" var="swap_usage_url">
					<c:param name="p" value="swap_usage"/>
					<c:param name="xz" value="${chartWidth}"/>
					<c:param name="yz" value="${chartHeight}"/>
					<c:param name="s1c" value="#FFCD9B"/>
					<c:param name="s1o" value="#D26900"/>
					<c:param name="l" value="false"/>
				</c:url>

				<c:url value="/chart.png" var="swap_usage_url_full">
					<c:param name="p" value="swap_usage"/>
					<c:param name="xz" value="${fullChartWidth}"/>
					<c:param name="yz" value="${fullChartHeight}"/>
					<c:param name="s1c" value="#FFCD9B"/>
					<c:param name="s1o" value="#D26900"/>
					<c:param name="s1l" value="${swap_legend}"/>
				</c:url>

				<spring:message code="probe.jsp.os.chart.cpu.legend" var="cpu_legend"/>

				<c:url value="/chart.png" var="cpu_usage_url">
					<c:param name="p" value="cpu_usage"/>
					<c:param name="xz" value="${chartWidth}"/>
					<c:param name="yz" value="${chartHeight}"/>
					<c:param name="s1c" value="#FFCCCC"/>
					<c:param name="s1o" value="#FF8484"/>
					<c:param name="l" value="false"/>
				</c:url>

				<c:url value="/chart.png" var="cpu_usage_url_full">
					<c:param name="p" value="cpu_usage"/>
					<c:param name="xz" value="${fullChartWidth}"/>
					<c:param name="yz" value="${fullChartHeight}"/>
					<c:param name="s1c" value="#FFCCCC"/>
					<c:param name="s1o" value="#FF8484"/>
					<c:param name="s1l" value="${cpu_legend}"/>
				</c:url>

				<div>
					<h3><spring:message code="probe.jsp.os.h3.information"/></h3>

					<div id="osinfo">
						<div class="ajax_activity"></div>
					</div>

					<div id="chart_group" style="width: 99%;">
						<h3><spring:message code="probe.jsp.os.h3.charts"/></h3>

						<div class="chartContainer">
							<dl>
								<dt><spring:message code="probe.jsp.os.chart.cpu"/></dt>
								<dd class="image">
									<img id="cpu_chart" border="0" src="<c:out value='${cpu_usage_url}' escapeXml='false'/>"
											width="${chartWidth}"
											height="${chartHeight}"
											alt="<spring:message code='probe.jsp.os.chart.cpu.alt'/>"/>
								</dd>
							</dl>
						</div>

						<div class="chartContainer">
							<dl>
								<dt><spring:message code="probe.jsp.os.chart.memory"/></dt>
								<dd class="image">
									<img id="mem_chart" border="0" src="<c:out value='${os_memory_url}' escapeXml='false'/>"
											width="${chartWidth}"
											height="${chartHeight}"
											alt="<spring:message code='probe.jsp.os.chart.memory.alt'/>"/>
								</dd>
							</dl>
						</div>

						<div class="chartContainer">
							<dl>
								<dt><spring:message code="probe.jsp.os.chart.swap"/></dt>
								<dd class="image">
									<img id="swap_chart" border="0" src="<c:out value='${swap_usage_url}' escapeXml='false'/>"
											width="${chartWidth}"
											height="${chartHeight}"
											alt="<spring:message code='probe.jsp.os.chart.swap.alt'/>"/>
								</dd>
							</dl>
						</div>
					</div>

					<div id="full_chart" style="display: none;">
						<img id="fullImg" class="clickable" src="" width="${fullChartWidth}" height="${fullChartHeight}" alt=""/>
					</div>
				</div>

				<script type="text/javascript">
					var fullImageUpdater;

					function zoomIn(url) {
						if (fullImageUpdater) {
							fullImageUpdater.stop();
						}
						var img = document.getElementById('fullImg');
						Effect.DropOut('chart_group');
						Effect.Appear('full_chart');
						fullImageUpdater = new Ajax.ImgUpdater('fullImg', ${probe:max(collectionPeriod, 5)}, url);
					}

					function zoomOut() {
						Effect.DropOut('full_chart');
						Effect.Appear('chart_group');
						if (fullImageUpdater) {
							fullImageUpdater.stop();
							fullImageUpdater = null;
						}
					}

					var rules = {
						'#mem_chart': function(element) {
							element.onclick = function() {
								zoomIn('<c:out value="${os_memory_url_full}" escapeXml="false"/>');
							}
						},
						'#swap_chart': function(element) {
							element.onclick = function() {
								zoomIn('<c:out value="${swap_usage_url_full}" escapeXml="false"/>');
							}
						},
						'#cpu_chart': function(element) {
							element.onclick = function() {
								zoomIn('<c:out value="${cpu_usage_url_full}" escapeXml="false"/>');
							}
						},
						'#full_chart': function(element) {
							element.onclick = function() {
								zoomOut();
							}
						}
					}

					Behaviour.register(rules);

					new Ajax.ImgUpdater('cpu_chart', ${probe:max(collectionPeriod, 5)});
					new Ajax.ImgUpdater('mem_chart', ${probe:max(collectionPeriod, 5)});
					new Ajax.ImgUpdater('swap_chart', ${probe:max(collectionPeriod, 5)});
					new Ajax.PeriodicalUpdater('osinfo', '<c:url value="/adm/osinfo.ajax"/>', {frequency: 5});

				</script>
			</c:otherwise>
		</c:choose>
	</body>
</html>
