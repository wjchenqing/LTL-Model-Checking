package Parser;

import LTL.*;

import java.util.HashMap;
import java.util.Stack;

public class Parser {
    public HashMap<String, APNode> ap_map = new HashMap<>();

    public FormulaNode ParseInput(String str) {
        return Parse(str.replaceAll("\\s", ""));
    }

    private FormulaNode Parse(String str) {
        str = PeelSpaceAndParen(str);

        FormulaNode ap_node = MatchAPNode(str);
        if (ap_node != null) {
            return ap_node;
        }

        FormulaNode binary_node = MatchBinaryOpNode(str);
        if (binary_node != null) {
            return binary_node;
        }

        FormulaNode unary_node = MatchUnaryOpNode(str);
        if (unary_node != null) {
            return unary_node;
        }

        assert false;
        return null;
    }

    private String PeelSpaceAndParen(String str) {
        Stack<Integer> idx_stack = new Stack<>();
        HashMap<Integer, Integer> idx_map = new HashMap<>();

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            assert ch != ' ';
            if (ch == '(') {
                idx_stack.push(i);
            } else if (ch == ')') {
                assert idx_stack.size() > 0;
                int left = idx_stack.pop();
                idx_map.put(left, i);
                idx_map.put(i, left);
            }
        }
        assert idx_stack.empty();

        int start = 0;
        while (idx_map.get(start) != null && idx_map.get(start) == str.length() - start - 1) {
            start++;
        }
        assert start < str.length() - start;
        return str.substring(start, str.length() - start);
    }

    private FormulaNode MatchAPNode(String str) {
        assert str.length() >= 1;
        if (str.length() != 1) {
            return null;
        }
        assert Character.isLowerCase(str.charAt(0));

        APNode node = ap_map.get(str);
        if (node == null) {
            node = new APNode(str);
            ap_map.put(str, node);
        }
        return node;
    }

    private FormulaNode MatchBinaryOpNode(String str) {
        int paren_cnt = 0;
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '(') {
                ++paren_cnt;
            } else if (ch == ')') {
                assert paren_cnt > 0;
                --paren_cnt;
            } else if (paren_cnt == 0) {
                if (ch == '/') {
                    assert i + 1 < str.length() - 1 && str.charAt(i + 1) == '\\';
                    FormulaNode lhs = Parse(str.substring(0, i));
                    FormulaNode rhs = Parse(str.substring(i + 2, str.length()));
                    assert lhs != null;
                    assert rhs != null;
                    return new BinaryOpNode(BinaryOpNode.Operator.conjunction, lhs, rhs);
                } else if (ch == '\\') {
                    assert i + 1 < str.length() - 1 && str.charAt(i + 1) == '/';
                    FormulaNode lhs = Parse(str.substring(0, i));
                    FormulaNode rhs = Parse(str.substring(i + 2, str.length()));
                    assert lhs != null;
                    assert rhs != null;
                    return (new BinaryOpNode(BinaryOpNode.Operator.conjunction, lhs.negation(), rhs.negation())).negation();
                } else if (ch == 'U') {
                    assert i < str.length() - 1;
                    FormulaNode lhs = Parse(str.substring(0, i));
                    FormulaNode rhs = Parse(str.substring(i + 1, str.length()));
                    assert lhs != null;
                    assert rhs != null;
                    return new BinaryOpNode(BinaryOpNode.Operator.until, lhs, rhs);
                } else if (ch == '-') {
                    assert i + 1 < str.length() - 1 && str.charAt(i + 1) == '>';
                    FormulaNode lhs = Parse(str.substring(0, i));
                    FormulaNode rhs = Parse(str.substring(i + 2, str.length()));
                    assert lhs != null;
                    assert rhs != null;
                    return (new BinaryOpNode(BinaryOpNode.Operator.conjunction, lhs, rhs.negation())).negation();
                }
            }
        }
        return null;
    }

    private FormulaNode MatchUnaryOpNode(String str) {
        if (str.charAt(0) == '!') {
            FormulaNode body = Parse(str.substring(1, str.length()));
            assert body != null;
            return body.negation();
        } else if (str.charAt(0) == 'X') {
            FormulaNode body = Parse(str.substring(1, str.length()));
            assert body != null;
            return new UnaryOpNode(UnaryOpNode.Operator.next, body);
        } else if (str.charAt(0) == 'F') {
            FormulaNode body = Parse(str.substring(1, str.length()));
            assert body != null;
            return new BinaryOpNode(BinaryOpNode.Operator.until, new TrueNode(), body);
        } else if (str.charAt(0) == 'G') {
            FormulaNode body = Parse(str.substring(1, str.length()));
            assert body != null;
            return (new BinaryOpNode(BinaryOpNode.Operator.until, new TrueNode(), body.negation())).negation();
        }
        return null;
    }
}