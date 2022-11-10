package LTL;

import java.util.HashSet;
import java.util.Set;

public class TrueNode extends FormulaNode {
    @Override
    public String toString() {
        return "true";
    }

    @Override
    public Set<FormulaNode> getChildren() {
        return new HashSet<>();
    }
}
