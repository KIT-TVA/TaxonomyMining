package main.java.core.model.interfaces;

import java.util.UUID;

public abstract class AbstractValue<Type> implements Value<Type> {
    private UUID uuid;
    private Type value;

    public AbstractValue(Type value) {
        setUUID(UUID.randomUUID());
        setValue(value);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Type getValue() {
        return this.value;
    }

    @Override
    public void setValue(Type value) {
        this.value = value;
    }
}
