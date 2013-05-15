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
package org.oryxeditor.server.diagram.label;

/**
 * Enumeration of positioning policies for vertical alignment of labels
 * @author philipp.maschke
 *
 */
public enum VerticalAlign {
	TOP("top", 0), MIDDLE("middle", 1), BOTTOM("bottom", 2);

	/**
	 * Returns the matching object for the given string
	 * @param enumString
	 * @throws IllegalArgumentException if no matching enumeration object was found
	 * @return
	 */
	public static VerticalAlign fromString(String enumString) {
		return fromString(enumString, true);
	}
	
	/**
	 * Returns the matching object for the given string
	 * @param enumString
	 * @param exceptionIfNoMatch whether to throw an exception if there was no match
	 * @throws IllegalArgumentException if no matching enumeration object was found and exceptionIfNoMatch is true
	 * @return
	 */
	public static VerticalAlign fromString(String enumString, boolean exceptionIfNoMatch) {
		for (VerticalAlign attrEnum : values()) {
			if (attrEnum.label.equals(enumString) || attrEnum.name().equals(enumString))
				return attrEnum;
		}

		if (exceptionIfNoMatch){
			throw new IllegalArgumentException("No matching enum constant found in '"
					+ VerticalAlign.class.getSimpleName() + "' for: " + enumString);
		}else{
			return null;
		}
	}

	private String label;
	private int index;


	VerticalAlign(String label, int index) {
		this.label = label;
		this.index = index;
	}


	/**
	 * Returns the alignment label
	 */
	@Override
	public String toString() {
		return label;
	}
	
	
	public int getIndex(){
		return index;
	}
}
