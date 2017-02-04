/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fgb.io.filecache;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * Class <code>DirectoryMonitor</code> will monitor a directory for events of interest.
 * <p>
 * This class will invoke a background thread that will perform the monitoring duties.
 * The background thread will stay alive until the user kills the process.
 *
 * @author Frederick Burkley
 */
public class DirectoryMonitor {
	/**
	 * The name of this class.
	 */
	private static final String _className = DirectoryMonitor.class.getName();
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
	 * Create a <code>DirectoryMonitor</code>.  A <code>DirectoryMonitor</code> will monitor a directory for events of interest.
	 *
	 * @param directoryName The directory to monitor.
	 */
	public DirectoryMonitor(final String directoryName) {
		File directory = new File(directoryName);
		this.pollingTime = 100;  // milliseconds
		this.directoryAlterationObserver = new FileAlterationObserver(directory);
		this.directoryAlterationListener = new FileAlterationListener() {

			@Override
			public void onStart(FileAlterationObserver fao) {
//				System.out.println(this.getClass().getName() + ".onStart()...");
			}

			@Override
			public void onDirectoryCreate(File file) {
				System.out.println(this.getClass().getName() + ".onDirectoryCreate(): file.getName()=" + file.getName());
			}

			@Override
			public void onDirectoryChange(File file) {
				System.out.println(this.getClass().getName() + ".onDirectoryChange(): file.getName()=" + file.getName());
			}

			@Override
			public void onDirectoryDelete(File file) {
				System.out.println(this.getClass().getName() + ".onDirectoryDelete(): file.getName()=" + file.getName());
			}

			@Override
			public void onFileCreate(File file) {
				System.out.println(this.getClass().getName() + ".onFileCreate(): file.getName()=" + file.getName());
			}

			@Override
			public void onFileChange(File file) {
				System.out.println(this.getClass().getName() + ".onFileChange(): file.getName()=" + file.getName());
			}

			@Override
			public void onFileDelete(File file) {
				System.out.println(this.getClass().getName() + ".onFileDelete(): file.getName()=" + file.getName());
			}

			@Override
			public void onStop(FileAlterationObserver fao) {
//				System.out.println(this.getClass().getName() + ".onStop()...");
			}
		};
		this.directoryAlterationObserver.addListener(this.directoryAlterationListener);
		this.directoryAlterationMonitor = new FileAlterationMonitor(this.pollingTime);
		this.directoryAlterationMonitor.addObserver(this.directoryAlterationObserver);
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DirectoryMonitor.this.stop();
				} catch (Exception ex) {
					_logger.log(Level.SEVERE, null, ex);
				}
			}

		}));

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

}