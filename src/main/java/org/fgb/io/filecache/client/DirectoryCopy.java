/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fgb.io.filecache.client;

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
import org.apache.commons.io.FileUtils;

/**
 * Does not seem to work for some strange reason.
 * 
 * @author burkley
 */
public class DirectoryCopy {
	/**
	 * The name of this class.
	 */
	private static final String _className = RecursiveCopy.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	/**
	 *
	 */
	public DirectoryCopy() {
	}

	public void copy(final File readPath, final File writePath) throws IOException {
		StringBuilder msg = new StringBuilder();
		msg.append("Copy from ").append(readPath.getAbsolutePath());
		msg.append(" to ").append(writePath);
		System.out.println(msg.toString());
		FileUtils.copyDirectory(writePath, readPath);
	}

	/**
	 * Bootstrap.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Recursively walk a file system.");
		Option readPathOption = new Option("r", "read", true, "The root directory of the file system to walk.  This argument is mandatory.");
		Option writePathOption = new Option("w", "write", true, "The root directory of the file system to write.  This argument is mandatory.");
		String readPathName = null;
		String writePathName = null;
		StringBuilder msg = new StringBuilder();

		Options options = new Options();
		options.addOption(helpOption);
		options.addOption(readPathOption);
		options.addOption(writePathOption);

		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new PosixParser();

		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("h")) {
				formatter.printHelp(_className, options);
				System.exit(0);
			}
			if (commandLine.hasOption("r")) {
				readPathName = commandLine.getOptionValue("r");
			}
			if (commandLine.hasOption("w")) {
				writePathName = commandLine.getOptionValue("w");
			}
		} catch (ParseException pe) {
			_logger.log(Level.SEVERE, null, pe);
			System.exit(1);
		}

		// Check for necessary command line args
		if (readPathName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}
		if (writePathName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}

		DirectoryCopy dc = new DirectoryCopy();
		File readPathFile = new File(readPathName);
		File writePathFile = new File(writePathName);
		try {
			dc.copy(readPathFile, writePathFile);
		} catch (IOException ex) {
			_logger.log(Level.SEVERE, null, ex);
		}
	}

}
