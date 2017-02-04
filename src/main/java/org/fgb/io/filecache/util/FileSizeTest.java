package org.fgb.io.filecache.util;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileSizeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test (expected = IllegalArgumentException.class)
	public void testFileSizeWithNullArg() {
		new FileSize(null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testFileSizeWithZeroLengthArg() {
		new FileSize("");
	}

	@Test (expected = IllegalArgumentException.class)
	public void testFileSizeWithMalformedArg1() {
		new FileSize("64G");
	}

	@Test (expected = IllegalArgumentException.class)
	public void testFileSizeWithMalformedArg2() {
		new FileSize("64 K");
	}

	@Test
	public void testGetFileSize() {
		long expected = 524_288; // 512 KByte
		FileSize fs = new FileSize("512K");
		assertTrue(expected == fs.getFileSize());
	}

}
