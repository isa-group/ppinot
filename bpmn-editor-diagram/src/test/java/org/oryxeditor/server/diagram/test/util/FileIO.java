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
package org.oryxeditor.server.diagram.test.util;

import java.io.*;

/**
 * Utility class for reading from and writing to file
 * @author philipp.maschke
 *
 */
public class FileIO {

	/**
	 * Reads the contents of the file and returns it as a string.
	 * @param file
	 * @return file's contents as a string or empty string if file not found/could not be read
	 */
	public static String readWholeFile(File file) {
		return readWholeFile(file.getAbsolutePath());
	}
	/**
	 * Reads the contents of the file and returns it as a string.
	 * @param fileName
	 * @return fileName's contents as a string or empty string if file not found/could not be read
	 */
	public static String readWholeFile(String fileName) {
		StringBuffer contents = new StringBuffer();

		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents.toString();
	}

	
	/**
	 * Writes content into a file.
	 * Will create a new file or overwrite an existing one.
	 * @param file
	 * @param content
	 * @throws java.io.IOException
	 */
	public static void writeToFile(File file, String content) throws IOException {
		writeToFile(file.getAbsolutePath(), content);
	}
	/**
	 * Writes content into a file defined by fileName.
	 * Will create a new file or overwrite an existing one.
	 * @param fileName
	 * @param content
	 * @throws java.io.IOException
	 */
	public static void writeToFile(String fileName, String content) throws IOException {
		BufferedWriter out = null;
		
		try{
			out = new BufferedWriter(new FileWriter(fileName));
			out.write(content);
		} finally{
			if(out != null) {
				out.close();
			}
		}
	}
}
