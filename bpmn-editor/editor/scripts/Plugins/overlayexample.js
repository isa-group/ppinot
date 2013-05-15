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

if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.OverlayExample = Clazz.extend({

    facade: undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
		this.active = false;
		this.el 	= undefined;
		this.callback = undefined;
		
        this.facade.offer({
            'name': "Overlay Test",
            'functionality': this.testing.bind(this),
            'group': "Overlay",
            'icon': ORYX.PATH + "images/disk.png",
            'description': "Overlay Test",
            'index': 1,
            'minShape': 0,
            'maxShape': 0
        });
		
    },
    
	testing: function(){

		if (this.active) {
			
			this.facade.raiseEvent({
				type: ORYX.CONFIG.EVENT_OVERLAY_HIDE,
				id: "overlaytest.test"
			});
			
		} else {
			
			var els = this.facade.getCanvas().getChildShapes(true);
			
			this.el = els[0]
			this.showOverlay( this.el )
			
		}
		
		this.active = !this.active;	
		
		if( this.active ){
			this.callback = this.doMouseUp.bind(this)
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MOUSEUP, this.callback)
		} else {
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_MOUSEUP, this.callback)
			this.callback = undefined;
		}				
		
	},
	
	doMouseUp: function(event, arg){
		if( arg instanceof ORYX.Core.Shape && arg != this.el){
			this.el = arg;
			this.showOverlay( this.el )
		}
	},
	
	showOverlay: function(shape){

		var cross = ORYX.Editor.graft("http://www.w3.org/2000/svg", null ,
					['path', {
						"stroke-width": 5.0, "stroke":"red", "d":  "M0,0 L-15,-15 M-15,0 L0,-15", "line-captions": "round"
						}]);
							
		this.facade.raiseEvent({
				type: 			ORYX.CONFIG.EVENT_OVERLAY_SHOW,
				id: 			"overlaytest.test",
				shapes: 		[shape],
				attributes: 	{fill: "red", stroke:"green", "stroke-width":4},
				node:			cross,
				nodePosition:	shape instanceof ORYX.Core.Edge ? "START" : "NE"
			});
					
	}
	
    
});
