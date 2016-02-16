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
			<c:if test="${systemKeyCerts != null || systemTrustCerts != null}">
				<h3><spring:message code="probe.jsp.certificates.systemCertificates"/></h3>
			</c:if>

			<div class="connectorCertificates">
				<c:if test="${systemKeyCerts != null}">
					<c:set var="certs" value="${systemKeyCerts}" scope="request" />
					<h4><spring:message code="probe.jsp.certificates.keyStore"/></h4>
					<jsp:include page="certificates_table.jsp" />
				</c:if>
				<c:if test="${systemTrustCerts != null}">
					<c:set var="certs" value="${systemTrustCerts}" scope="request" />
					<h4><spring:message code="probe.jsp.certificates.trustStore"/></h4>
					<jsp:include page="certificates_table.jsp" />
				</c:if>
			</div>

			<h3 style="margin: 20px 0 0px 0"><spring:message code="probe.jsp.certificates.connectorsCertificates"/></h3>

			<c:forEach items="${connectors}" var="connector">
			
				<h4 style="padding-top: 10px">${connector.name}</h4>

				<div class="connectorCertificates">
					<c:if test="${connector.keyStoreCerts != null}">
						<c:set var="certs" value="${connector.keyStoreCerts}" scope="request" />
						<h4><spring:message code="probe.jsp.certificates.keyStore"/></h4>
						<jsp:include page="certificates_table.jsp" />
					</c:if>
					<c:if test="${connector.trustStoreCerts != null}">
						<c:set var="certs" value="${connector.trustStoreCerts}" scope="request" />
						<h4><spring:message code="probe.jsp.certificates.trustStore"/></h4>
						<jsp:include page="certificates_table.jsp" />
					</c:if>
				</div>

			</c:forEach>
		</div>
	</body>
</html>
