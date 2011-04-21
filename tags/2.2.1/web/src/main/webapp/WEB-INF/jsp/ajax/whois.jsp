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

<div id="tooltip_title">
	${param.ip} (${domainName})
</div>
<div>
	[${whoisServer}]
</div>
<c:choose>
	<c:when test="${! empty result}">
		<code>
			<c:forEach items="${result}" var="line">
				<div>
					${line}&nbsp;
				</div>
			</c:forEach>
		</code>
	</c:when>
	<c:when test="${timeout}">
		<spring:message code="probe.jsp.whois.timeout"/>
	</c:when>
</c:choose>
