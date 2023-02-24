package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.List;


/**
 * This class iterates over a Tree to a given root.
 * For every Child the tree will be adjust whether a condition is met or not.
 * Adjustments are renames of NodeTypes, deleting the Node or a small restructure to specific parts of the tree.
 * Goal of the adjustments is to make this tree more similar to a tree made of java source code.
 * Every subclass adjust the Tree according to specific parts of code.
 *
 * @author David Bumm
 */
public abstract class TreeAdjuster {

    /**
     * This method iterates over a tree to a given root.
     * For each root the abstract adjust-method is called to adjust this root.
     *
     * @param node is the rootNode of the tree
     */
    public void recursiveAdjust(Node node) {
        if (node == null) {
            return;
        }

        adjust(node, node.getParent(), node.getNodeType());

        if (node.getNumberOfChildren() > 0) {
            List<Node> children = new ArrayList<Node>(node.getChildren());
            for (Node child : children) {
                recursiveAdjust(child);
            }
        }
    }

    /**
     * This method adjusts a Node to make this tree more similar to a tree made of java source code.
     *
     * @param node     is the current node
     * @param parent   is the parent of the current node
     * @param nodeType is the NodeType of the current node
     */
    protected abstract void adjust(Node node, Node parent, String nodeType);

    /**
     * This method returns the is the parent of the current node
     * Null is returned if no Child with the given nodeType exists.
     *
     * @param parent
     * @param name   the nodeType of the Child
     * @return first Child found with the nodeType or null.
     */
    protected Node getChild(Node parent, String name) {
        List<Node> children = parent.getChildren();
        for (int j = 0; j < children.size(); j++) {
            if (children.get(j).getNodeType().equals(name)) {
                return children.get(j);
            }
        }
        return null;
    }

    /**
     * This method translates a given operator to its String representation.
     * If the operator couldn't be matched an empty String is returned.
     *
     * @param operator to be translated
     * @return String representation of the operator
     */
    protected String getOperatorString(String operator) {
        switch (operator) {
            case Const.LESS_OP:
                return Const.LESS;
            case Const.GREATER_OP:
                return Const.GREATER;
            case Const.GREATER_EQUALS_OP:
                return Const.GREATER_EQUALS;
            case Const.LESS_EQUALS_OP:
                return Const.LESS_EQUALS;
            case Const.DIVIDE_OP:
                return Const.DIVIDE;
            case Const.PLUS_OP:
                return Const.PLUS;
            case Const.MINUS_OP:
                return Const.MINUS;
            case Const.MULTIPLY_OP:
                return Const.MULTIPLY;
            default:
                return operator;
        }
    }

    /**
     * This method translates a given unary operator to their String representation and
     * adds sets the attribute value.
     *
     * @param index which decides if its a pre- or postfix-expression
     * @param node  which gets the new attribute values
     */
    protected void changeOperator(int index, Node node) {
        String operator = node.getAttributes().get(0).getAttributeValues().get(0).getValue().toString();
        String value = Const.EMPTY;
        if (index == 1) {
            if (operator.equals(Const.PLUS_PLUS)) {
                value = Const.POSTFIX_INCREMENT;
            } else if (operator.equals(Const.MINUS_MINUS)) {
                value = Const.POSTFIX_DECREMENT;
            }
        } else if (index == 0) {
            if (operator.equals(Const.PLUS_PLUS)) {
                value = Const.PREFIX_INCREMENT;
            } else if (operator.equals(Const.MINUS_MINUS)) {
                value = Const.PREFIX_DECREMENT;
            }
        }
        node.getAttributes().get(0).getAttributeValues().set(0, new StringValueImpl(value));
        node.getAttributes().get(0).setAttributeKey(Const.OPERATOR_BIG);
    }

    /**
     * this method renames a Node to their literal equivalent (int, double, ..)
     * @param node the node that will be adjusted
     * @param value the value of the literal (e.g integer value, double value, ..)
     */
    protected void adjustLiteralNode(Node node, String value) {
        if (value.matches(Const.REGEX_INT)) {
            node.addAttribute(new AttributeImpl(Const.TYPE_BIG, new StringValueImpl(Const.INT)));
            node.setNodeType(Const.INT_LIT);
        } else if (value.equals(Const.TRUE) || value.equals(Const.FALSE)) {
            node.addAttribute(new AttributeImpl(Const.TYPE_BIG, new StringValueImpl(Const.BOOLEAN)));
            node.setNodeType(Const.BOOLEAN_LIT);
        } else if (value.matches(Const.REGEX_DOUBLE)) {
            node.addAttribute(new AttributeImpl(Const.TYPE_BIG, new StringValueImpl(Const.DOUBLE)));
            node.setNodeType(Const.DOUBLE_LIT);
        } else if (value.matches(Const.REGEX_FLOAT)) {
            node.addAttribute(new AttributeImpl(Const.TYPE_BIG, new StringValueImpl(Const.FLOAT)));
            node.setNodeType(Const.FLOAT_LIT);
        } else {
            node.addAttribute(new AttributeImpl(Const.TYPE_BIG, new StringValueImpl(Const.STRING)));
            node.setNodeType(Const.STRING_LIT);
        }
        node.getAttributes().get(0).setAttributeKey(Const.VALUE);
    }


    protected String getOperatorFromNodeType(String nodeType) {
        return switch (nodeType) {
            case "Add", "UAdd" -> Const.PLUS;
            case "Div" -> Const.DIVIDE;
            case "Mult" -> Const.MULTIPLY;
            case "Sub", "USub" -> Const.MINUS;
            case "Eq" -> Const.EQUALS;
            case "NotEq" -> Const.NOT_EQUALS;
            case "Gt" -> Const.GREATER;
            case "GtE" -> Const.GREATER_EQUALS;
            case "Lt" -> Const.LESS;
            case "LtE" -> Const.LESS_EQUALS;
            case "Not" -> Const.LOGICAL_COMP;
            case "Invert" -> Const.BITWISE_COMP;
            default -> nodeType;
        };
    }

}
