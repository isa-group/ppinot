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
 * Init namespace
 */
if(!ORYX) {var ORYX = {};}
if(!ORYX.Core) {ORYX.Core = {};}
if(!ORYX.Core.StencilSet) {ORYX.Core.StencilSet = {};}

/**
 * Class Stencil
 * uses Prototpye 1.5.0
 * uses Inheritance
 */
ORYX.Core.StencilSet.PropertyItem = Clazz.extend({

	/**
	 * Constructor
	 */
	construct: function(jsonItem, namespace, property) {
		arguments.callee.$.construct.apply(this, arguments);

		if(!jsonItem) {
			throw "ORYX.Core.StencilSet.PropertyItem(construct): Parameter jsonItem is not defined.";
		}
		if(!namespace) {
			throw "ORYX.Core.StencilSet.PropertyItem(construct): Parameter namespace is not defined.";
		}
		if(!property) {
			throw "ORYX.Core.StencilSet.PropertyItem(construct): Parameter property is not defined.";
		}
		
		this._jsonItem = jsonItem;
		this._namespace = namespace;
		this._property = property;
		
		//init all values
		if(!jsonItem.value) {
			throw "ORYX.Core.StencilSet.PropertyItem(construct): Value is not defined.";
		}
		
		if(this._jsonItem.refToView) {
			if(!(this._jsonItem.refToView instanceof Array)) {
				this._jsonItem.refToView = [this._jsonItem.refToView];
			}
		} else {
			this._jsonItem.refToView = [];
		}
	},

	/**
	 * @param {ORYX.Core.StencilSet.PropertyItem} item
	 * @return {Boolean} True, if item has the same namespace and id.
	 */
	equals: function(item) {
		return (this.property().equals(item.property()) &&
			this.value() === item.value());
	},

	namespace: function() {
		return this._namespace;
	},

	property: function() {
		return this._property;
	},

	value: function() {
		return this._jsonItem.value;
	},
	
	title: function() {
		return ORYX.Core.StencilSet.getTranslation(this._jsonItem, "title");
	},

	refToView: function() {
		return this._jsonItem.refToView;
	},
	
	icon: function() {
		return (this._jsonItem.icon) ? this.property().stencil()._source + "icons/" + this._jsonItem.icon : "";
	},

	toString: function() { return "PropertyItem " + this.property() + " (" + this.value() + ")"; }
});
