import Instructions.*;
import java.util.*;
import java.io.*;

public class Assembler {
    private PrintWriter writer;
    private int bytesWritten;
    private int align;

    public static void main(String[] args) {
        Assembler assem = new Assembler();
        assem.assemble("../data/test.as", false);
    }

    public Assembler() {
        this.writer = null;
        this.bytesWritten = 0;
        this.align = 1;
    }

    public void assemble(String filename, boolean hex) {
        String prefix = filename.substring(0, filename.lastIndexOf("/") + 1);
        String name = filename.substring(filename.lastIndexOf("/") + 1, filename.length() - 3);
        String file = prefix + name + ".o";

        ArrayList<String[]> tokens = tokenize(filename);
        String format = hex ? "#hex" : "#binary";
        String wordsize = "16";
        String regcnt = "4";
        String maxmem = "0x10000000";
        ArrayList<String[]> labels = new ArrayList<String[]>();

        for(int i = 0; i < tokens.size(); i++) {
            String[] line = tokens.get(i);
            System.out.println(Arrays.asList(line));

            if (line.length == 0) {}
            else if (i <= 2) {
                if (line[0].equals(".wordsize")) wordsize = line[1];
                else if (line[0].equals(".regcnt")) regcnt = line[1];
                else if (line[0].equals(".maxmem")) maxmem = line[1];

                if (i == 2) write(file, format + ":WS-" + wordsize + ":RC-" + regcnt + ":MM-" + maxmem);
            } else if (line[0].charAt(0) == '.') {
                if (line[0].equals(".pos")) {
                    //int newPos = Integer.parseInt(line[1].substring(2), 16);
                    //while (this.bytesWritten < newPos) write(file, "00000000");
                } else if (line[0].equals(".align")) {
                    this.align = Integer.parseInt(line[1]);
                } else if (line[0].equals(".double")) {

                }
            } else if (line[0].charAt(line[0].length() - 1) == ':') {
                int bytesPerLabel = (maxmem.length() - 2) / 2;
                labels.add(new String[] {line[0], (new Integer(this.bytesWritten)).toString()});
            } else {
                Instruction instruction = extract(line);
                if (instruction != null) {
                    System.out.println("TRANSLATION " + instruction.getPlain() + "     " + instruction.getBinary());
                    write(file, hex ? instruction.getHex() : instruction.getBinary());
                }
            }
        }
        if (this.writer != null) this.writer.close();
    }

    private Instruction extract(String[] line) {
        Instruction instruction = null;
        String type = InstructionList.getTypeFromMnemonic(line[0]);
        String opcode = InstructionList.getOpcodeFromMnemonic(line[0]);
        if (type.equals("R")) {
            instruction = new InstructionTypeR(line[0], opcode, line[1], line[2], line[3], "0");
        } else if (type.equals("I")) {
            instruction = new InstructionTypeI(line[0], opcode, line[1], line[2], line[3]);
        } else if (type.equals("M")) {
            instruction = new InstructionTypeM(line[0], opcode, line[1], line[2], line[3]);
        } else if (type.equals("B")) {
            instruction = new InstructionTypeB(line[0], opcode, line[1]);
        } else if (type.equals("O")) {
            instruction = new InstructionTypeO(line[0], opcode, line[1]);
        }

        return instruction;
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
        line = line.trim();
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
        } else if (line.contains("HALT")) {
            tokens = new String[] {"HALT", "0"};
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
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = list.get(i).trim();
        }
        return tokens;
    }

    private void write(String file, String line) {
        if (line.equals("")) return;
        if (line.charAt(0) != '#' && line.length() % 8 != 0) return;

        if (this.writer == null) {
            try {
                this.writer = new PrintWriter(new FileWriter(file));
            } catch(Exception e) {
                System.out.println(e);
                return;
            }
        }

        if (line.charAt(0) == '#') {
            this.writer.println(line);
        } else {
            this.bytesWritten += line.length() / 8;
            this.writer.print(line);
            if (bytesWritten % 4 == 0) this.writer.println();
        }
    }
}