package model.rules;

public class ReferenceParser {

    public static Interval parseIntervalReference(String refStr) {
        String[] refs = refStr.split("-");
        if (refs.length != 2) throw new NumberFormatException();
        Integer startNr = Integer.parseInt(refs[0].trim());
        Integer endNr = Integer.parseInt(refs[1].trim());
        return new Interval(startNr - 1, endNr - 1);//Converts to indexes instead of row numbers
    }

    public static Integer parseIntegerReference(String refStr) {
        return Integer.parseInt(refStr) - 1;
    }
}

