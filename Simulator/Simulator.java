package Simulator;

import java.util.*;
import java.io.*;
import Instructions.*;
import Operations.*;

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

            if (type.equals("R")) {
                int first = Integer.parseInt(ins.substring(6, 11), 2);
                int second = Integer.parseInt(ins.substring(11, 16), 2);
                int target = Integer.parseInt(ins.substring(16, 21), 2);

                char[] firstReg = pad(this.registers[first], 32);
                char[] secondReg = pad(this.registers[second], 32);

                // System.out.println("R INS " + ins);
                // System.out.println("R TYPE " + first + "," + second + "," + target + "," + printCharArray(firstReg) + "," + printCharArray(secondReg) + "," + printCharArray(this.registers[target]));

                if (mnemonic.equals("ADD")) setRegister(target, ArithmeticOperations.add(firstReg, secondReg));
                else if (mnemonic.equals("ADDS")) setRegister(target, ArithmeticOperations.add(firstReg, secondReg));
                else if (mnemonic.equals("SUB")) setRegister(target, ArithmeticOperations.subtract(firstReg, secondReg));
                else if (mnemonic.equals("SUBS")) setRegister(target, ArithmeticOperations.subtract(firstReg, secondReg));

                else if (mnemonic.equals("AND")) setRegister(target, LogicalOperations.and(firstReg, secondReg));
                else if (mnemonic.equals("IOR")) setRegister(target, LogicalOperations.ior(firstReg, secondReg));
                else if (mnemonic.equals("EOR")) setRegister(target, LogicalOperations.eor(firstReg, secondReg));

                // System.out.println("FINISHED R TYPE " + printCharArray(this.registers[target]));
            } else if (type.equals("I")) {
                int first = Integer.parseInt(ins.substring(6, 11), 2);
                int target = Integer.parseInt(ins.substring(11, 16), 2);
                char[] immediate = ArithmeticOperations.convertString(ins.substring(16));

                char[] firstReg = pad(this.registers[first], 32);

                // System.out.println("I TYPE " + first + "," + target + "," + printCharArray(immediate) + "," + printCharArray(firstReg) + "," + printCharArray(this.registers[target]));

                if (mnemonic.equals("ADDI")) setRegister(target, ArithmeticOperations.add(firstReg, immediate));
                else if (mnemonic.equals("ADDIS")) setRegister(target, ArithmeticOperations.add(firstReg, immediate));
                else if (mnemonic.equals("SUBI")) setRegister(target, ArithmeticOperations.subtract(firstReg, immediate));
                else if (mnemonic.equals("SUBIS")) setRegister(target, ArithmeticOperations.subtract(firstReg, immediate));

                else if (mnemonic.equals("ANDI")) setRegister(target, LogicalOperations.and(firstReg, immediate));
                else if (mnemonic.equals("IORI")) setRegister(target, LogicalOperations.ior(firstReg, immediate));
                else if (mnemonic.equals("EORI")) setRegister(target, LogicalOperations.eor(firstReg, immediate));
                else if (mnemonic.equals("LSL")) setRegister(target, LogicalOperations.lsl(firstReg, Integer.parseInt(ins.substring(16), 2)));
                else if (mnemonic.equals("LSR")) setRegister(target, LogicalOperations.lsr(firstReg, Integer.parseInt(ins.substring(16), 2)));

                // System.out.println("FINISHED I TYPE " + printCharArray(this.registers[target]));
            } else if (type.equals("M")) {
                int first = Integer.parseInt(ins.substring(6, 11), 2);
                int second = Integer.parseInt(ins.substring(11, 16), 2);
                char[] offset = ArithmeticOperations.convertString(ins.substring(16));

                char[] firstReg = pad(this.registers[first], 32);
                char[] secondReg = pad(this.registers[second], 32);

                // System.out.println("I TYPE " + first + "," + target + "," + printCharArray(immediate) + "," + printCharArray(firstReg) + "," + printCharArray(this.registers[target]));

                if (mnemonic.equals("LDUR")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 32));
                else if (mnemonic.equals("LDURW")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 32));
                else if (mnemonic.equals("LDURH")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 16));
                else if (mnemonic.equals("LDURB")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 8));

                else if (mnemonic.equals("STUR")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 32);
                else if (mnemonic.equals("STURW")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 32);
                else if (mnemonic.equals("STURH")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 16);
                else if (mnemonic.equals("STURB")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 8);

                // System.out.println("FINISHED I TYPE " + printCharArray(this.registers[target]));
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

    public char[] pad(char[] arr, int size) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = arr.length - 1; i >= 0; i--) num[i] = arr[i];
        return num;
    }

    public String printCharArray(char[] arr) {
        String result = "";
        for (int i = 0; i < arr.length; i++) result = result + arr[i];
        return result;
    }

    public void setRegister(int reg, char[] val) {
        System.out.println("SET REGISTER " + reg + " from " + printCharArray(this.registers[reg]) + " to " + printCharArray(val));
        this.registers[reg] = val;
    }
}
