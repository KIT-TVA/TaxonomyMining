package tva.kastel.kit.core.io.reader.gson;

import tva.kastel.kit.core.model.interfaces.Tree;

/**
 * Interface to allow multiple implementation of ImportService.
 * Can be used to extend functionality with other import formats.
 *
 * @param <T> Expected Type of imported data.
 * @author Team 6
 */
public interface ImportService<T> {
    Tree importTree(T object);
}
