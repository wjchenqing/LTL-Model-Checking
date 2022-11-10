package BA;

import TS.Act;

import java.util.*;

abstract public class BA {
    private final ArrayList<State> states = new ArrayList<>();
    private final ArrayList<Symbol> alphabet = new ArrayList<>();
    private final Map<State, Map<Symbol, Set<State>>> delta = new HashMap<>();
    private boolean madeNonBlocking = false;

//    public State makeNonBlocking() {
//        if (madeNonBlocking) {
//            return null;
//        }
//        State trap = new State();
//        states.add(trap);
//        Set<State> tmpSet = new HashSet<>();
//        Map<Symbol, Set<State>> tmpMap = new HashMap<>();
//        tmpMap.put(new Symbol(), tmpSet);
//        delta.put(trap, tmpMap);
//        for (State state: states) {
//            for (Symbol symbol: alphabet) {
//                if (delta.get(state).get(symbol) == delta.get(state).)
//            }
//        }
//        return trap;
//    }

    public boolean addState(State state) {
        return states.add(state);
    }

    public boolean addSymbol(Symbol symbol) {
        return alphabet.add(symbol);
    }

    public void addDelta(State start, Map<Symbol, Set<State>> map) {
        if (delta.containsKey(start)) {
            assert false;
        } else {
            delta.put(start, map);
        }
    }

    public ArrayList<Symbol> getAlphabet() {
        return alphabet;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public Map<State, Map<Symbol, Set<State>>> getDelta() {
        return delta;
    }

    public boolean addAllAlphabet(ArrayList<Symbol> alpha) {
        return alphabet.addAll(alpha);
    }

    public void addDelta(State start, State end, Symbol symbol) {
        if (delta.containsKey(start)) {
            Map<Symbol, Set<State>> map = delta.get(start);
            if (map.containsKey(symbol)) {
                map.get(symbol).add(end);
            } else {
                Set<State> stateList = new HashSet<>();
                stateList.add(end);
                map.put(symbol, stateList);
            }
        } else {
            Set<State> stateList = new HashSet<>();
            stateList.add(end);
            Map<Symbol, Set<State>> map = new HashMap<>();
            map.put(symbol, stateList);
            delta.put(start, map);
        }
    }
}
