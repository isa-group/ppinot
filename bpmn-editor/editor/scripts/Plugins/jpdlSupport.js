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

ORYX.Plugins.JPDLSupport = ORYX.Plugins.AbstractPlugin.extend({

	facade: undefined,
	
	
	stencilSetExtensionNamespace: 'http://oryx-editor.org/stencilsets/extensions/jbpm#',
	stencilSetExtensionDefinition: 'jbpm/jbpm.json',
	
	stencilSetNamespace: 'http://b3mn.org/stencilset/bpmn1.1#',
	stencilSetUrlSuffix: '/bpmn1.1/bpmn1.1.json',

	/**
	 * constructor method
	 * 
	 */
	construct: function(facade) {
		
		this.facade = facade;
			
		this.facade.offer({
			'name':				ORYX.I18N.jPDLSupport.exp,
			'functionality': 	this.exportJPDL.bind(this),
			'group': 			ORYX.I18N.jPDLSupport.group,
			'icon': 			ORYX.PATH + "images/jpdl_export_icon.png",
			'description': 		ORYX.I18N.jPDLSupport.expDesc,
			'index': 			1,
			'minShape': 		0,
			'maxShape': 		0,
			'maxShape': 		0,
			'isEnabled': 		this._isJpdlStencilSetExtensionLoaded.bind(this)
		});
					
		this.facade.offer({
			'name':				ORYX.I18N.jPDLSupport.imp,
			'functionality': 	this.importJPDL.bind(this),
			'group': 			ORYX.I18N.jPDLSupport.group,
			'icon': 			ORYX.PATH + "images/jpdl_import_icon.png",
			'description': 		ORYX.I18N.jPDLSupport.impDesc,
			'index': 			2,
			'minShape': 		0,
			'maxShape': 		0
		});

	},
	
	/**
	 * Checks if the jPDL stencil set is loaded right now.
	 */
	_isJpdlStencilSetExtensionLoaded: function() {
		return this.isStencilSetExtensionLoaded(this.stencilSetExtensionNamespace);
	},

	/**
	 * Imports jPDL
	 * 
	 */
	importJPDL: function(){
		this._showImportDialog();
	},		

	/**
	 * Exports jPDL
	 * 
	 */
	exportJPDL: function(){
		// raise loading enable event
        this.facade.raiseEvent({
            type: ORYX.CONFIG.EVENT_LOADING_ENABLE
        });
            
		// asynchronously ...
        window.setTimeout((function(){
			
			// ... save synchronously
    		this._doExport();			
			// raise loading disable event.
            this.facade.raiseEvent({
                type: ORYX.CONFIG.EVENT_LOADING_DISABLE
            });
			
        }).bind(this), 10);

		return true;

		
	},
	
	/**
	 * Sends request to a given URL.
	 * 
	 */
	_sendRequest: function( url, method, params, successcallback, failedcallback ){

		var suc = false;

		new Ajax.Request(url, {
           method			: method,
           asynchronous	: false,
           parameters		: params,
		   onSuccess		: function(transport) {
				
				suc = true;
				
				if(successcallback){
					successcallback( transport.responseText )	
				}
				
			}.bind(this),
			
			onFailure		: function(transport) {

				if(failedcallback){
					
					failedcallback();
					
				} else {
					this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.impFailedReq);
					ORYX.log.warn("Import jPDL failed: " + transport.responseText);	
				}
				
			}.bind(this)		
		});
		
		return suc;		
	},
	
	/**
	 * Loads JSON into the editor
	 * 
	 */
	_loadJSON: function( jsonString ){
		
		if (jsonString) {
			var jsonObj = jsonString.evalJSON();
			if( jsonObj && this._hasStencilset(jsonObj) ) {
				if ( this._isJpdlStencilSetExtensionLoaded() ) {
					this.facade.importJSON(jsonString);
				} else {
					Ext.MessageBox.confirm(
						ORYX.I18N.jPDLSupport.loadSseQuestionTitle,
						ORYX.I18N.jPDLSupport.loadSseQuestionBody,
						function(btn){
							if (btn == 'yes') {
								
								if (this.loadStencilSetExtension(this.stencilSetNamespace, this.stencilSetExtensionDefinition)){
									this.facade.importJSON(jsonString);
								} else {
									this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.impFailedJson);
								}
								
							} else {
								this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.impFailedJsonAbort);
							}
						},
						this
					);
				}
				
			} else {
				this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.impFailedJson);
			}
		} else {
			this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.impFailedJson);
		}
	},
	
	loadStencilSetExtension: function(stencilSetNamespace, stencilSetExtensionDefinition) {
		var stencilset = this.facade.getStencilSets()[stencilSetNamespace];
		if (stencilset) {
			stencilset.addExtension(ORYX.CONFIG.SS_EXTENSIONS_FOLDER + stencilSetExtensionDefinition);
			this.facade.getRules().initializeRules(stencilset);
			this.facade.raiseEvent({type: ORYX.CONFIG.EVENT_STENCIL_SET_LOADED});
			return true;
		} 
		return false;
	},
	
	/**
	 * Checks if a json object references the jPDL stencil set extension.
	 * 
	 */
	_hasStencilset: function( jsonObj ){
		return jsonObj.properties.ssextension == this.stencilSetExtensionNamespace && jsonObj.stencilset.url.endsWith(this.stencilSetUrlSuffix);
	},
	

	/**
	 * Opens an export window / tab.
	 * 
	 */
	_doExport: function(){
		var serialized_json = this.facade.getSerializedJSON();

		this._sendRequest(
			ORYX.CONFIG.JPDLEXPORTURL,
			'POST',
			{ data:serialized_json },
			function( result ) { 
				var parser = new DOMParser();
				var parsedResult = parser.parseFromString(result, "text/xml");
				if (parsedResult.firstChild.localName == "error") {
					this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.expFailedXml + parsedResult.firstChild.firstChild.data);
				} else {
					this.openXMLWindow(result);
				}
			}.bind(this),
			function() { 
				this._showErrorMessageBox(ORYX.I18N.Oryx.title, ORYX.I18N.jPDLSupport.expFailedReq);
		 	}.bind(this)
		)
	}, 
	
	/**
	 * Opens a upload dialog.
	 * 
	 */
	_showImportDialog: function( successCallback ){
	
	    var form = new Ext.form.FormPanel({
			baseCls: 		'x-plain',
	        labelWidth: 	50,
	        defaultType: 	'textfield',
	        items: [{
	            text : 		ORYX.I18N.jPDLSupport.selectFile, 
				style : 	'font-size:12px;margin-bottom:10px;display:block;',
	            anchor:		'100%',
				xtype : 	'label' 
	        },{
	            fieldLabel: ORYX.I18N.jPDLSupport.file,
	            name: 		'subject',
				inputType : 'file',
				style : 	'margin-bottom:10px;display:block;',
				itemCls :	'ext_specific_window_overflow'
	        }, {
	            xtype: 'textarea',
	            hideLabel: true,
	            name: 'msg',
	            anchor: '100% -63'  
	        }]
	    });

		// Create the panel
		var dialog = new Ext.Window({ 
			autoCreate: true, 
			layout: 	'fit',
			plain:		true,
			bodyStyle: 	'padding:5px;',
			title: 		ORYX.I18N.jPDLSupport.impJPDL, 
			height: 	350, 
			width:		500,
			modal:		true,
			fixedcenter:true, 
			shadow:		true, 
			proxyDrag: 	true,
			resizable:	true,
			items: 		[form],
			buttons:[
				{
					text:ORYX.I18N.jPDLSupport.impBtn,
					handler:function(){
						
						var loadMask = new Ext.LoadMask(Ext.getBody(), {msg:ORYX.I18N.jPDLSupport.impProgress});
						loadMask.show();
						
						window.setTimeout(function(){
					
							var jpdlString =  form.items.items[2].getValue();
							
							this._sendRequest(
									ORYX.CONFIG.JPDLIMPORTURL,
									'POST',
									{ 'data' : jpdlString },
									function( arg ) { this._loadJSON( arg );  loadMask.hide();  dialog.hide(); }.bind(this),
									function() { loadMask.hide();  dialog.hide(); }.bind(this)
								);

						}.bind(this), 100);
			
					}.bind(this)
				},{
					text:ORYX.I18N.jPDLSupport.close,
					handler:function(){
						
						dialog.hide();
					
					}.bind(this)
				}
			]
		});
		
		// Destroy the panel when hiding
		dialog.on('hide', function(){
			dialog.destroy(true);
			delete dialog;
		});


		// Show the panel
		dialog.show();
		
				
		// Adds the change event handler to 
		form.items.items[1].getEl().dom.addEventListener('change',function(evt){
				var text = evt.target.files[0].getAsText('UTF-8');
				form.items.items[2].setValue( text );
			}, true)

	},
	
	_showErrorMessageBox: function(title, msg){
        Ext.MessageBox.show({
           title: title,
           msg: msg,
           buttons: Ext.MessageBox.OK,
           icon: Ext.MessageBox.ERROR
       });
	}
	
});
