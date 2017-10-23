package Assembler;

import Instructions.*;
import java.util.*;
import java.io.*;

public class Assembler {
    private PrintWriter writer;
    private int bytesWritten;
    private int align;

    private int numR;
    private int numI;
    private int numM;
    private int numB;
    private int numO;

    public static void main(String[] args) {
        Assembler assem = new Assembler();
        assem.assemble(args[0], false);
    }

    /**
     * Initialize the instance variables of the Assembler
     */
    public Assembler() {
        this.writer = null;
        this.bytesWritten = 0;
        this.align = 1;

        this.numR = 0;
        this.numI = 0;
        this.numM = 0;
        this.numB = 0;
        this.numO = 0;
    }

    /**
     * Assemble the code into an object format
     * @param filename - file to write .o to
     * @param hex - true for output in hex, false for output in binary
     */
    public void assemble(String filename, boolean hex) {
        String prefix = filename.substring(0, filename.lastIndexOf("/") + 1);
        String name = filename.substring(filename.lastIndexOf("/") + 1, filename.length() - 3);
        String file = prefix + name + ".o";

        // Gets the configuration from first lines
        ArrayList<String[]> tokens = tokenize(filename);
        String format = hex ? "#hex" : "#binary";
        String wordsize = "16";
        String regcnt = "4";
        String maxmem = "0x10000000";
        ArrayList<String[]> labels = new ArrayList<String[]>();

        for(int i = 0; i < tokens.size(); i++) {
            String[] line = tokens.get(i);

            if (line.length == 0) {}
            else if (i <= 2) {
                // Handles first lines of directives
                if (line[0].equals(".wordsize")) wordsize = line[1];
                else if (line[0].equals(".regcnt")) regcnt = line[1];
                else if (line[0].equals(".maxmem")) maxmem = line[1];

                // Writes first line of .o file
                if (i == 2) write(file, format + ":WS-" + wordsize + ":RC-" + regcnt + ":MM-" + maxmem);
            } else if (line[0].charAt(0) == '.') {
                // Handles other directives
                if (line[0].equals(".pos")) {
                    int newPos = Integer.parseInt(line[1].substring(2), 16);
                    while (this.bytesWritten < newPos) write(file, "00000000");
                } else if (line[0].equals(".align")) {
                    this.align = Integer.parseInt(line[1]);
                } else if (line[0].equals(".double") || line[0].equals(".single") || line[0].equals(".half") || line[0].equals(".byte")) {
                    int bytes = 8;
                    if (line[0].equals(".single")) bytes = 4;
                    if (line[0].equals(".half")) bytes = 2;
                    if (line[0].equals(".byte")) bytes = 1;

                    String unpadded = line[1].substring(2);
                    if (!hex) unpadded = Integer.toBinaryString(Integer.parseInt(unpadded, 16));

                    if (hex) while (unpadded.length() < bytes) unpadded = "0" + unpadded;
                    else while (unpadded.length() < bytes * 8) unpadded = "0" + unpadded;
                    write(file, unpadded);

                    while (this.bytesWritten % this.align != 0) write(file, "00000000");
                }
            } else if (line[0].charAt(line[0].length() - 1) == ':') {
                // Adds label to list for second pass
                labels.add(new String[] {line[0].substring(0, line[0].length() - 1), (new Integer(this.bytesWritten)).toString()});
            } else {
                // Handles all instructions
                Instruction instruction = extract(line);
                if (instruction != null) {
                    write(file, hex ? instruction.getHex() : instruction.getBinary());
                    while (this.bytesWritten % this.align != 0) write(file, "00000000");
                }
            }
        }
        // Finishes first pass
        if (this.writer != null) this.writer.close();
        // Handles second pass
        this.replaceLabelsAddSpace(file, labels);

        System.out.println("STATISTICS:");
        System.out.println("Number of R type instructions: " + this.numR);
        System.out.println("Number of I type instructions: " + this.numI);
        System.out.println("Number of M type instructions: " + this.numM);
        System.out.println("Number of B type instructions: " + this.numB);
        System.out.println("Number of O type instructions: " + this.numO);
        System.out.println("Total Instructions: " + (this.numR + this.numI + this.numM + this.numB + this.numO));
    }

