/*
 * Class:   DirectoryMonitorDriver.java
 * Born On: Jan, 2012
 * Purpose: Class DirectoryMonitorDriver is a bootstrap class that will invoke a fgb.io.DirectoryMonitor.
 */
package org.fgb.io.filecache.driver;

import org.fgb.io.filecache.DirectoryMonitor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Class <code>DirectoryMonitorDriver</code> is a bootstrap class that will invoke a {@linkplain fgb.io.DirectoryMonitor}.
 * <p>
 * @author Frederick Burkley
 */
public class DirectoryMonitorDriver {
	/**
	 * The name of this class.
	 */
	private static final String _className = DirectoryMonitorDriver.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 * Bootstrap.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Monitor a directory for events of interest.");
		Option fileOption = new Option("f", "file", true, "The name of the directory to monitor.  This argument is mandatory.");
		String directoryName = null;
		StringBuilder msg = new StringBuilder();

		Options options = new Options();
		options.addOption(helpOption);
		options.addOption(fileOption);

		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new PosixParser();

		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("h")) {
				formatter.printHelp(_className, options);
				System.exit(0);
			}
			if (commandLine.hasOption("f")) {
				directoryName = commandLine.getOptionValue("f");
			}
		} catch (ParseException pe) {
			_logger.log(Level.SEVERE, null, pe);
			System.exit(1);
		}

		// Check for necessary command line args
		if (directoryName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}

		DirectoryMonitor monitor = new DirectoryMonitor(directoryName);
		try {
			monitor.start();
		} catch (Exception ex) {
			_logger.log(Level.SEVERE, null, ex);
		} finally {
		}
	}

}
