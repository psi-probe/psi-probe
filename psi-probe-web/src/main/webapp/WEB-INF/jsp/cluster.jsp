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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>

<%-- Cluster statistic view. Displays cluster members and sender and receiver traffic stats. --%>
<!DOCTYPE html>
<html lang="${lang}">
	<head>
		<title><spring:message code="probe.jsp.title.cluster"/></title>
		<script src="<c:url value='/js/prototype.js'/>"></script>
		<script src="<c:url value='/js/scriptaculous/scriptaculous.js'/>"></script>
		<script src="<c:url value='/js/func.js'/>"></script>
	</head>

	<c:set var="navTabCluster" value="active" scope="request"/>

	<c:set var="chartWidth" value="400"/>
	<c:set var="chartHeight" value="250"/>

	<c:url value="/chart.png" var="trafficimg" scope="page">
		<c:param name="xz" value="${chartWidth}"/>
		<c:param name="yz" value="${chartHeight}"/>
		<c:param name="s1l" value="sent"/>
		<c:param name="s2l" value="received"/>
		<c:param name="l" value="true"/>
		<c:param name="s1c" value="#95FE8B"/>
		<c:param name="s1o" value="#009406"/>
		<c:param name="s2c" value="#FDFB8B"/>
		<c:param name="s2o" value="#D9CB00"/>
		<c:param name="p" value="cl_traffic"/>
	</c:url>

	<c:url value="/chart.png" var="requestsimg" scope="page">
		<c:param name="xz" value="${chartWidth}"/>
		<c:param name="yz" value="${chartHeight}"/>
		<c:param name="s1l" value="sent"/>
		<c:param name="s2l" value="received"/>
		<c:param name="s1c" value="#FF0606"/>
		<c:param name="s1o" value="#9d0000"/>
		<c:param name="s2c" value="#FF7417"/>
		<c:param name="s2o" value="#C44F00"/>
		<c:param name="l" value="true"/>
		<c:param name="p" value="cl_request"/>
	</c:url>

	<body>

		<c:choose>
			<c:when test="${ empty cluster}">
				<div class="errorMessage">
					<p>
						<spring:message code="probe.jsp.cluster.notAvailable"/>
					</p>
				</div>
			</c:when>
			<c:otherwise>
				<div id="cluster">
					<h3><spring:message code="probe.jsp.cluster.h3.info"/></h3>

					<div class="shadow">
						<div class="info">
							<p>
								<spring:message code="probe.jsp.cluster.name"/>&#160;<span class="value">${cluster.name}</span>
								<spring:message code="probe.jsp.cluster.info"/>&#160;<span class="value">${cluster.info}</span>
								<spring:message code="probe.jsp.cluster.manager"/>&#160;<span class="value">${cluster.managerClassName}</span>
								<spring:message code="probe.jsp.cluster.mode"/>&#160;<span class="value">${cluster.senderReplicationMode}</span>
								<spring:message code="probe.jsp.cluster.mcastAddress"/>&#160;<span class="value">${cluster.mcastAddress}:${cluster.mcastPort}</span>
								<spring:message code="probe.jsp.cluster.mcastTtl"/>&#160;<span class="value">${cluster.mcastTtl}</span>
								<spring:message code="probe.jsp.cluster.mcastBindAddress"/>&#160;
								<span class="value">
									<c:choose>
										<c:when test="${! empty cluster.mcastBindAddress}">
											${cluster.mcastBindAddress}
										</c:when>
										<c:otherwise>
											<spring:message code="probe.jsp.cluster.mcastBindAddress.all"/>&#160;
										</c:otherwise>
									</c:choose>
								</span>
								<spring:message code="probe.jsp.cluster.heartbeatFreq"/>&#160;<span class="value">${cluster.mcastFrequency}ms.</span>
								<spring:message code="probe.jsp.cluster.heartbeatTimeout"/>&#160;<span class="value">${cluster.mcastDropTime}ms.</span>
								<spring:message code="probe.jsp.cluster.receiverAddress"/>&#160;<span class="value">${cluster.tcpListenAddress}</span>
								<spring:message code="probe.jsp.cluster.receiverPort"/>&#160;<span class="value">${cluster.tcpListenPort}</span>
							</p>
						</div>
					</div>

					<div>
						<div class="chartContainer">
							<dl>
								<dt><spring:message code="probe.jsp.cluster.chart.traffic" arguments="${probe:max(collectionPeriod, 5)}"/></dt>
								<dd>
									<img id="cl_traffic" border="0" src="${trafficimg}" width="${chartWidth}" height="${chartHeight}" alt="Bytes received"/>
								</dd>
								<dd id="dd_traffic"><div class="ajax_activity"></div></dd>
							</dl>
						</div>

						<div class="chartContainer">
							<dl>
								<dt><spring:message code="probe.jsp.cluster.chart.requests" arguments="${probe:max(collectionPeriod, 5)}"/></dt>
								<dd>
									<img id="cl_requests" border="0" src="${requestsimg}" width="${chartWidth}" height="${chartHeight}" alt="Bytes sent"/>
								</dd>
								<dd id="dd_requests"><div class="ajax_activity"></div></dd>
							</dl>
						</div>
					</div>

					<div style="clear: both;"></div>

					<h3><spring:message code="probe.jsp.cluster.h3.members"/></h3>

					<div id="members">
						<div class="ajax_activity"></div>
						<%--<p class="empty_list"><spring:message code="probe.jsp.cluster.loading"/></p>--%>
					</div>
				</div>

				<script>
					new Ajax.ImgUpdater('cl_traffic', '${probe:max(collectionPeriod, 5)}');
					new Ajax.ImgUpdater('cl_requests', '${probe:max(collectionPeriod, 5)}');
					new Ajax.PeriodicalUpdater('dd_traffic', '<c:url value="/cluster/traffic.ajax"/>', {frequency: 3});
					new Ajax.PeriodicalUpdater('dd_requests', '<c:url value="/cluster/requests.ajax"/>', {frequency: 3});
					new Ajax.PeriodicalUpdater('members', '<c:url value="/cluster/members.ajax"/>?<%=StringEscapeUtils.escapeHtml4(request.getQueryString())%>', {method:'get',frequency: 3});
				</script>

			</c:otherwise>
		</c:choose>

	</body>
</html>
