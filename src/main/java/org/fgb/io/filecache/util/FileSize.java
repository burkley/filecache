package org.fgb.io.filecache.util;

import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

/**
 * Class <code>FileSize</code> is a helper class to represent the size of a file.
 * <p>
 * This class receives a <code>String</code> representation of the size of a file,
 * then parses that string to ensure it is compliant with the supported format.
 * The supported format is one or more digit(s) immediately followed by one of the characters: 'k' or 'K' or 'm' or 'M'.
 * <p>
 * For practical purposes, the maximum size of the file is limited to 512 Megabytes.  If a file size exceeds this value, this class will default the value to <i>512M</i>.
 * <p>
 * The following are examples of properly formatted file sizes: <i>1K</i>, <i>1M</i>, <i>24K</i>, <i>512K</i>, <i>16M</i>.
 * <p>
 * The following is an examples of an improperly formatted file size: <i>1 K</i>.
 * 
 * @author burkley
 *
 */
public class FileSize {
	/**
	 * The canonical name of this class.
	 */
	private static final String _className = FileSize.class.getCanonicalName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 * Regex to match the input string. One or more digits followed by one k or
	 * K or m or M.
	 */
	private final static String _regex = "\\d+[kKmM]$";
	/**
	 * Default the size to 1024
	 */
	private final static long _defaultFileSize = FileUtils.ONE_KB;

	/**
	 * Supported file sizes are Kilobyte and Megabyte.
	 */
	private static enum SupportedSizes {
		K, M
	};

	/**
	 * Default the units to Kilobytes
	 */
	private final static SupportedSizes _defaultUnits = SupportedSizes.K;

	/**
	 * The size of the file.
	 */
	private long fileSize = _defaultFileSize;
	/**
	 * The units corresponding to the <code>fileSize</code> field.
	 */
	private SupportedSizes units = _defaultUnits;


	/**
	 * Construct an instance of this class with the specified file size.
	 * 
	 * @param fileSize A <code>String</code> specifying the size of the file.
	 * @throws IllegalArgumentException If the <code>fileSize</code> is not properly formatted.
	 */
	public FileSize(final String fileSize) throws IllegalArgumentException {
		this.setFileSize(fileSize);
	}

	/**
	 * Set method for the size of the file.
	 * 
	 * @param fileSize A <code>String</code> specifying the size of the file.
	 * @throws IllegalArgumentException If the <code>fileSize</code> is not properly formatted.
	 */
	private void setFileSize(final String fileSize) throws IllegalArgumentException {
		StringBuilder msg = new StringBuilder(_className).append(": The file size \"").append(fileSize)
				.append("\" is not properly formatted.").append("  Defaulting file size to ").append(_defaultFileSize)
				.append(_defaultUnits).append(".");

		if (fileSize == null) {
			throw new IllegalArgumentException(msg.toString());
		}
		if (!fileSize.matches(_regex)) {
			throw new IllegalArgumentException(msg.toString());
		}

		int length = fileSize.length();
		String unitPart = fileSize.substring(length - 1, length).toUpperCase();
//		if (!this.containsUnit(unitPart)) {
//			throw new IllegalArgumentException(msg.toString());
//		}

		String intPart = fileSize.substring(0, length - 1);
		this.fileSize = Integer.parseInt(intPart); // At this point we know the
													// intPart is a valid int
													// since we passed the
													// regex.
		this.units = SupportedSizes.valueOf(unitPart);
		// System.out.println(_className + ": intPart = " + intPart);
		// System.out.println(_className + ": unitPart = " + unitPart);
	}


	/**
	 * 
	 * @return
	 */
	public long getFileSize() {
		long size = this.fileSize;
		if (this.units.equals(SupportedSizes.K)) {
			size = size * FileUtils.ONE_KB;
		} else { // Must be 1 Meg if not 1 Kilo
			size = size * FileUtils.ONE_MB;
		}
		return size;
	}


	private boolean containsUnit(final String unit) {
		boolean ret = false;
		for (SupportedSizes size : SupportedSizes.values()) {
			if (unit.equalsIgnoreCase(size.toString())) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
