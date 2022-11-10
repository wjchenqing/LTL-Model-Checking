package LTL;

import java.util.HashSet;
import java.util.Set;

public class Elementary {
    private final Set<FormulaNode> elementarySet = new HashSet<>();

    public boolean add(FormulaNode formulaNode) {
        return elementarySet.add(formulaNode);
    }

    public boolean contains(FormulaNode formulaNode) {
        return elementarySet.contains(formulaNode);
    }

    public Set<FormulaNode> getElementarySet() {
        return elementarySet;
    }

    public boolean retainAll(Set<FormulaNode> formulaNodes) {
        return elementarySet.retainAll(formulaNodes);
    }

    public boolean isElementarySet(Set<FormulaNode> closure, boolean hasTrue) {
        if (hasTrue && !elementarySet.contains(new TrueNode())) {
            return false;
        }
        for (FormulaNode formulaNode : elementarySet) {
            if ((formulaNode instanceof BinaryOpNode) && ((BinaryOpNode) formulaNode).op == BinaryOpNode.Operator.conjunction) {
                if ((!elementarySet.contains(((BinaryOpNode) formulaNode).lhs)) || (!elementarySet.contains(((BinaryOpNode) formulaNode).rhs)))
                    return false;
            } else if ((formulaNode.negation() instanceof BinaryOpNode) && ((BinaryOpNode) formulaNode.negation()).op == BinaryOpNode.Operator.conjunction) {
                if (elementarySet.contains(((BinaryOpNode) formulaNode.negation()).lhs) && elementarySet.contains(((BinaryOpNode) formulaNode.negation()).rhs))
                    return false;
            } else if (elementarySet.contains(formulaNode.negation())) {
                return false;
            }
        }
        for (FormulaNode formulaNode : closure) {
            if ((formulaNode instanceof BinaryOpNode) && ((BinaryOpNode) formulaNode).op == BinaryOpNode.Operator.until) {
                if (elementarySet.contains(((BinaryOpNode) formulaNode).rhs)) {
                    if (!elementarySet.contains(formulaNode)) {
                        return false;
                    }
                } else if (elementarySet.contains(formulaNode)) {
                    if (!elementarySet.contains(((BinaryOpNode) formulaNode).lhs)) {
                        return false;
                    }
                }
            }
            if ((!elementarySet.contains(formulaNode)) && (!elementarySet.contains(formulaNode.negation()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Elementary) {
            return elementarySet.toString().equals(elementarySet.toString());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return elementarySet.toString().hashCode();
    }

    @Override
    public String toString() {
        return elementarySet.toString();
    }
}
