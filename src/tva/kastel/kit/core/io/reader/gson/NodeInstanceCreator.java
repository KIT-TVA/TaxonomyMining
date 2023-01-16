package tva.kastel.kit.core.io.reader.gson;

import com.google.gson.InstanceCreator;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.AbstractNode;

import java.lang.reflect.Type;

public class NodeInstanceCreator implements InstanceCreator<AbstractNode> {
    /**
     * Creates a NodeImpl with an empty string.
     *
     * @param type Unused.
     */
    @Override
    public AbstractNode createInstance(Type type) {
        return new NodeImpl();
    }
}
