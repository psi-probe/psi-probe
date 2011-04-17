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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%--
	Displays results of "quick check" controller.

	Author: Vlad Ilyushchenko
--%>

<html>
	<head><title><spring:message code="probe.jsp.title.quickcheck"/></title></head>

	<body>

		<c:set var="navTabQuickCheck" value="active" scope="request"/>

		<table>
			<tr>
				<td>Datasource test</td>
				<td>
					<c:choose>
						<c:when test="${testReport.datasourceTest == 0}">
							<span class="ck_unk">
								UNKNOWN
							</span>
						</c:when>
						<c:when test="${testReport.datasourceTest == 1 && testReport.datasourceUsageScore < 100 }">
							<span class="ck_pass">
								PASSED
							</span>
						</c:when>
						<c:otherwise>
							<span class="ck_fail">
								FAILED
							</span>
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<span class="checkdetails">
						(Max usage ${testReport.datasourceUsageScore}%)
					</span>
				</td>
			</tr>

			<tr>
				<td>Memory test</td>
				<td>
					<span class="checkres">
						<c:choose>
							<c:when test="${testReport.memoryTest == 0}">
								<span class="ck_unk">
									UNKNOWN
								</span>
							</c:when>
							<c:when test="${testReport.memoryTest == 1}">
								<span class="ck_pass">
									PASSED
								</span>
							</c:when>
							<c:when test="${testReport.memoryTest == 2}">
								<span class="ck_fail">
									FAILED
								</span>
							</c:when>
						</c:choose>
					</span>
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					File creation test:
				</td>
				<td>
					<span class="checkres">
						<c:choose>
							<c:when test="${testReport.fileTest == 0}">
								<span class="ck_unk">
									UNKNOWN
								</span>
							</c:when>
							<c:when test="${testReport.fileTest == 1}">
								<span class="ck_pass">
									PASSED
								</span>
							</c:when>
							<c:when test="${testReport.fileTest == 2}">
								<span class="ck_fail">
									FAILED
								</span>
							</c:when>
						</c:choose>
					</span>
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					Application test:
				</td>
				<td>
					<span class="checkres">
						<c:choose>
							<c:when test="${testReport.webappAvailabilityTest == 0}">
								<span class="ck_unk">
									UNKNOWN
								</span>
							</c:when>
							<c:when test="${testReport.webappAvailabilityTest == 1}">
								<span class="ck_pass">
									PASSED
								</span>
							</c:when>
							<c:when test="${testReport.webappAvailabilityTest == 2}">
								<span class="ck_fail">
									FAILED
								</span>
							</c:when>
						</c:choose>
					</span>
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
		</table>
		<p>
			Test duration: <c:out value="${testReport.testDuration}"/>ms.
			<a href="quickcheck.xml.htm">XML version</a>
		</p>
	</body>
</html>
