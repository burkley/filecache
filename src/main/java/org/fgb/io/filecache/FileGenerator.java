/**
 * 
 */
package org.fgb.io.filecache;

import java.io.IOException;

/**
 * @author burkley
 *
 */
public interface FileGenerator {
	public void generateFile() throws IOException;
	public void setBenchmark(boolean benchmark);
	public void enableZipCompression(boolean compress);
	public void enableGZipCompression(boolean compress);
}
