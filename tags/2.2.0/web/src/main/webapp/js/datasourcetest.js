/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

/*
	Java Script functions for datasourcetest.jsp

	Author: Andy Shapoval, Vlad Ilyushchenko
*/

var connectUrl = '';
var recordsetUrl = '';
var queryHistoryUrl = '';
var sqlOutputDivId = 'outputHolder';
var formId = 'sqlForm';
var ajaxActivityId = 'ajaxActivity';
var metaDataH3Id = 'metaDataH3';
var resultsH3Id = "resultsH3";
var historyContainerDivId = "queryHistoryContainer";
var historyOutputDivId = 'queryHistoryHolder';
var historyVisible = false;
var historyWrapped = true;
var historyHeight = 150;
var optionsDivId = 'optionsDL';
var optionsVisible = false;
var ajaxActivityTimer;

function setupAjaxActions(aConnectUrl, aRecordsetUrl, aQueryHistoryUrl) {
	connectUrl = aConnectUrl;
	recordsetUrl = aRecordsetUrl;
	queryHistoryUrl = aQueryHistoryUrl;

	var rules = {
		'li#connect': function(element) {
			element.onclick = function() {
				testConnction();
				$('sql').focus();
				return false;
			}
		},
		'li#executeSql': function(element) {
			element.onclick = function() {
				executeSql();
				$('sql').focus();
				return false;
			}
		},
		'li#showHistory': function(element) {
			element.onclick = function() {
				showQueryHistory();
				$('sql').focus();
				return false;
			}
		},
		'li#hideHistory': function(element) {
			element.onclick = function() {
				hideQueryHistory();
				$('sql').focus();
				return false;
			}
		},
		'li#showOptions': function(element) {
			element.onclick = function() {
				showOptions();
				$('sql').focus();
				return false;
			}
		},
		'li#hideOptions': function(element) {
			element.onclick = function() {
				hideOptions();
				$('sql').focus();
				return false;
			}
		},
		'li#wrap': function(element) {
			element.onclick = function() {
				wrapQueryHistory();
				$('sql').focus();
				return false;
			}
		},
		'li#nowrap': function(element) {
			element.onclick = function() {
				nowrapQueryHistory();
				$('sql').focus();
				return false;
			}
		}
	}

	Behaviour.register(rules);
}

function testConnction() {
	hideQueryHistory();
	Element.show(ajaxActivityId);
	Element.hide(resultsH3Id);
	Element.show(metaDataH3Id);
	$('rowsAffected').innerHTML = "";
	$('pagebanner').innerHTML = "";
	$('pagelinks').innerHTML = "";
	var params = Form.serialize(formId);
	new Ajax.Updater(sqlOutputDivId, connectUrl, {
		method: 'post',
		postBody: params,
		onComplete: function(req, obj) {
			if (ajaxActivityTimer) clearTimeout(ajaxActivityTimer);
			ajaxActivityTimer = setTimeout('Element.hide("' + ajaxActivityId + '")', 250);
			$('sqlResultsWrapper').show();
		}
	});
}

function executeSql() {
	hideQueryHistory();
	Element.show(ajaxActivityId);
	Element.hide(metaDataH3Id);
	Element.show(resultsH3Id);
	var params = Form.serialize(formId);
	new Ajax.Updater(sqlOutputDivId, recordsetUrl, {
		method: 'post',
		postBody: params,
		onComplete: function() {
			setupPaginationLinks();
			if ($('rs_empty') || $('rs_error')) {
				$('left_scroller').hide();
				$('right_scroller').hide();
				$('separator').hide();
			} else {
				$('left_scroller').show();
				$('right_scroller').show();
				$('separator').show();
			}
			$('sqlResultsWrapper').show();
		}
	});
}

function setupPaginationLinks(req, obj) {
	if ($('rs_rowsAffected') && $('rs_pagebanner') && $('rs_pagelinks')) {
		$('rowsAffected').innerHTML = $('rs_rowsAffected').innerHTML;
		$('pagebanner').innerHTML = $('rs_pagebanner').innerHTML;
		$('pagelinks').innerHTML = $('rs_pagelinks').innerHTML;
	} else {
		$('rowsAffected').innerHTML = "";
		$('pagebanner').innerHTML = "";
		$('pagelinks').innerHTML = "";
	}

	var links = $$('#pagelinks a');

	links.each(function(lnk) {
		lnk.onclick = function() {
			Element.show(ajaxActivityId);
			Element.show(resultsH3Id);
			var p = Form.serialize(formId);
			new Ajax.Updater(sqlOutputDivId, lnk.href, {
				method: 'post',
				postBody: p,
				onComplete: setupPaginationLinks
			});
			return false;
		}
	});

	if (ajaxActivityTimer) clearTimeout(ajaxActivityTimer);
	ajaxActivityTimer = setTimeout('Element.hide("' + ajaxActivityId + '")', 250);
}

