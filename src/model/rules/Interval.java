package model.rules;

import java.io.Serializable;

public class Interval implements Serializable{
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
        return (startIndex+1) + "--" + (endIndex+1);
    }
}
