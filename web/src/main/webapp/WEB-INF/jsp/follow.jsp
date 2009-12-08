<%--
  ~ Licensed under the GPL License. You may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~   http://probe.jstripe.com/d/license.shtml
  ~
  ~  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
  ~  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
  ~  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tags/jstripe.tld" prefix="inf" %>

<%--
    Log file view. The view is a simple markup that gets updated via AJAX calls. Top menu does not go to the server but
    rather does DOM tricks to modify content appearence.

    Author: Vlad Ilyushchenko.
--%>

<html>
<head>
    <title><spring:message code="probe.jsp.title.follow"/></title>
</head>

<c:set var="navTabLogs" value="active" scope="request"/>

<body>

<script type="text/javascript" language="javascript" src="<c:url value="/js/prototype.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/scriptaculous.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/func.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/behaviour.js"/>"></script>

<ul class="options">
    <li id="back"><a href="<c:url value="/logs/index.htm"/>"><spring:message code="probe.jsp.follow.menu.back"/></a></li>
    <li id="pause"><a href=""><spring:message code="probe.jsp.follow.menu.pause"/></a></li>
    <li id="resume" style="display: none;"><a href=""><spring:message code="probe.jsp.follow.menu.resume"/></a></li>
    <li id="zoomin"><a href=""><spring:message code="probe.jsp.follow.menu.zoomin"/></a></li>
    <li id="zoomout"><a href=""><spring:message code="probe.jsp.follow.menu.zoomout"/></a></li>
    <li id="wrap"><a href=""><spring:message code="probe.jsp.follow.menu.wrap"/></a></li>
    <li id="nowrap" style="display: none;"><a href=""><spring:message code="probe.jsp.follow.menu.nowrap"/></a></li>
    <li id="clear"><a href=""><spring:message code="probe.jsp.follow.menu.clear"/></a></li>
    <li id="download"><a
            href="<c:url value="/logs/download"><c:param name="id" value="${param.id}"/></c:url>"><spring:message code="probe.jsp.follow.menu.download"/></a></li>
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
</div>

<script type="text/javascript">

    var file_content_div = "file_content";
    var topPosition = -1;
    var tailingEnabled = true;
    var lastResponseText = '';

    function logChanged(responseText) {
		var changed = (responseText != lastResponseText);
		lastResponseText = responseText;
		return changed;
    }

    var infoUpdater = new Ajax.PeriodicalUpdater('info', '<c:url value="/logs/ff_info.ajax"/>', {
        frequency: 3,
        onSuccess: function(transport) {
            if (tailingEnabled && logChanged(transport.responseText)) {
                new Ajax.Updater(file_content_div, '<c:url value="/logs/follow.ajax"/>', {
                    onComplete: function() {
                        objDiv = document.getElementById(file_content_div);
                        if (topPosition == -1) {
                            objDiv.scrollTop = objDiv.scrollHeight;
                        } else {
                            objDiv.scrollTop = topPosition
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
        }
    });

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
                style = Element.getStyle(file_content_div, "font-size").replace("px", "");
                e = document.getElementById(file_content_div);
                if (e) {
                    new_size = (style - 1 + 3);
                    if (new_size <= 32) {
                        e.style.fontSize = new_size + "px";
                    }
                }
                return false;
            }
        },
        '#zoomout': function(element) {
            element.onclick = function () {
                style = Element.getStyle(file_content_div, "font-size").replace("px", "");
                e = document.getElementById(file_content_div);
                if (e) {
                    new_size = (style - 3 + 1);
                    if (new_size >= 4) {
                        e.style.fontSize = new_size + "px";
                    }
                }
                return false;
            }
        },
        '#wrap': function(element) {
            element.onclick = function () {
                Element.setStyle(file_content_div, {"white-space": "normal"});
                Element.hide('wrap');
                Element.show('nowrap');
                return false;
            }
        },
        '#nowrap': function(element) {
            element.onclick = function () {
                Element.setStyle(file_content_div, {"white-space": "nowrap"});
                Element.hide('nowrap');
                Element.show('wrap');
                return false;
            }
        },
        '#clear': function(element) {
            element.onclick = function() {
                new Ajax.Request('<c:url value="/logs/clear.ajax"/>', {method:'get',asynchronous:true});
                new Ajax.Updater(file_content_div, "<c:url value="/logs/follow.ajax"/>");
                return false;
            }
        }

    }
    Behaviour.register(rules);
</script>

</body>
</html>