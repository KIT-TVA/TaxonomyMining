package io.reader.java.factory;

import java.util.HashSet;

import io.reader.java.AccessModifier;

public class JavaReaderUtil {
	
	
	public static boolean isAccsessModifier(String modifier) {
		HashSet<String> accesModifier = new HashSet<String>();
		for(AccessModifier mod : AccessModifier.values()) {
			accesModifier.add(mod.name());
		}
		return accesModifier.contains(modifier);
	}

}
