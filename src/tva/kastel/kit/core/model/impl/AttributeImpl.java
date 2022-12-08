package tva.kastel.kit.core.model.impl;

import java.util.List;

import tva.kastel.kit.core.model.interfaces.AbstractAttribute;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Value;

/**
 * Concrete implementation of the Attribute interface.
 *
 * @author Kamil Rosiak
 */
public class AttributeImpl extends AbstractAttribute {
    private static final long serialVersionUID = 4021250213480973744L;

    /**
     * Create an Attribute with only a key and no value
     */
    public AttributeImpl(String attrKey) {
        setAttributeKey(attrKey);
    }

    /**
     * Create an Attribute with a kay and corrosponding value
     */
    public AttributeImpl(String attrKey, Value attrValue) {
        this(attrKey);
        addAttributeValue(attrValue);
    }

    /**
     * Create an Attribute with a kay and corrosponding value
     */
    public AttributeImpl(String attrKey, List<Value> attrValues) {
        this(attrKey);
        getAttributeValues().addAll(attrValues);
    }

    public Attribute cloneAttribute() {
        Attribute attribute = new AttributeImpl(this.getAttributeKey());

        for (Value value : this.getAttributeValues()) {
            attribute.addAttributeValue(new StringValueImpl((String) value.getValue()));
        }

        return attribute;

    }
}
