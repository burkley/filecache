package org.fgb.io.filecache.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.fgb.io.filecache.FileGenerator;
import org.fgb.io.filecache.util.RandomLineHelper;

public class FileSizeFileGenerator implements FileGenerator {

	/**
	 * The name of this class.
	 */
	private static final String _className = FileSizeFileGenerator.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);
	/**
	 * The name of the file that will be generated.
	 */
	private final String fileName;
	/**
	 * The name of the directory where the file will be generated.
	 */
	private final String dirName;
	/**
	 * The size of the file that will be generated
	 */
	private final long fileSize;
	/**
	 * 
	 */
	private final RandomLineHelper randomLineHelper;

	public FileSizeFileGenerator(final String fileName, long fileSize, final short numberOfFieldsPerLine) {
		super();
//		System.out.println(_className + ".FileSizeFileGenerator: Constructor...");

		this.fileName = fileName;
		int index = this.fileName.lastIndexOf(File.separator);
		if (index >= 0) {
		this.dirName = this.fileName.substring(0, index);
		} else {
			this.dirName = "." + File.separator;
		}
		
		this.fileSize = fileSize;
		this.randomLineHelper = new RandomLineHelper(numberOfFieldsPerLine);
//		System.out.println(_className + ".FileSizeFileGenerator: fileName = " + this.fileName);
//		System.out.println(_className + ".FileSizeFileGenerator: fileName.lastIndexOf(\"/\") = " + this.fileName.lastIndexOf(File.separator));
//		System.out.println(_className + ".FileSizeFileGenerator: dirName = " + this.dirName);
	}


	@Override
	public void generateFile() throws IOException {
		StringBuilder msg = new StringBuilder();
		byte[] bytesToWrite;
		long bytesWritten = 0;
		long startTime = System.currentTimeMillis();
		File dir = new File(this.dirName);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (SecurityException ex) {
				throw new IOException(ex);
			}
		}
		File file = new File(this.fileName);

		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			do {
				bytesToWrite = this.randomLineHelper.getLine().getBytes();
				// msg.append(_className).append(".generateFile(): Wrote
				// ").append(bytesToWrite.length).append(" bytes to file.");
				// System.out.println(msg.toString());
				// msg.delete(0, msg.length());
				outputStream.write(bytesToWrite);
				bytesWritten += bytesToWrite.length;
			} while (bytesWritten < this.fileSize);
		}

	}

	@Override
	public void setBenchmark(boolean benchmark) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableZipCompression(boolean compress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableGZipCompression(boolean compress) {
		// TODO Auto-generated method stub

	}

}
