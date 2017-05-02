package start;

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
        unicodeToLaTeX.put('₀', "0");
        unicodeToLaTeX.put('₁', "1");
        unicodeToLaTeX.put('₂', "2");
        unicodeToLaTeX.put('₃', "3");
        unicodeToLaTeX.put('₄', "4");
        unicodeToLaTeX.put('₅', "5");
        unicodeToLaTeX.put('₆', "6");
        unicodeToLaTeX.put('₇', "7");
        unicodeToLaTeX.put('₈', "8");
        unicodeToLaTeX.put('₈', "9");
    }

    public static void export(Proof proof, String file) throws IOException {
        String[] lines = proof.getProofString().split("\\r?\\n");
        String output = "% For use with https://www.ctan.org/pkg/logicproof\n";
        output += "% Note that ending a box in a box is not valid and will not give the desired results.\n";
        int maxDepth = -1;
        int curDepth = 1;
        for (String line : lines) {
            String[] info = line.split("::");
            int depth = Integer.parseInt(info[0]);
            if (maxDepth < depth) {
                maxDepth = depth;
            }
            if (curDepth == depth && Integer.parseInt(info[4])==1) { // Edge case same depth box after box
                // Need to remove backslashes and both end and start a new environment
                output = trimEnd(output);
                output += "\\end{subproof}\n";
                output += "\\begin{subproof}\n";

            } else if (curDepth < depth) { // Regular depth increase
                output += "\\begin{subproof}\n";
                ++curDepth;
            } else while (curDepth > depth) {
                output = trimEnd(output);
                output += "\\end{subproof}\n";
                --curDepth;
            }
            output += attachIndex(replaceAllUnicode(info[1])).replaceAll("null", "");
            // Everything before the &, is in a math environment.
            output += "&";
            // We surround the crucial part in $ in the rule and references.
            output += replaceAllUnicode(info[2]).replaceAll(
                    "(\\\\land |\\\\lor |\\\\to |\\\\forall |\\\\exists |\\\\bot |\\\\neg )(i|e)(_\\{(\\d)\\})?","\\$$1\\\\mathrm\\{$2\\}$3\\$").replaceAll("null", "");
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

    private static String attachIndex(String s) {
        return s.replaceAll("(\\d+)","_\\{$1\\}");
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
        if (s.endsWith("\\\\\n")) // Might need \r for cross platform
            return s.substring(0, s.length() - 3) + '\n';
        return s;

    }
}
