package LTL;

import TS.Propositions;

import java.util.HashSet;
import java.util.Set;

public class APSubset {
    private final Set<FormulaNode> APSubset = new HashSet<>();

    public APSubset(Set<FormulaNode> formulaNodes) {
        APSubset.addAll(formulaNodes);
    }

    public boolean add(FormulaNode formulaNode) {
        return APSubset.add(formulaNode);
    }

    public boolean contains(FormulaNode formulaNode) {
        return APSubset.contains(formulaNode);
    }


    public boolean retainAll(Set<FormulaNode> formulaNodes) {
        return APSubset.retainAll(formulaNodes);
    }

    public Set<FormulaNode> getAPSubset() {
        return APSubset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof APSubset) {
            for (FormulaNode formulaNode: ((APSubset) obj).APSubset) {
                if (!APSubset.contains(formulaNode))
                    return false;
            }
            for (FormulaNode formulaNode: APSubset) {
                if (!((APSubset) obj).APSubset.contains(formulaNode))
                    return false;
            }
            return true;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return APSubset.toString().hashCode();
    }

    @Override
    public String toString() {
        return APSubset.toString();
    }
}
