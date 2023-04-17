package main.java.taxonomy;

import main.java.core.io.reader.ReaderManager;
import main.java.taxonomy.model.Taxonomy;
import main.java.taxonomy.util.DirectoryTable;
import org.apache.commons.io.FileUtils;
import main.java.core.io.writer.dimacs.DimacsWriter;
import main.java.core.io.writer.gson.GsonExportService;
import main.java.core.io.writer.taxonomy.TaxonomyWriter;
import main.java.core.model.interfaces.Tree;
import main.java.taxonomy.mining.TaxonomyMiner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Demo {


    public static void main(String[] args) {

        DirectoryTable.createDirectoryStructure();

        try {

            FileUtils.deleteDirectory(DirectoryTable.outputDirectory);
            DirectoryTable.outputDirectory.mkdir();

            if (!DirectoryTable.inputDirectory.exists()) {
                DirectoryTable.inputDirectory.mkdir();
            }

            if (DirectoryTable.inputDirectory.listFiles() != null) {
                for (File exampleDirectory : DirectoryTable.inputDirectory.listFiles()) {
                    runExample(exampleDirectory);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public static void runExample(File exampleDirectory) {

        System.out.println("Processing example: " + exampleDirectory.getName());
        ReaderManager readerManager = new ReaderManager();
        List<Tree> trees = readerManager.readFiles(exampleDirectory);

        TaxonomyMiner miner = new TaxonomyMiner();
        Taxonomy taxonomy = miner.mine(trees);
        TaxonomyWriter taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());

        File outputDirectory = new File(DirectoryTable.outputDirectory, exampleDirectory.getName());
        outputDirectory.mkdir();

        taxonomyWriter.writeToFile(taxonomy, outputDirectory.getAbsolutePath());

        System.out.println("DONE");

    }


}
