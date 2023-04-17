# Taxonomy Mining

Taxonomy Mining is a framework prototype to analyze solution space variability in Software Product Lines (SPLs).
At the moment, SPL implementations in Java, C++ and Python are supported.

## Setup guide

To run Taxonomy Mining, the following tools and packages are required:

- [SrcML](https://www.srcml.org/) 1.0.0
- [graphviz](https://graphviz.org/) 8.0.3
- [Python](https://www.python.org/) 3.11 with
    - scikit-learn (1.1.3)
    - numpy (1.23.4)
    - jep (4.1.0)
    - graphviz (0.20.1)
    - ast2json (0.3)
- [OpenJDK](https://openjdk.org/) 19
- [JavaFX](https://openjfx.io/) 19
- [Maven](https://maven.apache.org/) 3.8.8

In the project folder, run

```
mvn package
```

to build and package the application. To run the application, use the following command with the correct paths to JavaFX
and jep:

```
java -p /opt/javafx-sdk-19.0.2.1/lib --add-modules=javafx.controls,javafx.fxml -Djava.library.path=/usr/local/lib/python3.11/dist-packages/jep -jar TaxonomyMining.jar
```

## Example usage

In the folder *example*, we provide three exemplary SPLs that we also used for evaluation. To generate the taxonomy
graph, the SPLs must be copied to the folder *mining_data/input* located in the user directory. Then, restart the
application, select all variant folders of a particular SPL (e.g. Stack/Stack_Logging) and click on *Taxonomize*. The
taxonomy graph is then visualized in the *Taxonomy* tab pane. Note that the matching of refinements and feature presence
conditions is not automated yet. To explore the variant ASTs and refinements, double-click the items in the *output*
folder with the *.tree* extension.


