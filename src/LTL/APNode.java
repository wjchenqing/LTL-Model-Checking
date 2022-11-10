package LTL;

import TS.Propositions;

import java.util.HashSet;
import java.util.Set;

public class APNode extends FormulaNode {
    public String name;

    public APNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Set<FormulaNode> getChildren() {
        return null;
    }

    @Override
    public Set<FormulaNode> getClosure() {
            Set<FormulaNode> ret = new HashSet<>();
            ret.add(this);
            ret.add(this.negation());
            return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof APNode) {
            return name.equals(((APNode) obj).name);
        } else if (obj instanceof Propositions) {
            return name.equals(((Propositions) obj).getName());
        } else return false;
    }
}