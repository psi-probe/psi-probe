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

<display:table name="pools" class="genericTbl" cellspacing="0" uid="pool" requestURI="">

	<display:column title="&nbsp;" style="width:16px;" class="leftMostIcon">
		<c:choose>
			<c:when test="${pool.type == 'HEAP'}">
				<img src="${pageContext.request.contextPath}<spring:theme code='heap_pool.png'/>" alt=""/>
			</c:when>
			<c:otherwise>
				<img src="${pageContext.request.contextPath}<spring:theme code='non_heap_pool.png'/>" alt=""/>
			</c:otherwise>
		</c:choose>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.name" sortable="true" style="width:180px;">
		${pool.name}
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.usageScore" sortable="true" class="score_wrapper">
		<div class="score_wrapper">
			<probe:score value="${pool.usageScore}" partialBlocks="5" fullBlocks="10" showEmptyBlocks="true" showA="true" showB="true">
				<img src="<c:url value='/css/classic/gifs/rb_{0}.gif'/>" alt="+"
						title="<spring:message code='probe.jsp.memory.usage.title' arguments='${pool.usageScore}'/>"/>
			</probe:score>
		</div>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.plot" style="width:20px;">

		<c:set var="cookie_name" value="mem_${pool.id}" scope="page"/>

		<c:choose>
			<c:when test="${cookie[probe:safeCookieName(cookie_name)].value == 'off'}">
				<c:set var="style_on" value="display:none"/>
				<c:set var="style_off" value=""/>
			</c:when>
			<c:otherwise>
				<c:set var="style_on" value=""/>
				<c:set var="style_off" value="display:none"/>
			</c:otherwise>
		</c:choose>

		<c:url var="poolUrl" value="/remember.ajax?cn=mem_${pool.id}" />
		<img id="visible_${pool.id}"
				onclick="return togglePanel('${pool.id}', '${poolUrl}')"
				class="clickable"
				style="${style_on}"
				src="${pageContext.request.contextPath}<spring:theme code='memory_chart_visible.png'/>"
				alt=""
				border="0"/>

		<img id="invisible_${pool.id}"
				onclick="return togglePanel('${pool.id}', '${poolUrl}')"
				class="clickable"
				style="${style_off}"
				src="${pageContext.request.contextPath}<spring:theme code='memory_chart_hidden.png'/>"
				alt=""
				border="0"/>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.used" sortable="true" sortProperty="used" style="width:80px;">
		<probe:volume value="${pool.used}" fractions="2"/>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.committed" sortable="true" sortProperty="committed" style="width:80px;">
		<probe:volume value="${pool.committed}" fractions="2"/>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.max" sortable="true" sortProperty="max" style="width:80px;">
		<probe:volume value="${pool.max}" fractions="2"/>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.initial" sortable="true" sortProperty="init" style="width:80px;">
		<probe:volume value="${pool.init}" fractions="2"/>
	</display:column>

	<display:column titleKey="probe.jsp.memory.col.group" property="type" sortable="true"/>

</display:table>
