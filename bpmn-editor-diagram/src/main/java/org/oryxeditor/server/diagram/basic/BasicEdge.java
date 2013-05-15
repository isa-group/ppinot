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
package org.oryxeditor.server.diagram.basic;

import org.oryxeditor.server.diagram.generic.GenericDiagram;
import org.oryxeditor.server.diagram.generic.GenericEdge;

/**
 * Simple extension of {@link GenericDiagram} to allow for easier usage without having to use generics.
 * Does not add or change any functionality
 * 
 * @author Philipp Maschke
 *
 */
public class BasicEdge extends GenericEdge<BasicShape, BasicDiagram> implements BasicShape {

	public BasicEdge(String resourceId) {
		super(resourceId);
	}


	public BasicEdge(String resourceId, String stencilId) {
		super(resourceId, stencilId);
	}

}
