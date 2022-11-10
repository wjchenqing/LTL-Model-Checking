package BA;

import LTL.*;

import java.util.*;

public class GNBA extends BA {
    private final ArrayList<Set<State>> F = new ArrayList<>();
    private final Map<Elementary, State> stateMap = new HashMap<>();
    private final Map<APSubset, Symbol> symbolMap = new HashMap<>();
    Set<FormulaNode> APSet;

    public ArrayList<Set<State>> getF() {
        return F;
    }

    public GNBA(FormulaNode formulaNode) {
        Set<Elementary> elementarySets = formulaNode.computeElementarySets();
        Set<FormulaNode> closure = formulaNode.getClosure();
        APSet = formulaNode.getAP();
        //States Q
        int num = 0;
        for (Elementary elementary: elementarySets) {
            State state = new State("GS"+num+":"+elementary.toString());
            num ++;
            if (elementary.contains(formulaNode)) state.setIs_initial();
            addState(state);
            stateMap.put(elementary, state);
        }
        //Accepting Condition F
        for (FormulaNode formula: closure) {
            if ((formula instanceof BinaryOpNode) && (((BinaryOpNode) formula).op == BinaryOpNode.Operator.until)) {
                Set<State> F_formula = new HashSet<>();
                for (Elementary elementary: elementarySets) {
                    if (!(elementary.contains(formula) && (!elementary.contains(((BinaryOpNode) formula).rhs)))){
                        F_formula.add(stateMap.get(elementary));
                    }
                }
                F.add(F_formula);
            }
        }
        if (F.isEmpty()) {
            Set<State> F_formula = new HashSet();
            for (Elementary elementary: elementarySets) {
                F_formula.add(stateMap.get(elementary));
            }
            F.add(F_formula);
        }
        //Alphabet \Sigma
        int max = 1 << APSet.size();
        Set<APSubset> powerAP = new HashSet<>();
        for (int i = 0; i < max; i++) {
            int mask = i;
             APSubset A = new APSubset(new HashSet<>());
             for (FormulaNode ap: APSet) {
                 if (mask % 2 == 1) {
                     A.add(ap);
                 }
                 mask /= 2;
             }
             powerAP.add(A);
             Symbol symbol = new Symbol(A);
             symbolMap.put(A, symbol);
             addSymbol(symbol);
        }
        //delta
        for (Elementary B: elementarySets) {
            State start = stateMap.get(B);
            APSubset A = new APSubset(B.getElementarySet());
            A.retainAll(APSet);
            Symbol symbol = symbolMap.get(A);
            for (Elementary BPrime: elementarySets) {
                boolean flag = true;
                State end = stateMap.get(BPrime);
                for (FormulaNode f: closure) {
                    if ((f instanceof UnaryOpNode) && ((UnaryOpNode) f).op == UnaryOpNode.Operator.next) {
                        if ((B.contains(f) && !BPrime.contains(((UnaryOpNode) f).body)) || (BPrime.contains(((UnaryOpNode) f).body) && !B.contains(f))) {
                            flag = false;
                            break;
                        }
                    } else if ((f instanceof BinaryOpNode) && ((BinaryOpNode) f).op == BinaryOpNode.Operator.until) {
                        if ((B.contains(f) && !(B.contains(((BinaryOpNode) f).rhs) || (B.contains(((BinaryOpNode) f).lhs) && BPrime.contains(f))))
                                || (!B.contains(f) && (B.contains(((BinaryOpNode) f).rhs) || (B.contains(((BinaryOpNode) f).lhs) && BPrime.contains(f))))) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    addDelta(start, end, symbol);
                }
            }
        }

    }
}
