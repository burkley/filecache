/*
 * Class:   DateTesting
 * Born On: Oct 21, 2012
 * Purpose: Test manipulating the date / time.
 */
package org.fgb.io.filecache.dateTime;

import org.fgb.io.filecache.util.CalendarUtils;
import org.fgb.io.filecache.util.DirectoryUtils;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test manipulating the date / time.
 * @author Frederick Burkley
 */
public class DateTimeTesting {

	/**
	 * The name of this class.
	 */
	private static final String _className = DateTimeTesting.class.getName();

	/**
	 * Public default constructor.
	 */
	public DateTimeTesting() {
	}

	/**
	 * Do test number 1.
	 */
	private void doTest1(final int numberOfDays) {
		StringBuilder msg = new StringBuilder();
		Date theDate = new Date();
		msg.append(_className).append(": Date->").append(theDate);
		System.out.println(msg.toString());
		msg.delete(0, msg.length());
		for (int i = 0; i < numberOfDays; i++) {
			theDate = CalendarUtils.addDays(theDate, 1);
			msg.append(_className).append(": Date->").append(theDate);
			System.out.println(msg.toString());
			msg.delete(0, msg.length());
		}
	}

	/**
	 * Do test number 2.
	 */
	private void doTest2(final int numberOfDays) throws IOException {
		StringBuilder msg = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		Date theDate = cal.getTime();
//		msg.append(_className).append(": Date->").append(theDate);
//		msg.append("  Year->").append(cal.get(Calendar.YEAR));
//		msg.append("  Month->").append(cal.get(Calendar.MONTH));
//		msg.append("  Week->").append(cal.get(Calendar.WEEK_OF_MONTH));
//		System.out.println(msg.toString());
//		msg.delete(0, msg.length());
		DirectoryUtils.mkdir(new File("."), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.WEEK_OF_MONTH));
		for (int i = 0; i < numberOfDays; i++) {
			cal.add(Calendar.DATE, 1);
			theDate = cal.getTime();
//			msg.append(_className).append(": Date->").append(theDate);
//			msg.append("  Year->").append(cal.get(Calendar.YEAR));
//			msg.append("  Month->").append(cal.get(Calendar.MONTH));
//			msg.append("  Week->").append(cal.get(Calendar.WEEK_OF_MONTH));
//			System.out.println(msg.toString());
//			msg.delete(0, msg.length());
			DirectoryUtils.mkdir(new File("."), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.WEEK_OF_MONTH));
		}
	}

	/**
	 * Bootstrap.
	 * @param args
	 */
	public static void main(String[] args) {
		DateTimeTesting dtt = new DateTimeTesting();
//		dtt.doTest1(4000);
		try {
			dtt.doTest2(16);
		} catch (IOException ex) {
			Logger.getLogger(DateTimeTesting.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
