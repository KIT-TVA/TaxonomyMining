package tva.kastel.kit.core.model.impl;

import tva.kastel.kit.core.model.interfaces.AbstractValue;
import tva.kastel.kit.core.model.interfaces.Value;

public class LongValueImpl extends AbstractValue<Long> {

    public LongValueImpl(long value) {
        super(value);
    }

    @Override
    public boolean equals(Value<Long> val) {
        if (val == null) {
            return false;
        }
        return this.getValue().equals(val.getValue());
    }

}
