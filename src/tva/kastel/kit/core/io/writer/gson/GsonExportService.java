package tva.kastel.kit.core.io.writer.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tva.kastel.kit.core.model.interfaces.Tree;


public class GsonExportService implements ExportService<String> {
    private Gson gson;

    public GsonExportService() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        this.gson = gsonBuilder.create();
    }

    @Override
    public String exportTree(Tree tree) {
        return this.gson.toJson(tree);
    }
}
