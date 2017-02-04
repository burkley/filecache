/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fgb.io.filecache.util.test;

import org.fgb.io.filecache.util.Data;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Class <code>DataTester</code> is a standalone class to test functionality of the {@linkplain fgb.io.util.Data} class.
 *
 * @author burkley
 */
public class DataTester {

	/**
	 * The name of this class.
	 */
	private static final String _className = DataTester.class.getName();
	/**
	 * JDK logger.
	 */
	private static Logger _logger = Logger.getLogger(_className);

	/**
	 *
	 */
	public boolean testForImmutability() {
		boolean immutable = true;
		String line = "339.24905	146.5093	105.2965	198.25989	39.127373	64.57314	168.21101	187.49997	233.83878	29.534412	295.6248	233.64357	7.372895	195.51558	42.97322	193.02809";
		Data data = new Data(line);
		List<String> thisList = data.getData();
		thisList.clear();

		List<String> thatList = data.getData();
		if (thisList.size() == thatList.size()) {
			immutable = false;
		}
		return immutable;
	}

	/**
	 * Bootstrap.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Command line options
		Option helpOption = new Option("h", "help", false, "Test class to test the class " + Data.class.getName());

		Options options = new Options();
		options.addOption(helpOption);

		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new PosixParser();

		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("h")) {
				formatter.printHelp(_className, options);
				System.exit(0);
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			System.exit(1);
		}

		DataTester dataTester = new DataTester();
		System.out.println(dataTester.testForImmutability());
	}
}
