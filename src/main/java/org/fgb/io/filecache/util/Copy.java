/*
 * Class:   Compress
 * Born On: Mar 25, 2012
 * Purpose:
 */
package org.fgb.io.filecache.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Frederick Burkley
 */
public class Copy {

	/**
	 * The name of this class.
	 */
	private static final String _className = Copy.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	public Copy() {
	}

	public void copy(File inputFile, File outputFile) throws IOException {
		InputStream inputStream = new FileInputStream(inputFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		OutputStream outputStream = new FileOutputStream(outputFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		byte[] buf = new byte[1024];
		int length;
		while ((length = bufferedInputStream.read(buf)) > 0) {
			bufferedOutputStream.write(buf, 0, length);
		}
		bufferedInputStream.close();
		bufferedOutputStream.close();
	}

	public void copyWithGZipCompression(File inputFile, File outputFile) throws IOException {
		InputStream inputStream = new FileInputStream(inputFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		OutputStream outputStream = new FileOutputStream(outputFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bufferedOutputStream);
		byte[] buf = new byte[8192];
		int length;
		while ((length = bufferedInputStream.read(buf)) > 0) {
			gzipOutputStream.write(buf, 0, length);
		}
		bufferedInputStream.close();
		gzipOutputStream.close();
	}

	public void copyWithZipCompression(File inputFile, File outputFile) throws IOException {
		InputStream inputStream = new FileInputStream(inputFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		OutputStream outputStream = new FileOutputStream(outputFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		ZipEntry zipEntry = new ZipEntry("fgb.txt");
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
		zipOutputStream.putNextEntry(zipEntry);
		byte[] buf = new byte[1024];
		int length;
		while ((length = bufferedInputStream.read(buf)) > 0) {
			zipOutputStream.write(buf, 0, length);
		}
		bufferedInputStream.close();
		zipOutputStream.closeEntry();
		zipOutputStream.close();
	}

	public void copyWithIOUtils(File inputFile, File outputFile) throws IOException {
		InputStream inputStream = new FileInputStream(inputFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		OutputStream outputStream = new FileOutputStream(outputFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bufferedOutputStream);
		IOUtils.copy(bufferedInputStream, gzipOutputStream);
		bufferedInputStream.close();
		gzipOutputStream.close();

	}

	/**
	 * Bootstrap.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Copy a file.");
		Option inputOption = new Option("i", "input", true, "The input file.  This argument is mandatory.");
		Option outputOption = new Option("o", "output", true, "The output file.  This argument is mandatory.");
		Option zipOption = new Option("z", "zip", false, "Zip compress the output file.  This argument is optional.");
		Option gZipOption = new Option("g", "gzip", false, "GZip compress the output file.  This argument is optional.");
		String inputFileName = null;
		String outputFileName = null;
		File inputFile;
		File outputFile;
		boolean zipCompression = false;
		boolean gzipCompression = false;
		StringBuilder msg = new StringBuilder();

		Options options = new Options();
		options.addOption(helpOption);
		options.addOption(inputOption);
		options.addOption(outputOption);
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
			if (commandLine.hasOption("i")) {
				inputFileName = commandLine.getOptionValue("i");
			}
			if (commandLine.hasOption("o")) {
				outputFileName = commandLine.getOptionValue("o");
			}
			if (commandLine.hasOption("z")) {
				zipCompression = true;
			}
			if (commandLine.hasOption("g")) {
				gzipCompression = true;
			}
		} catch (ParseException pe) {
			_logger.log(Level.SEVERE, null, pe);
			System.exit(1);
		}

		// Check for necessary command line args
		if (inputFileName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}
		if (outputFileName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}

		Copy copy = new Copy();
		inputFile = new File(inputFileName);
		try {
			if (zipCompression) {
				outputFile = new File(outputFileName + ".zip");
				copy.copyWithZipCompression(inputFile, outputFile);
			} else if (gzipCompression) {
				outputFile = new File(outputFileName + ".gz");
				copy.copyWithGZipCompression(inputFile, outputFile);
			} else {
				outputFile = new File(outputFileName);
				copy.copy(inputFile, outputFile);
			}
		} catch (IOException ex) {
			_logger.log(Level.SEVERE, null, ex);
		}
	}
}
