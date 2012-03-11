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

<%--
	an Ajax HTML snippet to display results returned by an SQL query

	Author: Andy Shapoval
--%>

<div id="sqlResultsContainer">

	<c:if test="${! empty errorMessage}">
		<div id="rs_error" class="errorMessage">
			<p>${errorMessage}</p>
		</div>
	</c:if>

	<c:if test="${! empty rowsAffected}">

		<div id="rs_rowsAffected">
			<spring:message code="probe.jsp.dataSourceTest.results.rowcount" arguments="${rowsAffected}"/>
		</div>

	</c:if>

	<c:choose>
		<c:when test="${! empty results}">
			<c:url value="/sql/cachedRecordset.ajax" var="requestURI" />
			<display:table htmlId="sqlResultTbl" name="results" uid="row" class="genericTbl"
					cellspacing="0" cellpadding="0" excludedParams="*"
					requestURI="${requestURI}" pagesize="${rowsPerPage}">
				<display:setProperty name="paging.banner.item_name">
					<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.item_name"/>
				</display:setProperty>
				<display:setProperty name="paging.banner.items_name">
					<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.items_name"/>
				</display:setProperty>
				<display:setProperty name="paging.banner.no_items_found">
					<span id="rs_pagebanner"><spring:message code="probe.jsp.dataSourceTest.results.paging.banner.no_items_found"/></span>
				</display:setProperty>
				<display:setProperty name="paging.banner.one_item_found">
					<span id="rs_pagebanner"><spring:message code="probe.jsp.dataSourceTest.results.paging.banner.one_item_found"/></span>
				</display:setProperty>
				<display:setProperty name="paging.banner.all_items_found">
					<span id="rs_pagebanner"><spring:message code="probe.jsp.dataSourceTest.results.paging.banner.all_items_found"/></span>
				</display:setProperty>
				<display:setProperty name="paging.banner.some_items_found">
					<span id="rs_pagebanner"><spring:message code="probe.jsp.dataSourceTest.results.paging.banner.some_items_found"/></span>
				</display:setProperty>
				<display:setProperty name="paging.banner.full">
					<span id="rs_pagelinks">[<a href="{1}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.first"/></a>/<a href="{2}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.prev"/></a>] {0} [<a href="{3}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.next"/></a>/<a href="{4}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.last"/></a>]</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.first">
					<span id="rs_pagelinks">[<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.first"/>/<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.prev"/>] {0} [<a href="{3}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.next"/></a>/<a href="{4}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.last"/></a>]</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.last">
					<span id="rs_pagelinks">[<a href="{1}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.first"/></a>/<a href="{2}">
							<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.prev"/></a>] {0} [<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.next"/>/<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.last"/>]</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.onepage">
					<span id="rs_pagelinks">[<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.first"/>/<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.prev"/>] {0} [<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.next"/>/<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.last"/>]</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.page.link">
					<a href="{1}" title="<spring:message code="probe.jsp.dataSourceTest.results.paging.banner.page.link.title"/>">{0}</a>
				</display:setProperty>
			</display:table>
		</c:when>
		<c:when test="${empty errorMessage}">
			<div id="rs_empty" class="infoMessage">
				<p>
					<spring:message code="probe.jsp.dataSourceTest.sql.completed"/>
					<spring:message code="probe.jsp.dataSourceTest.results.rowcount" arguments="${rowsAffected}"/>
				</p>
			</div>
		</c:when>
	</c:choose>
</div>
