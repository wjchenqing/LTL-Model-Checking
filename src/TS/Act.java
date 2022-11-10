package TS;

public class Act {
    private final String name;

    public Act(String string) {
        name = string;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