    /**
     * Extracts an instruction from a set of tokens of a line
     * @param line - array of tokens that represents one line of Assembly
     * @return Instruction object of the Assembly
     */
    private Instruction extract(String[] line) {
        Instruction instruction = null;
        String type = InstructionList.getTypeFromMnemonic(line[0]);
        String opcode = InstructionList.getOpcodeFromMnemonic(line[0]);
        // System.out.println(Arrays.asList(line));
        if (type.equals("R")) {
            this.numR++;
            instruction = new InstructionTypeR(line[0], opcode, line[1], line[2], line[3], "0");
        } else if (type.equals("I")) {
            this.numI++;
            instruction = new InstructionTypeI(line[0], opcode, line[1], line[2], line[3]);
        } else if (type.equals("M")) {
            this.numM++;
            instruction = new InstructionTypeM(line[0], opcode, line[1], line[2], line[3]);
        } else if (type.equals("B")) {
            this.numB++;
            if (line.length == 2) {
                if (line[0].equals("BR")) instruction = new InstructionTypeB(line[0], opcode, line[1], "0");
                else instruction = new InstructionTypeB(line[0], opcode, "x0", line[1]);
            } else instruction = new InstructionTypeB(line[0], opcode, line[1], line[2]);
        } else if (type.equals("O")) {
            this.numO++;
            instruction = new InstructionTypeO(line[0], opcode, line[1]);
        }

        return instruction;
    }

    /**
     * Splits the entire .as file into tokens
     * @param filename - filename of .as file
     * @return ArrayList of token arrays, one per line
     */
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

    /**
     * Splits one line of Assembly into tokens
     * @param line - line of assembly to tokenize
     * @return String array of tokens
     */
    private String[] tokenizeLine(String line) {
        line = line.trim();
        String[] tokens;

        if (line.equals("")) {
            tokens = new String[]{};
        } else if (line.contains(":") && line.contains(".")) {
            // Handles label and directive in same line
            String label = line.split(" ")[0];
            line = line.substring(line.indexOf(":") + 1).trim();

            String[] split = line.split(" ");

            tokens = new String[split.length + 1];
            tokens[0] = label;
            for(int i = 0; i < split.length; i++) tokens[i+1] = split[i];
        } else if (line.contains(":")) {
            // Handles label
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
            // Handles directives
            tokens = line.split(" ");
        } else if (line.contains("HALT")) {
            // Special case for HALT because of single input
            tokens = new String[] {"HALT", "x0"};
        } else if (line.contains("POP")) {
            // Special case for POP because of single input
            tokens = new String[] {"POP", "x0"};
        } else {
            // Handles instructions
            String opcode = line.split(" ")[0];
            line = line.substring(line.indexOf(" ") + 1).trim();

            String[] split = line.split(",");

            tokens = new String[split.length + 1];
            tokens[0] = opcode;
            for(int i = 0; i < split.length; i++) tokens[i+1] = split[i];
        }

        // Remove invalid tokens (like empty strings)
        List<String> list = new ArrayList<String>(Arrays.asList(tokens));
        list.removeAll(Arrays.asList(""));
        tokens = new String[list.size()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = list.get(i).trim();
        }
        return tokens;
    }

    /**
     * Writes one line to file
     * First pass of Assembler
     * @param file - filename of .as file
     * @param line - line to write to file
     */
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
        }
    }

    /**
     * Replaces labels with address and format file with spaces
     * Second pass of Assembler
     * @param file - filename of .as file
     * @param labels - list of labels to replace
     */
    private void replaceLabelsAddSpace(String file, ArrayList<String[]> labels) {
        try {
            Scanner reader = new Scanner(new File(file));
            String line1 = reader.nextLine();
            String data = reader.nextLine();

            this.writer = new PrintWriter(new FileWriter(file));

            // Finds stack label and sets it as a parameter at the top of the .o file
            String stackLoc = "512";
            for (String[] l : labels) {
                if (l[0].equals("stack")) stackLoc = l[1];
            }
            this.writer.println(line1 + ":SP-0x" + Integer.toHexString(Integer.parseInt(stackLoc)));

            // Search for instances of labels and replace with binary addresses
            for(int i = 0; i < labels.size(); i++) {
                while (data.contains(labels.get(i)[0])) {
                    String unpadded = Integer.toBinaryString(Integer.parseInt(labels.get(i)[1]));
                    while (unpadded.length() < 21) unpadded = "0" + unpadded;
                    data = data.substring(0, data.indexOf(labels.get(i)[0])) + unpadded + data.substring(data.indexOf(labels.get(i)[0]) + 21);
                }
            }

            // Format .o file as four bytes to a line with a tab between each byte
            int bitsInLine = 0;
            for(int i = 0; i < data.length(); i++) {
                this.writer.print(data.charAt(i));
                bitsInLine++;
                if (bitsInLine % 32 == 0) {
                    bitsInLine = 0;
                    this.writer.println();
                } else if (bitsInLine % 8 == 0) this.writer.print("\t");
            }
            this.writer.close();
        } catch(Exception e) {
            System.out.println(e);
            return;
        }
    }

}