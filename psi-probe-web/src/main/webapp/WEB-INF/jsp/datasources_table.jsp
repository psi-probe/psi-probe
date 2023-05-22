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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>

<display:table class="genericTbl" style="border-spacing:0;border-collapse:separate;" name="resources" uid="resource"
        htmlId="${isGlobalResources ? 'global_resources' : 'app_resources'}" requestURI="">

    <display:column class="leftmost" title="&#160;">
        <c:choose>
            <c:when test="${resource.dataSourceInfo.resettable}">
                <c:url value="/app/resetds.htm" var="reset_url">
                    <c:if test="${!isGlobalResources}">
                        <c:param name="webapp" value="${resource.applicationName}"/>
                    </c:if>
                    <c:param name="resource" value="${resource.name}"/>
                </c:url>
                <a class="imglink" href="${reset_url}">
                    <img border="0" src="${pageContext.request.contextPath}<spring:theme code='reset.gif'/>"
                            alt="<spring:message code='probe.jsp.datasources.list.col.reset.alt'/>"/>
                </a>
            </c:when>
            <c:otherwise>
                &#160;
            </c:otherwise>
        </c:choose>
    </display:column>

    <c:if test="${!isGlobalResources}">
        <display:column titleKey="probe.jsp.datasources.list.col.application" sortable="true" sortProperty="applicationName">
            <c:url value="/resources.htm" var="application_url">
                <c:param name="webapp" value="${resource.applicationName}"/>
            </c:url>
            <a href="${application_url}">
                <c:if test="${!resource.lookedUp || resource.dataSourceInfo.jdbcUrl == null}">
                    <img border="0" src="${pageContext.request.contextPath}<spring:theme code='exclamation.gif'/>"
                            alt="<spring:message code='probe.jsp.datasources.list.misconfigured.alt'/>"/>
                </c:if>
                ${resource.applicationName}
            </a>
        </display:column>
    </c:if>

    <display:column titleKey="probe.jsp.datasources.list.col.resource" sortable="true" sortProperty="name">
        <c:choose>
            <c:when test="${supportsDSLookup}">
                <c:url value="/sql/datasourcetest.htm" var="name_url">
                    <c:if test="${!isGlobalResources}">
                        <c:param name="webapp" value="${resource.applicationName}"/>
                    </c:if>
                    <c:param name="resource" value="${resource.name}"/>
                </c:url>
                <a href="${name_url}">
                    ${resource.name}
                </a>
            </c:when>
            <c:otherwise>
                ${resource.name}
            </c:otherwise>
        </c:choose>
    </display:column>

    <display:column titleKey="probe.jsp.datasources.list.col.usage" sortable="true" sortProperty="dataSourceInfo.busyScore" class="score_wrapper">
        <div class="score_wrapper">
            <probe:score value="${resource.dataSourceInfo.busyScore}" value2="${resource.dataSourceInfo.establishedScore - resource.dataSourceInfo.busyScore}" fullBlocks="10" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
                <img src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+"
                        title="<spring:message code='probe.jsp.applications.jdbcUsage.title' arguments='${resource.dataSourceInfo.busyScore},${resource.dataSourceInfo.establishedScore}'/>"/>
            </probe:score>
        </div>
    </display:column>

    <display:column titleKey="probe.jsp.datasources.list.col.max" sortable="true" property="dataSourceInfo.maxConnections"/>
    <display:column titleKey="probe.jsp.datasources.list.col.established" sortable="true" property="dataSourceInfo.establishedConnections"/>
    <display:column titleKey="probe.jsp.datasources.list.col.busy" sortable="true" property="dataSourceInfo.busyConnections"/>

    <display:column titleKey="probe.jsp.datasources.list.col.user" sortable="true" sortProperty="dataSourceInfo.username" property="dataSourceInfo.username" nulls="false">
        ${resource.dataSourceInfo.username}&#160;
    </display:column>

    <!-- this does have to be one liner due to the tag forcing maxLength -->
    <display:column titleKey="probe.jsp.datasources.list.col.url" sortable="true" property="dataSourceInfo.jdbcUrl" nulls="true" maxLength="50"/>

    <display:column titleKey="probe.jsp.datasources.list.col.description" sortable="true" sortProperty="description" maxLength="50">
        ${resource.description}&#160;
    </display:column>

    <display:column titleKey="probe.jsp.datasources.list.col.type" sortable="true" sortProperty="dataSourceInfo.type">
        ${resource.dataSourceInfo.type}&#160;
    </display:column>

    <c:if test="${! isGlobalResources}">
        <display:column titleKey="probe.jsp.datasources.list.col.linkTo" sortable="true" sortProperty="linkTo">
            ${resource.linkTo}&#160;
        </display:column>
    </c:if>

    <display:column titleKey="probe.jsp.datasources.list.col.auth" sortable="true" sortProperty="auth">
        ${resource.auth}&#160;
    </display:column>

</display:table>
