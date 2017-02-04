/*
 * Class:   LineCountFileGenerator
 * Born On: Mar 25, 2012
 * Purpose: Generate a file with sample text.
 */
package org.fgb.io.filecache.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import org.fgb.io.filecache.FileGenerator;
import org.fgb.io.filecache.util.RandomLineHelper;


/**
 * Generate a file with sample text.
 *
 * @author Frederick Burkley
 */
public class LineCountFileGenerator implements FileGenerator {

	/**
	 * The name of this class.
	 */
	private static final String _className = LineCountFileGenerator.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);
	/**
	 * The name of the file that will be generated.
	 */
	private String fileName;
	/**
	 * The number of lines in the file that will be generated.
	 */
	private final long numberOfLinesInFile;
	/**
	 * Flag to enable application benchmarking.
	 */
	private boolean benchmark;
	/**
	 * Flag to enable zip compression.
	 */
	private boolean zipCompress;
	/**
	 * Flag to enable gzip compression.
	 */
	private boolean gzipCompress;
	/**
	 * 
	 */
	private final RandomLineHelper randomLineHelper;
	/**
	 * The stream to write to.
	 */
//	private CountingOutputStream countingOutputStream;
	/**
	 * The default number of lines to write in the output file.  The user can override this on the command line.
	 */
	private static final int _DEFAULT_NUMBER_OF_LINES = 100000;
	/**
	 * The default number of times to generate the output file.  The user can override this on the command line.
	 */
	private static final int _DEFAULT_LOOP_COUNT = 1;

	/**
	 * Create a <code>LineCountFileGenerator</code>.
	 */
	public LineCountFileGenerator(final String fileName, final long numberOfLines, final short numberOfFieldsPerLine) {
		this.fileName = fileName;
		this.numberOfLinesInFile = numberOfLines;
		this.randomLineHelper = new RandomLineHelper(numberOfFieldsPerLine);
	}

	/**
	 * Generate a file with sample text.
	 */
	@Override
	public void generateFile() throws IOException {
		String line;
		OutputStream outputStream;
		long startTime = System.currentTimeMillis();

		outputStream = this.openOutputStream(this.fileName);
		for (long lineNumber = 0; lineNumber < this.numberOfLinesInFile; lineNumber++) {
			line = this.randomLineHelper.getLine();
			outputStream.write(line.getBytes());
			_logger.log(Level.FINEST, line);
		}

		this.closeOutputStream(outputStream);
//		Copy copy = new Copy();
//		File inputFile = new File(this.fileName);
//		File outputFile = new File(this.fileName + ".gz");
//		copy.copyWithIOUtils(inputFile, outputFile);

		long stopTime = System.currentTimeMillis();
		if (this.benchmark) {
			System.out.println(_className + ".generate(): Elapsed Time = " + (stopTime - startTime) + " milliseconds");
		}
	}

	/**
	 *
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private OutputStream openOutputStream(String fileName) throws IOException {
		OutputStream outputStream;
		if (this.gzipCompress) {
			outputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(fileName+".gz")));
		} else {
			outputStream = new BufferedOutputStream(new FileOutputStream(fileName));
		}
		return outputStream;
	}

	/**
	 *
	 * @param outputStream
	 */
	private void closeOutputStream(OutputStream outputStream) throws IOException {
		outputStream.flush();
		outputStream.close();
	}

	@Override
	public void setBenchmark(boolean benchmark) {
		this.benchmark = benchmark;
	}


	@Override
	public void enableZipCompression(final boolean compress) {
		this.zipCompress = compress;
	}

	@Override
	public void enableGZipCompression(final boolean compress) {
		this.gzipCompress = compress;
	}

}
