/**
 * Tooltip.js
 *
 * Advanced Tooltip class
 *
 * WARNING: Due to an IE bug, Tooltips will NOT display on top of a <select> element.
 *
 * @copyright Davey Shafik (c) 2005 All Rights Reserved
 * @authors Davey Shafik <davey@php.net>
 * @version 0.6.0
 * @license MIT-style <http://tooltip.crtx.org/LICENSE>
 * @todo Fix accessibility, make it possible to tab in/out of tooltips, and look into CSS Voice stuff
 * @todo Add ability to change the events *per* Tooltip
 */

/**
 * Add an Array.contains() method, mimics PHPs in_array() function
 */

Array.prototype.contains = function (value)
{
	for (var i = 0; i < this.length; i++) {
		if (this[i] == value) {
			return true;
		}
	}
	return false;
}

/**
 * Tooltip Object definition
 */
var Tooltip = {
	/**
	 * @var string|Array An event name or an array of event names on which to trigger showing the Tooltip
	 */
	showEvent: "click",

	/**
	 * @var string|Array An event name or an array of event names on which to trigger hiding the Tooltip
	 */
	hideEvent: "click",

	/**
	 * @var float Duration of the fade events, in seconds
	 * @author Idea contributed by Richard Thomas <cyberlot@cyberlot.net>
	 */
	fade: 0.5,

	/**
	 * @var string Close Link Text
	 */
	closeText: "Close",

	/**
	 * @var function Set the method which will be called for showing the tooltip
	 */
	showMethod: Effect.Appear,

	/**
	 * @var function Set the method which will be called for hiding the tooltip
	 */
	hideMethod: Effect.Fade,

	/**
     * @var boolean Whether the Tooltip should follow the mouse or not. Warning: Cheesy!
     */
    autoFollowMouse: false,

	/**
	 * @var integer If set, the Tooltip will automatically hide after X seconds
     *
     * When followMouse is true, the mouseout event does not trigger the hide callback
     * till X has passed. This is to allow the user to move a little off the element -
     * which is especially useful when it's an inline element such as a link.
     */
    autoHideTimeout: 40,

	/**
	 * @var boolean Allow user to click anywhere to hide current tooltip
	 */
	autoHideClick: true,

	/**
	 * @var boolean If set to true, the Tooltip will be displayed (static) at the current Mouse Cursor location.
	 */
	autoMoveToCursor: true,

	/**
	 * @const int Indicate that the current tooltip should be used
	 */
	CURRENT_TOOLTIP: 1,

	/**
	 * @var object Currently shown Tooltip
	 */
	_current: false,

	/**
	 * Initial Setup
	 *
	 * Find all standard tooltips and auto-initialize them
	 *
	 * @return void
	 */
	setup: function ()
	{
		match_class = new RegExp("^(.*)\s?tooltip\s?(.*)$", 'i');
		match_for = new RegExp("^.*\s?for_(.*)\s?.*$", 'i');
		var divs = document.getElementsByTagName('div');
		var for_result;
		if (divs.length > 0) {
            // Automatically register the mouseout event if followMouse = true and autoHideTimeout is being used
            if (Tooltip.autoFollowMouse && Tooltip.autoHideTimeout) {
                Tooltip.hideEvent[Tooltip.hideEvent.length] = "mouseout";
            }

			if (Tooltip.autoHideClick) {
				Tooltip._attachEvent(document.getElementsByTagName("body").item(0), "clickanywhere");
			}

			for (var i = 0; i < divs.length; i++) {
				if (divs.item(i).className.match(match_class)) {
					for_result = divs.item(i).className.match(match_for);
					if (for_result && for_result.length > 0) {
						if (document.getElementById(for_result[1])) {
							var activator = document.getElementById(for_result[1]);
						}
					} else {
						var foundPrevious = false;
						var activator = divs.item(i);
						while (foundPrevious == false && activator) {
							activator = activator.previousSibling;
                            if (activator && activator.tagName) {
								foundPrevious = true;
								break;
							}
						}
					}
                    if (activator) {
                        activator.Tooltip = divs.item(i);
                        if (!activator.id) {
                            activator.id = "tt" + i;
                        }
                        activator.Tooltip.activator = activator.id;
                        Tooltip.init(activator);
                        // Just in case you need to access the activator from the Tooltip (i.e. a "Close" button)
                    }



				}
			}
		}
	},


	/**
	 * Initiate an Activator/Tooltip for events and display
	 *
	 * @param activator DomElement The element to which the Tooltip show/hide events are attached
	 * @return void
	 */
	init: function (activator)
	{
		var tooltip = activator.Tooltip;
		activator.Tooltip.style.visibility = "hidden";

		Tooltip._attachEvent(activator, "toggle");


		// Remove Link Hrefs
		if (activator.tagName.toLowerCase() == "a") {
			try {
				activator.removeAttribute("href");
				activator.style.cursor = (document.links[0].style.cursor.length > 0) ? document.links[0].style.cursor : "pointer";
			}
			catch (e) {
				//DEBUG alert(e.message);
			}
		}

        // Make sure the Tooltip is on top, only works if the element has position: absolute; in the CSS
        tooltip.style.zIndex = "1000";

		if (Tooltip.autoFollowMouse != true && Tooltip.closeText ) {
			// Create the <p><a href="#">Close</a></p> and add it to the Tooltip

			// <p> element
			var p = document.createElement('p');

			// <p> styles
			p.style.textAlign = "right";
			p.style.padding.padding = "0";
			p.style.margin = "0";

			// <p> class name
			p.className = "close";

			// <a> element
			var link = document.createElement('a');

			// Set the Tooltip var to the tooltip element
			link.Tooltip = tooltip;

			link.style.cursor = "pointer";

			// Add the click handler
			Tooltip._attachEvent(link, "click");

			// "Close" text node
			var close = document.createTextNode(Tooltip.closeText);

			// Append the text to the <a> element
			link.appendChild(close);

			// Append the <a> to the <p> element
			p.appendChild(link);

			// Stick the entire thing on the end of the Tooltip
			tooltip.appendChild(p, tooltip.firstChild);
		}
	},

	/**
	 * Manually add a Tooltip
	 *
	 * When passed an Activator and Tooltip element or ID, it is setup as a Tooltip
	 *
	 * @param activator Activator Element or ID, this is the element that activates the Tooltip
	 * @param tooltip Tooltip Element or ID, this is the Tooltip element itself that is shown/hidden
	 */
	add: function (activator, tooltip)
	{
		if (typeof activator == 'string') {
			activator = document.getElementById(activator);
		}
		if (typeof tooltip == 'string') {
			tooltip = document.getElementById(tooltip);
		}

		activator.Tooltip = tooltip;
		Tooltip.init(activator);
	},

	/**
	 * Toggle the Tooltip
         *
         * Shows or Hides the Tooltip
         *
         * @param activator Activator Element
         * @return void
	 */

	toggle: function (activator, event, directHit)
	{
		try {
			if (activator == 1) {
				activator = document.getElementById(window._currentTT);
			}
		}
		catch (e) { }

		if (Tooltip.autoHideClick && event.type == "click") {
			var close_class = new RegExp("^(.*)\s?close\s?(.*)$", 'i');
			var tooltip_class = new RegExp("^(.*)\s?tooltip\s?(.*)$", 'i');
			if (event.srcElement) {
				var node = event.srcElement;
			} else if (event.fromElement) {
				var node = event.fromElement;
			} else if (event.target) {
				var node = event.target;
			}
			if (node.className == null  || !node.className.match(close_class)) {
				var isWithinTooltip = false;
				while (!isWithinTooltip && node.parentNode) {
					// Check if the parent is a close element first, if so, we can break
					// and we still want to close the tooltip
					if (node.className && node.className.match(close_class)) {
						break;
					}
					if (node.className && node.className != null && node.className.match(tooltip_class)) {
						isWithinTooltip = true;
						break;
					}
					node = node.parentNode;
				}
			}

			if (isWithinTooltip) {
				return false;
			}
		}

		try {
			if (directHit && (activator.Tooltip.style.visibility == 'hidden' || activator.Tooltip.style.display == 'none')) {
                Tooltip._show(activator, event);
			} else {
                Tooltip._hide(activator, event);
			}
		}
		catch (e) {
			try {
				Tooltip._hide(activator, event);
			}
			catch (e) { }
		}
		event.cancelBubble = true;
		try {
			event.stopPropagation();
		}
		catch (e) { }

        return false;
    },

	/**
	 * Show the Tooltip
	 *
	 * Displays the Tooltip and sets the hide events up. You should never need to call this manually.
	 *
	 * @param activator Activator Element
    * @private
	 * @return void
	 */
	_show: function (activator, event, ignore_event)
	{
		if (Tooltip.autoHideClick && window._currentTT ) {
			Tooltip._hide(document.getElementById(window._currentTT), event, true);
		}

		window._currentTT = activator.id;

		if (ignore_event == true || typeof Tooltip.showEvent == "string" || Tooltip.showEvent.constructor && Tooltip.showEvent.constructor == Array && Tooltip.showEvent.contains(event.type)) {
			activator.Tooltip.isVisible = true;
         if (Tooltip.autoFollowMouse || Tooltip.autoMoveToCursor) {
            Tooltip._follow(activator, event);
         }
			try {
				if (typeof Effect) {
					Element.setOpacity(activator.Tooltip, 0.1);
					activator.Tooltip.style.visibility = "visible";
					Tooltip.showMethod(activator.Tooltip, {duration:Tooltip.fade});
				} else {
					activator.Tooltip.style.visibility = "visible";
				}
			}
			catch (e) {
				try {
					activator.Tooltip.style.visibility = "visible";
				}
				catch (e) {
					//DEBUG alert(tooltip.id);
				}
			}
            if (Tooltip.autoFollowMouse) {
                Tooltip._attachEvent(activator, "follow");
            }

            if (Tooltip.autoHideTimeout && !Tooltip.autoFollowMouse) {
                activator.timer = setTimeout(function () {
                                            try {
                                                Tooltip.hideMethod(activator.Tooltip, {duration:Tooltip.fade});
                                            }
                                            catch (e) {
                                                activator.Tooltip.style.visibility = "hidden"; }
                                            }, Tooltip.autoHideTimeout * 1000);
            }

			return;
		}
	},

	/**
	 * Hide the Tooltip
	 *
	 * Hides the Tooltip and sets the show events up. You should never need to call this manually.
	 *
	 * @param activator Activator Element
         * @private
	 * @return void
	 */
	_hide: function (activator, event, ignore_event)
	{
		if (!activator) {
			return;
		}

		event = event.type;

		var tooltip = activator.Tooltip;
        // We need to defer this
        if (event == "mouseout" && Tooltip.autoFollowMouse) {
            activator.timer = setTimeout(function () {
                                            try {
                                                Tooltip.hideMethod(tooltip, {duration:Tooltip.fade});
                                            }
                                            catch (e) {
                                                activator.Tooltip.style.visibility = "hidden"; }
                                            }, Tooltip.autoHideTimeout * 1000);
        } else if (ignore_event == true || ((typeof Tooltip.hideEvent == "string" && Tooltip.hideEvent == event) || Tooltip.hideEvent.constructor && Tooltip.hideEvent.constructor == Array && Tooltip.hideEvent.contains(event))) {
			activator.Tooltip.isVisible = false;
            if( tooltip.style.visibility == 'vidible' || activator.Tooltip.style.display != 'none') {
                try {
                    Tooltip.hideMethod(tooltip, {duration:Tooltip.fade});
                }
                catch (e) {
                    activator.Tooltip.style.visibility = "hidden";
                }

                if (Tooltip.autoFollowMouse) {
                    Tooltip._removeEvent(activator, "follow");
                }
            }

            if (activator.timer) {
                clearTimeout(activator.timer);
            }
            window._currentTT = false;

			return;
		}
	},

    _follow: function (activator, event)
    {
      if (activator.timer) {
	      try {
	         clearTimeout(activator.timer);
         }
         catch (e) { }
      }

		var winWidth, winHeight, d=document;
		if (typeof window.innerWidth!='undefined') {
			winWidth = window.innerWidth;
			winHeight = window.innerHeight;
		} else {
			if (d.documentElement && typeof d.documentElement.clientWidth!='undefined' && d.documentElement.clientWidth!=0) {
				winWidth = d.documentElement.clientWidth
				winHeight = d.documentElement.clientHeight
			} else {
				if (d.body && typeof d.body.clientWidth!='undefined') {
					winWidth = d.body.clientWidth
					winHeight = d.body.clientHeight
				}
			}
		}

		var tooltipWidth, tooltipHeight;
		if (activator.Tooltip.currentStyle) {
			tooltipWidth = activator.Tooltip.currentStyle.width;
			tooltipHeight = activator.Tooltip.currentStyle.height;
		} else if (window.getComputedStyle) {
			tooltipWidth = window.getComputedStyle(activator.Tooltip, null).width;
			tooltipHeight = window.getComputedStyle(activator.Tooltip, null).height;
		}

      activator.Tooltip.style.position = "absolute";

		if (event.pageY) {
			var top = event.pageY;
			var left = event.pageX;
		} else if (event.clientY) {
			// put an If here instead, ?: doesn't seem to work
			if (document.body.scrollTop > document.documentElement.scrollTop) {
				var top = event.clientY + document.body.scrollTop;
			} else {
				var top = event.clientY + document.documentElement.scrollTop;
			}

			if (document.body.scrollLeft > document.documentElement.scrollLeft) {
				var left = event.clientX + document.body.scrollLeft;
			} else {
				var left = event.clientX + document.documentElement.scrollLeft;
			}
		}

		// Make sure the Tooltip doesn't go off the page. The 1.2 comes from Trial and error.
		// We don't track the height, its possible (and much more common) that the height of an item will be more than the browser pane
		if ((left + parseInt(tooltipWidth)) > winWidth) {
			left = winWidth - parseInt(tooltipWidth) * 1.2;
		}

		activator.Tooltip.style.top = top + "px";
		activator.Tooltip.style.left = left + "px";
    },

	/**
	 * Attach show/hide/load events
	 *
	 * This method removes any existing events first
	 * in case show/hide are the same.
	 *
	 * @param element Element to which events should be attached
	 * @param event Event for which events are being registered. One of show/hide/load/click.
	 */

	_attachEvent: function (element, event)
	{
		var i;
		var events = new Array();
        if (event == "toggle") {
			if (Tooltip.showEvent.constructor && Tooltip.showEvent.constructor == Array) {
                for (i = 0; i < Tooltip.showEvent.length; i++) {
                    events.push(Tooltip.showEvent[i]);
                    if (element.addEventListener) {
                        element.addEventListener(Tooltip.showEvent[i], function (e) { Tooltip.toggle(element, e, true); return false; }, false);
                    } else if (element.attachEvent) {
                        element.attachEvent('on' + Tooltip.showEvent[i], function (e) { Tooltip.toggle(element, e, true); return false; });
                    }
                }
            } else {
                events.push(Tooltip.showEvent);
                if (element.addEventListener) {
                    element.addEventListener(Tooltip.showEvent, function (e) { Tooltip.toggle(element, e, true); return false; }, false);
                } else if (element.attachEvent) {
                    element.attachEvent('on' + Tooltip.showEvent, function (e) { Tooltip.toggle(element, e, true); return false; });
                }
            }

            if (Tooltip.hideEvent.constructor && Tooltip.hideEvent.constructor == Array) {
                for (i = 0; i < Tooltip.hideEvent.length; i++) {
                    if (!events.contains(Tooltip.hideEvent[i])) {
                        events.push(Tooltip.hideEvent[i]);
                        if (element.addEventListener) {
                            element.addEventListener(Tooltip.hideEvent[i], function (e) { Tooltip.toggle(element, e, true); return false; }, false);
                        } else if (element.attachEvent) {
                            element.attachEvent('on' + Tooltip.hideEvent[i], function (e) { Tooltip.toggle(element, e, true); return false; });
                        }
                    }
                }
            } else {
                if (!events.contains(Tooltip.hideEvent)) {
                    events.push(Tooltip.hideEvent);
                    if (element.addEventListener) {
                        element.addEventListener(Tooltip.hideEvent, function (e) { Tooltip.toggle(element, e, false); return false; }, false);
                    } else if (element.attachEvent) {
                        element.attachEvent('on' + Tooltip.hideEvent, function (e) { Tooltip.toggle(element, e, false); return false; });
                    }
                }
            }
        } else if (event == "load") {
            if (element.addEventListener) {
                element.addEventListener("load", function () { Tooltip.setup(); }, false);
            } else if (element.attachEvent) {
                element.attachEvent('on' + "load", function () { Tooltip.setup(); });
            }
        } else if (event == "click") {
            if (element.addEventListener) {
                element.addEventListener("click", function (e) { Tooltip.toggle(element, e, true); }, false);
            } else if (element.attachEvent) {
                element.attachEvent('on' + "click", function (e) { Tooltip.toggle(element, e, true); });
            }
        } else if (event == "follow") {
            if (element.addEventListener) {
                element.addEventListener("mousemove", function (e) { Tooltip._follow(element, e); }, false);
            }  else {
                element.attachEvent('onmousemove', function (e) { Tooltip._follow(element, e);  });
            }
        } else if (event == "clickanywhere") {
			if (element.addEventListener) {
                element.addEventListener("click", function (e) { Tooltip.toggle(Tooltip.CURRENT_TOOLTIP, e, false); }, false);
            }  else {
                element.attachEvent('onclick', function (e) { Tooltip.toggle(Tooltip.CURRENT_TOOLTIP, e, false);  });
            }
		}
	},

    _removeEvent: function (element, event)
    {
        try {
            if (event == "follow") {
                if (element.addEventListener) {
                    element.removeEventListener("mousemove", function (e) { Tooltip._follow(element, e); }, false);
                } else {
                    element.dettachEvent('onmousemove', function (e) { Tooltip._follow(element, e); });
                }
            }
        }
        catch (e) {}
    }
}

// Start the Tooltips in motion
try {
	Tooltip._attachEvent(window, 'load');
}
catch (e) { }
