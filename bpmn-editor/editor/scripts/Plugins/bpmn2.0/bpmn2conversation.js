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


if(!ORYX.Plugins)
	ORYX.Plugins = new Object();

ORYX.Plugins.BPMN2CONVERSATION = {

	/**
	 *	Constructor
	 *	@param {Object} Facade: The Facade of the Editor
	 */
	construct: function(facade) {
		this.facade = facade;

		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_DRAGDOCKER_DOCKED, this.handleDockerDocked.bind(this));		
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_PROPWINDOW_PROP_CHANGED, this.handlePropertyChanged.bind(this));
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_SHAPE_MENU_CLOSE, this.handleDragDrop.bind(this));
	},
	
	checkForMultiInstance: function(shape) {
		
		var incomingShapes = shape.getIncomingShapes();
		var outgoingShapes = shape.getOutgoingShapes();
		var miProp = shape.properties["oryx-multiinstance"];
		
		if (incomingShapes) {
			incomingShapes.find(function(aShape){
				if (aShape.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#ConversationLink") {
					if (miProp) 
						aShape.setProperty("oryx-showforkend", true);
					else 
						aShape.setProperty("oryx-showforkend", false);
				}
			});
		}
		
		if (outgoingShapes) {
			outgoingShapes.find(function(aShape){
				if (aShape.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#ConversationLink") {
					if (miProp) 
						aShape.setProperty("oryx-showforkstart", true);
					else 
						aShape.setProperty("oryx-showforkstart", false);
				}
			});
		}
	},
	
	/**
	 * DragDocker.Docked Handler
	 *
	 */	
	handleDockerDocked: function(options) {

		var edge = options.parent;
		var edgeSource = options.target;
		
		if(edge.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#ConversationLink") {
			if(edgeSource.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#Participant") {
				this.checkForMultiInstance(edgeSource);
				
				this.facade.getCanvas().update();
			}
		}
	},
	
	/**
	 * PropertyWindow.PropertyChanged Handler
	 */
	handlePropertyChanged: function(option) {
		
		var shapes = option.elements;
		var changed = false;

		shapes.each(function(shape) {
			if (shape.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#Participant") {								
				this.checkForMultiInstance(shape);
				changed = true;
			}
		}.bind(this));	
		
		if(changed) this.facade.getCanvas().update();
	},
	
	handleDragDrop: function(option) {
		
		var source = option.source;
		var destination = option.destination;
		var changed = false;
		
		source.each(function(shape) {
			// a Communication node was dragged from a participant node
			if(shape.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#Participant") {
				this.checkForMultiInstance(shape);
				changed = true;
			}
		}.bind(this));
		
		destination.each(function(shape) {
			// a connection is drawn towards a participant node
			if(shape.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#Communication"){
				var outgoingEdges = shape.getOutgoingShapes();
				
				// we have to check the drawn edge if its ending in an Participant Node
				if(outgoingEdges) {
					outgoingEdges.each(function(aShape) {
						var edgeTarget = aShape.getTarget();
						
						if(edgeTarget) {
							if(edgeTarget.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#Participant") {
								this.checkForMultiInstance(edgeTarget);
								changed = true;
							}
						}
					}.bind(this));
				}
			}			
			
			if(shape.getStencil().id() === "http://b3mn.org/stencilset/bpmn2.0conversation#Participant") {
				this.checkForMultiInstance(shape);
				changed = true;
			}
		}.bind(this));
		
		if(changed) this.facade.getCanvas().update();
	}
};

ORYX.Plugins.BPMN2CONVERSATION = ORYX.Plugins.AbstractPlugin.extend(ORYX.Plugins.BPMN2CONVERSATION);
