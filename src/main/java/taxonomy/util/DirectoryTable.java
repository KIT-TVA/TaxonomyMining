package main.java.taxonomy.util;

import java.io.File;
import java.nio.file.Paths;

public class DirectoryTable {

    public static final File baseDirectory = Paths.get(System.getProperty("user.home"), "mining_data").toFile();
    public static final File outputDirectory = Paths.get(System.getProperty("user.home"), "mining_data", "output").toFile();
    public static final File inputDirectory = Paths.get(System.getProperty("user.home"), "mining_data", "input").toFile();

    public static void createDirectoryStructure() {
        if (!baseDirectory.exists()) {
            baseDirectory.mkdir();
        }
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        if (!inputDirectory.exists()) {
            inputDirectory.mkdir();
        }
    }
}
