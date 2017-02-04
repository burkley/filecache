/*
 * Prolog
 * Package:         fileOperations
 * File:            FileList.java
 * Original Author: Frederick G. Burkley
 * Born On Date:    Jul 09
 * Description:     See Javadoc below
 */
package org.fgb.io.filecache.client;

import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Recursively list all files from a specified root directory.  The root directory
 * is specified on the command line.
 *
 * @author Frederick Burkley
 */
public class FileList {

    /**
     * The name of this class.
     */
    private static final String _className = FileList.class.getName();
    /**
     * JDK logger.
     */
    private Logger logger = Logger.getLogger(_className);
    /**
     * Show hidden files.
     */
    private boolean showHiddenFiles;

    /**
     * Public constructor.
     */
    public FileList( ) {
    }

    /**
     * Recursively list the files starting at the directory <code>root</code>.
     *
     * @param root The starting directory.
     * @throws IOException If an IOException occurs.
     */
    public void list( final File root ) throws IOException {
        StringBuilder msg;
        if (root.canRead()) {
            if (this.showHiddenFiles || !root.isHidden()) {
                System.out.println(root);
                if (root.isDirectory()) {
                    File[] files = root.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        this.list(files[i]);
                    }
                }
            }
        } else {
            msg = new StringBuilder(_className);
            msg.append(": Unable to read ");
            msg.append(root.getCanonicalPath());
            System.err.println(msg.toString());
            msg.delete(0, msg.length());
        }
        return;
    }

    /**
     * Get method for the <i>Show Hidden Files</i> property.
     * <p>
     * The exact definition of hidden is system-dependent.
     * On UNIX systems, a file is considered to be hidden if its name begins with
     * a period character ('.'). On Microsoft Windows systems, a file is considered
     * to be hidden if it has been marked as such in the filesystem.
     *
     * @return  The <i>Show Hidden Files</i> property.
     */
    public boolean isShowHiddenFiles( ) {
        return showHiddenFiles;
    }

    /**
     * <i>Show Hidden Files</i> property.  Set to <code>true</code> to show hidden files.
     * <p>
     * The exact definition of hidden is system-dependent.
     * On UNIX systems, a file is considered to be hidden if its name begins with
     * a period character ('.'). On Microsoft Windows systems, a file is considered
     * to be hidden if it has been marked as such in the filesystem.
     *
     * @param showHiddenFiles Set to <code>true</code> to show hidden files, else set to <code>false</code>.
     */
    public void setShowHiddenFiles( boolean showHiddenFiles ) {
        this.showHiddenFiles = showHiddenFiles;
    }

    /**
     * Bootstrap.
     * @param args The command line arguments.
     */
    public static void main( String[] args ) {
        // Command line options
        Option helpOption = new Option("h", "help", false, "Recursively list files.");
        Option pathOption = new Option("p", "path", true, "The root path.  This argument is mandatory.");
        Option showHiddenOption = new Option("s", "show-hidden", false, "Show hidden files.  By default, hidden files are not shown.  This argument is optional.");
        String path = null;
        boolean showHiddenFiles = false;

        Options options = new Options();
        options.addOption(helpOption);
        options.addOption(pathOption);
        options.addOption(showHiddenOption);

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new PosixParser();

        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("h")) {
                formatter.printHelp(_className, options);
                System.exit(0);
            }
            if (commandLine.hasOption("p")) {
                path = commandLine.getOptionValue("p");
            }
            if (commandLine.hasOption("s")) {
                showHiddenFiles = true;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
            System.exit(1);
        }

        // Check for mandatory command line arguments
        if (path == null) {
			formatter.printHelp(_className, options);
            System.exit(1);
        }

        FileList fileList = new FileList();
        fileList.setShowHiddenFiles(showHiddenFiles);
        try {
            File file = new File(path).getCanonicalFile();
            fileList.list(file);
        } catch (IOException ex) {
            Logger.getLogger(_className).log(Level.SEVERE, null, ex);
        }
    }
}
