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

import org.oryxeditor.server.diagram.generic.GenericShape;

/**
 * Simple extension of {@link GenericShape} to allow for easier usage without having to use generics.
 * Does not add or change any functionality
 * 
 * @author Philipp Maschke
 *
 */
public interface BasicShape extends GenericShape<BasicShape, BasicDiagram> {

}
