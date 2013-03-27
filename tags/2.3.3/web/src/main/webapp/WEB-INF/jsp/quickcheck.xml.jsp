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

<%@ page contentType="text/xml;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
	"Quick check" results in machine-readable XML format.

	Author: Vlad Ilyushchenko
--%>

<report>
	<c:choose>
		<c:when test="${testReport.datasourceTest == 1 &&
						testReport.datasourceUsageScore < 100 &&
						testReport.memoryTest == 1 &&
						testReport.fileTest == 1 &&
						(testReport.webappAvailabilityTest == 1 || (! empty param.webapp && !param.webapp))}">
			<status>OK</status>
		</c:when>
		<c:otherwise>
			<status>FAULTY</status>
		</c:otherwise>
	</c:choose>
</report>
