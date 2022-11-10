package io.reader.gson;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

import model.impl.NodeImpl;
import model.interfaces.AbstractNode;

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
