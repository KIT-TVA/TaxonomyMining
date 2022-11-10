package model.impl;

import model.interfaces.AbstractValue;
import model.interfaces.Value;

public class FloatValueImpl extends AbstractValue<Float> {

	public FloatValueImpl(float value) {
		super(value);
	}

	@Override
	public boolean equals(Value<Float> val) {
		if (val == null) {
			return false;
		}
		return this.getValue().equals(val.getValue());
	}

}
