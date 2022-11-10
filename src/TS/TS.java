package TS;

import BA.NBA;
import Util.Pair;

import java.io.InputStream;
import java.util.*;

public class TS {
    private final ArrayList<State> states = new ArrayList<>();
    private final ArrayList<Act> acts = new ArrayList<>();
    private final Map<State, Map<State,Set<Act>>> transitions = new HashMap<>();
    private final ArrayList<Propositions> propositions = new ArrayList<>();
    private final Map<State, Set<Propositions>> L = new HashMap<>();

    private Integer NS, NT;

    private final Set<State> acceptingStates = new HashSet<>();

    public TS(TS ts, NBA nba) { //Construct by Product
        Map<Pair<State, BA.State>, State> stateMap = new HashMap<>();

        //AP
        Map<BA.State, Propositions> propositionsMap = new HashMap<>();
        for (BA.State q: nba.getStates()) {
            Propositions prop = new Propositions(q.getName() + "_AP'");
            propositionsMap.put(q, prop);
            propositions.add(prop);
        }

        //States and initial states and L
        for (State s: ts.states) {
            for (BA.State q: nba.getStates()) {
                State state = new State("<" + s + ", " + q + ">");
                states.add(state);
                stateMap.put(new Pair<>(s, q), state);
                Set<Propositions> l = new HashSet<>();
                l.add(propositionsMap.get(q));
                L.put(state, l);
                if (s.isIs_initial()) {
                    for (BA.State q0: nba.getStates()) {
                        if (q0.isIs_initial()) {
                            Set<BA.State> targ = nba.getTargets(q0, ts.L.get(s));
                            if (targ != null && targ.contains(q)){
                                state.setIs_initial();
                                break;
                            }
                        }
                    }
                }
                if (nba.getF().contains(q)) {
                    acceptingStates.add(state);
                }
            }
        }
        //Acts
        acts.addAll(ts.acts);
        //Transition
        for (State s: ts.states) {
            Map<State, Set<Act>> targetMap = ts.transitions.get(s);
            if (targetMap != null) {
                for (State t : targetMap.keySet()) {
                    Set<Act> acts = targetMap.get(t);
                    for (BA.State q : nba.getStates()) {
                        State start = stateMap.get(new Pair<>(s, q));
                        Set<BA.State> targets = nba.getTargets(q, ts.L.get(t));
                        if (targets != null) {
                            for (BA.State p : targets) {
                                State end = stateMap.get(new Pair<>(t, p));
                                for (Act act : acts) {
                                    addTransition(start, end, act);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public TS(Scanner scanner) {
        NS = scanner.nextInt();
        for (int i = 0; i < NS; i++) {
            states.add(new State("s"+i));
        }

        NT = scanner.nextInt();

        String initialStates = scanner.nextLine();
        initialStates = scanner.nextLine();
        String[] initialNum = initialStates.split(" ");
        for (String s : initialNum) {
            int num = Integer.parseInt(s);
            states.get(num).setIs_initial();
        }

        String actList = scanner.nextLine();
        String[] actArray = actList.split(" ");
        for (String s : actArray) {
            acts.add(new Act("alpha" + s));
        }

        String propositionList = scanner.nextLine();
        String[] propositionArray = propositionList.split(" ");
        for (String s : propositionArray) {
            propositions.add(new Propositions(s));
        }

        for (int i = 0; i < NT; i++) {
            int start = scanner.nextInt();
            int act = scanner.nextInt();
            int end = scanner.nextInt();
            this.addTransition(states.get(start), states.get(end), acts.get(act));
        }

        scanner.nextLine();

        for (int i = 0; i < NS; i++) {
            String prop = scanner.nextLine();
            String[] props = prop.split(" ");
            Set<Propositions> propositionSet = new HashSet<>();
            for (String s: props) {
                propositionSet.add(propositions.get(Integer.parseInt(s)));
            }
            L.put(states.get(i), propositionSet);
        }
    }

    private void addTransition(State start, State end, Act act) {
        if (transitions.containsKey(start)) {
            Map<State, Set<Act>> map = transitions.get(start);
            if (map.containsKey(end)) {
                map.get(end).add(act);
            } else {
                Set<Act> acts = new HashSet<>();
                acts.add(act);
                map.put(end,acts);
            }
        } else {
            Set<Act> acts = new HashSet<>();
            acts.add(act);
            Map<State, Set<Act>> transition = new HashMap<>();
            transition.put(end, acts);
            transitions.put(start, transition);
        }
    }

    public ArrayList<State> getStates() {
        return states;
    }


    public boolean cycleCheck(State s) {
        boolean cycleFound = false;
        V.push(s);
        T.add(s);
        do {
            State s1 = V.peek();
            if (transitions.get(s1) != null && transitions.get(s1).containsKey(s)) {
                cycleFound = true;
                V.push(s);
            } else {
                if (transitions.get(s1) == null) {
                    V.pop();
                } else {
                    Set<State> post = new HashSet<>(transitions.get(s1).keySet());
                    post.removeAll(T);
                    if (!post.isEmpty()) {
                        for (State s2 : post) {
                            V.push(s2);
                            T.add(s2);
                            break;
                        }
                    } else {
                        V.pop();
                    }
                }
            }
        } while (!(V.empty() || cycleFound));
        return cycleFound;
    }

    Set<State> R, T;
    Stack<State> U,V;
    boolean cycleFound;

    public int PersistenceChecking() {
        R = new HashSet<>();
        T = new HashSet<>();
        U = new Stack<>();
        V = new Stack<>();
        cycleFound = false;

        Set<State> initial_set = new HashSet<>();
        for (State s: states) {
            if (s.isIs_initial()) initial_set.add(s);
        }
        Set<State> delta = new HashSet<>(initial_set);
        delta.removeAll(R);
        while ((!delta.isEmpty()) && (!cycleFound)) {
            for (State s: delta){
                reachableCycle(s);
                break;
            }
            delta.removeAll(R);
        }
        if (!cycleFound) {
            return 1;
        } else {
            return 0;
        }
    }

    public void reachableCycle(State s) {
        U.push(s);
        R.add(s);

        do {
            State s1 = U.peek();
            if (transitions.get(s1) == null) {
                U.pop();
                if (acceptingStates.contains(s1)) {
                    cycleFound = cycleCheck(s1);
                }
            } else {
                Set<State> post = new HashSet<>(transitions.get(s1).keySet());
                post.removeAll(R);
                if (!post.isEmpty()) {
                    for (State s2 : post) {
                        U.push(s2);
                        R.add(s2);
                        break;
                    }
                } else {
                    U.pop();
                    if (acceptingStates.contains(s1)) {
                        cycleFound = cycleCheck(s1);
                    }
                }
            }
        } while (!(U.empty() || cycleFound));
    }


}
