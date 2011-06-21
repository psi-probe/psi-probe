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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>

<p>
	<spring:message code="probe.jsp.followed_file_info.fileName"/>&nbsp;<span class="value">${log.file.absolutePath}</span>
	<spring:message code="probe.jsp.followed_file_info.size"/>&nbsp;<span class="value"><probe:volume value="${log.size}"/></span>
	<spring:message code="probe.jsp.followed_file_info.lastModified"/>&nbsp;<span class="value">${log.lastModified}</span>
</p>
