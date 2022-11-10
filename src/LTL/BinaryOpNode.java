package LTL;

import java.util.HashSet;
import java.util.Set;

public class BinaryOpNode extends FormulaNode {
    public enum Operator {
        conjunction("/\\"), disjunction("\\/"), until("U"), implication("->");

        private String name;

        Operator(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public Operator op;
    public FormulaNode lhs;
    public FormulaNode rhs;

    public BinaryOpNode(Operator op, FormulaNode lhs, FormulaNode rhs) {
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return "(" + lhs.toString() + " " + op.toString() + " " + rhs.toString() + ")";
    }

    @Override
    public Set<FormulaNode> getChildren() {
        Set<FormulaNode> ret = new HashSet<>();
        ret.add(lhs);
        ret.add(rhs);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOpNode && (((BinaryOpNode) obj).op == Operator.conjunction || ((BinaryOpNode) obj).op == Operator.disjunction)) {
                return (this.op == ((BinaryOpNode) obj).op)
                        && ((this.lhs.equals(((BinaryOpNode) obj).lhs) && this.rhs.equals(((BinaryOpNode) obj).rhs))
                        || (this.lhs.equals(((BinaryOpNode) obj).rhs) && this.rhs.equals(((BinaryOpNode) obj).lhs)));
        } else if (obj instanceof FormulaNode) {
            return toString().equals(obj.toString());
        } else
            return false;
    }
}