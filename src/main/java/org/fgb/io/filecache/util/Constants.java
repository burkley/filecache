/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fgb.io.filecache.util;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author burkley
 */
public class Constants {
	public final static long _MAX_DIRECTORY_SIZE = FileUtils.ONE_GB;
	public final static float _USED_DIRECTORY_SPACE_RATIO = 0.1f;
	public final static long _USED_DIRECTORY_SPACE_LIMIT = (long) Math.ceil(_MAX_DIRECTORY_SIZE * _USED_DIRECTORY_SPACE_RATIO);
	public final static String _FILENAME_PATTERN = "FGB";
}
