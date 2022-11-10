package io.writer.gson;

import model.interfaces.Tree;

/**
 * Interface to allow multiple implementation of ExportServices.
 * Can be used to extend functionality with other export formats.
 *
 * @author Team 6
 *
 * @param <T> Expected Type of export format.
 */
public interface ExportService<T> {
	T exportTree(Tree tree);
}
