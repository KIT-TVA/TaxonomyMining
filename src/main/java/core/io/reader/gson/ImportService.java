package main.java.core.io.reader.gson;

import main.java.core.model.interfaces.Tree;

/**
 * Interface to allow multiple implementation of ImportService.
 * Can be used to extend functionality with other import formats.
 *
 * @param <T> Expected Type of imported data.
 */
public interface ImportService<T> {
    Tree importTree(T object);
}
