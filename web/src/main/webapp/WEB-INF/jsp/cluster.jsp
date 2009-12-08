<%--
  ~ Licensed under the GPL License. You may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~   http://probe.jstripe.com/d/license.shtml
  ~
  ~  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
  ~  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
  ~  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tags/jstripe.tld" prefix="inf" %>

<%--
    Cluster statistic view. Displays cluster members and sender and receiver traffic stats.

    Author: Vlad Ilyushchenko
--%>
<html>
<head>
    <title><spring:message code="probe.jsp.title.cluster"/></title>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
</head>

<c:set var="navTabCluster" value="active" scope="request"/>

<c:url value="/chart.png" var="receivedimg" scope="page">
    <c:param name="xz" value="400"/>
    <c:param name="yz" value="250"/>
    <c:param name="s1l" value="sent"/>
    <c:param name="s2l" value="received"/>
    <c:param name="l" value="true"/>
    <c:param name="s2c" value="#95FE8B"/>
    <c:param name="s2o" value="#009406"/>
    <c:param name="p" value="cl_traffic"/>
</c:url>

<c:url value="/chart.png" var="sentimg" scope="page">
    <c:param name="xz" value="400"/>
    <c:param name="yz" value="250"/>
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
                    <p><spring:message code="probe.jsp.cluster.name"/>&nbsp;<span class="value">${cluster.name}</span>
                        <spring:message code="probe.jsp.cluster.info"/>&nbsp;<span class="value">${cluster.info}</span>
                        <spring:message code="probe.jsp.cluster.manager"/>&nbsp;<span class="value">${cluster.managerClassName}</span>
                        <spring:message code="probe.jsp.cluster.mode"/>&nbsp;<span class="value">${cluster.senderReplicationMode}</span>
                        <spring:message code="probe.jsp.cluster.mcastAddress"/>&nbsp;<span class="value">${cluster.mcastAddress}:${cluster.mcastPort}</span>
                        <spring:message code="probe.jsp.cluster.mcastTTL"/>&nbsp;<span class="value">${cluster.mcastTTL}</span>
                        <spring:message code="probe.jsp.cluster.mcastBindAddress"/>&nbsp;
                        <span class="value">
                            <c:choose>
                                <c:when test="${! empty cluster.mcastBindAddress}">
                                    ${cluster.mcastBindAddress}
                                </c:when>
                                <c:otherwise>
                                    <spring:message code="probe.jsp.cluster.mcastBindAddress.all"/>&nbsp;
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <spring:message code="probe.jsp.cluster.heartbeatFreq"/>&nbsp;<span class="value">${cluster.mcastFrequency}ms.</span>
                        <spring:message code="probe.jsp.cluster.heartbeatTimeout"/>&nbsp;<span class="value">${cluster.mcastDropTime}ms.</span>
                        <spring:message code="probe.jsp.cluster.receiverAddress"/>&nbsp;<span class="value">${cluster.tcpListenAddress}</span>
                        <spring:message code="probe.jsp.cluster.receiverPort"/>&nbsp;<span class="value">${cluster.tcpListenPort}</span>
                    </p>
                </div>
            </div>

            <div>
                <div class="chartContainer">
                    <dl>
                        <dt><spring:message code="probe.jsp.cluster.chart.traffic"/></dt>
                        <dd>
                            <img id="cl_traffic" border="0" src="${receivedimg}" alt="Bytes received"/>
                        </dd>
                        <dd id="dd_traffic"><div class="ajax_activity"/></dd>
                    </dl>
                </div>

                <div class="chartContainer">
                    <dl>
                        <dt><spring:message code="probe.jsp.cluster.chart.requests"/></dt>
                        <dd>
                            <img id="cl_requests" border="0" src="${sentimg}" alt="Bytes sent"/>
                        </dd>
                        <dd id="dd_requests"><div class="ajax_activity"/></dd>
                    </dl>
                </div>
            </div>

            <div style="clear: both;"/>

            <h3><spring:message code="probe.jsp.cluster.h3.members"/></h3>

            <div id="members">
                <div class="ajax_activity"/>
                <%--<p class="empty_list"><spring:message code="probe.jsp.cluster.loading"/></p>--%>
            </div>
        </div>

        <script type="text/javascript">
            new Ajax.ImgUpdater('cl_traffic', 30);
            new Ajax.ImgUpdater('cl_requests', 30);
            new Ajax.PeriodicalUpdater('dd_traffic', '<c:url value="/cluster/traffic.ajax"/>', {frequency: 3});
            new Ajax.PeriodicalUpdater('dd_requests', '<c:url value="/cluster/requests.ajax"/>', {frequency: 3});
            new Ajax.PeriodicalUpdater('members', '<c:url value="/cluster/members.ajax"/>?<%=request.getQueryString()%>', {frequency: 3});
        </script>


    </c:otherwise>
</c:choose>


</body>
</html>