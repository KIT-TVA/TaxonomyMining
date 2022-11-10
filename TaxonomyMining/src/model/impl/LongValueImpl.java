package model.impl;

import model.interfaces.AbstractValue;
import model.interfaces.Value;

public class LongValueImpl extends AbstractValue<Long> {
	
	public LongValueImpl(long value) {
		super(value);		
	}

	@Override
	public boolean equals(Value<Long> val) {
		if(val == null) {
			return false;
		}		
		return this.getValue().equals(val.getValue());
	}

}
