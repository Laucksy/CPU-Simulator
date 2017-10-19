import java.util.*;
import java.io.*;

public class Assembler {
    public static void main(String[] args) {
        Assembler assem = new Assembler();
        assem.assemble("../data/test.as");
    }

    public Assembler() {

    }

    public void assemble(String filename) {
        String prefix = filename.substring(0, filename.lastIndexOf("/") + 1);
        String name = filename.substring(filename.lastIndexOf("/") + 1, filename.length() - 3);
        System.out.println(prefix + "," + name);

        ArrayList<String[]> tokens = tokenize(filename);
        for(String[] t : tokens) System.out.println(Arrays.asList(t));
    }

    public ArrayList<String[]> tokenize(String filename) {
        ArrayList<String[]> tokens = new ArrayList<String[]>();
        try {
            Scanner reader = new Scanner(new File(filename));

            while (reader.hasNextLine()) {
                String line = (reader.nextLine().split(";")[0]);

                tokens.add(tokenizeLine(line));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return tokens;
    }

    private String[] tokenizeLine(String line) {
        String[] tokens;

        if (line.equals("")) {
            tokens = new String[]{};
        } else if (line.contains(":") && line.contains(".")) {
            String label = line.split(" ")[0];
            line = line.substring(line.indexOf(":") + 1).trim();

            String[] split = line.split(" ");

            tokens = new String[split.length + 1];
            tokens[0] = label;
            for(int i = 0; i < split.length; i++) tokens[i+1] = split[i];
        } else if (line.contains(":")) {
            String label = line.split(" ")[0];
            line = line.substring(line.indexOf(":") + 1).trim();
            String opcode = line.split(" ")[0];
            line = line.substring(line.indexOf(" ") + 1).trim();

            String[] split = line.split(",");

            tokens = new String[split.length + 2];
            tokens[0] = label;
            tokens[1] = opcode;
            for(int i = 0; i < split.length; i++) tokens[i+2] = split[i];
        } else if (line.contains(".")) {
            tokens = line.split(" ");
        } else {
            String opcode = line.split(" ")[0];
            line = line.substring(line.indexOf(" ") + 1).trim();

            String[] split = line.split(",");

            tokens = new String[split.length + 1];
            tokens[0] = opcode;
            for(int i = 0; i < split.length; i++) tokens[i+1] = split[i];
        }

        List<String> list = new ArrayList<String>(Arrays.asList(tokens));
        list.removeAll(Arrays.asList(""));
        tokens = new String[list.size()];
        tokens = list.toArray(tokens);
        return tokens;
    }
}