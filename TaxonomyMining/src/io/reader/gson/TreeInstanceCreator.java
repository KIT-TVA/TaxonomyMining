package io.reader.gson;

import com.google.gson.InstanceCreator;

import model.impl.TreeImpl;
import model.interfaces.Tree;

import java.lang.reflect.Type;

/**
 * Factory to create instances implementing the Tree interface.
 */
public class TreeInstanceCreator implements InstanceCreator<Tree> {
	/**
	 * Creates a TreeImpl with an empty string.
	 *
	 * @param type Unused.
	 */
	@Override
	public Tree createInstance(Type type) {
		return new TreeImpl("");
	}
}
