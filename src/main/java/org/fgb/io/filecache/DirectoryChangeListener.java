/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fgb.io.filecache;

import org.fgb.io.filecache.util.Constants;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 *
 * @author burkley
 */
public class DirectoryChangeListener implements FileAlterationListener {

	/**
	 * The name of this class.
	 */
	private static final String _className = DirectoryChangeListener.class.getName();
	/**
	 * JDK logger.
	 */
	private static final Logger _logger = Logger.getLogger(_className);

	public DirectoryChangeListener() {
	}

	@Override
	public void onDirectoryChange(File file) {
//		if (_logger.isLoggable(Level.FINER)) {
//			_logger.entering(_className, "onDirectoryChange");
//		}
		System.out.println(_className + ": onDirectoryChange()...");
//		if (_logger.isLoggable(Level.FINER)) {
//			_logger.exiting(_className, "onDirectoryChange");
//		}
	}

	@Override
	public void onDirectoryCreate(File file) {
	}

	@Override
	public void onDirectoryDelete(File file) {
	}

	@Override
	public void onFileChange(File file) {
		System.out.println(_className + ".onFileCreate(): BEGIN for file " + file.getName());
		System.out.println(_className + ".onFileCreate(): END for file " + file.getName());
	}

	@Override
	public void onFileCreate(File file) {
		System.out.println(_className + ".onFileCreate(): BEGIN for file " + file.getName());
//		String directory = FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath());
//		String dir = file.getParent();  // Need to check and account for null return
		File dirFile = file.getParentFile();
		long sizeOfDirectory = FileUtils.sizeOf(dirFile);

//		if (_logger.isLoggable(Level.FINER)) {
//			_logger.entering(_className, "onFileCreate");
//		}
//		System.out.println(_className + ": onFileCreate()..." + file.getAbsolutePath());
//		System.out.println(_className + ": onFileCreate()..." + file.getParent());
		System.out.println(_className + ".onFileCreate(): sizeOfDirectory = " + sizeOfDirectory);
//		System.out.println(_className + ".onFileCreate(): Constants._USED_DIRECTORY_SPACE_LIMIT = " + Constants._USED_DIRECTORY_SPACE_LIMIT);

//		if (sizeOfDirectory > Constants._USED_DIRECTORY_SPACE_LIMIT) {
		File fileList[] = dirFile.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				boolean ret = false;
				String name = pathname.getName();
				if (name.startsWith(Constants._FILENAME_PATTERN) && !name.endsWith(".lck")) {
					ret = true;
				}
				return ret;
			}
		});
//		for (int i = 0; i < fileList.length; i++) {
//			System.out.println(_className + ": fileList[" + i + "]=" + fileList[i].getName());
//		}
//		System.out.println("-----");

		Arrays.sort(fileList, new Comparator<File>() {

			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			}
		});

//		for (int i = 0; i < fileList.length; i++) {
//			System.out.println(_className + ": fileList[" + i + "]=" + fileList[i].getName() + "  fileList[" + i + "]=" + new Date(fileList[i].lastModified()).toString());
//		}
//		}

//		if (_logger.isLoggable(Level.FINER)) {
//			_logger.exiting(_className, "onFileCreate");
//		}
		System.out.println(_className + ".onFileCreate(): END for file " + file.getName());
		System.out.println();
	}

	@Override
	public void onFileDelete(File file) {
	}

	@Override
	public void onStart(FileAlterationObserver fao) {
		// Gets called frequently
//		System.out.println(_className + ": onStart()...");
	}

	@Override
	public void onStop(FileAlterationObserver fao) {
		// Gets called frequently
//		System.out.println(_className + ": onStop()...");
	}
}
