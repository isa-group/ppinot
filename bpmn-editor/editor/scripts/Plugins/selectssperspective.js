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



if (!ORYX.Plugins) {
	ORYX.Plugins = new Object();
}

ORYX.Plugins.SelectStencilSetPerspective = {

	facade: undefined,
	
	extensions : undefined,
	
	perspectives: undefined,

	construct: function(facade) {
		this.facade = facade;

		var panel = new Ext.Panel({
			cls:'selectssperspective',
			border: false,
			autoWidth:true,
			autoScroll:true
		});

		var region = this.facade.addToRegion("west", panel);
		
		
		var jsonObject = this.facade.getStencilSetExtensionDefinition();
		
		/* Determine available extensions */
		this.extensions = {};
		jsonObject.extensions.each(function(ext) {
			this.extensions[ext.namespace] = ext;
		}.bind(this));
		
		/* Determine available extensions */
		this.perspectives = {};
		jsonObject.perspectives.each(function(per) {
			this.perspectives[per.namespace] = per;
		}.bind(this));

		this.facade.getStencilSets().values().each((function(sset) {

			var validPerspectives = jsonObject.perspectives.findAll(function(perspective){
				if(perspective.stencilset == sset.namespace()) return true;
				else return false;
			}); 
			
			
			// If one perspective is defined, load this
			if (validPerspectives.size() === 1) {
				this.loadPerspective(validPerspectives.first().namespace);
			
			// If more than one perspective is defined, add a combobox and load the first one
			} else if (validPerspectives.size() > 1) {
				this.createPerspectivesCombobox(panel, sset, validPerspectives);
			}

		}).bind(this));
					 

	},

	createPerspectivesCombobox: function(panel, stencilset, perspectives) {
	
		var lang = ORYX.I18N.Language.split("_").first();
	
		var data = [];
		perspectives.each(function(perspective) {
			data.push([perspective.namespace, (perspective["title_"+lang]||perspective.title).unescapeHTML(), perspective["description_"+lang]||perspective.description]);
		});
		
	
		var store = new Ext.data.SimpleStore({
			fields: ['namespace', 'title', 'tooltip'],
			data: data
		});
	
		var combobox = new Ext.form.ComboBox({
			store			: store,
			displayField	: 'title',
			valueField		: 'namespace',
			forceSelection	: true,
			typeAhead		: true,
			mode			: 'local',
			allowBlank		: false,
			autoWidth		: true,
			triggerAction	: 'all',
			emptyText		: 'Select a perspective...',
			selectOnFocus	: true,
			tpl				: '<tpl for="."><div ext:qtip="{tooltip}" class="x-combo-list-item">{[(values.title||"").escapeHTML()]}</div></tpl>'
		});
		
		//panel.on("resize", function(){combobox.setWidth(panel.body.getWidth())});
		
		panel.add(combobox);
		panel.doLayout();
		
		combobox.on('beforeselect', this.onSelect ,this)
		
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_LOADED, function(){
					this.facade.getStencilSets().values().each(function(stencilset) {
						var ext = stencilset.extensions().values()
						if (ext.length > 0){
							var persp = perspectives.find(function(perspective){  
											return	(perspective.extensions && perspective.extensions.include(ext[0].namespace)) ||  			// Check if there is the extension part of the extension in the perspectives
													(perspective.addExtensions && perspective.addExtensions.any(function(add){ return 		// OR Check if the namespace if part of the addExtension part
														(add.ifIsLoaded === stencilset.namespace() && add.add == ext[0].namespace) || 
														(add.ifIsLoaded !== stencilset.namespace() && add["default"] === ext[0].namespace) 	// OR is some in the default
													})
												)
											})
							
							if (!persp){
								persp = perspectives.find(function(r){ return !(r.extensions instanceof Array) || r.extensions.length <= 0 })
							}
							
							if (persp) {
								combobox.setValue(data[perspectives.indexOf(persp)][1]);
								throw $break;
							}
						}
						// Force to load extension
						combobox.setValue(data[0][1]);
						this.loadPerspective(data[0][0]);
					}.bind(this));
				}.bind(this))
		
	},
	
	onSelect: function(combobox, record) {
		if (combobox.getValue() === record.get("namespace") || combobox.getValue() === record.get("title")){
			return;
		}
		
		this.loadPerspective(record.json[0]);
			
	},
	
	loadPerspective: function(ns){
		// If there is no namespace
		if (!ns){
			// unload all extensions
			this._loadExtensions([], [], true);
			return;
		}
		
		/* Get loaded stencil set extensions */
		var stencilSets = this.facade.getStencilSets();
		var loadedExtensions = new Object();
		var perspective = this.perspectives[ns];
		
		stencilSets.values().each(function(ss) { 
	    	ss.extensions().values().each(function(extension) {
				if(this.extensions[extension.namespace])
					loadedExtensions[extension.namespace] = extension;
			}.bind(this));
		}.bind(this));
		
		
		/* Determine extensions that are required for this perspective */
		var addExtensions = new Array();
		if(perspective.addExtensions||perspective.extensions) {
			[]
			 .concat(this.perspectives[ns].addExtensions||[])
			 .concat(this.perspectives[ns].extensions||[])
			 .compact()
			 .each(function(ext){
				if(!ext.ifIsLoaded) {
					addExtensions.push(this.extensions[ext]);
					return;
				}
				
				if(loadedExtensions[ext.ifIsLoaded] && this.extensions[ext.add]) {
					addExtensions.push(this.extensions[ext.add]);
				} else {
					if(ext["default"] && this.extensions[ext["default"]]) {
						addExtensions.push(this.extensions[ext["default"]]);
					}
				}
			}.bind(this));
		}
		
		/* Determine extension that are not allowed in this perspective */
		
		/* Check if flag to remove all other extension is set */
		if(this.perspectives[ns].removeAllExtensions) {	
			window.setTimeout(function(){
				this._loadExtensions(addExtensions, undefined, true);
			}.bind(this), 10);
			return;		
		}
		
		/* Check on specific extensions */
		var removeExtensions = new Array();
		if(perspective.removeExtensions) {
			perspective.removeExtensions.each(function(ns){
				if (loadedExtensions[ns])
					removeExtensions.push(this.extensions[ns]);
			}.bind(this));
		}
		
		if (perspective.extensions && !perspective.addExtensions && !perspective.removeExtensions) {
			var combined = [].concat(addExtensions).concat(removeExtensions).compact();
			$H(loadedExtensions).each(function(extension){
				var key = extension.key;
				if (!extension.value.includeAlways&&!combined.any(function(r){ return r.namespace == key })) {
					removeExtensions.push(this.extensions[key]);
				}
			}.bind(this))
		}
		
		window.setTimeout(function(){
			this._loadExtensions(addExtensions, removeExtensions, false);
		}.bind(this), 10);
	},
	
	/*
	 * Load all stencil set extensions specified in param extensions (key map: String -> Object)
	 * Unload all other extensions (method copied from addssextension plugin)
	 */
	_loadExtensions: function(addExtensions, removeExtensions, removeAll) {
		var stencilsets = this.facade.getStencilSets();
		
		var atLeastOne = false;
		
		// unload unselected extensions
		stencilsets.values().each(function(stencilset) {
			var unselected = stencilset.extensions().values().select(function(ext) { return addExtensions[ext.namespace] == undefined }); 
			if(removeAll) {
				unselected.each(function(ext) {
					stencilset.removeExtension(ext.namespace);
					atLeastOne = true;
				});
			} else {
				unselected.each(function(ext) {
					var remove = removeExtensions.find(function(remExt) {
						return ext.namespace === remExt.namespace;
					});
					
					if(remove) {
						stencilset.removeExtension(ext.namespace);
						atLeastOne = true;
					}
				});
			}
		});
		
		// load selected extensions
		addExtensions.each(function(extension) {
			
			var stencilset = stencilsets[extension["extends"]];
			
			if(stencilset) {
				// Load absolute
				if ((extension.definition || "").startsWith("/")) {
					stencilset.addExtension(extension.definition);
				// Load relative
				} else {
					stencilset.addExtension(ORYX.CONFIG.SS_EXTENSIONS_FOLDER + extension.definition);
				}
				atLeastOne = true;
			}
		}.bind(this));
		
		if (atLeastOne) {
			stencilsets.values().each(function(stencilset) {
				this.facade.getRules().initializeRules(stencilset);
			}.bind(this));
			this.facade.raiseEvent({
				type: ORYX.CONFIG.EVENT_STENCIL_SET_LOADED
			});
			var selection = this.facade.getSelection();
			this.facade.setSelection();
			this.facade.setSelection(selection);
		}
	}

}



ORYX.Plugins.SelectStencilSetPerspective = Clazz.extend(ORYX.Plugins.SelectStencilSetPerspective);

