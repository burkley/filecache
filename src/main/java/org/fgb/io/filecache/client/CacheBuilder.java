package org.fgb.io.filecache.client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import org.fgb.io.filecache.impl.FileSizeFileGenerator;
import org.fgb.io.filecache.impl.LineCountFileGenerator;
import org.fgb.io.filecache.util.FileNameGenerator;
import org.fgb.io.filecache.util.FileSize;

/**
 * Class <code>CacheBuilder</code> will write files to a directory on the file system.
 * 
 * @author Frederick Burkley
 *
 */
public class CacheBuilder {
	/**
	 * The canonical name of this class.
	 */
	private static final String _className = CacheBuilder.class.getCanonicalName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);
	/**
	 * An executor service to execute threads.
	 */
	private final ExecutorService executor;
	/**
	 * The size of the file to generate. Either <code>fileSize</code> or
	 * <code>numberOfLines</code> will be specified, but not both.
	 */
	private FileSize fileSize;
	/**
	 * The number of lines in the file (to generate). Either
	 * <code>fileSize</code> or <code>numberOfLines</code> will be specified,
	 * but not both.
	 */
	private long numberOfLines;

	/**
	 * Default Constructor.
	 */
	public CacheBuilder() {
		super();
		this.executor = Executors.newSingleThreadExecutor();
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//		    public void run() {
//		    	CacheBuilder.this.executor.shutdown();
//		    }
//		});
	}

	public void buildCache(final String directoryName, final long numberOfLines) {
		short numberOfFieldsPerLine = 24;
		String fileName = FileNameGenerator.getHopefullyUniqueFileName(directoryName, "txt");
		FileGenerator generator = new LineCountFileGenerator(fileName, numberOfLines, numberOfFieldsPerLine);
		try {
			generator.generateFile();
		} catch (IOException e) {
			_logger.logp(Level.SEVERE, _className, "buildCache", e.toString());
		}
	}

	public void buildCache(final String directoryName, final FileSize fileSize) {
		Runnable task = () -> {
			short numberOfFieldsPerLine = 24;
			String fileName = FileNameGenerator.getHopefullyUniqueFileName(directoryName, ".txt");
			FileGenerator generator = new FileSizeFileGenerator(fileName, fileSize.getFileSize(), numberOfFieldsPerLine);
			try {
				generator.generateFile();
			} catch (IOException e) {
				_logger.logp(Level.SEVERE, _className, "buildCache", e.toString());
			}
		};
		Future f = this.executor.submit(task);
	}


	public void shutdown() {
    	this.executor.shutdown();
	}

	
	/**
	 * 
	 * @param size
	 */
	// public void setFileSize(final FileSize size) {
	// this.fileSize = size;
	// }

	/**
	 * 
	 * @param numberOfLines
	 */
	// public void setNumberOfLines(final long numberOfLines) {
	// this.numberOfLines = numberOfLines;
	// }

	/**
	 * Bootstrap the <code>CacheBuilder</code>.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Create a series of files in a directory.");
		Option directoryOption = new Option("d", "directory", true,
				"The name of the directory where the files will be created.  This argument is mandatory.");
		Option numberOfLinesOption = new Option("n", "numberLines", true,
				"The number of lines to generate (in the files that are created).  Can work in conjunction with the \"-r\" command line option; if the \"-r\" option is specified, then the \"-n\" option will specify the maximum number of lines to generated (in the files that are created).  This option is optional.");
		Option randomOption = new Option("r", "random", false,
				"Randomize the files that are generated.  Works in conjunction with either the \"-n\" or the \"-s\" command line argument (but not both).");
		Option sizeOption = new Option("s", "size", true,
				"The approximate size of the file to generate.  Valid units are \"K\" and \"M\".  For example, \"20M\" will specify a file that is approximately 20 Megabytes in size.  Can work in conjunction with the \"-r\" command line option; if the \"-r\" option is specified, then the \"-s\" option will specify the maximum size of the files that are generated.  This option is optional.");
		String directoryName = null;
		String numberOfLines = null;
		String fileSize = null;
		StringBuilder msg = new StringBuilder();

		Options options = new Options();
		options.addOption(helpOption);
		options.addOption(directoryOption);
		options.addOption(numberOfLinesOption);
		options.addOption(randomOption);
		options.addOption(sizeOption);

		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new PosixParser();

		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("h")) {
				formatter.printHelp(_className, options);
				System.exit(0);
			}
			if (commandLine.hasOption("d")) {
				directoryName = commandLine.getOptionValue("d").trim();
			}
			if (commandLine.hasOption("n")) {
				numberOfLines = commandLine.getOptionValue("n").trim();
			}
			if (commandLine.hasOption("r")) {
				System.out.println(_className + ": The command line option \"-r\" is not supported at this time.");
			}
			if (commandLine.hasOption("s")) {
				fileSize = commandLine.getOptionValue("s").trim();
			}
		} catch (ParseException pe) {
			_logger.log(Level.SEVERE, null, pe);
			System.exit(1);
		}

		System.out.println(_className + ": directoryName = <" + directoryName + ">");
		System.out.println(_className + ": fileSize = <" + fileSize + ">");

		// Check for necessary command line args
		if (directoryName == null) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}
		if (!(numberOfLines == null ^ fileSize == null)) {
			formatter.printHelp(_className, options);
			System.exit(1);
		}

		// boolean[] all = { false, true };
		// for (boolean a : all) {
		// for (boolean b: all) {
		// boolean c = a ^ b;
		// System.out.println(a + " ^ " + b + " = " + c);
		// }
		// }

		CacheBuilder cacheBuilder = new CacheBuilder();
		if (numberOfLines != null) {
			cacheBuilder.buildCache(directoryName, Short.parseShort(numberOfLines));
		} else {
			FileSize size = null;
			try {
				size = new FileSize(fileSize);
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
			cacheBuilder.buildCache(directoryName, size);
		}
		cacheBuilder.shutdown();
	}

}
