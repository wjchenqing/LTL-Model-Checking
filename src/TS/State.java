package TS;

public class State {
    private boolean is_initial = false;
    private final String name;

    public State(String string) {
        name = string;
    }
    public boolean equal(String s){
        return name.equals(s);
    }

    public void setIs_initial() {
        this.is_initial = true;
    }

    public boolean isIs_initial() {
        return is_initial;
    }

    public String getName() {
        return name;
    }

    public void setIs_initial(boolean is_initial) {
        this.is_initial = is_initial;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
