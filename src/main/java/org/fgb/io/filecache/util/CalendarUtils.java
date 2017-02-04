/*
 * Class:   DateTesting
 * Born On: Oct 21, 2012
 * Purpose: Test manipulating the date / time.
 */
package org.fgb.io.filecache.util;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Test manipulating the date / time.
 * @author Frederick Burkley
 */
public class CalendarUtils {

	/**
	 * The name of this class.
	 */
	private static final String _className = CalendarUtils.class.getName();

	/**
	 * JDK Logger
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 * Prevent instances.
	 */
	private CalendarUtils() {
	}

	/**
	 * Add some number of days to the date.
	 *
	 * @param initialDate The initial date.
	 * @param days The number of days to add to the initial date.
	 *   If the number of days <code>days</code> is less than 0, then that number of days will be subtracted from the initial date.
	 * @return The new date.  The new date is the initial date <code>initialDate</code> modified by the number of days <code>days</code>.
	 */
	public static Date addDays(Date initialDate, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(initialDate);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
}
