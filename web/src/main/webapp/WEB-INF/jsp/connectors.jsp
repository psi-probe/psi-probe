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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>

<html>

	<head>
		<title><spring:message code="probe.jsp.title.connectors"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/Tooltip.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
	</head>

	<c:set var="chartWidth" value="280"/>
	<c:set var="chartHeight" value="175"/>

	<c:set var="navTabConnectors" value="active" scope="request"/>

	<body>

		<div id="ttdiv" class="tooltip" style="display: none;">
			<div class="tt_top">
				<span id="tt_title" style="display: none;"></span>
				<a id="ttdiv_close" href="#"><spring:message code="probe.jsp.tooltip.close"/></a>
			</div>
			<div class="tt_content" id="tt_content"></div>
		</div>

		<div id="connectors">
			<div class="shadow">
				<div class="info">
					<p><spring:message code="probe.jsp.connectors.information"/></p>
				</div>
			</div>

			<c:forEach items="${connectors}" var="connector">

				<c:set var="name" value="${connector.name}" />

				<c:url value="/chart.png" var="reqimg" scope="page">
					<c:param name="p" value="connector"/>
					<c:param name="sp" value="${name}"/>
					<c:param name="xz" value="${chartWidth}"/>
					<c:param name="yz" value="${chartHeight}"/>
					<c:param name="l" value="false"/>
				</c:url>

				<c:url value="/chart.png" var="traffimg" scope="page">
					<c:param name="p" value="traffic"/>
					<c:param name="sp" value="${name}"/>
					<c:param name="xz" value="${chartWidth}"/>
					<c:param name="yz" value="${chartHeight}"/>
					<c:param name="xl" value="Bytes"/>
					<c:param name="s1c" value="#95FE8B"/>
					<c:param name="s1o" value="#009406"/>
					<c:param name="s2c" value="#FDFB8B"/>
					<c:param name="s2o" value="#D9CB00"/>
					<c:param name="l" value="false"/>
				</c:url>

				<c:url value="/chart.png" var="proctimeimg" scope="page">
					<c:param name="p" value="connector_proc_time"/>
					<c:param name="sp" value="${name}"/>
					<c:param name="xz" value="${chartWidth}"/>
					<c:param name="yz" value="${chartHeight}"/>
					<c:param name="s1c" value="#FFCD9B"/>
					<c:param name="s1o" value="#D26900"/>
					<c:param name="l" value="false"/>
				</c:url>

				<c:url value="/zoomchart.htm" var="reqZoomUrl">
					<c:param name="p" value="connector" />
					<c:param name="sp" value="${name}" />
				</c:url>
				
				<c:url value="/zoomchart.htm" var="proctimeZoomUrl">
					<c:param name="p" value="connector_proc_time" />
					<c:param name="sp" value="${name}" />
				</c:url>

				<c:url value="/zoomchart.htm" var="trafficZoomUrl">
					<c:param name="p" value="traffic" />
					<c:param name="sp" value="${name}" />
				</c:url>

				<c:url value="/cnreqdetails.ajax" var="reqAjaxUrl">
					<c:param name="cn" value="${name}" />
				</c:url>

				<c:url value="/cnprocdetails.ajax" var="proctimeAjaxUrl">
					<c:param name="cn" value="${name}" />
				</c:url>

				<c:url value="/cntrafdetails.ajax" var="trafficAjaxUrl">
					<c:param name="cn" value="${name}" />
				</c:url>

				<c:url value="/app/connectorReset.htm" var="reset_url">
					<c:param name="cn" value="${name}"/>
				</c:url>

				<c:url value="/remember.ajax" var="remember_url">
					<c:param name="cn" value="${name}"/>
				</c:url>

				<%--
					create style of the div based on user cookies
				--%>
				<c:choose>
					<c:when test="${cookie[probe:safeCookieName(name)].value == 'off'}">
						<c:set var="style_collapse" value="display:none"/>
						<c:set var="style_expand" value=""/>
					</c:when>
					<c:otherwise>
						<c:set var="style_collapse" value=""/>
						<c:set var="style_expand" value="display:none"/>
					</c:otherwise>
				</c:choose>

				<div class="connectorChartHeader">
					<span class="headerTitle" onclick="togglePanel('chartdata-${probe:escapeHtml(name)}', '${remember_url}')">
						<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='section.collapse.img'/>" alt="collapse" id="visible_chartdata-${probe:escapeHtml(name)}" style="${style_collapse}"/>
						<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='section.expand.img'/>" alt="expand" id="invisible_chartdata-${probe:escapeHtml(name)}" style="${style_expand}"/>
						${name}
					</span>
					<span class="actions">
						<a href="${reset_url}">
							<img border="0" src="${pageContext.request.contextPath}<spring:theme code='reset.gif'/>" alt="reset"/>
						</a>
					</span>
				</div>

				<div id="chartdata-${probe:escapeHtml(name)}" style="${style_collapse}">
					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.connectors.requests.title"/></dt>
							<dd class="image">
								<a href="${reqZoomUrl}"><img
										id="req-${probe:escapeHtml(name)}"
										border="0" src="${reqimg}"
										width="${chartWidth}"
										height="${chartHeight}"
										alt="+"/></a>
							</dd>
							<dd id="dd-req-${probe:escapeHtml(name)}">
								<div class="ajax_activity"/>
							</dd>
						</dl>
					</div>

					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.connectors.proc_time.title"/></dt>
							<dd class="image">
								<a href="${proctimeZoomUrl}"><img
										id="proc_time-${probe:escapeHtml(name)}"
										border="0" src="${proctimeimg}"
										width="${chartWidth}"
										height="${chartHeight}"
										alt="+"/></a>
							</dd>
							<dd id="dd-proc_time-${probe:escapeHtml(name)}">
								<div class="ajax_activity"/>
							</dd>
						</dl>
					</div>

					<div class="chartContainer">
						<dl>
							<dt><spring:message code="probe.jsp.connectors.traffic.title"/></dt>
							<dd class="image">
								<a href="${trafficZoomUrl}"><img
										id="traf-${probe:escapeHtml(name)}"
										border="0" src="${traffimg}"
										width="${chartWidth}"
										height="${chartHeight}"
										alt="+"/></a>
							</dd>
							<dd id="dd-traf-${probe:escapeHtml(name)}">
								<div class="ajax_activity"/>
							</dd>
						</dl>
					</div>

					<script type="text/javascript">
						new Ajax.ImgUpdater('req-${probe:escapeJS(name)}', ${probe:max(collectionPeriod, 5)});
						new Ajax.ImgUpdater('proc_time-${probe:escapeJS(name)}', ${probe:max(collectionPeriod, 5)});
						new Ajax.ImgUpdater('traf-${probe:escapeJS(name)}', ${probe:max(collectionPeriod, 5)});
						new Ajax.PeriodicalUpdater('dd-req-${probe:escapeJS(name)}', '${reqAjaxUrl}', {frequency: 3});
						new Ajax.PeriodicalUpdater('dd-proc_time-${probe:escapeJS(name)}', '${proctimeAjaxUrl}', {frequency: 3});
						new Ajax.PeriodicalUpdater('dd-traf-${probe:escapeJS(name)}', '${trafficAjaxUrl}', {frequency: 3});
					</script>

					<div class="connectorInfo">
						<c:choose>
							<c:when test="${! empty connector.requestProcessors}">
								<div class="workerInfo">
									<display:table name="${connector.requestProcessors}"
											class="genericTbl" cellspacing="0"
											requestURI="" uid="rp" defaultsort="7" defaultorder="descending">
										<display:column title="&nbsp;" style="width:18px;" class="leftmost">
											<c:choose>
												<c:when test="${! empty rp.remoteAddrLocale.country && rp.remoteAddrLocale.country != '**'}">
													<img border="0" src="<c:url value='/flags/${fn:toLowerCase(rp.remoteAddrLocale.country)}.gif'/>"
															alt="${rp.remoteAddrLocale.country}" title="${rp.remoteAddrLocale.displayCountry}"/>
												</c:when>
												<c:otherwise>
													&nbsp;
												</c:otherwise>
											</c:choose>
										</display:column>

										<display:column style="white-space:nowrap;" sortable="true" titleKey="probe.jsp.connectors.wrk.col.remoteAddr">
											<a id="ip_${probe:escapeHtml(connector.name)}_${rp_rowNum}" href="#">${rp.remoteAddr}</a>

											<c:if test="${rp.remoteAddr != ''}">
												<script type="text/javascript">
													addAjaxTooltip('ip_${probe:escapeJS(connector.name)}_${rp_rowNum}',
													'ttdiv', '<c:url value="/whois.ajax?ip=${rp.remoteAddr}"/>');
												</script>
											</c:if>

										</display:column>

										<display:column sortable="true" sortProperty="stage"
												titleKey="probe.jsp.connectors.wrk.col.stage">
											<c:choose>
												<c:when test="${rp.stage == 1}">
													<spring:message code="probe.jsp.connectors.wrk.stage.parse"/>
												</c:when>
												<c:when test="${rp.stage == 2}">
													<spring:message code="probe.jsp.connectors.wrk.stage.prepare"/>
												</c:when>
												<c:when test="${rp.stage == 3}">
													<spring:message code="probe.jsp.connectors.wrk.stage.service"/>
												</c:when>
												<c:when test="${rp.stage == 4}">
													<spring:message code="probe.jsp.connectors.wrk.stage.endInput"/>
												</c:when>
												<c:when test="${rp.stage == 5}">
													<spring:message code="probe.jsp.connectors.wrk.stage.endOutput"/>
												</c:when>
												<c:when test="${rp.stage == 6}">
													<spring:message code="probe.jsp.connectors.wrk.stage.keepAlive"/>
												</c:when>
												<c:when test="${rp.stage == 7}">
													<spring:message code="probe.jsp.connectors.wrk.stage.ended"/>
												</c:when>
												<c:when test="${rp.stage == 0}">
													<spring:message code="probe.jsp.connectors.wrk.stage.new"/>
												</c:when>
												<c:otherwise>?</c:otherwise>
											</c:choose>
										</display:column>
										<display:column sortable="true" sortProperty="processingTime"
												titleKey="probe.jsp.connectors.wrk.col.processingTime">
											<probe:duration value="${rp.processingTime}"/>
										</display:column>
										<display:column sortable="true" sortProperty="bytesReceived"
												titleKey="probe.jsp.connectors.wrk.col.in">
											<probe:volume value="${rp.bytesReceived}"/>
										</display:column>
										<display:column sortable="true" sortProperty="bytesSent"
												titleKey="probe.jsp.connectors.wrk.col.out">
											<probe:volume value="${rp.bytesSent}"/>
										</display:column>

										<c:if test="${workerThreadNameSupported}">
											<display:column sortable="true" titleKey="probe.jsp.connectors.wrk.col.thread">
												<c:choose>
													<c:when test="${! empty rp.workerThreadName}">
														<a id="thr${rp_rowNum}">
															${rp.workerThreadName}
														</a>
														<script type="text/javascript">
															addAjaxTooltip('thr${rp_rowNum}', 'ttdiv', '<c:url value="/app/threadstack.ajax"/>?name=${rp.workerThreadName}');
														</script>
													</c:when>
													<c:otherwise>
														&nbsp;
													</c:otherwise>
												</c:choose>
											</display:column>

											<display:column sortable="false" title="&nbsp;" style="width:16px;">
												<c:choose>
													<c:when test="${! empty rp.workerThreadName}">
														<c:set var="confirmMessage">
															<spring:message code="probe.jsp.threads.killmsg" arguments="${rp.workerThreadName}"/>
														</c:set>
														<a class="imglink"
																onclick="return confirm('${confirmMessage}')"
																href="<c:url value='/adm/kill.htm'>
																<c:param name='thread' value='${rp.workerThreadName}'/>
																</c:url>">
															<img class="lnk" src="${pageContext.request.contextPath}<spring:theme code='delete.png'/>"
																	alt="<spring:message code='probe.jsp.threads.stop.alt'/>"/>
														</a>
													</c:when>
													<c:otherwise>
														&nbsp;
													</c:otherwise>
												</c:choose>
											</display:column>
										</c:if>

										<display:column sortable="true" titleKey="probe.jsp.connectors.wrk.col.url" >
											<c:choose>
												<c:when test="${rp.stage == 3 && ! empty rp.currentUri}">
													<c:set var="req">
														${rp.method}&nbsp;${rp.currentUri}<c:if test="${! empty rp.currentQueryString}">?${rp.currentQueryString}</c:if>
													</c:set>
													<probe:out value="${req}" maxLength="60"/>
												</c:when>
												<c:otherwise>
													&nbsp;
												</c:otherwise>
											</c:choose>
										</display:column>
									</display:table>
								</div>
							</c:when>
							<c:otherwise>
								<spring:message code="probe.jsp.connectors.wrk.empty"/>
							</c:otherwise>
						</c:choose>
					</div>

				</div>
			</c:forEach>
		</div>

		<script type="text/javascript">
			var rules = {
				'#ttdiv_close': function(e) {
					e.onclick = function(e) {
						Effect.Fade('ttdiv');
						return false
					}
				}
			}
			Behaviour.register(rules);
		</script>

	</body>
</html>
