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

<html>

	<head>
		<title><spring:message code="probe.jsp.title.testDataSource" arguments="${param.webapp},${param.resource}"/></title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='datasourcetest.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='scroller.css'/>"/>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/areascroller.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/datasourcetest.js'/>"></script>
	</head>

	<%--
		Provides GUI for datasource connectivity testing.
		Allows a user to enter an SQL query and displays results returned by the query.

		Author: Andy Shapoval, Vlad Ilyushchenko
	--%>

	<body>

		<c:set var="navTabDatasources" value="active" scope="request"/>

		<div class="dataSourceTestMenu">
			<ul class="options">
				<c:if test="${! empty backURL}">
					<li id="back">
						<a href="${backURL}">
							<spring:message code="probe.jsp.dataSourceTest.menu.back"/>
						</a>
					</li>
				</c:if>
				<li id="connect">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.connect"/>
					</a>
				</li>
				<li id="executeSql">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.execute"/>
					</a>
				</li>
				<li id="showHistory">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.showHistory"/>
					</a>
				</li>
				<li id="hideHistory" style="display: none;">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.hideHistory"/>
					</a>
				</li>
				<li id="showOptions">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.showOptions"/>
					</a>
				</li>
				<li id="hideOptions" style="display: none;">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.hideOptions"/>
					</a>
				</li>
				<li id="abbreviations">
					<a href="#">
						<spring:message code="probe.jsp.dataSourceTest.menu.abbreviations"/>
					</a>
				</li>
			</ul>
		</div>

		<div class="dataSourceTestContent">
			<div id="help" class="helpMessage" style="display: none;">
				<div class="ajax_activity"></div>
			</div>

			<form id="sqlForm" action="" method="post">
				<c:if test="${param.webapp != null}">
					<input type="hidden" name="webapp" value="${param.webapp}"/>
				</c:if>
				<input type="hidden" name="resource" value="${param.resource}"/>
				<dl id="sqlDL">
					<dt><label for="sql"><spring:message code="probe.jsp.dataSourceTest.sqlForm.sql.label"/></label></dt>
					<dd id="sqlContainer">
						<textarea id="sql" name="sql" rows="5" cols="80"></textarea>
						<div id="sqlDragHandle">&nbsp;</div>
					</dd>
				</dl>
				<dl id="optionsDL" style="display: none;">
					<dt><label for="maxRows"><spring:message code="probe.jsp.dataSourceTest.sqlForm.maxRows.label"/></label></dt>
					<dd><input type="text" id="maxRows" name="maxRows" class="txtInput" value="${maxRows}" size="6"/></dd>
					<dt><label for="rowsPerPage"><spring:message code="probe.jsp.dataSourceTest.sqlForm.rowsPerPage.label"/></label></dt>
					<dd><input type="text" id="rowsPerPage" name="rowsPerPage" class="txtInput" value="${rowsPerPage}" size="6"/></dd>
					<dt><label for="historySize"><spring:message code="probe.jsp.dataSourceTest.sqlForm.historySize.label"/></label></dt>
					<dd><input type="text" id="historySize" name="historySize"  class="txtInput" value="${historySize}" size="6"/></dd>
				</dl>
			</form>

			<div id="queryHistoryContainer" style="display: none;">
				<h3 id="queryHistoryH3"><spring:message code="probe.jsp.dataSourceTest.h3.queryHistory"/></h3>
				<ul class="options">
					<li id="wrap">
						<a href="#">
							<spring:message code="probe.jsp.dataSourceTest.menu.wrap"/>
						</a>
					</li>
					<li id="nowrap" style="display: none;">
						<a href="#">
							<spring:message code="probe.jsp.dataSourceTest.menu.nowrap"/>
						</a>
					</li>
				</ul>
				<div id="queryHistoryBorder">
					<div id="queryHistoryHolder"></div>
					<div id="historyDragHandle">&nbsp;</div>
				</div>
			</div>

			<div id="sqlResultsHeader">
				<h3 id="metaDataH3" style="display: none;"><spring:message code="probe.jsp.dataSourceTest.h3.metaData"/></h3>

				<h3 id="resultsH3" style="display: none;"><spring:message code="probe.jsp.dataSourceTest.h3.results"/></h3>

				<div id="ajaxActivity" class="ajax_activity" style="display: none;"></div>
			</div>

			<div id="sqlResultsWrapper" style="display: none;">
				<div>
					<span id="rowsAffected"></span><span id="pagebanner"></span><span id="pagelinks"></span>
				</div>
				<table id="resultsTable" cellspacing="0">
					<tr>
						<td id="left_scroller" class="scroller" style="display: none;">&nbsp;</td>
						<td id="separator" width="1%" style="display: none;">&nbsp;</td>
						<td><div id="outputHolder"></div></td>
						<td id="right_scroller" class="scroller" style="display: none;">&nbsp;</td>
					</tr>
				</table>
			</div>

		</div>

		<div id="charts" class="embeddedBlockContainer">
			<c:set var="chartWidth" value="400"/>
			<c:set var="chartHeight" value="250"/>

			<c:url value="/chart.png" var="usage_img" scope="page">
				<c:param name="p" value="datasource_usage"/>
				<c:param name="sp" value="${param.webapp == null ? '' : param.webapp}/${param.resource}"/>
				<c:param name="xz" value="${chartWidth}"/>
				<c:param name="yz" value="${chartHeight}"/>
				<c:param name="l" value="false"/>
			</c:url>
			<div class="chartContainer">
				<dl>
					<dt><spring:message code="probe.jsp.dataSourceTest.chart.usage.title"/></dt>
					<dd class="image">
						<img id="usage-${param.resource}" border="0" src="${usage_img}" width="${chartWidth}" height="${chartHeight}" alt="Datasource usage"/>
					</dd>
				</dl>
			</div>
		</div>

		<script type="text/javascript">
			new Ajax.ImgUpdater('usage-${param.resource}', ${probe:max(collectionPeriod, 5)});
			setupAjaxActions(
				'<c:url value="/sql/connection.ajax"/>',
				'<c:url value="/sql/recordset.ajax"/>',
				'<c:url value="/sql/queryHistory.ajax"/>');
			setupShortcuts();
			setupHelpToggle('<c:url value="/help/datasourcetest.ajax"/>');
			new Draggable('sqlDragHandle', {
				constraint: 'vertical',
				change: resizeTextArea,
				revert: revertDragHandle
			});
			new Draggable('historyDragHandle', {
				constraint: 'vertical',
				change: resizeQueryHistory,
				revert: revertDragHandle
			});
			setupScrollers('sqlResultsContainer');

			$('sql').focus();
		</script>

	</body>

</html>
