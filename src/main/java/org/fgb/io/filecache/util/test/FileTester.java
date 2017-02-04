/*
 * Class:   FileTester.java
 * Born On: Jan, 2012
 * Purpose: Test various file system operations.
 */
package org.fgb.io.filecache.util.test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;

/**
 * Test various file system operations.
 *
 * @author Frederick Burkley
 */
public class FileTester {

	/**
	 * The name of this class.
	 */
	private static final String _className = FileTester.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 * Test various file system operations.
	 * @param fileName The name of the file to test.
	 */
	public void test(String fileName) throws IOException {
		StringBuilder msg = new StringBuilder();
		long freeSpaceKb;
		long sizeOfDirectory;

		msg.append(_className).append(": 1 Gigabyte = ").append(FileUtils.ONE_GB).append(" bytes.");
		System.out.println(msg.toString());
		msg.delete(0, msg.length());

		freeSpaceKb = FileSystemUtils.freeSpaceKb(fileName);
		msg.append(_className).append(": Free space = ").append(freeSpaceKb).append(" KB.");
		System.out.println(msg.toString());
		msg.delete(0, msg.length());

		File file = new File(fileName);

		msg.append(_className).append(": File is ");
		msg.append(file.getCanonicalPath());
		System.out.println(msg.toString());
		msg.delete(0, msg.length());

		if (file.getParentFile() != null) {
			msg.append(_className).append(": Parent is ");
			msg.append(file.getParentFile().getCanonicalFile());
			System.out.println(msg.toString());
			msg.delete(0, msg.length());
		}

		if (file.isDirectory()) {
			sizeOfDirectory = FileUtils.sizeOf(file);
			sizeOfDirectory = sizeOfDirectory / FileUtils.ONE_KB;
			msg.append(_className).append(": Size of directory = ").append(sizeOfDirectory).append(" KB.");
			System.out.println(msg.toString());
			msg.delete(0, msg.length());
		}
	}

	/**
	 * Bootstrap.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Read a file with sample text.");
		Option fileOption = new Option("f", "file", true, "The name of the file to read.  This argument is mandatory.");
		String fileName = null;
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
				fileName = commandLine.getOptionValue("f");
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			System.exit(1);
		}

		if (fileName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}

		FileTester fileTester = new FileTester();
		try {
			fileTester.test(fileName);
		} catch (IOException ex) {
			_logger.log(Level.SEVERE, null, ex);
		}
	}
}
