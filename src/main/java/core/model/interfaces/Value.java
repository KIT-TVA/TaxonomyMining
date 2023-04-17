package main.java.core.model.interfaces;

import java.util.UUID;

/**
 * This interface represents a single value of a attribute
 */
public interface Value<Type> {
    /**
     * returns the uuid of this value.
     */
    public UUID getUUID();

    /**
     * returns the uuid of this value.
     */
    public void setUUID(UUID uuid);


    /**
     * returns the value of the specific type
     */
    public Type getValue();


    /**
     * sets the value of the specific type
     */
    public void setValue(Type value);


    /**
     * returns true if values are equals else false
     */
    public boolean equals(Value<Type> val);
}
