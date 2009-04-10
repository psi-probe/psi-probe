<%--
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%--
    An Ajax snippet that displays processing time value
    to be used in the application average processing time chart

    Author: Andy Shapoval
--%>

<c:if test="${! empty app}">
  <span class="name"><spring:message code="probe.jsp.app.summary.charts.avgProcTime"/></span>&nbsp;${app.avgTime}&nbsp
  <span class="name"><spring:message code="probe.jsp.app.summary.charts.proc.time"/></span>&nbsp;${app.processingTime}
</c:if>