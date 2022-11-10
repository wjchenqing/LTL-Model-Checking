package LTL;

import java.util.HashSet;
import java.util.Set;

public class UnaryOpNode extends FormulaNode {
    public enum Operator {
        negation("!"), next("X"), eventually("F"), always("G");

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
    public FormulaNode body;

    public UnaryOpNode(Operator op, FormulaNode body) {
        this.op = op;
        this.body = body;
    }

    @Override
    public String toString() {
        return "(" + op.toString() + body.toString() + ")";
    }

    @Override
    public Set<FormulaNode> getChildren() {
        Set<FormulaNode> ret = new HashSet<>();
        ret.add(body);
        return ret;
    }


    @Override
    public FormulaNode negation() {
        if (op == Operator.negation) {
            return body;
        } else {
            return super.negation();
        }
    }
}