package tva.kastel.kit.ui;

import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import tva.kastel.kit.core.io.reader.ReaderManager;
import tva.kastel.kit.core.io.reader.gson.GsonImportService;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.io.writer.taxonomy.TaxonomyWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.core.model.interfaces.Value;
import tva.kastel.kit.taxonomy.mining.TaxonomyMiner;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.taxonomy.model.TaxonomyEdge;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class Controller implements Initializable {
    @FXML
    public Button showTreeButton;
    @FXML
    private ListView<String> listView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TreeView<Node> treeView;

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

    private ReaderManager readerManager;

    private GsonImportService gsonImportService;

    private Tree tree;

    private Map<String, Tree> artifactMap;

    private TaxonomyWriter taxonomyWriter;


    private CompareEngineHierarchical compareEngine;


    public Controller() {
        readerManager = new ReaderManager();
        gsonImportService = new GsonImportService();
        compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""));
        taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());

    }

    public void showFiles() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open folder");
        File directory = directoryChooser.showDialog(anchorPane.getScene().getWindow());

        if (directory != null) {
            String directoryPath = directory.getAbsolutePath();
            File[] files = directory.listFiles();

            Arrays.sort(files);

            artifactMap = new HashMap<>();

            for (File file : files) {
                Tree tree = readerManager.readFile(file);
                listView.getItems().add(file.getName());
                artifactMap.put(file.getName(), tree);
            }


        }
    }

    public void importFiles() {

        listView.getItems().clear();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open taxonomy folder");
        File directory = directoryChooser.showDialog(anchorPane.getScene().getWindow());

        if (directory != null) {
            String directoryPath = directory.getAbsolutePath();
            File[] files = directory.listFiles();

            Arrays.sort(files);

            TaxonomyMiner taxonomyMiner = new TaxonomyMiner();
            artifactMap = new HashMap<>();
            List<Tree> artifacts = new ArrayList<>();

            for (File file : files) {
                Tree tree = readerManager.readFile(file);
                artifacts.add(tree);
            }

            Taxonomy taxonomy = taxonomyMiner.mine(artifacts);

            List<String> listViewItems = new ArrayList<>();

            for (TaxonomyEdge edge : taxonomy.getEdges()) {

                String startName = edge.getStart().toString();
                Tree startTree = edge.getStart().getTree();

                artifactMap.put(startName, startTree);

                String endName = edge.getEnd().toString();
                Tree endTree = edge.getEnd().getTree();

                artifactMap.put(endName, endTree);

                String featureName = edge.getFeature().getName();
                Tree featureTree = edge.getRefinementTree();

                artifactMap.put(featureName, featureTree);

                if (!listViewItems.contains(startName)) {
                    listViewItems.add(startName);
                }
                if (!listViewItems.contains(endName)) {
                    listViewItems.add(endName);
                }
                if (!listViewItems.contains(featureName)) {
                    listViewItems.add(featureName);

                }


            }

            listViewItems = listViewItems.stream().sorted().collect(Collectors.toList());

            for (String item : listViewItems) {
                listView.getItems().add(item);
            }

            try {
                tabPane.getSelectionModel().select(taxonomyTab);

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


    }


    public void listViewItemClicked(MouseEvent event) {

        if (event.getClickCount() == 2) {
            tabPane.getSelectionModel().select(familyModelTab);
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            tree = artifactMap.get(selectedItem);
            buildTreeView();
        }

        compareButton.setDisable(listView.getSelectionModel().getSelectedItems().size() < 2);
        compareButton.setDisable(listView.getSelectionModel().getSelectedItems().size() == 1);
    }


    private void updateTableView() {
        Node node = treeView.getSelectionModel().getSelectedItem().getValue();

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

    private void buildTreeView() {

        FileTable.createString();

        treeView.setRoot(new TreeItem<Node>(tree.getRoot()));
        treeView.getRoot().setGraphic(new ImageView(FileTable.rootImage));
        treeView.getRoot().setExpanded(true);
        treeView.setShowRoot(true);

        createTreeView(tree.getRoot(), treeView.getRoot());
    }

    public void createTreeView(Node node, TreeItem<Node> parent) {
        for (Node n : node.getChildren()) {
            TreeItem<Node> ti = decorateNode(new TreeItem<Node>(n));
            parent.getChildren().add(ti);
            if (!n.isLeaf()) {
                createTreeView(n, ti);
            }
        }
    }

    public TreeItem<Node> decorateNode(TreeItem<Node> node) {
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
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (treeView.getSelectionModel().getSelectedIndices().size() == 1) {
                updateTableView();
            }

        });
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        compareButton.setDisable(true);
        showTreeButton.setDisable(true);


    }

    public void compare() {

        List<String> selectedFiles = listView.getSelectionModel().getSelectedItems();

        List<Tree> trees = new ArrayList<>();

        for (String file : selectedFiles) {
            trees.add(artifactMap.get(file).cloneTree());
        }

        tree = compareEngine.compareMerge(trees);
        buildTreeView();
    }

    public void showTree() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();

        tree = artifactMap.get(selectedItem);

        buildTreeView();

    }
}