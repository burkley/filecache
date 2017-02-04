/*
 * Class:   RecursiveCopy.java
 * Born On: Jan, 2012
 * Purpose: Recursively copy a file system, excluding all hidden files.
 */
package org.fgb.io.filecache.client;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * Recursively copy a file system, excluding all hidden files.
 * <p>
 * @author Frederick Burkley
 */
public class RecursiveCopy {

	/**
	 * The name of this class.
	 */
	private static final String _className = RecursiveCopy.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);
	/**
	 * The directory walker will recursively walk the file system.
	 */
	private RecursiveCopier recursiveCopier;
	/**
	 * The path to read from.
	 */
	private File readPath;
	/**
	 * The path to write the file tree to.
	 */
	private File writePath;
	/**
	 * Boolean to indicate whether the <code>readPath</code> is absolute.
	 */
	private boolean readPathIsAbsolute;

	/**
	 * Recursively copy a file system, excluding all hidden files.
	 */
	public RecursiveCopy() {
		this.recursiveCopier = new RecursiveCopier(HiddenFileFilter.VISIBLE, null, -1);
	}

	/**
	 *
	 * @param readPath The root directory of the file system to read from.
	 * @param writePath The root directory of the file system to write to.
	 */
	public void copy(final File readPath, final File writePath) throws IOException {
		this.readPath = readPath;
		this.writePath = writePath;
		if (this.readPath.getPath().startsWith(".")) {
			this.readPathIsAbsolute = false;
		} else {
			this.readPathIsAbsolute = true;
		}
		System.out.println(_className + ".copy(): this.readPathIsAbsolute=" + this.readPathIsAbsolute);
		this.recursiveCopier.invoke();
	}

	/**
	 * Class <code>RecursiveCopier</code> will recursively copy a directory tree.  Hidden files will not be copied.
	 */
	class RecursiveCopier extends DirectoryWalker {

		public RecursiveCopier(IOFileFilter directoryFilter, IOFileFilter fileFilter, int depthLimit) {
			super(directoryFilter, fileFilter, depthLimit);
		}

		public RecursiveCopier(FileFilter filter, int depthLimit) {
			super(filter, depthLimit);
		}

		public RecursiveCopier() {
		}

		public void invoke() throws IOException {
			super.walk(RecursiveCopy.this.readPath, null);
		}

		@Override
		protected void handleDirectoryEnd(File directory, int depth, Collection results) throws IOException {
//			System.out.println(this.getClass().getName() + ".handleDirectoryEnd(): dir = " + directory.getName());
			super.handleDirectoryEnd(directory, depth, results);
		}

		@Override
		protected void handleDirectoryStart(File directory, int depth, Collection results) throws IOException {
//			System.out.println(this.getClass().getName() + ".handleDirectoryStart(): dir = " + directory.getName());
			super.handleDirectoryStart(directory, depth, results);
		}

		@Override
		protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
//			System.out.println(this.getClass().getName() + ".handleDirectory(): directory.getAbsolutePath() = " + directory.getAbsolutePath());
			boolean ret = this.createDestinationDirectory(directory);
			return ret;
		}

		@Override
		protected void handleFile(File file, int depth, Collection results) throws IOException {
			// Get the root of the directory that is to be read.
			String readFile = file.getCanonicalPath().substring(RecursiveCopy.this.readPath.getCanonicalFile().getParent().length() + 1);

			// The root of the directory that is to be read will be appended to the directory that is to be written.
			StringBuilder writeFile = new StringBuilder(RecursiveCopy.this.writePath.getCanonicalPath());
			writeFile.append(File.separator);
			writeFile.append(readFile);

//			System.out.println(this.getClass().getName() + ".handleFile():  readFile = >" + readFile + "<");
//			System.out.println(this.getClass().getName() + ".handleFile(): writeFile = >" + writeFile + "<");
			FileUtils.copyFile(file, new File(writeFile.toString()));
			return;
		}

		/**
		 * Create the destination directory.
		 *
		 * @param directory The directory that is to be read.
		 * @return <code>true</code> if the destination directory was created, else <code>false</code>.
		 * @throws IOException If an <code>IOException</code> occurs during processing.
		 */
		private boolean createDestinationDirectory(final File directory) throws IOException {
//			System.out.println("START: directory.getCanonicalPath()=" + directory.getCanonicalPath());
//			System.out.println("     :  readPath.getCanonicalPath()=" + RecursiveCopy.this.readPath.getCanonicalPath());
			String readDir = directory.getCanonicalPath().substring(RecursiveCopy.this.readPath.getCanonicalFile().getParent().length() + 1);
			StringBuilder writeDir = new StringBuilder(RecursiveCopy.this.writePath.getCanonicalPath());
			writeDir.append(File.separator);
			writeDir.append(readDir);
//			System.out.println(this.getClass().getName() + ".createDestinationDirectory1(): readDir = >" + readDir + "<");
//			System.out.println(this.getClass().getName() + ".createDestinationDirectory1(): writeDir = >" + writeDir.toString() + "<");

			boolean ret = false;
			File file = new File(writeDir.toString());
			if (file.exists() && file.canWrite() && file.canExecute()) {
				ret = true;
			} else {
				ret = file.mkdirs();
			}
			return true;
		}
	}

	/**
	 * Recursively copy a file system, excluding all hidden files.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Recursively copy a file system, excluding all hidden files.");
		Option readPathOption = new Option("r", "read", true, "The root directory of the file system to read from.  This argument is mandatory.");
		Option writePathOption = new Option("w", "write", true, "The root directory of the file system to write to.  This argument is mandatory.");
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

		RecursiveCopy fs = new RecursiveCopy();
		File readPathFile = new File(readPathName);
		File writePathFile = new File(writePathName);
		System.out.println(_className + "File.separator=" + File.separator);
		System.out.println(_className + "File.pathSeparator=" + File.pathSeparator);
		System.out.println(_className + "readPathName=>" + readPathName + "<");
		System.out.println(_className + "readPathFile.getAbsolutePath()=>" + readPathFile.getAbsolutePath() + "<");
		System.out.println(_className + "readPathFile.getAbsolutePath()=>" + readPathFile.getAbsolutePath() + "<");
		try {
			System.out.println(_className + "readPathFile.getCanonicalPath()=>" + readPathFile.getCanonicalPath() + "<");
			System.out.println(_className + "writePathFile.getCanonicalPath()=>" + writePathFile.getCanonicalPath() + "<");
		} catch (IOException ex) {
			Logger.getLogger(RecursiveCopy.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			System.out.println(_className + "readPathFile.getCanonicalPath()=>" + readPathFile.getCanonicalPath() + "<");
		} catch (IOException ex) {
			Logger.getLogger(RecursiveCopy.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println(_className + "readPathFile.getName()=>" + readPathFile.getName() + "<");
		System.out.println(_className + "readPathFile.getPath()=>" + readPathFile.getPath() + "<");
		try {
			fs.copy(readPathFile, writePathFile);
		} catch (IOException ex) {
			_logger.log(Level.SEVERE, null, ex);
		}
	}
}
