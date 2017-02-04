/*
 * Class:   DateTesting
 * Born On: Oct 21, 2012
 * Purpose: Test manipulating the date / time.
 */
package org.fgb.io.filecache.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Test manipulating the date / time.
 * @author Frederick Burkley
 */
public class DirectoryUtils {

	/**
	 * The name of this class.
	 */
	private static final String _className = DirectoryUtils.class.getName();

	/**
	 * JDK Logger
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 * Prevent instances.
	 */
	private DirectoryUtils() {
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param week
	 */
	public static boolean mkdir(final File baseDir, int year, int month, int week) throws IOException {
		boolean ret;
		StringBuilder buf = new StringBuilder();
		System.out.println(_className + ": Path->" + baseDir.getAbsolutePath());
		buf.append(baseDir.getAbsolutePath()).append(File.separator).append(year).append(File.separator).append(month).append(File.separator).append(week);
		System.out.println(_className + ": New Path->" + buf.toString());
		File newDir = new File(buf.toString());
		ret = newDir.mkdirs();
		if (ret) {
			System.out.println(_className + ".mkdir(): Successfully created path->" + newDir.getAbsolutePath());
		} else {
			System.out.println(_className + ".mkdir(): Failed to created path->" + newDir.getAbsolutePath());
		}
		return ret;
	}
}
