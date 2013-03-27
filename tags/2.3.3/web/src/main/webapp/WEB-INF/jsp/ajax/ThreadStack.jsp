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

<div>
	<div id="tooltip_title">
		${threadName}
	</div>
	<c:choose>
		<c:when test="${! empty stack}">

			<c:forEach items="${stack}" var="element">
				<div>
					${element.className}.${element.methodName}
					(
					<c:choose>
						<c:when test="${!element.nativeMethod && element.lineNumber > 0}">
							${element.fileName}:${element.lineNumber}
						</c:when>
						<c:when test="${element.nativeMethod}">
							<spring:message code="probe.jsp.threadstack.native"/>
						</c:when>
						<c:otherwise>
							<spring:message code="probe.jsp.threadstack.unknown"/>
						</c:otherwise>
					</c:choose>
					)
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<spring:message code="probe.jsp.threadstack.unavailable"/>
		</c:otherwise>
	</c:choose>
</div>