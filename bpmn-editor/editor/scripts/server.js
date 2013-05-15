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

if(!ORYX) var ORYX = {};

if(!ORYX.CONFIG) ORYX.CONFIG = {};

/**
 * This file contains URI constants that may be used for XMLHTTPRequests.
 */

ORYX.CONFIG.ROOT_PATH =					"../editor/"; //TODO: Remove last slash!!
ORYX.CONFIG.EXPLORER_PATH =				"../explorer";
ORYX.CONFIG.LIBS_PATH =					"../libs";

/**
 * Regular Config
 */	
ORYX.CONFIG.SERVER_HANDLER_ROOT = 		"../service";
ORYX.CONFIG.SERVER_EDITOR_HANDLER =		ORYX.CONFIG.SERVER_HANDLER_ROOT + "/editor";
ORYX.CONFIG.SERVER_MODEL_HANDLER =		ORYX.CONFIG.SERVER_HANDLER_ROOT + "/model";
ORYX.CONFIG.STENCILSET_HANDLER = 		ORYX.CONFIG.SERVER_HANDLER_ROOT + "/editor_stencilset?embedsvg=true&url=true&namespace=";    
ORYX.CONFIG.STENCIL_SETS_URL = 			ORYX.CONFIG.SERVER_HANDLER_ROOT + "/editor/stencilset";

ORYX.CONFIG.PLUGINS_CONFIG =			ORYX.CONFIG.SERVER_HANDLER_ROOT + "/editor/plugins";
ORYX.CONFIG.SYNTAXCHECKER_URL =			ORYX.CONFIG.SERVER_HANDLER_ROOT + "/syntaxchecker";
ORYX.CONFIG.DEPLOY_URL = 				ORYX.CONFIG.SERVER_HANDLER_ROOT + "/model/deploy";
ORYX.CONFIG.MODEL_LIST_URL = 			ORYX.CONFIG.SERVER_HANDLER_ROOT + "/models";

ORYX.CONFIG.SS_EXTENSIONS_FOLDER =		ORYX.CONFIG.ROOT_PATH + "stencilsets/extensions/";
ORYX.CONFIG.SS_EXTENSIONS_CONFIG =		ORYX.CONFIG.SERVER_HANDLER_ROOT + "/editor/ssextensions";
ORYX.CONFIG.ORYX_NEW_URL =				"/new";	
ORYX.CONFIG.BPMN_LAYOUTER =				ORYX.CONFIG.ROOT_PATH + "bpmnlayouter";
