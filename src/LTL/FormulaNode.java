package LTL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

abstract public class FormulaNode {
    private Set<FormulaNode> closure;
    private Set<Elementary> elementaries;

    @Override
    abstract public String toString();

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FormulaNode)
            return toString().equals(obj.toString());
        else
            return false;
    }

    abstract public Set<FormulaNode> getChildren();

    public FormulaNode negation() {
        return new UnaryOpNode(UnaryOpNode.Operator.negation, this);
    }

    public Set<FormulaNode> getClosure() {
        if (closure == null) {
            Set<FormulaNode> ret = new HashSet<>();
            ret.add(this);
            ret.add(this.negation());
            for (FormulaNode child : getChildren()) {
                ret.addAll(child.getClosure());
            }
            closure = ret;
            return ret;
        } else return closure;
    }

    public Set<Elementary> computeElementarySets() {
        if (this.elementaries == null){
            Set<Elementary> elementarySets = new HashSet<>();

            boolean hasTrue = false;
            Set<FormulaNode> closure = getClosure();
            if (closure.contains(new TrueNode())) {
                hasTrue = true;
                closure.remove(new TrueNode());
                closure.remove((new TrueNode()).negation());
            }

            ArrayList<FormulaNode> list = new ArrayList<>(closure);
            int max = 1 << list.size();
            for (int i = 0; i < max; i++) {
                int mask = i;
                Elementary B = new Elementary();
                for (FormulaNode formulaNode : list) {
                    if (mask % 2 == 1) {
                        B.add(formulaNode);
                    }
                    mask /= 2;
                }
                if (hasTrue) {
                    B.add(new TrueNode());
                }
                if (B.isElementarySet(closure, hasTrue)) {
                    elementarySets.add(B);
                }
            }
            this.elementaries = elementarySets;
            return elementarySets;
        } else return elementaries;
    }

    public Set<FormulaNode> getAP() {
        Set<FormulaNode> ret = new HashSet<>();
        Set<FormulaNode> closure = getClosure();
        for (FormulaNode formulaNode: closure) {
            if (formulaNode instanceof APNode) {
                ret.add(formulaNode);
            }
        }
        return ret;
    }
}