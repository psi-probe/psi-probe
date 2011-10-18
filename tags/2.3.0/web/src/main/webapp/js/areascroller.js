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

Effect.Scroll = Class.create();
Object.extend(Object.extend(Effect.Scroll.prototype, Effect.Base.prototype), {
	initialize: function(element, options) {
		this.element = $(element);
		this.direction = options && options.direction ? options.direction : 'right';
		this.pps = options && options.pps ? options.pps : 100;

		var opts = Object.extend({
			duration: (this.element.scrollWidth - this.element.scrollLeft) / this.pps
		}, options || {});


		this.start(opts);
	},
	setup: function() {
		this.scrollStart = this.element.scrollLeft;
		this.delta = this.element.scrollWidth;
	},
	update: function(position) {
		if (this.direction == 'right')
			this.element.scrollLeft = this.scrollStart + (position * this.delta);
		else
			this.element.scrollLeft = this.scrollStart - (position * this.delta);
	}
});


var rightScroller;
var leftScroller;

function setupScrollers(container) {
	var containerID = container;
	var r = {
		'#right_scroller': function(e) {
			e.onmouseover = function(e) {
				if (leftScroller) leftScroller.cancel();
				rightScroller = new Effect.Scroll(containerID, {
					pps: 100, 
					direction: 'right'
				});
			}
			e.onmouseout = function(e) {
				if (rightScroller) {
					rightScroller.cancel();
				}
			}
			e.onmousedown = function(e) {
				if (rightScroller) rightScroller.cancel();
				rightScroller = new Effect.Scroll(containerID, {
					pps: 1000,
					direction: 'right'
				});
			}
			e.onmouseup = function(e) {
				if (rightScroller) rightScroller.cancel();
				rightScroller = new Effect.Scroll(containerID, {
					pps: 100,
					direction: 'right'
				});
			}
		},
		'#left_scroller': function(e) {
			e.onmouseover = function(e) {
				if (rightScroller) rightScroller.cancel();
				leftScroller = new Effect.Scroll(containerID, {
					pps: 100,
					direction: 'left'
				});
			}
			e.onmouseout = function(e) {
				if (leftScroller) {
					leftScroller.cancel();
				}
			}
			e.onmousedown = function(e) {
				if (leftScroller) leftScroller.cancel();
				leftScroller = new Effect.Scroll(containerID, {
					pps: 1000,
					direction: 'left'
				});
			}
			e.onmouseup = function(e) {
				if (leftScroller) leftScroller.cancel();
				leftScroller = new Effect.Scroll(containerID, {
					pps: 100,
					direction: 'left'
				});
			}
		}
	}

	Behaviour.register(r);
}
