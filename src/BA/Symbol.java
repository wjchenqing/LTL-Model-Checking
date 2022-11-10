package BA;

import LTL.APNode;
import LTL.APSubset;
import LTL.FormulaNode;
import TS.Propositions;

import java.util.HashSet;
import java.util.Set;

public class Symbol {
    private final APSubset apSubset;

    public Symbol(Set<Propositions> ap) {
        Set<FormulaNode> apNodes = new HashSet<>();
        for (Propositions p: ap) {
            apNodes.add(new APNode(p.getName()));
        }
        apSubset = new APSubset(apNodes);
    }

    public Symbol(APSubset subset) {
        apSubset = subset;
    }

    public APSubset getApSubset() {
        return apSubset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            return apSubset.equals(((Symbol) obj).apSubset);
        } else return false;
    }

    @Override
    public String toString() {
        return apSubset.toString();
    }
}
