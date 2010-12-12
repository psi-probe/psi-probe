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
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>

<html>
	<head>
		<title>Login</title>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='main.css'/>"/>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}<spring:theme code='login.css'/>"/>
	</head>

	<body>

		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>

		<div id="container">

			<div id="wrapper">
				<div id="header">
					sldn99880 - login
				</div>

				<img id="key" src="<c:url value='/css/classic/img/password.png'/>" alt="key"/>
				<div id="content">

					<%-- this form-login-page form is also used as the
					form-error-page to ask for a login again.
					--%>
					<c:choose>
						<c:when test="${! empty param.login_error}">
							<div id="error" style="display: none;">
								Invalid username or password
							</div>
						</c:when>
					</c:choose>
					<div id="msg">Restricted access area. Please identify yourself</div>

					<form class="login" action="<c:url value='j_acegi_security_check'/>" method="POST">
						<fieldset>
							<ol>
								<li>
									<label for="username">Username:</label>
									<input id="username" name="j_username"
										<c:if test="${not empty param.login_error}">value="<c:out value='${ACEGI_SECURITY_LAST_USERNAME}'/>"
										</c:if>/>
								</li>
								<li>
									<label for="password">Password:</label>
									<input id="password" type='password' name='j_password'>
								</li>
							</ol>
						</fieldset>
						<div id="buttons">
							<input id="submit" type="submit" value="Login">
						</div>
					</form>


					<script type="text/javascript">
						$('username').focus();
						Effect.fade('error');
					</script>
				</div>

				<script type="text/javascript">
					//Effect.Shake('msg');
					Effect.BlindDown('error');
				</script>
			</div>

		</div>
	</body>
</html>
