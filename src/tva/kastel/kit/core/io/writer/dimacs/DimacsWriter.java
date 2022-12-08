package tva.kastel.kit.core.io.writer.dimacs;

import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import org.logicng.formulas.Formula;
import org.logicng.io.writers.FormulaDimacsFileWriter;
import tva.kastel.kit.taxonomy.model.TaxonomyEdge;
import tva.kastel.kit.taxonomy.model.TaxonomyNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DimacsWriter {

    public void writeToFile(Formula formula, String folderPath) {
        try {

            String cnfPath = Paths.get(folderPath, "featureModel.cnf").toAbsolutePath().toString();
            String dimacsPath = Paths.get(folderPath, "featureModel.dimacs").toAbsolutePath().toString();
            String helperFilePath = Paths.get(folderPath, "featureModel.map").toAbsolutePath().toString();

            FormulaDimacsFileWriter.write(cnfPath, formula, true);

            List<String> lines = Files.readAllLines(Paths.get(helperFilePath));
            String transformedString = "";

            for (int z = 0; z < lines.size(); z++) {
                String[] splittedLine = lines.get(z).split(";");
                String first = splittedLine[0];
                String second = splittedLine[1];
                transformedString += ("c " + second + " " + first);
                transformedString += "\n";

            }

            if (!Files.exists(Paths.get(dimacsPath))) {
                Files.createFile(Paths.get(dimacsPath));
            }

            File file = new File(dimacsPath);

            FileWriter writer = new FileWriter(file);
            writer.write(transformedString);
            String content = new String(Files.readAllBytes(Paths.get(cnfPath)));
            writer.write(content);
            writer.flush();
            writer.close();

            file = new File(cnfPath);
            file.delete();

            file = new File(helperFilePath);
            file.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
