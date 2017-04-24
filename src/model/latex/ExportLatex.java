package model.latex;

import model.Proof;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ExportLatex {
    private static final HashMap<Character, String> unicodeToLaTeX;

    static {
        unicodeToLaTeX = new HashMap<Character, String>();
        unicodeToLaTeX.put('∧', "\\land ");
        unicodeToLaTeX.put('∨', "\\lor ");
        unicodeToLaTeX.put('¬', "\\neg ");
        unicodeToLaTeX.put('→', "\\to ");
        unicodeToLaTeX.put('∃', "\\exists ");
        unicodeToLaTeX.put('∀', "\\forall ");
        unicodeToLaTeX.put('⊥', "\\bot ");
    }

    public static void export(Proof proof, String file) throws IOException {
        String[] lines = proof.getProofString().split("\\r?\\n");
        String output = "% For use with https://www.ctan.org/pkg/logicproof\n";
        int maxDepth = -1;
        int curDepth = 1;
        for (String line : lines) {
            String[] info = line.split("::");
            int depth = Integer.parseInt(info[0]);
            if (maxDepth < depth) {
                maxDepth = depth;
            }
            while (curDepth < depth) {
                output += "\\begin{subproof}\n";
                ++curDepth;
            }
            while (curDepth > depth) {
                output = trimEnd(output);
                output += "\\end{subproof}\n";
                --curDepth;
            }
            output += replaceAllUnicode(info[1]);
            output += "&";
            output += replaceAllUnicode(info[2]).replaceAll(
                    "(i|e)_(\\{\\d\\})","\\$\\\\mathrm\\{$1\\}_$2\\$");
            output += "\\\\\n";

        }
        while (curDepth > 1) {
            output = trimEnd(output);
            output += "\\end{subproof}\n";
            --curDepth;
        }
        output = trimEnd(output);
        output = "\\begin{logicproof}{" + maxDepth + "}\n" + output + "\\end{logicproof}";
        Files.write(Paths.get(file), output.getBytes());
    }

    private static String replaceAllUnicode(String s) {
        String ret = "";
        for (int i = 0; i < s.length(); i++) {
            String next = unicodeToLaTeX.getOrDefault(s.charAt(i), null);
            if (next == null)
                ret += s.charAt(i);
            else
                ret += next;
        }
        return ret;
    }

    private static String trimEnd(String s) {
        if (s.substring(s.length() - 3, s.length()).equals("\\\\\n")) // Might need \r for cross platform
            return s.substring(0, s.length() - 3) + '\n';
        return s;

    }
}
