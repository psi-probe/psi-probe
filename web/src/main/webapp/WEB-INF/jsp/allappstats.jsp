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
		<title>
			<spring:message code="probe.jsp.title.allappstats"/>
		</title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<c:set var="navTabApps" value="active" scope="request"/>

	<body>

		<div id="charts" class="embeddedBlockContainer">
			<h3><spring:message code="probe.jsp.allappstats.h3.charts"/></h3>

			<c:set var="chartWidth" value="345"/>
			<c:set var="chartHeight" value="250"/>
			<c:set var="fullChartWidth" value="700"/>
			<c:set var="fullChartHeight" value="320"/>

			<%-- Cumulative Response Time --%>
			<c:url value="/chart.png" var="total_avg_proc_time_url">
				<c:param name="ct" value="area"/>
				<c:param name="p" value="total_avg_proc_time"/>
				<c:param name="xz" value="${chartWidth}"/>
				<c:param name="yz" value="${chartHeight}"/>
				<c:param name="s1c" value="#FFCD9B"/>
				<c:param name="s1o" value="#D26900"/>
				<c:param name="l" value="false"/>
			</c:url>

			<c:url value="/chart.png" var="total_avg_proc_time_url_full">
				<c:param name="p" value="total_avg_proc_time"/>
				<c:param name="xz" value="${fullChartWidth}"/>
				<c:param name="yz" value="${fullChartHeight}"/>
				<c:param name="s1c" value="#FFCD9B"/>
				<c:param name="s1o" value="#D26900"/>
				<c:param name="s1l">
					<spring:message code="probe.jsp.allappstats.charts.totalAvgProcTime.legend"/>
				</c:param>
			</c:url>

			<%-- Response Time by App --%>
			<c:url value="/chart.png" var="all_app_avg_proc_time_url">
				<c:param name="ct" value="line"/>
				<c:param name="p" value="all_app_avg_proc_time"/>
				<c:param name="xz" value="${chartWidth}"/>
				<c:param name="yz" value="${chartHeight}"/>
				<c:param name="s1c" value="#9BD2FB"/>
				<c:param name="s1o" value="#0665AA"/>
				<c:param name="s2c" value="#FFCCCC"/>
				<c:param name="s2o" value="#FF8484"/>
				<c:param name="s3c" value="#95FE8B"/>
				<c:param name="s3o" value="#009406"/>
				<c:param name="s4c" value="#FFCD9B"/>
				<c:param name="s4o" value="#D26900"/>
			</c:url>

			<c:url value="/chart.png" var="all_app_avg_proc_time_url_full">
				<c:param name="ct" value="line"/>
				<c:param name="p" value="all_app_avg_proc_time"/>
				<c:param name="xz" value="${fullChartWidth}"/>
				<c:param name="yz" value="${fullChartHeight}"/>
				<c:param name="s1c" value="#9BD2FB"/>
				<c:param name="s1o" value="#0665AA"/>
				<c:param name="s2c" value="#FFCCCC"/>
				<c:param name="s2o" value="#FF8484"/>
				<c:param name="s3c" value="#95FE8B"/>
				<c:param name="s3o" value="#009406"/>
				<c:param name="s4c" value="#FFCD9B"/>
				<c:param name="s4o" value="#D26900"/>
			</c:url>

			<%-- Cumulative Requests --%>
			<c:url value="/chart.png" var="total_req_url">
				<c:param name="ct" value="area"/>
				<c:param name="p" value="total_req"/>
				<c:param name="xz" value="${chartWidth}"/>
				<c:param name="yz" value="${chartHeight}"/>
				<c:param name="l" value="false"/>
			</c:url>

			<c:url value="/chart.png" var="total_req_url_full">
				<c:param name="p" value="total_req"/>
				<c:param name="xz" value="${fullChartWidth}"/>
				<c:param name="yz" value="${fullChartHeight}"/>
				<c:param name="s1l">
					<spring:message code="probe.jsp.allappstats.charts.totalReq.legend"/>
				</c:param>
			</c:url>

			<%-- Requests By App --%>
			<c:url value="/chart.png" var="all_app_req_url">
				<c:param name="ct" value="line"/>
				<c:param name="p" value="all_app_req"/>
				<c:param name="xz" value="${chartWidth}"/>
				<c:param name="yz" value="${chartHeight}"/>
				<c:param name="s1c" value="#9BD2FB"/>
				<c:param name="s1o" value="#0665AA"/>
				<c:param name="s2c" value="#FFCCCC"/>
				<c:param name="s2o" value="#FF8484"/>
				<c:param name="s3c" value="#95FE8B"/>
				<c:param name="s3o" value="#009406"/>
				<c:param name="s4c" value="#FFCD9B"/>
				<c:param name="s4o" value="#D26900"/>
			</c:url>

			<c:url value="/chart.png" var="all_app_req_url_full">
				<c:param name="ct" value="line"/>
				<c:param name="p" value="all_app_req"/>
				<c:param name="xz" value="${fullChartWidth}"/>
				<c:param name="yz" value="${fullChartHeight}"/>
				<c:param name="s1c" value="#9BD2FB"/>
				<c:param name="s1o" value="#0665AA"/>
				<c:param name="s2c" value="#FFCCCC"/>
				<c:param name="s2o" value="#FF8484"/>
				<c:param name="s3c" value="#95FE8B"/>
				<c:param name="s3o" value="#009406"/>
				<c:param name="s4c" value="#FFCD9B"/>
				<c:param name="s4o" value="#D26900"/>
			</c:url>

			<div id="chart_group">
				<div class="cumulative">
					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.allappstats.charts.totalReq.title"/></dt>
							<dd class="image">
								<img id="total_req_chart" border="0" src="${total_req_url}" width="${chartWidth}" height="${chartHeight}" alt="+"/>
							</dd>
						</dl>
					</div>
					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.allappstats.charts.totalAvgProcTime.title"/></dt>
							<dd class="image">
								<img id="total_avg_proc_time_chart" border="0" src="${total_avg_proc_time_url}" width="${chartWidth}" height="${chartHeight}" alt="+"/>
							</dd>
						</dl>
					</div>
				</div>
				<div class="combined">
					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.allappstats.charts.allAppReq.title"/></dt>
							<dd class="image">
								<img id="all_app_req_chart" border="0" src="${all_app_req_url}" width="${chartWidth}" height="${chartHeight}" alt="+"/>
							</dd>
						</dl>
					</div>
					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.allappstats.charts.allAppAvgProcTime.title"/></dt>
							<dd class="image">
								<img id="all_app_avg_proc_time_chart" border="0" src="${all_app_avg_proc_time_url}" width="${chartWidth}" height="${chartHeight}" alt="+"/>
							</dd>
						</dl>
					</div>
				</div>
			</div>

			<div id="full_chart" class="chartContainer" style="display: none;">
				<dl>
					<dt id="full_title"></dt>
					<dd class="image">
						<img id="fullImg" border="0" src="${total_avg_proc_time_url_full}" width="${fullChartWidth}" height="${fullChartHeight}" alt="-"/>
					</dd>
				</dl>
			</div>
		</div>

		<script type="text/javascript">
			var imageUpdaters = new Array();
			var fullImageUpdater;

			function zoomIn(imgUrl, title) {
				if (fullImageUpdater) {
					fullImageUpdater.stop();
				}
				for (var i = 0; i < imageUpdaters.length; i++) {
					if (imageUpdaters[i]) {
						imageUpdaters[i].stop();
					}
				}
				$('full_title').update(title);
				Effect.DropOut('chart_group');
				Effect.Appear('full_chart');
				fullImageUpdater = new Ajax.ImgUpdater('fullImg', ${probe:max(collectionPeriod, 5)}, imgUrl);
			}

			function zoomOut() {
				Effect.DropOut('full_chart');
				Effect.Appear('chart_group');
				if (fullImageUpdater) {
					fullImageUpdater.stop();
					fullImageUpdater = null;
				}
				for (var i = 0; i < imageUpdaters.length; i++) {
					if (imageUpdaters[i]) {
						imageUpdaters[i].start();
					}
				}
			}

			var rules = {
				'#total_avg_proc_time_chart': function(element) {
					element.onclick = function() {
						zoomIn('${total_avg_proc_time_url_full}', '<spring:message code="probe.jsp.allappstats.charts.totalAvgProcTime.title"/>');
					}
				},
				'#all_app_avg_proc_time_chart': function(element) {
					element.onclick = function() {
						zoomIn('${all_app_avg_proc_time_url_full}', '<spring:message code="probe.jsp.allappstats.charts.allAppAvgProcTime.title"/>');
					}
				},
				'#total_req_chart': function(element) {
					element.onclick = function() {
						zoomIn('${total_req_url_full}', '<spring:message code="probe.jsp.allappstats.charts.totalReq.title"/>');
					}
				},
				'#all_app_req_chart': function(element) {
					element.onclick = function() {
						zoomIn('${all_app_req_url_full}', '<spring:message code="probe.jsp.allappstats.charts.allAppReq.title"/>');
					}
				},
				'#full_chart': function(element) {
					element.onclick = function() {
						zoomOut();
					}
				}
			}

			Behaviour.register(rules);

			imageUpdaters[0] = new Ajax.ImgUpdater('total_avg_proc_time_chart', ${probe:max(collectionPeriod, 5)});
			imageUpdaters[1] = new Ajax.ImgUpdater('all_app_avg_proc_time_chart', ${probe:max(collectionPeriod, 5)});
			imageUpdaters[2] = new Ajax.ImgUpdater('total_req_chart', ${probe:max(collectionPeriod, 5)});
			imageUpdaters[3] = new Ajax.ImgUpdater('all_app_req_chart', ${probe:max(collectionPeriod, 5)});
		</script>
	</body>
</html>
