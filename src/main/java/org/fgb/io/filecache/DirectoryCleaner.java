/*
 * Class:   DirectoryCleaner.java
 * Born On: Jan, 2012
 * Purpose: Monitor a directory and delete files in the directory when the directory reaches a certain size.
 */
package org.fgb.io.filecache;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * Class <code>DirectoryCleaner</code> will monitor a directory and delete files in the directory when the directory reaches a certain size.
 * <p>
 * This class will invoke a background thread that will perform the monitoring duties.
 * The background thread will stay alive until the user kills the process.
 *
 * @author Frederick Burkley
 */
public class DirectoryCleaner {

	/**
	 * The name of this class.
	 */
	private static final String _className = DirectoryCleaner.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);
	/**
	 * An observer to observe file system events.
	 */
	private final FileAlterationObserver directoryAlterationObserver;
	/**
	 * A listener to listen for file system events.
	 */
	private final FileAlterationListener directoryAlterationListener;
	/**
	 * A runnable that spawns a monitoring thread triggering any registered FileAlterationObserver at a specified interval.
	 */
	private final FileAlterationMonitor directoryAlterationMonitor;
	/**
	 * The polling time (i.e. sleep time) for the FileAlterationMonitor
	 */
	private final int pollingTime;
	/**
	 * The maximum size of the directory.
	 *
	 * TODO: Specify this in a config file.
	 */
	private static final long MAXIMUM_DIRECTORY_SIZE = FileUtils.ONE_GB;
	/**
	 * Directory cleaning activity will kick in when the "size of the directory" = "percent of maximum directory size" * "maximum directory size".
	 *
	 * TODO: Specify this in a config file.
	 */
	private static final float PERCENT_OF_MAXIMUM_DIRECTORY_SIZE = 0.25f;
	/**
	 *
	 */
	private static final long DIRECTORY_HIGH_WATER_MARK = (long) (MAXIMUM_DIRECTORY_SIZE * PERCENT_OF_MAXIMUM_DIRECTORY_SIZE);

	/**
	 * Create a <code>DirectoryCleaner</code>.  A <code>DirectoryCleaner</code> will monitor a directory
	 * and delete files in the directory when the directory reaches a certain size.
	 *
	 * @param directoryName The directory to monitor.
	 */
	public DirectoryCleaner(final String directoryName) {
		System.out.println(_className + ".DirectoryCleaner(): MAXIMUM_DIRECTORY_SIZE = " + MAXIMUM_DIRECTORY_SIZE);
		System.out.println(_className + ".DirectoryCleaner(): DIRECTORY_HIGH_WATER_MARK = " + DIRECTORY_HIGH_WATER_MARK);
		File directory = new File(directoryName);
		this.pollingTime = 100;  // milliseconds
		this.directoryAlterationObserver = new FileAlterationObserver(directory);
		this.directoryAlterationListener = new MyFileAlterationListener();
		this.directoryAlterationObserver.addListener(this.directoryAlterationListener);
		this.directoryAlterationMonitor = new FileAlterationMonitor(this.pollingTime);
		this.directoryAlterationMonitor.addObserver(this.directoryAlterationObserver);
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DirectoryCleaner.this.stop();
				} catch (Exception ex) {
					_logger.log(Level.SEVERE, null, ex);
				}
			}
		}));

	}

	/**
	 *
	 * @param files
	 */
	private void printFileInfo(final File[] files) {
		Date date;
		for (File file : files) {
			date = new Date(file.lastModified());
			System.out.println(_className + ".printFileInfo(): " + file.getName() + " " + date.toString());
		}

	}

	/**
	 * Start the <code>DirectoryMonitor</code>.
	 *
	 * @throws Exception If an <code>Exception</code> occurs.
	 */
	public void start() throws Exception {
		System.out.println(_className + ".start()...");
		this.directoryAlterationObserver.initialize();
		this.directoryAlterationMonitor.start();

	}

	/**
	 * Stop the <code>DirectoryMonitor</code>.
	 *
	 * @throws Exception If an <code>Exception</code> occurs.
	 */
	public void stop() throws Exception {
		System.out.println(_className + ".stop()...");
		this.directoryAlterationObserver.removeListener(this.directoryAlterationListener);
		this.directoryAlterationObserver.destroy();
		this.directoryAlterationMonitor.removeObserver(this.directoryAlterationObserver);
		this.directoryAlterationMonitor.stop();
	}

	class MyFileAlterationListener implements FileAlterationListener {

		/**
		 * The name of this class.
		 */
		private final String _className = MyFileAlterationListener.class.getName();

		@Override
		public void onStart(FileAlterationObserver fao) {
//				System.out.println(this.getClass().getName() + ".onStart()...");
		}

		@Override
		public void onDirectoryCreate(File file) {
//				System.out.println(this.getClass().getName() + ".onDirectoryCreate(): file.getName()=" + file.getName());
		}

		@Override
		public void onDirectoryChange(File file) {
			System.out.println(this.getClass().getName() + ".onDirectoryChange(): file.getName()=" + file.getName());
		}

		@Override
		public void onDirectoryDelete(File file) {
//				System.out.println(this.getClass().getName() + ".onDirectoryDelete(): file.getName()=" + file.getName());
		}

		@Override
		public void onFileCreate(File file) {
//			System.out.println(this.getClass().getName() + ".onFileCreate(): file.getName()=" + file.getName());
			if (!file.getName().endsWith(".lck")) {
				File directory = file.getParentFile();
				if (directory != null) {
					try {
						long sizeOfDirectory = FileUtils.sizeOfDirectory(directory); // Can throw IllegalArgumentException
						if (sizeOfDirectory > DIRECTORY_HIGH_WATER_MARK) {
							long startTime = System.currentTimeMillis();
							do {
								this.checkAndCleanDirectory(directory);
							} while ((sizeOfDirectory = FileUtils.sizeOfDirectory(directory)) > DIRECTORY_HIGH_WATER_MARK);
							long stopTime = System.currentTimeMillis();
							System.out.println(this.getClass().getName() + ".onFileCreate(): Elapsed time = " + (stopTime - startTime));
						}
					} catch (IllegalArgumentException ie) {
						System.out.println(_className + ": " + ie.getMessage());
					}
				}
			}
		}

		@Override
		public void onFileChange(File file) {
//				System.out.println(this.getClass().getName() + ".onFileChange(): file.getName()=" + file.getName());
		}

		@Override
		public void onFileDelete(File file) {
//			System.out.println(this.getClass().getName() + ".onFileDelete(): file.getName()=" + file.getName());
		}

		@Override
		public void onStop(FileAlterationObserver fao) {
//				System.out.println(this.getClass().getName() + ".onStop()...");
		}

		/**
		 *
		 */
		private void checkAndCleanDirectory(final File directory) {
			StringBuilder msg = new StringBuilder();
			if (_logger.isLoggable(Level.FINER)) {
				_logger.entering(_className, "checkAndCleanDirectory");
			}

//			try {
//				System.out.println(_className + ": Sleeping...");
//				Thread.sleep(5000);
//			} catch (InterruptedException ie) {
//				ie.printStackTrace();
//			}
			File files[] = this.getListOfFiles(directory);
//			File files[] = directory.listFiles(new FileFilter() {
//
//				@Override
//				public boolean accept(File pathname) {
//					String name = pathname.getName();
//					if (name.startsWith("FGB") && !name.endsWith(".lck")) {
//						return true;
//					} else {
//						return false;
//					}
//				}
//			});
//		System.out.println(_className + ".checkAndCleanDirectory(): -----------------------");
//		System.out.println(_className + ".checkAndCleanDirectory(): FILE LIST PRIOR TO SORT");
//		System.out.println(_className + ".checkAndCleanDirectory(): -----------------------");
//		this.printFileInfo(files);
			LastModifiedFileComparator comparator = new LastModifiedFileComparator();
			files = comparator.sort(files);
//		System.out.println(_className + ".checkAndCleanDirectory(): --------------------");
//		System.out.println(_className + ".checkAndCleanDirectory(): FILE LIST AFTER SORT");
//		System.out.println(_className + ".checkAndCleanDirectory(): --------------------");
//		this.printFileInfo(files);


			File fileToDelete = files[0];
//		if (_logger.isLoggable(Level.FINE)) {
			msg.append("Attempting to delete ").append(fileToDelete.getAbsolutePath());
			_logger.fine(msg.toString());
			msg.delete(0, msg.length());
//		}
			if (fileToDelete.delete()) {
				msg.append("Successfully deleted ").append(fileToDelete.getAbsolutePath());
				_logger.fine(msg.toString());
				msg.delete(0, msg.length());
			} else {
				msg.append("Unable to delete ").append(fileToDelete.getAbsolutePath());
				_logger.fine(msg.toString());
				msg.delete(0, msg.length());
			}

			if (_logger.isLoggable(Level.FINER)) {
				_logger.exiting(_className, "checkAndCleanDirectory");
			}

		}

		private File[] getListOfFiles(final File directory) {
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
			return files;
		}
	}
}


