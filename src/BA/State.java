package BA;

import LTL.Elementary;

public class State {
    private boolean is_initial = false;
    private final String name;

    public State(String string) {
        name = string;
    }

    public String getName() {
        return name;
    }

    public void setIs_initial() {
        this.is_initial = true;
    }

    public boolean isIs_initial() {
        return is_initial;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
