package Simulator;

import java.util.*;
import java.io.*;
import Instructions.*;
import Operations.*;
import sun.rmi.runtime.Log;

public class Simulator {
    private int wordsize;
    private int regcnt;
    private int maxmem;
    private char[][] registers;
    private Byte[] memory;

    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(new File(args[0]));
            String firstLine = reader.nextLine();
            String[] params = firstLine.split(":");

            String format = params[0].substring(1);
            int wordsize = Integer.parseInt(params[1].substring(3));
            int regcnt = Integer.parseInt(params[2].substring(3));
            int maxmem = Integer.parseInt(params[3].substring(5), 16);

            System.out.println(format + "," + wordsize + "," + regcnt + "," + maxmem);

            Simulator sim = new Simulator(wordsize, regcnt, maxmem);
            sim.execute(args[0], format);
        } catch (FileNotFoundException e) {
            System.out.println("BBB" + e);
        }
    }

    public Simulator(int w, int r, int m) {
        this.wordsize = w;
        this.regcnt = r;
        this.maxmem = m;
        this.registers = new char[r][w];
        this.memory = new Byte[m];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < w; j++) {
                this.registers[i][j] = '0';
            }
        }

        for (int i = 0; i < m; i++) {
            this.memory[i] = Byte.decode("00000000");
        }
    }

    public void printState(String format) {
        boolean hex = format == "hex";
        System.out.println("Wordsize: " + this.wordsize);
        System.out.println("Registers: " + this.regcnt);
        System.out.println("Max Memory: " + this.maxmem);
        System.out.println("\nRegister List:");
        for (char[] reg : this.registers) {
            for (char c : reg) System.out.print(c);
            System.out.println();
        }

        int printed = 0;
        System.out.println("\nMemory: ");
        for (Byte b : this.memory) {
            if (hex) {
                String hexString = Integer.toHexString(Integer.parseInt(b.toString()));
                while (hexString.length() < 2) hexString = "0" + hexString;
                if (hexString.length() > 2) hexString = hexString.substring(hexString.length() - 2);

                for (int i = 0; i < hexString.length(); i++) {
                    System.out.print(hexString.charAt(i));
                    printed++;
                    if (printed % 8 == 0 && printed % 32 != 0) System.out.print("\t");
                    if (printed % 32 == 0) System.out.println();
                }
            } else {
                String binary = byteToBinary(b);

                for (int i = 0; i < binary.length(); i++) {
                    System.out.print(binary.charAt(i));
                    printed++;
                    if (printed % 8 == 0 && printed % 32 != 0) System.out.print("\t");
                    if (printed % 32 == 0) System.out.println();
                }
            }
        }
    }

    public void execute(String filename, String format) {
        loadFromMemory(filename, format);

        int pc = 0;
        boolean halt = false;
        while (pc < this.maxmem - 4 && !halt) {
            String ins = byteToBinary(this.memory[pc]) + byteToBinary(this.memory[pc+1]) + byteToBinary(this.memory[pc+2]) + byteToBinary(this.memory[pc+3]);
            // System.out.println(ins);

            String opcode = ins.substring(0, 6);
            String mnemonic = InstructionList.getMnemonicFromOpcode(opcode);
            String type = InstructionList.getTypeFromMnemonic(mnemonic);
            System.out.println(mnemonic + " " + type);

            if (type.equals("R") {
                int first = Integer.parseInt(ins.substring(6, 11), 2);
                int second = Integer.parseInt(ins.substring(11, 16), 2);
                int target = Integer.parseInt(ins.substring(16, 21), 2);

                if (mnemonic.equals("ADD")) this.registers[target] = ArithmeticOperations.add(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("ADDS")) this.registers[target] = ArithmeticOperations.add(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("SUB")) this.registers[target] = ArithmeticOperations.subtract(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("SUBS")) this.registers[target] = ArithmeticOperations.subtract(this.registers[first], this.registers[second]);

                else if (mnemonic.equals("AND")) this.registers[target] = LogicalOperations.and(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("IOR")) this.registers[target] = LogicalOperations.ior(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("EOR")) this.registers[target] = LogicalOperations.eor(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("LSL")) this.registers[target] = LogicalOperations.lsl(this.registers[first], this.registers[second]);
                else if (mnemonic.equals("LSR")) this.registers[target] = LogicalOperations.lsr(this.registers[first], this.registers[second]);
            } else if (type.equals("I") {
                int first = Integer.parseInt(ins.substring(6, 11), 2);
                int target = Integer.parseInt(ins.substring(11, 16), 2);
                char[] immediate = ArithmeticOperations.convertString(ins.substring(16, 21));

                if (mnemonic.equals("ADDI")) this.registers[target] = ArithmeticOperations.add(this.registers[first], immediate);
                else if (mnemonic.equals("ADDIS")) this.registers[target] = ArithmeticOperations.add(this.registers[first], immediate);
                else if (mnemonic.equals("SUBI")) this.registers[target] = ArithmeticOperations.subtract(this.registers[first], immediate);
                else if (mnemonic.equals("SUBIS")) this.registers[target] = ArithmeticOperations.subtract(this.registers[first], immediate);

                else if (mnemonic.equals("ANDI")) this.registers[target] = LogicalOperations.and(this.registers[first], immediate);
                else if (mnemonic.equals("IORI")) this.registers[target] = LogicalOperations.ior(this.registers[first], immediate);
                else if (mnemonic.equals("EORI")) this.registers[target] = LogicalOperations.eor(this.registers[first], immediate);
            }

            // char[] result = ArithmeticOperations.add(ArithmeticOperations.convertString("1111"), ArithmeticOperations.complement(ArithmeticOperations.convertString("1010")));
            // char[] result = ArithmeticOperations.subtract(ArithmeticOperations.convertString("1111"), ArithmeticOperations.convertString("1010"));
            // System.out.print("SUB 15 - 10 = ");
            // for (char c : result) System.out.print(c);
            // System.out.println();

            pc += 4;
        }
    }

    public void loadFromMemory(String filename, String format) {
        try {
            boolean hex = format == "hex";
            Scanner reader = new Scanner(new File(filename));
            reader.nextLine();
            int bytesLoaded = 0;
            while (reader.hasNextLine()) {
                String[] line = reader.nextLine().split("\t");
                for (String l : line) {
                    if (l.charAt(0) == '0') {
                        this.memory[bytesLoaded] = Byte.parseByte(l, hex ? 16 : 2);
                    } else {
                        this.memory[bytesLoaded] = Byte.parseByte(""+ (Integer.parseInt(l, hex ? 16 : 2) - 256), 10);
                    }
                    bytesLoaded++;
                }
            }
            // this.printState(format);
        } catch (Exception e) {
            System.out.println("AAA" + e);
        }
    }

    public String byteToBinary(Byte b) {
        String binary = Integer.toBinaryString(Integer.parseInt(b.toString()));
        while (binary.length() < 8) binary = "0" + binary;
        if (binary.length() > 8) binary = binary.substring(binary.length() - 8);
        return binary;
    }
}
