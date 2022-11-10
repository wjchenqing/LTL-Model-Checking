package TS;

import LTL.APNode;

public class Propositions {
    private String name;

    public Propositions(String string) {
        name = string;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof APNode) {
            return name.equals(((APNode) obj).name);
        } else if (obj instanceof Propositions) {
            return name.equals(((Propositions) obj).getName());
        } else return false;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
