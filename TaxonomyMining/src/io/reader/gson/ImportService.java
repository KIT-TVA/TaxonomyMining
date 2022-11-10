package io.reader.gson;

import model.interfaces.Tree;

/**
 * Interface to allow multiple implementation of ImportService.
 * Can be used to extend functionality with other import formats.
 *
 * @author Team 6
 *
 * @param <T> Expected Type of imported data.
 */
public interface ImportService<T> {
	Tree importTree(T object);
}
