package main.java.core.io.writer.gson;

import main.java.core.model.interfaces.Tree;

/**
 * Interface to allow multiple implementation of ExportServices.
 * Can be used to extend functionality with other export formats.
 *
 * @param <T> Expected Type of export format.
 */
public interface ExportService<T> {
    T exportTree(Tree tree);
}
