package tva.kastel.kit.ui;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import tva.kastel.kit.core.io.reader.ReaderManager;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.io.writer.taxonomy.TaxonomyWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.core.model.interfaces.Value;
import tva.kastel.kit.taxonomy.mining.TaxonomyMiner;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.ui.util.FileTable;
import tva.kastel.kit.ui.util.FileWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class Controller implements Initializable {
    @FXML
    public Button taxonomizeButton;
    @FXML
    private TreeView<FileWrapper> explorerTree;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TreeView<Node> familyModelTree;

    @FXML
    private TableView<Attribute> tableView;

    @FXML
    private Button compareButton;

    @FXML
    private Canvas canvas;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab taxonomyTab;

    @FXML
    private Tab familyModelTab;

    private final ReaderManager readerManager;

    private final TaxonomyWriter taxonomyWriter;
    private final CompareEngineHierarchical compareEngine;

    private final TaxonomyMiner taxonomyMiner;


    public Controller() {
        readerManager = new ReaderManager();
        compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""));
        taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());
        taxonomyMiner = new TaxonomyMiner();


    }


    public void createExplorerTree() {

        Configurations configurations = new Configurations();
        try {
            Configuration configuration = configurations.properties(FileTable.CONFIGURATION_FILE);
            String path = configuration.getString("root_directory");
            File rootFile = new File(path);
            TreeItem<FileWrapper> rootItem = new TreeItem<>(new FileWrapper(rootFile));
            explorerTree.setRoot(rootItem);
            rootItem.setExpanded(true);
            addFileToBrowserRecursively(rootItem);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }


    }

    private void addFileToBrowserRecursively(TreeItem<FileWrapper> treeItem) {

        decorateExplorerNode(treeItem);
        if (treeItem.getValue().getFile().isDirectory()) {

            for (File child : treeItem.getValue().getFile().listFiles()) {

                TreeItem<FileWrapper> childItem = new TreeItem<>(new FileWrapper(child));
                treeItem.getChildren().add(childItem);
                addFileToBrowserRecursively(childItem);

            }
        }

    }

    public void decorateExplorerNode(TreeItem<FileWrapper> treeItem) {
        File file = treeItem.getValue().getFile();

        if (file.isDirectory()) {
            treeItem.setGraphic(new ImageView(FileTable.FV_DIRECTORY_16));
        } else {
            treeItem.setGraphic(new ImageView(FileTable.FV_FILE_16));
        }


    }


    private void updateTableView() {
        Node node = familyModelTree.getSelectionModel().getSelectedItem().getValue();

        tableView.getColumns().clear();
        ObservableList<Attribute> data = FXCollections.observableArrayList(node.getAttributes());

        TableColumn<Attribute, String> property = new TableColumn<Attribute, String>("Property");
        property.setCellValueFactory(new PropertyValueFactory<>("attributeKey"));
        TableColumn<Attribute, String> value = new TableColumn<Attribute, String>("Value");
        value.setCellValueFactory(new PropertyValueFactory<>("attributeValues"));
        value.setCellValueFactory(e -> {
            String valueString = "";
            for (Value singleValue : e.getValue().getAttributeValues()) {

                valueString += singleValue.getValue() + " ";

            }
            return new SimpleStringProperty(valueString);
        });

        tableView.setItems(data);
        tableView.getColumns().addAll(property, value);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void createFamilyModelTree(Tree tree) {
        familyModelTree.setRoot(new TreeItem<Node>(tree.getRoot()));
        familyModelTree.getRoot().setGraphic(new ImageView(FileTable.rootImage));
        familyModelTree.getRoot().setExpanded(true);
        familyModelTree.setShowRoot(true);

        createFamilyModelNodes(tree.getRoot(), familyModelTree.getRoot());
    }

    public void createFamilyModelNodes(Node node, TreeItem<Node> parent) {
        for (Node n : node.getChildren()) {
            TreeItem<Node> ti = decorateFamilyModelNode(new TreeItem<Node>(n));
            parent.getChildren().add(ti);
            if (!n.isLeaf()) {
                createFamilyModelNodes(n, ti);
            }
        }
    }

    public TreeItem<Node> decorateFamilyModelNode(TreeItem<Node> node) {
        switch (node.getValue().getVariabilityClass()) {
            case MANDATORY:
                node.setGraphic(new ImageView(FileTable.FV_MANDATORY_16));
                break;
            case ALTERNATIVE:
                node.setGraphic(new ImageView(FileTable.FV_ALTERNATIVE_16));
                break;
            case OPTIONAL:
                node.setGraphic(new ImageView(FileTable.FV_OPTIONAL_16));
                break;
            default:
                break;
        }
        // Special Case for Root Node
        if (node.getValue().isRoot())
            node.setGraphic(new ImageView(FileTable.rootImage));
        node.getValue().setRepresentation(node.getValue().getNodeType());
        return node;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        familyModelTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (familyModelTree.getSelectionModel().getSelectedIndices().size() == 1) {
                updateTableView();
            }

        });

        explorerTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    TreeItem<FileWrapper> selectedFile = explorerTree.getSelectionModel().getSelectedItem();
                    Tree tree = readerManager.readFile(selectedFile.getValue().getFile());
                    createFamilyModelTree(tree);
                }
            }
        });

        explorerTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            compareButton.setDisable(explorerTree.getSelectionModel().getSelectedIndices().size() < 2);
            taxonomizeButton.setDisable(explorerTree.getSelectionModel().getSelectedIndices().size() < 2);

        });

        createExplorerTree();
        explorerTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        compareButton.setDisable(true);
        taxonomizeButton.setDisable(true);
    }


    public void createTaxonomy() {

        List<Tree> trees = getSelectedFiles();
        Taxonomy taxonomy = taxonomyMiner.mine(trees);

        TreeItem<FileWrapper> rootItem = explorerTree.getRoot();

        boolean hasTaxonomyFolder = false;
        TreeItem<FileWrapper> taxonomyFolderItem = null;
        for (TreeItem<FileWrapper> item : rootItem.getChildren()) {
            if (item.getValue().getFile().getName().equals("Taxonomy")) {
                hasTaxonomyFolder = true;
                taxonomyFolderItem = item;
            }
        }

        if (!hasTaxonomyFolder) {
            File taxonomyFolder = Paths.get(rootItem.getValue().getFile().getAbsolutePath(), "Taxonomy").toFile();
            taxonomyFolder.mkdir();
            taxonomyFolderItem = new TreeItem<>();
            taxonomyFolderItem.setValue(new FileWrapper(taxonomyFolder));
            decorateExplorerNode(taxonomyFolderItem);
            rootItem.getChildren().add(taxonomyFolderItem);
        }

        int count = 0;
        for (TreeItem<FileWrapper> item : taxonomyFolderItem.getChildren()) {
            if (item.getValue().getFile().getName().equals("Taxonomy_" + count)) {
                count++;
            } else {
                break;
            }
        }

        File innerTaxonomyFolder = Paths.get(taxonomyFolderItem.getValue().getFile().getAbsolutePath(), "Taxonomy_" + count).toFile();
        innerTaxonomyFolder.mkdir();
        TreeItem<FileWrapper> innerTaxonomyFolderItem = new TreeItem<>();
        innerTaxonomyFolderItem.setValue(new FileWrapper(innerTaxonomyFolder));
        decorateExplorerNode(innerTaxonomyFolderItem);
        taxonomyFolderItem.getChildren().add(innerTaxonomyFolderItem);


        taxonomyWriter.writeToFile(taxonomy, innerTaxonomyFolder.getAbsolutePath());

        for (File file : innerTaxonomyFolder.listFiles()) {
            TreeItem<FileWrapper> item = new TreeItem<>();
            item.setValue(new FileWrapper(file));
            decorateExplorerNode(item);
            innerTaxonomyFolderItem.getChildren().add(item);
        }

        tabPane.getSelectionModel().select(taxonomyTab);
        try {

            Graph graph = taxonomyWriter.plot(taxonomy);
            BufferedImage bufferedImage = Graphviz.fromGraph(graph).render(Format.SVG).toImage();

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            canvas.setHeight(image.getHeight());
            canvas.setWidth(image.getWidth());


            context.drawImage(image, 0, 0, image.getWidth(), image.getHeight());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private List<Tree> getSelectedFiles() {
        List<TreeItem<FileWrapper>> selectedItems = explorerTree.getSelectionModel().getSelectedItems();

        List<Tree> trees = new ArrayList<>();

        for (TreeItem<FileWrapper> item : selectedItems) {
            trees.add(readerManager.readFile(item.getValue().getFile()));
        }
        return trees;
    }


    public void compare() {
        Tree tree = compareEngine.compareMerge(getSelectedFiles());
        createFamilyModelTree(tree);
    }


}