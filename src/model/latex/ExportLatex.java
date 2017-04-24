package model.latex;

import model.Proof;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportLatex {
    private Map<Char>
    public static void export (Proof proof, Path file) throws IOException {
        String[] lines = proof.getProofString().split("\\r?\\n");
        String output = "";
        int maxDepth = -1;
        int curDepth = 1;
        for (String line : lines) {
            System.out.println(line);
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
                output += "\\end{subproof}\n";
                --curDepth;
            }
            output += replaceAllUnicode(info[1]);
            output += "&";
            output += replaceAllUnicode(info[2]);
            output += "\n";

        }
        while (curDepth > 1) {
            output += "\\end{subproof}\n";
            --curDepth;
        }
        output = "\\begin{logicproof}{" +maxDepth + "}\n" + output + "\\end{logicproof}";
        Files.write(file, output.getBytes());
    }

    private static String replaceAllUnicode(String s) {
        return s;
    }
}
