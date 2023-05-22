<%--

    Licensed under the GPL License. You may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html

    THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
    WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
    PURPOSE.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.PrintStream" %>
<%@ page import="java.io.File" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Servlet exception handler page. Unfortunately we cannot use here any fancy libraries if we want
 any kind of reliability. Spring or sitemesh could have caused the exception we are handling here. --%>

<!DOCTYPE html>
<html lang="${lang}">

    <head>
        <title>Probe - Error</title>
        <link type="text/css" rel="stylesheet" href="<c:url value='css/classic/main.css'/>"/>
        <link type="text/css" rel="stylesheet" href="<c:url value='css/classic/messages.css'/>"/>
    </head>

    <body>
        <div id="errors">
            <h2>There was an error</h2>
        </div>

        <c:choose>
            <c:when test="${requestScope['javax.servlet.error.exception'] != null}">
                <%
                Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
                if (error instanceof NoClassDefFoundError && error != null && error.getMessage().startsWith("org/apache/catalina")) {

                    File conf = new File(System.getProperty("catalina.base"), "conf/Catalina/localhost");

                    %>
                    <div id="errorMessage">
                        <p>
                            It seems that Probe application does not have sufficient access to Tomcat core libraries.
                            Please make sure the context (<%=request.getContextPath()%>) has flag "privileged" set to "true".
                            You can do so by creating file <b>"probe.xml"</b> in <b><%=conf.getAbsolutePath()%></b> with the following
                            contents:
                        </p>
                        <div class="codesample">
                            &lt;?xml version="1.0" encoding="UTF-8"?&gt;<br/>
                            &lt;Context path="/probe" privileged="true"/&gt;
                        </div>
                        <p>
                            and restarting Tomcat.  If you need help, please visit our
                            <a href="http://groups.google.com/group/psi-probe-discuss">discussion board</a>.
                        </p>
                    </div>
                    <%
                } else {
                    %>
                    <div class="errorMessage">
                        <p>
                            Application encountered an unexpected error.
                            Please check the Tomcat logs.
                            We will greatly appreciate it if you share the information
                            with us.  Sharing this information through our <a href="https://github.com/psi-probe/psi-probe/issues">issue tracker</a>
                            or our <a href="http://groups.google.com/group/psi-probe-discuss">discussion board</a>
                            will help us find and correct this problem as soon as possible!
                        </p>
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
