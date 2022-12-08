package tva.kastel.kit.core.model.impl;

import tva.kastel.kit.core.model.interfaces.AbstractValue;
import tva.kastel.kit.core.model.interfaces.Value;

public class FloatValueImpl extends AbstractValue<Double> {

    public FloatValueImpl(double value) {
        super(value);
    }

    @Override
    public boolean equals(Value<Double> val) {
        if (val == null) {
            return false;
        }
        return this.getValue().equals(val.getValue());
    }

}
