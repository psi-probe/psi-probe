<%--
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/fmt' prefix='fmt' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn' %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tags/jstripe.tld" prefix="js" %>

<%--
    Displays current traffic information.

    Author: Vlad Ilyushchenko
--%>

<html>
<head>
    <title><spring:message code="probe.jsp.title.status"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='status.css'/>" type="text/css"/>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/Tooltip.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
</head>

<body>

<c:set var="navTabStatus" value="active" scope="request"/>

<div id="ttdiv" class="tooltip" style="display: none;">
    <div class="tt_top">
        <span id="tt_title" style="display: none;"></span>
        <a id="ttdiv_close" href="#"><spring:message code="probe.jsp.tooltip.close"/></a>
    </div>
    <div class="tt_content" id="tt_content"></div>
</div>

<c:forEach items="${pools}" var="pool" varStatus="poolStatus">

    <div class="poolInfo">
        <b>${pool.name}</b><br/>

        <div class="threadInfo">
            <spring:message code="probe.jsp.status.currentThreadCount"/>&nbsp;${pool.currentThreadCount}
            <spring:message code="probe.jsp.status.currentThreadsBusy"/>&nbsp;${pool.currentThreadsBusy}
            <spring:message code="probe.jsp.status.maxThreads"/>&nbsp;${pool.maxThreads}
            <spring:message code="probe.jsp.status.maxSpareThreads"/>&nbsp;${pool.maxSpareThreads}
            <spring:message code="probe.jsp.status.minSpareThreads"/>&nbsp;${pool.minSpareThreads}
        </div>

        <div class="processorInfo">
            <spring:message code="probe.jsp.status.processor.maxTime"/>&nbsp;${pool.maxTime}
            <spring:message code="probe.jsp.status.processor.processingTime"/>&nbsp;${pool.processingTime}
            <spring:message code="probe.jsp.status.processor.requestCount"/>&nbsp;${pool.requestCount}
            <spring:message code="probe.jsp.status.processor.errorCount"/>&nbsp;${pool.errorCount}
            <spring:message code="probe.jsp.status.processor.received"/>&nbsp;<js:volume value="${pool.bytesReceived}"/>
            <spring:message code="probe.jsp.status.processor.sent"/>&nbsp;<js:volume value="${pool.bytesSent}"/>
        </div>

        <c:choose>
            <c:when test="${! empty pool.requestProcessors}">
                <div class="workerInfo">
                    <display:table name="${pool.requestProcessors}"
                                   class="genericTbl" cellspacing="0"
                                   requestURI="" id="rp" defaultsort="7" defaultorder="descending">
                        <display:column title="&nbsp;" width="18px" class="leftmost">
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

                        <display:column width="90px"
                                        nowrap="" sortable="true" titleKey="probe.jsp.status.wrk.col.remoteAddr">
                            <a id="ip_${pool.name}_${rp_rowNum}" href="#">${rp.remoteAddr}</a>

                            <script type="text/javascript">
                                addAjaxTooltip('ip_${pool.name}_${rp_rowNum}',
                                        'ttdiv', '<c:url value="/whois.ajax?ip=${rp.remoteAddr}"/>');
                            </script>

                        </display:column>

                        <display:column width="60px" sortable="true" sortProperty="stage"
                                        titleKey="probe.jsp.status.wrk.col.stage">
                            <c:choose>
                                <c:when test="${rp.stage == 1}"><spring:message
                                        code="probe.jsp.status.wrk.stage.parse"/></c:when>
                                <c:when test="${rp.stage == 2}"><spring:message
                                        code="probe.jsp.status.wrk.stage.prepare"/></c:when>
                                <c:when test="${rp.stage == 3}"><spring:message
                                        code="probe.jsp.status.wrk.stage.service"/></c:when>
                                <c:when test="${rp.stage == 4}"><spring:message
                                        code="probe.jsp.status.wrk.stage.endInput"/></c:when>
                                <c:when test="${rp.stage == 5}"><spring:message
                                        code="probe.jsp.status.wrk.stage.endOutput"/></c:when>
                                <c:when test="${rp.stage == 6}"><spring:message
                                        code="probe.jsp.status.wrk.stage.keepAlive"/></c:when>
                                <c:when test="${rp.stage == 7}"><spring:message
                                        code="probe.jsp.status.wrk.stage.ended"/></c:when>
                                <c:when test="${rp.stage == 0}"><spring:message code="probe.jsp.status.wrk.stage.new"/>
                                </c:when>
                                <c:otherwise>?</c:otherwise>
                            </c:choose>
                        </display:column>
                        <display:column width="80px" sortable="true" sortProperty="processingTime"
                                        titleKey="probe.jsp.status.wrk.col.processingTime">
                            <js:duration value="${rp.processingTime}"/>
                        </display:column>
                        <display:column width="60px" sortable="true" sortProperty="bytesReceived"
                                        titleKey="probe.jsp.status.wrk.col.in">
                            <js:volume value="${rp.bytesReceived}"/>
                        </display:column>
                        <display:column width="60px" sortable="true" sortProperty="bytesSent"
                                        titleKey="probe.jsp.status.wrk.col.out">
                            <js:volume value="${rp.bytesSent}"/>
                        </display:column>

                        <c:if test="${workerThreadNameSupported}">
                            <display:column width="130px" sortable="true" titleKey="probe.jsp.status.wrk.col.thread">
                                <c:choose>
                                    <c:when test="${! empty rp.workerThreadName}">
                                        <a id="thr${rp.workerThreadName}">
                                            ${rp.workerThreadName}
                                        </a>
                                        <script type="text/javascript">
                                            addAjaxTooltip('thr${rp.workerThreadName}', 'ttdiv', '<c:url value="/app/threadstack.ajax"/>?name=${rp.workerThreadName}');
                                        </script>
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;
                                    </c:otherwise>
                                </c:choose>
                            </display:column>

                        </c:if>

                        <display:column sortable="true" titleKey="probe.jsp.status.wrk.col.url" >
                            <c:choose>
                                <c:when test="${rp.stage == 3 && ! empty rp.currentUri}">
                                    ${rp.method}&nbsp;${rp.currentUri}<c:if test="${! empty rp.currentQueryString}">?${rp.currentQueryString}</c:if>
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
                <spring:message code="probe.jsp.status.wrk.empty"/>
            </c:otherwise>
        </c:choose>
    </div>
</c:forEach>

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
