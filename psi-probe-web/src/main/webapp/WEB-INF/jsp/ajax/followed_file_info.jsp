<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="https://github.com/psi-probe/psi-probe/jsp/tags" prefix="probe" %>

<p>
    <spring:message code="probe.jsp.followed_file_info.fileName"/>&#160;<span class="value">${log.file.absolutePath}</span>
    <spring:message code="probe.jsp.followed_file_info.size"/>&#160;<span class="value"><probe:volume value="${log.size}"/></span>
    <spring:message code="probe.jsp.followed_file_info.lastModified"/>&#160;<span class="value">${log.lastModified}</span>
</p>
