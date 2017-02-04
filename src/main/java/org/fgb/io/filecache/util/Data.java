/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fgb.io.filecache.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * An immutable class to represent data.
 *
 * @author burkley
 */
public class Data {
	/**
	 * The name of this class.
	 */
	private static final String _className = Data.class.getName();
	/**
	 * JDK logger.
	 */
	private static Logger _logger = Logger.getLogger(_className);
	/**
	 * A <code>List</code> to store the data.
	 */
	private final List<String> theData;
	/**
	 * The delimiter that separates the fields in the input.  The input is the parameter that is passed to the constructor of this class.
	 */
	private final String delimiter;

	/**
	 * Constructor.
	 *
	 * @param line The data that is to be stored in this class.
	 */
	public Data(final String line) {
		this.theData = new ArrayList<String>();
		this.delimiter = "\t";
		this.parse(line);
	}

	/**
	 * Parse the data, storing individual data items internally.
	 *
	 * @param line The line that is to be parsed.
	 */
	private void parse(final String line) {
		String tokens[] = line.split(this.delimiter);
		for (int i=0; i<tokens.length; i++) {
			this.theData.add(tokens[i]);
		}
	}

	/**
	 * Get the data that is stored in this class.
	 *
	 * @return The data that is stored in this class.
	 */
	public List<String> getData() {
		List<String> copy = new ArrayList<String>();
		for (int i=0; i<this.theData.size(); i++) {
			copy.add(this.theData.get(i));
		}
		return copy;
	}
}
