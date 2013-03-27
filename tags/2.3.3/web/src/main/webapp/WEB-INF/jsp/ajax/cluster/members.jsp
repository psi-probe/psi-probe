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

<c:choose>
	<c:when test="${! empty cluster.members}">
		<display:table name="cluster.members" class="genericTbl" cellspacing="0" uid="member" requestURI="">

			<display:column title="&nbsp;" class="leftMostIcon" style="width:16px;">
				<c:choose>
					<c:when test="${member.suspect}">
						<img src="${pageContext.request.contextPath}<spring:theme code='transmit_error.png'/>" alt="<spring:message code='probe.jsp.cluster.members.failing.alt'/>"
								title="<spring:message code='probe.jsp.cluster.members.failing.title'/>"/>
					</c:when>
					<c:otherwise>
						<img src="${pageContext.request.contextPath}<spring:theme code='transmit.png'/>" alt="<spring:message code='probe.jsp.cluster.members.healthy.alt'/>"
								title="<spring:message code='probe.jsp.cluster.members.healthy.title'/>"/>
					</c:otherwise>
				</c:choose>
			</display:column>

			<display:column titleKey="probe.jsp.cluster.members.col.address" sortable="true">
				${member.address}:${member.port}
			</display:column>

			<display:column titleKey="probe.jsp.cluster.members.col.connected" sortable="true" sortProperty="connected">
				<a href="#">
					<c:choose>
						<c:when test="${member.connected}">
							<spring:message code="probe.jsp.generic.yes"/>
						</c:when>
						<c:otherwise>
							<spring:message code="probe.jsp.generic.no"/>
						</c:otherwise>
					</c:choose>
				</a>
			</display:column>

			<display:column titleKey="probe.jsp.cluster.members.col.keepAlive" sortable="true"
					sortProperty="keepAliveTimeout">
				${member.keepAliveTimeout}ms.
			</display:column>

			<display:column titleKey="probe.jsp.cluster.members.col.connectCount" sortable="true"
					property="connectCounter"/>
			<display:column titleKey="probe.jsp.cluster.members.col.disconnectCount" sortable="true"
					property="disconnectCounter"/>

			<display:column titleKey="probe.jsp.cluster.members.col.requests" sortable="true" property="nrOfRequests"/>

			<display:column titleKey="probe.jsp.cluster.members.col.avgMsgSize" sortable="true"
					sortProperty="avgMessageSize">
				<probe:volume value="${member.avgMessageSize}"/>
			</display:column>

			<display:column titleKey="probe.jsp.cluster.members.col.sent" sortable="true" sortProperty="totalBytes">
				<probe:volume value="${member.totalBytes}"/>
			</display:column>

			<c:if test="${cluster.senderReplicationMode == 'pooled'}">
				<display:column titleKey="probe.jsp.cluster.members.col.socketLimit" property="maxPoolSocketLimit"
						sortable="true"/>
			</c:if>

			<c:if test="${cluster.senderReplicationMode == 'synchronous' || cluster.senderReplicationMode == 'asynchronous' ||
					cluster.senderReplicationMode == 'fastasyncqueue'}">
				<display:column titleKey="probe.jsp.cluster.members.col.failures" property="dataFailureCounter"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.resends" property="dataResendCounter"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.opens" property="socketOpenCounter"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.closes" property="socketCloseCounter"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.openFailures"
						property="socketOpenFailureCounter" sortable="true"/>
			</c:if>

			<c:if test="${cluster.senderReplicationMode == 'asynchronous' || cluster.senderReplicationMode == 'fastasyncqueue'}">
				<display:column titleKey="probe.jsp.cluster.members.col.inQueueCounter" property="inQueueCounter"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.outQueueCounter" property="outQueueCounter"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.queueSize" property="queueSize"
						sortable="true"/>
				<display:column titleKey="probe.jsp.cluster.members.col.queueSizeBytes" property="queuedNrOfBytes"
						sortable="true"/>
			</c:if>
		</display:table>
	</c:when>
	<c:otherwise>
		<div class="warningMessage">
			<p>
				<spring:message code="probe.jsp.cluster.noMembers"/>
			</p>
		</div>
	</c:otherwise>
</c:choose>
