package tva.kastel.kit.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tva.kastel.kit.taxonomy.util.DirectoryTable;

import java.io.IOException;
import java.net.URL;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        DirectoryTable.createDirectoryStructure();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/ui/View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        stage.setTitle("Taxonomy Mining");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}