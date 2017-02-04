/*
 * Class:   CleanDirectory.java
 * Born On: Jan, 2012
 * Purpose: Monitor a directory and delete files in the directory when the directory reaches a certain size.
 */
package org.fgb.io.filecache;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

/**
 * Class <code>CleanDirectory</code> will monitor a directory and delete files in the directory when the directory reaches a certain size.
 * <p>
 * This class will invoke a background thread that will perform the monitoring duties.
 * The background thread will stay alive until the user kills the process.
 *
 * @author Frederick Burkley
 */
public class CleanDirectory {

	/**
	 * The name of this class.
	 */
	private static final String _className = CleanDirectory.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 * Prevent instances.
	 */
	private CleanDirectory() {
	}

	/**
	 *
	 */
	private static void cleanDirectory(final File directory) {
		if (_logger.isLoggable(Level.FINER)) {
			_logger.entering(_className, "cleanDirectory");
		}

		System.out.println(_className + ".cleanDirectory(): directory = " + directory.getAbsolutePath());

		File files[] = sortedFileList(directory);

		System.out.println(_className + ".checkAndCleanDirectory(): --------------------");
		System.out.println(_className + ".checkAndCleanDirectory(): FILE LIST AFTER SORT");
		System.out.println(_className + ".checkAndCleanDirectory(): --------------------");
		CleanDirectory.printFileInfo(files);

		deleteFileList(files);
//		File fileToDelete = files[0];
//		if (fileToDelete.delete()) {
//			System.out.println(_className + "Successfully deleted the file " + fileToDelete.getName());
//		} else {
//			System.out.println(_className + "Unable to delete the file " + fileToDelete.getName());
//		}

		if (_logger.isLoggable(Level.FINER)) {
			_logger.exiting(_className, "cleanDirectory");
		}
	}

	private static void deleteFileList(File[] files) {
		long fileSize;
		long deletedBytes = 0;
		long bytesToDelete = 25 * FileUtils.ONE_MB;

		for (File file : files) {
			fileSize = file.length();
			System.out.println(_className + ".deleteFileList(): Attempting to delete " + file.getAbsolutePath());
			file.delete();
			System.out.println(_className + ".deleteFileList(): Deleted " + file.getAbsolutePath());
			deletedBytes += fileSize;
			fileSize = 0;
			if (deletedBytes > bytesToDelete) {
				break;
			}
		}


	}


	private static void deleteOldestFile(final File directory) {
	}

	private static File[] sortedFileList(File directory) {
		File files[] = directory.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName();
				if (name.startsWith("FGB") && !name.endsWith(".lck")) {
					return true;
				} else {
					return false;
				}
			}
		});
		LastModifiedFileComparator comparator = new LastModifiedFileComparator();
		files = comparator.sort(files);
		return files;
	}

	/**
	 *
	 * @param files
	 */
	private static void printFileInfo(final File[] files) {
		Date date;
		for (File file : files) {
			date = new Date(file.lastModified());
			System.out.println(_className + ".printFileInfo(): " + file.getName() + " " + date.toString());
		}

	}

	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Monitor a directory and delete files in the directory when the directory reaches a certain size.");
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

		File file = new File(directoryName);

		CleanDirectory.cleanDirectory(file);
	}
}