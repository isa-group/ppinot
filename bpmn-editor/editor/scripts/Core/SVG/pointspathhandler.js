/*******************************************************************************
 * Signavio Core Components
 * Copyright (C) 2012  Signavio GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/


/**
 * Init namespaces
 */
if(!ORYX) {var ORYX = {};}
if(!ORYX.Core) {ORYX.Core = {};}
if(!ORYX.Core.SVG) {ORYX.Core.SVG = {};}


/**
 * PathHandler
 * 
 * Determine absolute points of a SVG path. The coordinates are stored 
 * sequentially in the attribute points (x-coordinates at even indices,
 * y-coordinates at odd indices).
 * 
 * @constructor
 */
ORYX.Core.SVG.PointsPathHandler = Clazz.extend({
	
	construct: function() {
		arguments.callee.$.construct.apply(this, arguments);
		
		this.points = [];
		
		this._lastAbsX = undefined;
		this._lastAbsY = undefined;
	},

	/**
	 * addPoints
	 * 
	 * @param {Array} points Array of absolutePoints
	 */
	addPoints: function(points) {
		if(points instanceof Array) {
			var x, y;
			for(var i = 0; i < points.length; i++) {
				x = parseFloat(points[i]);
				i++;
				y = parseFloat(points[i]);
				
				this.points.push(x);
				this.points.push(y);
				//this.points.push({x:x, y:y});
					
				this._lastAbsX = x;
				this._lastAbsY = y;
			}
		} else {
			//TODO error
		}
	},

	/**
	 * arcAbs - A
	 * 
	 * @param {Number} rx
	 * @param {Number} ry
	 * @param {Number} xAxisRotation
	 * @param {Boolean} largeArcFlag
	 * @param {Boolean} sweepFlag
	 * @param {Number} x
	 * @param {Number} y
	 */
	arcAbs: function(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * arcRel - a
	 * 
	 * @param {Number} rx
	 * @param {Number} ry
	 * @param {Number} xAxisRotation
	 * @param {Boolean} largeArcFlag
	 * @param {Boolean} sweepFlag
	 * @param {Number} x
	 * @param {Number} y
	 */
	arcRel: function(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
	},

	/**
	 * curvetoCubicAbs - C
	 * 
	 * @param {Number} x1
	 * @param {Number} y1
	 * @param {Number} x2
	 * @param {Number} y2
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoCubicAbs: function(x1, y1, x2, y2, x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * curvetoCubicRel - c
	 * 
	 * @param {Number} x1
	 * @param {Number} y1
	 * @param {Number} x2
	 * @param {Number} y2
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoCubicRel: function(x1, y1, x2, y2, x, y) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
	},

	/**
	 * linetoHorizontalAbs - H
	 * 
	 * @param {Number} x
	 */
	linetoHorizontalAbs: function(x) {
	    this.addPoints([x, this._lastAbsY]);
	},

	/**
	 * linetoHorizontalRel - h
	 * 
	 * @param {Number} x
	 */
	linetoHorizontalRel: function(x) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY]);
	},

	/**
	 * linetoAbs - L
	 * 
	 * @param {Number} x
	 * @param {Number} y
	 */
	linetoAbs: function(x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * linetoRel - l
	 * 
	 * @param {Number} x
	 * @param {Number} y
	 */
	linetoRel: function(x, y) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
	},

	/**
	 * movetoAbs - M
	 * 
	 * @param {Number} x
	 * @param {Number} y
	 */
	movetoAbs: function(x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * movetoRel - m
	 * 
	 * @param {Number} x
	 * @param {Number} y
	 */
	movetoRel: function(x, y) {
	    if(this._lastAbsX && this._lastAbsY) {
			this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
		} else {
			this.addPoints([x, y]);
		}
	},

	/**
	 * curvetoQuadraticAbs - Q
	 * 
	 * @param {Number} x1
	 * @param {Number} y1
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoQuadraticAbs: function(x1, y1, x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * curvetoQuadraticRel - q
	 * 
	 * @param {Number} x1
	 * @param {Number} y1
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoQuadraticRel: function(x1, y1, x, y) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
	},

	/**
	 * curvetoCubicSmoothAbs - S
	 * 
	 * @param {Number} x2
	 * @param {Number} y2
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoCubicSmoothAbs: function(x2, y2, x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * curvetoCubicSmoothRel - s
	 * 
	 * @param {Number} x2
	 * @param {Number} y2
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoCubicSmoothRel: function(x2, y2, x, y) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
	},

	/**
	 * curvetoQuadraticSmoothAbs - T
	 * 
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoQuadraticSmoothAbs: function(x, y) {
	    this.addPoints([x, y]);
	},

	/**
	 * curvetoQuadraticSmoothRel - t
	 * 
	 * @param {Number} x
	 * @param {Number} y
	 */
	curvetoQuadraticSmoothRel: function(x, y) {
	    this.addPoints([this._lastAbsX + x, this._lastAbsY + y]);
	},

	/**
	 * linetoVerticalAbs - V
	 * 
	 * @param {Number} y
	 */
	linetoVerticalAbs: function(y) {
	    this.addPoints([this._lastAbsX, y]);
	},

	/**
	 * linetoVerticalRel - v
	 * 
	 * @param {Number} y
	 */
	linetoVerticalRel: function(y) {
	    this.addPoints([this._lastAbsX, this._lastAbsY + y]);
	},

	/**
	 * closePath - z or Z
	 */
	closePath: function() {
	    return;// do nothing
	}

});
