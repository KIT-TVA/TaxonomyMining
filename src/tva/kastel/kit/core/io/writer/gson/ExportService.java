package tva.kastel.kit.core.io.writer.gson;

import tva.kastel.kit.core.model.interfaces.Tree;

/**
 * Interface to allow multiple implementation of ExportServices.
 * Can be used to extend functionality with other export formats.
 *
 * @param <T> Expected Type of export format.
 * @author Team 6
 */
public interface ExportService<T> {
    T exportTree(Tree tree);
}
