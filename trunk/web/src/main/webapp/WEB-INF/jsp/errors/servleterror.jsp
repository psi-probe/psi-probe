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

<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.PrintStream" %>
<%@ page import="java.io.File" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/fmt' prefix='fmt' %>

<%--
    Servlet exception handler page. Unfortunately we cannot use here any fancy libraries if we want
    any kind of reliability. Spring or sitemesh could have caused the exception we are handling here.

    Author: Vlad Ilyushchenko
--%>

<html>

<head><title>Lambda Probe - Error</title></head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/classic/main.css" type="text/css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/classic/messages.css" type="text/css"/>

<body>
<div id="errors">
    <h2>There was an error</h2>
</div>

<c:choose>
    <c:when test="${requestScope['javax.servlet.error.exception'] != null}">
        <%
            Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
            if (error instanceof NoClassDefFoundError && error.getMessage().startsWith("org/apache/catalina")) {

                File conf = new File(System.getProperty("catalina.base"), "conf/Catalina/localhost");

        %>
        <div id="errorMessage">
            It seems that Probe application does not have sufficient access to Tomcat core libraries.
            Please make sure the context (<%=request.getContextPath()%>) has flag "privileged" set to "true".
            You can do so by creating file <b>"probe.xml"</b> in <b><%=conf.getAbsolutePath()%></b> with the following
            contents:<br/><br/>

            <div class="codesample">
                &lt;?xml version="1.0" encoding="UTF-8"?&gt;<br/>
                &lt;Context path="/probe" privileged="true"/&gt;
            </div>
            <br/>
            and restarting Tomcat.
        </div>
        <%
        } else {
        %>
        <div class="errorMessage">
            <p>
                Application encountered an unexpected error.
                We would greatly appreciate if you send the information below back to us so we can correct the problem
                as soon as possible!
            </p>
        </div>

        <div class="errorMessageDetails">
            <%
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(bos);
                error.printStackTrace(ps);

                Throwable e = error;

                int maxCauses = 10;

                while (maxCauses-- > 0) {
                    if (e instanceof ServletException) {
                        e = ((ServletException) e).getRootCause();
                    } else {
                        e = e.getCause();
                    }

                    if (e != null) {
                        ps.print("<br/><br/>Caused by:</br></br>");
                        e.printStackTrace(ps);
                    } else {
                        break;
                    }
                }

                out.print(error.getMessage());
                out.print(bos.toString());
            %>
        </div>
        <%
            }
        %>
    </c:when>
    <c:otherwise>
        <div class="errorMessage">
            <p>
                No additional information available
            </p>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>