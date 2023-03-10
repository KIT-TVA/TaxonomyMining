package tva.kastel.kit.ui;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import org.apache.commons.lang3.time.StopWatch;
import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import tva.kastel.kit.core.io.reader.ReaderConfiguration;
import tva.kastel.kit.core.io.reader.ReaderManager;
import tva.kastel.kit.core.io.reader.ReaderTypes;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.io.writer.taxonomy.TaxonomyWriter;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.core.model.interfaces.Value;
import tva.kastel.kit.taxonomy.mining.TaxonomyMiner;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.taxonomy.util.DirectoryTable;
import tva.kastel.kit.ui.util.FileTable;
import tva.kastel.kit.ui.util.FileWrapper;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Controller implements Initializable {
    @FXML
    public Button taxonomizeButton;
    @FXML
    public Button identifyButton;
    @FXML
    private TreeView<FileWrapper> explorerTree;

    @FXML
    private TextArea consoleTextArea;

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

    private final ReaderManager readerManager;

    private final TaxonomyWriter taxonomyWriter;
    private final CompareEngineHierarchical compareEngine;

    private final TaxonomyMiner taxonomyMiner;

    private Taxonomy taxonomy;


    public Controller() {
        readerManager = new ReaderManager(new ReaderConfiguration(ReaderTypes.CPP, ReaderTypes.JAVA, ReaderTypes.PY));
        compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""));
        taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());
        taxonomyMiner = new TaxonomyMiner();
    }


    public void createExplorerTree() {

        File rootFile = DirectoryTable.baseDirectory;
        TreeItem<FileWrapper> rootItem = new TreeItem<>(new FileWrapper(rootFile));
        explorerTree.setRoot(rootItem);
        rootItem.setExpanded(true);
        addFileToBrowserRecursively(rootItem);


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
                    showTree();
                }
            }
        });

        explorerTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            compareButton.setDisable(explorerTree.getSelectionModel().getSelectedIndices().size() < 2);
            taxonomizeButton.setDisable(explorerTree.getSelectionModel().getSelectedIndices().size() < 2);
            identifyButton.setDisable(explorerTree.getSelectionModel().getSelectedIndices().size() != 1 || taxonomy == null);

        });

        createExplorerTree();
        explorerTree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        compareButton.setDisable(true);
        taxonomizeButton.setDisable(true);
        identifyButton.setDisable(true);


        ContextMenu contextMenu = new ContextMenu();
        MenuItem showTreeItem = new MenuItem("Show tree");
        showTreeItem.setGraphic(new ImageView(FileTable.FV_TREE_16));

        MenuItem openInExplorerItem = new MenuItem("Open in explorer");
        openInExplorerItem.setGraphic(new ImageView(FileTable.FV_EXPLORER_16));

        showTreeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showTree();
            }
        });

        openInExplorerItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (explorerTree.getSelectionModel().getSelectedItems().size() == 1) {
                    TreeItem<FileWrapper> selectedFile = explorerTree.getSelectionModel().getSelectedItem();
                    try {
                        Runtime.getRuntime().exec("explorer.exe /select," + selectedFile.getValue().getFile().getAbsolutePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }


            }
        });

        contextMenu.getItems().add(showTreeItem);
        contextMenu.getItems().add(openInExplorerItem);
        explorerTree.setContextMenu(contextMenu);


        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                consoleTextArea.appendText(String.valueOf((char) b));
                consoleTextArea.positionCaret(consoleTextArea.getText().length());
            }
        };

        System.setOut(new PrintStream(stream));


    }


    public void showTree() {

        if (explorerTree.getSelectionModel().getSelectedItems().size() == 1) {
            TreeItem<FileWrapper> selectedFile = explorerTree.getSelectionModel().getSelectedItem();
            Tree tree = readerManager.readFile(selectedFile.getValue().getFile());
            createFamilyModelTree(tree);
        }


    }

    public void identify() {
        Tree selectedItem = readerManager.readFile(explorerTree.getSelectionModel().getSelectedItem().getValue().getFile());
        taxonomyMiner.identify(taxonomy, selectedItem);
        drawTaxonomy();
    }


    public void createTaxonomy() {

        List<Tree> trees = getSelectedFiles();

        taxonomy = taxonomyMiner.mine(trees);

        TreeItem<FileWrapper> rootItem = explorerTree.getRoot();


        TreeItem<FileWrapper> taxonomyFolderItem = null;
        for (TreeItem<FileWrapper> item : rootItem.getChildren()) {
            if (item.getValue().getFile().equals(DirectoryTable.outputDirectory)) {
                taxonomyFolderItem = item;
            }
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

        drawTaxonomy();

    }

    private void drawTaxonomy() {
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

        List<File> files = new ArrayList<>();
        for (TreeItem<FileWrapper> item : selectedItems) {
            files.add(item.getValue().getFile());
        }
        Collections.sort(files);
        for (File file : files) {


            trees.add(readerManager.readFile(file));

        }
        return trees;
    }


    public void compare() {
        Tree tree = compareEngine.compareMerge(getSelectedFiles());
        createFamilyModelTree(tree);
    }


}