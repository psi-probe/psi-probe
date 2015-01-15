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
	<em><spring:message code="probe.jsp.datasources.list.col.max"/></em> <spring:message code="probe.jsp.datasources.help.max"/>,
	<em><spring:message code="probe.jsp.datasources.list.col.established"/></em> <spring:message code="probe.jsp.datasources.help.established"/>,
	<em><spring:message code="probe.jsp.datasources.list.col.busy"/></em> <spring:message code="probe.jsp.datasources.help.busy"/>
</p>