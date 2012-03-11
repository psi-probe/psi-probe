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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/tld/probe.tld" prefix="probe" %>

<%--
	Log file view. The view is a simple markup that gets updated via AJAX calls. Top menu does not go to the server but
	rather does DOM tricks to modify content appearence.

	Author: Vlad Ilyushchenko.
--%>

<html>
	<head>
		<title><spring:message code="probe.jsp.title.follow"/></title>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/prototype.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/scriptaculous.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/func.js'/>"></script>
		<script type="text/javascript" language="javascript" src="<c:url value='/js/behaviour.js'/>"></script>
	</head>

	<c:set var="navTabLogs" value="active" scope="request"/>

	<body>

		<ul class="options">
			<li id="back">
				<a href="<c:url value='/logs/index.htm'/>">
					<spring:message code="probe.jsp.follow.menu.back"/>
				</a>
			</li>
			<li id="pause">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.pause"/>
				</a>
			</li>
			<li id="resume" style="display: none;">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.resume"/>
				</a>
			</li>
			<li id="zoomin">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.zoomin"/>
				</a>
			</li>
			<li id="zoomout">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.zoomout"/>
				</a>
			</li>
			<li id="wrap">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.wrap"/>
				</a>
			</li>
			<li id="nowrap" style="display: none;">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.nowrap"/>
				</a>
			</li>
			<li id="clear">
				<a href="#">
					<spring:message code="probe.jsp.follow.menu.clear"/>
				</a>
			</li>
			<li id="download">
				<c:url value="/logs/download" var="downloadUrl">
					<c:param name="logType" value="${log.logType}"/>
					<c:if test="${log.application != null}">
						<c:param name="webapp" value="${log.application.name}"/>
						<c:if test="${log.context}">
							<c:param name="context" value="${log.context}"/>
						</c:if>
					</c:if>
					<c:if test="${!log.context}">
						<c:choose>
							<c:when test="${log.root}">
								<c:param name="root" value="${log.root}"/>
							</c:when>
							<c:otherwise>
								<c:param name="logName" value="${log.name}"/>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${log.index != null}">
						<c:param name="logIndex" value="${log.index}"/>
					</c:if>
				</c:url>
				<a href="${downloadUrl}">
					<spring:message code="probe.jsp.follow.menu.download"/>
				</a>
			</li>
		</ul>


		<div class="blockContainer">
			<h3><spring:message code="probe.jsp.follow.h3.fileInfo"/></h3>

			<div class="shadow">
				<div id="info" class="info">
					<div class="ajax_activity"></div>
				</div>
			</div>

			<h3><spring:message code="probe.jsp.follow.h3.fileContent"/></h3>

			<div class="shaper">
				<div id="file_content" class="fixed_width">
					<div class="ajax_activity"></div>
				</div>
			</div>

			<h3><spring:message code="probe.jsp.follow.h3.sources"/></h3>

			<display:table name="sources" class="genericTbl" cellspacing="0" uid="logsource" requestURI="">

				<display:column titleKey="probe.jsp.logs.col.app" sortable="true" class="leftmost">
					${logsource.application.name}
				</display:column>

				<display:column titleKey="probe.jsp.logs.col.type" sortable="true" property="logType"/>

				<display:column titleKey="probe.jsp.logs.col.name" sortable="true">
					<c:choose>
						<c:when test="${logsource.context}">
							(CONTEXT)
						</c:when>
						<c:when test="${logsource.root}">
							(ROOT)
						</c:when>
						<c:otherwise>
							${logsource.name}
						</c:otherwise>
					</c:choose>
				</display:column>

				<display:column titleKey="probe.jsp.logs.col.class" sortable="true">
					${logsource.targetClass}
					(${logsource.index})
				</display:column>

				<display:column titleKey="probe.jsp.logs.col.level" sortable="false">
					<c:if test="${not empty logsource.validLevels && logsource.level != null}">
						<select id="log_${logsource_rowNum}">
							<c:forEach items="${logsource.validLevels}" var="validLogLevel">
								<option value="${validLogLevel}" ${validLogLevel == logsource.level ? 'selected="selected"' : ''}>${validLogLevel}</option>
							</c:forEach>
						</select>

						<c:url value="/adm/changeloglevel.ajax" var="changeLogLevelUrl">
							<c:param name="logType" value="${logsource.logType}"/>
							<c:if test="${logsource.application != null}">
								<c:param name="webapp" value="${logsource.application.name}"/>
								<c:if test="${logsource.context}">
									<c:param name="context" value="${logsource.context}"/>
								</c:if>
							</c:if>
							<c:if test="${!logsource.context}">
								<c:choose>
									<c:when test="${logsource.root}">
										<c:param name="root" value="${logsource.root}"/>
									</c:when>
									<c:otherwise>
										<c:param name="logName" value="${logsource.name}"/>
									</c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${logsource.index != null}">
								<c:param name="logIndex" value="${logsource.index}"/>
							</c:if>
						</c:url>

						<script type="text/javascript">
							Event.observe(window, 'load', function() {
								$('log_${logsource_rowNum}').observe('change', function(event) {
									this.disable();
									new Ajax.Request('${changeLogLevelUrl}', {
										method: 'get',
										parameters: {
											level: this.value
										},
										asynchronous: true,
										onSuccess: function(response) {
											event.element().enable();
										}
									});
								});
							});
						</script>
					</c:if>
				</display:column>
			</display:table>
		</div>

		<c:choose>
			<c:when test="${log.application != null}">
				<c:set var="webapp" value="'${probe:escapeJS(log.application.name)}'" />
			</c:when>
			<c:otherwise>
				<c:set var="webapp" value="null" />
			</c:otherwise>
		</c:choose>

		<script type="text/javascript">

			var file_content_div = 'file_content';
			var topPosition = -1;
			var tailingEnabled = true;
			var maxLines = 1000;
			var initialLines = 250;
			var lastLogSize = -1;
			var logSizeRegex = /<span title="(\d*)">/;

			function logSize(responseText) {
				var captures = logSizeRegex.exec(responseText);
				return captures.length > 1 ? captures[1] : lastLogSize;
			}

			var infoUpdater = new Ajax.PeriodicalUpdater('info', '<c:url value="/logs/ff_info.ajax"/>', {
				parameters: {
					logType: '${probe:escapeJS(log.logType)}',
					webapp: ${webapp},
					context: ${log.context},
					root: ${log.root},
					logName: '${probe:escapeJS(log.name)}',
					logIndex: '${probe:escapeJS(log.index)}'
				},
				frequency: 3,
				onSuccess: function(response) {
					if (tailingEnabled) {
						var currentLogSize = logSize(response.responseText);
						if (lastLogSize != currentLogSize) {
							followLog(currentLogSize);
							lastLogSize = currentLogSize;
						}
					}
				}
			});

			function followLog(currentLogSize) {
				new Ajax.Updater(file_content_div, '<c:url value="/logs/follow.ajax"/>', {
					parameters: {
						logType: '${probe:escapeJS(log.logType)}',
						webapp: ${webapp},
						context: ${log.context},
						root: ${log.root},
						logName: '${probe:escapeJS(log.name)}',
						logIndex: '${probe:escapeJS(log.index)}',
						lastKnownLength: (lastLogSize == -1 ? 0 : lastLogSize),
						currentLength: currentLogSize,
						maxReadLines: (lastLogSize == -1 ? initialLines : undefined)
					},
					insertion: (lastLogSize == -1 ? undefined : 'bottom'),
					onComplete: function() {
						objDiv = document.getElementById(file_content_div);
						if (topPosition == -1) {
							objDiv.scrollTop = objDiv.scrollHeight;
						} else {
							objDiv.scrollTop = topPosition
						}
						
						var lines = $(objDiv).childElements();
						var numOfLines = lines.length;
						var toBeRemoved = new Array();
						for (var i = 0; i < numOfLines - maxLines; i++) {
							toBeRemoved.push(lines[i]);
						}
						for (var i = 0; i < toBeRemoved.length; i++) {
							toBeRemoved[i].remove();
						}
					},

					onCreate: function() {
						objDiv = document.getElementById(file_content_div);
						if (objDiv.scrollTop + objDiv.clientHeight == objDiv.scrollHeight) {
							topPosition = -1;
						} else {
							topPosition = objDiv.scrollTop;
						}
					}
				});
			}

			//
			// unfortunately it is not possible to set the size of "file_content" div in percent.
			// i'm not sure why, but most likely it is a browser bug.
			// Therefore we need to adjust the size of the view div when browser window resizes,
			// hence this hook:
			//
			window.onresize = function() {
				var h = (getWindowHeight() - 390) + 'px';
				Element.setStyle(file_content_div, {height: h});
			}

			//
			// now resize the view div on page load
			//
			window.onresize();

			//
			// install "onClick" rules
			//
			var rules = {
				'#pause' : function (element) {
					element.onclick = function () {
						tailingEnabled = false;
						Element.hide('pause');
						Element.show('resume');
						return false;
					}
				},
				'#resume': function (element) {
					element.onclick = function () {
						tailingEnabled = true;
						Element.hide('resume');
						Element.show('pause');
						return false;
					}
				},
				'#zoomin': function(element) {
					element.onclick = function () {
						var e = $(file_content_div);
						if (e) {
							var old_size = e.getStyle('font-size').replace('px', '');
							var new_size = (old_size - 1 + 3);
							if (new_size <= 32) {
								setFontSize(e, new_size, true);
							}
						}
						return false;
					}
				},
				'#zoomout': function(element) {
					element.onclick = function () {
						var e = $(file_content_div);
						if (e) {
							var old_size = e.getStyle('font-size').replace('px', '');
							var new_size = (old_size - 3 + 1);
							if (new_size >= 4) {
								setFontSize(e, new_size, true);
							}
						}
						return false;
					}
				},
				'#wrap': function(element) {
					element.onclick = function () {
						Element.setStyle(file_content_div, {'white-space': 'normal'});
						Element.hide('wrap');
						Element.show('nowrap');
						return false;
					}
				},
				'#nowrap': function(element) {
					element.onclick = function () {
						Element.setStyle(file_content_div, {'white-space': 'nowrap'});
						Element.hide('nowrap');
						Element.show('wrap');
						return false;
					}
				},
				'#clear': function(element) {
					element.onclick = function() {
						$(file_content_div).update();
						followLog(undefined);
						return false;
					}
				}

			}
			Behaviour.register(rules);

			function setFontSize(elm, new_size, save) {
				elm.setStyle({'font-size': new_size + 'px'});
				if (save) {
					new Ajax.Request('<c:url value="/remember.ajax"/>?cn=file_content_font_size&state=' + new_size, {method:'get',asynchronous:true});
				}
			}

		</script>

		<c:if test="${cookie['file_content_font_size'] != null}">
			<script type="text/javascript">
				Event.observe(window, 'load', function() {
					setFontSize($(file_content_div), ${cookie['file_content_font_size'].value}, false);
				});
			</script>
		</c:if>

	</body>
</html>
