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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<p>
	<em><spring:message code="probe.jsp.threads.col.priority"/></em> <spring:message code="probe.jsp.threads.help.priority"/>,
	<em><spring:message code="probe.jsp.threads.col.daemon"/></em> <spring:message code="probe.jsp.threads.help.daemon"/>,
	<em><spring:message code="probe.jsp.threads.col.interrupted"/></em> <spring:message code="probe.jsp.threads.help.interrupted"/>
</p>