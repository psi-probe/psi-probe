<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<display:table class="genericTbl" style="border-spacing:0;border-collapse:separate;" name="${certs}" uid="cert" requestURI="">

    <display:column property="alias" class="leftmost" sortable="true" nulls="false"
                titleKey="probe.jsp.certificates.col.alias" />

    <display:column property="subjectDistinguishedName" class="leftmost" sortable="true" nulls="false"
                titleKey="probe.jsp.certificates.col.dn" />

    <display:column class="leftmost" sortable="true" nulls="false"
                titleKey="probe.jsp.certificates.col.notBefore">
        <fmt:formatDate value="${cert.notBefore}" type="BOTH" dateStyle="SHORT" timeStyle="MEDIUM"/>
    </display:column>

    <display:column class="leftmost" sortable="true" nulls="false"
                titleKey="probe.jsp.certificates.col.notAfter">
        <fmt:formatDate value="${cert.notAfter}" type="BOTH" dateStyle="SHORT" timeStyle="MEDIUM"/>
    </display:column>

    <display:column title="&#160;">
        <img border="0" src="${pageContext.request.contextPath}<spring:theme code='magnifier.png'/>" title="<spring:message code='probe.jsp.certificates.viewCertDetails'/>">
    </display:column>

</display:table>
