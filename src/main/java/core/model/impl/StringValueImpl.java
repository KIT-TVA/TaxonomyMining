package main.java.core.model.impl;

import main.java.core.model.interfaces.AbstractValue;
import main.java.core.model.interfaces.Value;

/**
 * Implementation of value
 */
public class StringValueImpl extends AbstractValue<String> {

    public StringValueImpl(String value) {
        super(value);
    }

    @Override
    public boolean equals(Value<String> val) {
        if (val instanceof StringValueImpl) {
            return val.getValue().equals(getValue());
        } else {
            return false;
        }
    }
}
