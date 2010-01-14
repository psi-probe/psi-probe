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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/tags/probe.tld" prefix="probe" %>

<html>
<head>
    <title><spring:message code="probe.jsp.title.datasources"/></title>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
    <script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
</head>

<body>

<c:set var="navTabDatasources" value="active" scope="request"/>

<c:choose>
    <c:when test="${! empty resources}">
        <c:if test="${! empty errorMessage}">
            <div class="errorMessage">
                <p>
                    ${errorMessage}
                </p>
            </div>
        </c:if>

        <ul class="options">
            <li id="groupByJdbcUrl">
                <a href="<c:url value='/datasourcegroups.htm' />"><spring:message code="probe.jsp.datasources.opt.groupByJdbcUrl"/></a>
            </li>
            <li id="abbreviations"><a href=""><spring:message code="probe.jsp.generic.abbreviations"/></a></li>
        </ul>

        <div class="blockContainer">

            <div id="help" class="helpMessage" style="display: none;">
                <div class="ajax_activity"></div>
            </div>

            <display:table class="genericTbl" cellspacing="0" name="resources" uid="resource" requestURI="">
                <c:choose>
                    <c:when test="${! global_resources}">

                        <display:column sortable="true" class="leftmost" titleKey="probe.jsp.datasources.list.col.application">
                            <a href="<c:url value='/resources.htm'/>?webapp=${resource.applicationName}">
                                <c:if test="${!resource.lookedUp || resource.dataSourceInfo.jdbcURL == null}">
                                    <img border="0" src="${pageContext.request.contextPath}<spring:theme code='exclamation.gif'/>" alt="<spring:message code='probe.jsp.datasources.list.misconfigured.alt'/>"/>
                                </c:if>
                                ${resource.applicationName}
                            </a>
                        </display:column>

                        <display:column sortable="true" sortProperty="name"
                                        titleKey="probe.jsp.datasources.list.col.resource">
                            <a href="<c:url value='/sql/datasourcetest.htm'/>?webapp=${resource.applicationName}&resource=${resource.name}">
                                ${resource.name}
                            </a>
                        </display:column>

                    </c:when>

                    <c:otherwise>

                        <display:column class="leftmost" property="name" sortable="true" titleKey="probe.jsp.datasources.list.col.resource"/>

                    </c:otherwise>
                </c:choose>

                <display:column sortable="true" sortProperty="dataSourceInfo.score"
                                titleKey="probe.jsp.datasources.list.col.usage" class="score_wrapper">
                    <div class="score_wrapper">
                        <probe:score value="${resource.dataSourceInfo.score}" fullBlocks="10" partialBlocks="5" showEmptyBlocks="true" showA="true" showB="true">
                            <img src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+" title="<spring:message code='probe.jsp.applications.jdbcUsage.title' arguments='${resource.dataSourceInfo.score}'/>"/>
                        </probe:score>
                    </div>
                </display:column>

                <display:column property="dataSourceInfo.maxConnections" sortable="true"
                                titleKey="probe.jsp.datasources.list.col.max"/>

                <display:column property="dataSourceInfo.establishedConnections" sortable="true"
                                titleKey="probe.jsp.datasources.list.col.established"/>

                <display:column property="dataSourceInfo.busyConnections" sortable="true"
                                titleKey="probe.jsp.datasources.list.col.busy"/>

                <display:column title="&nbsp;">
                    <c:choose>
                        <c:when test="${resource.dataSourceInfo.resettable}">
                            <a class="imglink" href="<c:url value='/app/resetds.htm'/>?webapp=${resource.applicationName}&resource=${resource.name}&view=redirect:/datasources.htm">
                                <img border="0" src="${pageContext.request.contextPath}<spring:theme code='reset.gif'/>" alt="<spring:message code='probe.jsp.datasources.list.col.reset.alt'/>"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </display:column>

                <display:column property="dataSourceInfo.username" sortable="true"
                                sortProperty="dataSourceInfo.username" nulls="false"
                                titleKey="probe.jsp.datasources.list.col.user">
                    ${resource.dataSourceInfo.username}&nbsp;
                </display:column>

                <!--
                this does have to be one liner due to the tag forcing maxLength
                -->
                <display:column property="dataSourceInfo.jdbcURL" sortable="true" maxLength="50" nulls="true"
                                titleKey="probe.jsp.datasources.list.col.url"/>

                <display:column maxLength="50" sortable="true" sortProperty="description"
                                titleKey="probe.jsp.datasources.list.col.description">
                    ${resource.description}&nbsp;
                </display:column>

                <display:column sortable="true" titleKey="probe.jsp.datasources.list.col.type">
                    <c:choose>
                        <c:when test="${resource.type == 'com.mchange.v2.c3p0.ComboPooledDataSource'}">
                            c3p0
                        </c:when>
                        <c:when test="${resource.type == 'javax.sql.DataSource'}">
                            dbcp
                        </c:when>
                        <c:when test="${resource.type == 'oracle.jdbc.pool.OracleDataSource'}">
                            oracle
                        </c:when>
                        <c:otherwise>
                            ${resource.type}
                        </c:otherwise>
                    </c:choose>
                </display:column>

                <display:column sortable="true" sortProperty="linkTo" titleKey="probe.jsp.datasources.list.col.linkTo">
                    ${resource.linkTo}&nbsp;
                </display:column>

                <display:column sortable="true" sortProperty="auth" titleKey="probe.jsp.datasources.list.col.auth">
                    ${resource.auth}&nbsp;
                </display:column>

            </display:table>
        </div>

        <script type="text/javascript">
            setupHelpToggle('<c:url value="/datasources.help.ajax"/>');
        </script>

    </c:when>
    <c:otherwise>
        <div class="errorMessage">
            <p>
                <spring:message code="probe.jsp.datasources.empty"/>
            </p>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>