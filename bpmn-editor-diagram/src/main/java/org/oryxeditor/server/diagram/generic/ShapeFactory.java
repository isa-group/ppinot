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
package org.oryxeditor.server.diagram.generic;

import org.oryxeditor.server.diagram.Point;

import java.util.List;


public interface ShapeFactory<S extends GenericShape<S,D>, D extends GenericDiagram<S,D>, E extends GenericEdge<S,D>, N extends GenericNode<S,D>>{

	/**
	 * Creates a new diagram
	 * @param resourceId
	 * @return
	 */
	public D createNewDiagram(String resourceId);
	
	/**
	 * Creates a new edge
	 * @param resourceId
	 * @return
	 */
	public E createNewEdge(String resourceId);
	
	/**
	 * Creates a new node
	 * @param resourceId
	 * @return
	 */
	public N createNewNode(String resourceId);
	
	/**
	 * Creates either a new edge or a new node, depending on the given list of dockers. Sets the given dockers as the shape's dockers.
	 * <br/>
	 * If {@link org.oryxeditor.server.diagram.generic.GenericShapeImpl#isEdge(java.util.List)} returns true, then a new edge will be created. Otherwise a new node will be returned.
	 * 
	 * @param resourceId
	 * @param dockers the list of dockers for the new shape
	 * @return either a new node or a new edge
	 */
	public S createNewShapeOfCorrectType(String resourceId, List<Point> dockers);
}
