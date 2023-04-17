package main.java.core.model.impl;

import main.java.core.model.interfaces.AbstractValue;
import main.java.core.model.interfaces.Value;

public class IntegerValueImpl extends AbstractValue<Integer> {

    public IntegerValueImpl(int value) {
        super(value);
    }

    @Override
    public boolean equals(Value<Integer> val) {
        if (val == null) {
            return false;
        }
        return this.getValue().equals(val.getValue());
    }

}