/*
	event handlers to display a query history list
*/

function showQueryHistory() {
	new Ajax.Updater(historyOutputDivId, queryHistoryUrl, {
		method: 'post',
		onComplete: function(req, obj) {
			Element.hide('showHistory');
			Element.show('hideHistory');
			Element.setStyle(historyOutputDivId, {
				height: historyHeight + 'px'
			});
			Effect.Appear(historyContainerDivId, {
				duration:0.20
			});

			if (historyWrapped) {
				wrapQueryHistory();
			} else {
				nowrapQueryHistory();
			}

			historyVisible = true;
		}
	});
}

function getQueryHistoryItem(lnk) {
	new Ajax.Request(lnk.href, {
		method: 'get',
		onComplete: function(lnkReq) {
			$('sql').value = lnkReq.responseText;
		}
	});

hideQueryHistory();
$('sql').focus();
}

function hideQueryHistory() {
	Element.hide('hideHistory');
	Element.show('showHistory');
	Effect.Fade(historyContainerDivId, {
		duration:0.20
	});
	historyVisible = false;
}

function wrapQueryHistory() {
	Element.setStyle(historyOutputDivId, {
		"white-space": "normal"
	});
	Element.hide('wrap');
	Element.show('nowrap');
	historyWrapped = true;
}

function nowrapQueryHistory() {
	Element.setStyle(historyOutputDivId, {
		"white-space": "nowrap"
	});
	Element.hide('nowrap');
	Element.show('wrap');
	historyWrapped = false;
}

/*
	event handlers to display an option entry form
*/

function showOptions() {
	Element.hide('showOptions');
	Element.show('hideOptions');
	Effect.Appear(optionsDivId, {
		duration:0.20
	});
	optionsVisible = true;
}

function hideOptions() {
	Element.hide('hideOptions');
	Element.show('showOptions');
	Effect.Fade(optionsDivId, {
		duration:0.20
	});
	optionsVisible = false;
}

/*
	event handlers to provide keyboard shortcuts for major form actions
*/

function setupShortcuts() {
	var rules = {
		'body': function(element) {
			element.onkeydown = function() {
				var sUserAgent = navigator.userAgent;
				var isIE = sUserAgent.indexOf('compatible') > -1 && sUserAgent.indexOf('MSIE') > -1
				var e;

				if (isIE) {
					e = window.event;
				} else {
					e = arguments[0];
				}

				if (e.keyCode == 13 && e.ctrlKey && ! e.altKey && ! e.shiftKey) {
					executeSql();
					$('sql').focus();
				} else if (e.keyCode == 40 && e.ctrlKey && ! e.altKey && ! e.shiftKey) {
					if (historyVisible) {
						hideQueryHistory();
					} else {
						showQueryHistory();
					}
					$('sql').focus();
				} else if (e.keyCode == 38 && e.ctrlKey && ! e.altKey && ! e.shiftKey) {
					if (optionsVisible) {
						hideOptions();
						$('sql').focus();
					} else {
						showOptions();
						$('sql').focus();
					}
				}
			}
		}
	};

	Behaviour.register(rules);
}

/*
   event handlers for sql textarea and query history div resizing
*/

function resizeTextArea(drag) {
	var deltaY = drag.currentDelta()[1];
	var h = (Element.getDimensions('sql').height + deltaY);
	h = Math.max(h, 100);
	Element.setStyle('sql', {
		height: h + 'px'
	});
}

function resizeQueryHistory(drag) {
	var deltaY = drag.currentDelta()[1];
	var h = (Element.getDimensions(historyOutputDivId).height + deltaY);
	h = Math.max(h, 20);
	historyHeight = h;
	Element.setStyle(historyOutputDivId, {
		height: h + 'px'
	});
}

function revertDragHandle(handle) {
	handle.style.top = 0;
	handle.style.position = 'relative';
	$('sql').focus();
}
