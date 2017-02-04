/*
 * Class:   SampleFileGeneratorDriver
 * Born On: Mar 25, 2012
 * Purpose: Generate a file with sample text.
 */
package org.fgb.io.filecache.driver;

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

import org.fgb.io.filecache.FileGenerator;
import org.fgb.io.filecache.impl.LineCountFileGenerator;


/**
 * Generate a file with sample text.
 *
 * @author Frederick Burkley
 */
public class SampleFileGeneratorDriver {

	/**
	 * The name of this class.
	 */
	private static final String _className = SampleFileGeneratorDriver.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);
	/**
	 * The default number of lines to write in the output file.  The user can override this on the command line.
	 */
	private static final int _DEFAULT_NUMBER_OF_LINES = 100000;
	/**
	 * The default number of times to generate the output file.  The user can override this on the command line.
	 */
	private static final int _DEFAULT_LOOP_COUNT = 1;

	/**
	 * Create a <code>SampleFileGeneratorDriver</code>.
	 */
	public SampleFileGeneratorDriver() {
	}


	/**
	 * Bootstrap.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Generate a file with sample text.");
		Option fileOption = new Option("f", "file", true, "The name of the file to generate.  This argument is mandatory.");
		Option benchmarkOption = new Option("b", "benchmark", false, "Enable benchmarking.  This argument is optional.");
		Option loopOption = new Option("l", "numLoops", true, "Enable looping (run the application multiple times).  This is useful when benchmarking.  This argument is optional.");
		Option lineCountOption = new Option("n", "numlines", true, "The number of lines in the output file.  This argument is optional.");
		Option zipOption = new Option("z", "zip", false, "Zip compress the output file.  This argument is optional.");
		Option gZipOption = new Option("g", "gzip", false, "GZip compress the output file.  This argument is optional.");
//		Option gZipOption = new Option("g", "gzip", false, "Uncompressed output followed by explicit copy with GZip compression.  This argument is optional.");
		String fileName = null;
		int numberOfLines = _DEFAULT_NUMBER_OF_LINES;
		short numberOfFieldsPerLine;
		boolean benchmark = false;
		boolean zipCompress = false;
		boolean gzipCompress = false;
		int loopCount = _DEFAULT_LOOP_COUNT;
		StringBuilder msg = new StringBuilder();

		Options options = new Options();
		options.addOption(helpOption);
		options.addOption(fileOption);
		options.addOption(benchmarkOption);
		options.addOption(loopOption);
		options.addOption(lineCountOption);
		options.addOption(zipOption);
		options.addOption(gZipOption);

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
			if (commandLine.hasOption("n")) {
				try {
					numberOfLines = Integer.parseInt(commandLine.getOptionValue("n"));
					if (numberOfLines < 1) {
						msg.append("The number of lines for the output file can not be less than 1.  Defaulting the number of lines for the output file to ").append(_DEFAULT_NUMBER_OF_LINES).append(".\n");
						_logger.log(Level.WARNING, msg.toString());
						msg.delete(0, msg.length());
						numberOfLines = _DEFAULT_NUMBER_OF_LINES;
					}
				} catch (NumberFormatException ex) {
//					_logger.log(Level.WARNING, ex.toString());
					msg.append(ex.toString()).append("  Defaulting the number of lines for the output file to ").append(_DEFAULT_NUMBER_OF_LINES).append(".\n");
					_logger.log(Level.WARNING, msg.toString());
					msg.delete(0, msg.length());
				}
			}
			if (commandLine.hasOption("b")) {
				benchmark = true;
			}
			if (commandLine.hasOption("l")) {
				try {
					loopCount = Integer.parseInt(commandLine.getOptionValue("l"));
//					System.out.println(_className + ".main(): loopCount = " + loopCount);
					if (numberOfLines < 1) {
						msg.append("The number of loops can not be less than 1.  Defaulting the number of loops to ").append(_DEFAULT_LOOP_COUNT).append(".\n");
						_logger.log(Level.WARNING, msg.toString());
						msg.delete(0, msg.length());
						loopCount = _DEFAULT_LOOP_COUNT;
					}
				} catch (NumberFormatException ex) {
					msg.append(ex.toString()).append("  Defaulting the number of lines for the output file to ").append(_DEFAULT_NUMBER_OF_LINES).append(".\n");
					_logger.log(Level.WARNING, msg.toString());
					msg.delete(0, msg.length());
				}

			}
			if (commandLine.hasOption("z")) {
				zipCompress = true;
			}
			if (commandLine.hasOption("g")) {
				gzipCompress = true;
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			System.exit(1);
		}

		// Check for necessary command line args
		if (fileName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}

		numberOfFieldsPerLine = 16;
		FileGenerator generator = new LineCountFileGenerator(fileName, numberOfLines, numberOfFieldsPerLine);
		generator.setBenchmark(benchmark);
		generator.enableZipCompression(zipCompress);
		generator.enableGZipCompression(gzipCompress);
		try {
			for (int i = 0; i < loopCount; i++) {
				generator.generateFile();
			}
		} catch (IOException ex) {
			_logger.log(Level.SEVERE, null, ex);
		}
	}
}
