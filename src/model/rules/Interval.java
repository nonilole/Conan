package model.rules;

public class Interval {
    public int startIndex;//inclusive
    public int endIndex;//inclusive

    public Interval(int start, int end) {
        startIndex = start;
        endIndex = end;
    }

    public int size() {
        return endIndex - startIndex + 1;
    }

    @Override
    public String toString() {
        return startIndex + "-" + endIndex;
    }
}
