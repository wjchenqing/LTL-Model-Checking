package BA;

import LTL.APSubset;
import LTL.FormulaNode;
import TS.Propositions;
import Util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NBA extends BA {
    private final Set<State> F = new HashSet<>();
    private final Map<Pair<State, Integer>, State> stateMap = new HashMap<>();
    Set<FormulaNode> APSet;

    public Set<State> getF() {
        return F;
    }

    public NBA(GNBA gnba) {
        APSet = gnba.APSet;
        int k = gnba.getF().size();
        //alphabet stay the same
        addAllAlphabet(gnba.getAlphabet());
        //States
        for(State gs: gnba.getStates()){
            for (int i = 0; i < k; i++) {
                Pair<State, Integer> pair = new Pair<>(gs, i);
                State state = new State("(" + gs + ", " + i +")");
                if (i == 0 && gs.isIs_initial()) {
                    state.setIs_initial();
                }
                stateMap.put(pair,state);
                getStates().add(state);
            }
        }
        //Accepting Condition
        for (State gs: gnba.getF().get(0)) {
            Pair<State, Integer> pair = new Pair<>(gs, 0);
            F.add(stateMap.get(pair));
        }
        //Delta
        for (State gs: gnba.getStates()) {
            Map<Symbol, Set<State>> deltaForQ = gnba.getDelta().get(gs);
            for (int j = 0; j< k; j++) {
                State start = stateMap.get(new Pair<>(gs, j));
                if (!gnba.getF().get(j).contains(gs)) {
                    for (Symbol symbol: deltaForQ.keySet()) {
                        for (State qPrime: deltaForQ.get(symbol)) {
                            addDelta(start, stateMap.get(new Pair<>(qPrime, j)), symbol);
                        }
                    }
                } else {
                    for (Symbol symbol: deltaForQ.keySet()) {
                        for (State qPrime: deltaForQ.get(symbol)) {
                            addDelta(start, stateMap.get(new Pair<>(qPrime, (j + 1) % k)), symbol);
                        }
                    }
                }
            }
        }
    }

    public Set<State> getTargets(State start, Set<Propositions> L) {
        Map<Symbol, Set<State>> deltaMap = getDelta().get(start);
        Symbol symbol = new Symbol(L);
        symbol.getApSubset().retainAll(APSet);
        Symbol tmp = symbol;
        for (Symbol s: getAlphabet()) {
            if (s.equals(symbol)) {
                tmp = s;
                break;
            }
        }
        return deltaMap.get(tmp);
    }
}
