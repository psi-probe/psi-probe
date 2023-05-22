<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%-- An Ajax HTML snippet to display datasource connection info. --%>

<div id="sqlResultsContainer">

    <c:if test="${! empty errorMessage}">
        <div class="errorMessage">
            <p>${errorMessage}</p>
        </div>
    </c:if>

    <c:if test="${! empty dbMetaData}">
        <div id="dbMetaData" class="sqlResultsContainer">
            <display:table name="dbMetaData" uid="row" class="genericTbl"
                    style="padding:0;border-spacing:0;border-collapse:separate;" requestURI="">
                <display:column class="leftmost" titleKey="probe.jsp.dataSourceTest.dbMetaData.col.propName" property="propertyName"/>
                <display:column titleKey="probe.jsp.dataSourceTest.dbMetaData.col.propValue" property="propertyValue"/>
            </display:table>
        </div>
    </c:if>

</div>
