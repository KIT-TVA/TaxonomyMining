package tva.kastel.kit.core.model.impl;

import tva.kastel.kit.core.model.interfaces.AbstractValue;
import tva.kastel.kit.core.model.interfaces.Value;

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
