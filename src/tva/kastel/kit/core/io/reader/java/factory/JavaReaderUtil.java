package tva.kastel.kit.core.io.reader.java.factory;

import tva.kastel.kit.core.io.reader.java.AccessModifier;

import java.util.HashSet;

public class JavaReaderUtil {


    public static boolean isAccsessModifier(String modifier) {
        HashSet<String> accesModifier = new HashSet<String>();
        for (AccessModifier mod : AccessModifier.values()) {
            accesModifier.add(mod.name());
        }
        return accesModifier.contains(modifier);
    }

}
