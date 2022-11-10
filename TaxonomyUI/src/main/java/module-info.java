module tva.kastel.kit.edu.taxonomyui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.apache.commons.io;
    requires TaxonomyMining;
    requires guru.nidi.graphviz;
    requires javafx.swing;

    opens tva.kastel.kit.edu.taxonomyui to javafx.fxml;
    exports tva.kastel.kit.edu.taxonomyui;
}