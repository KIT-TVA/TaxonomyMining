package main.java.core.io.reader.gson;

import com.google.gson.InstanceCreator;
import main.java.core.model.interfaces.AbstractNode;
import main.java.core.model.impl.NodeImpl;

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
