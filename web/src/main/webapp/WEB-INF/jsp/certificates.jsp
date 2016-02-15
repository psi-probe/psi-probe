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
		<title><spring:message code="probe.jsp.title.certificates"/></title>
		<script type="text/javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/Tooltip.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/behaviour.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/js/func.js'/>"></script>
	</head>

	<c:set var="navTabCertificates" value="active" scope="request"/>

	<body>

		<div id="certificates">
			<c:if test="${systemCerts != null}">
				<c:set var="certs" value="${systemCerts}" scope="request" />
				<p>System Trust Store</p>
				<jsp:include page="certificates_table.jsp" />
			</c:if>

			<c:forEach items="${connectors}" var="connector">

				<c:if test="${connector.keyStoreCerts != null}">
					<c:set var="certs" value="${connector.keyStoreCerts}" scope="request" />
					<p>Key Store</p>
					<jsp:include page="certificates_table.jsp" />
				</c:if>

				<c:if test="${connector.trustStoreCerts != null}">
					<c:set var="certs" value="${connector.trustStoreCerts}" scope="request" />
					<p>Trust Store</p>
					<jsp:include page="certificates_table.jsp" />
				</c:if>

			</c:forEach>

		</div>
	</body>
</html>
