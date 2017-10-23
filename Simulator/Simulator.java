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

    private String filename;
    private String format;
    private boolean noisy;

    private SimulatorGUI gui;
    private int pc;
    private int sp;
    private Instruction currentInstruction;

    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(new File(args[0]));
            String firstLine = reader.nextLine();
            String[] params = firstLine.split(":");

            String format = params[0].substring(1);
            int wordsize = Integer.parseInt(params[1].substring(3));
            int regcnt = Integer.parseInt(params[2].substring(3));
            int maxmem = Integer.parseInt(params[3].substring(5), 16);
            int stack = Integer.parseInt(params[4].substring(5), 16);

            // System.out.println(format + "," + wordsize + "," + regcnt + "," + maxmem);

            boolean n = args.length == 2 && args[1].equals("noisy") ? true : false;

            Simulator sim = new Simulator(wordsize, regcnt, maxmem, stack, args[0], format, n);
            // sim.execute(args[0], format);
        } catch (FileNotFoundException e) {
            System.out.println("BBB" + e);
        }
    }

    /**
     * Initializes the Simulator, the GUI, and loads the object file
     * @param w - wordsize of the CPU
     * @param r - register count of the CPU
     * @param m - max memory size of the CPU
     * @param s - starting stack pointer of the CPU
     * @param fn - filename of the object file
     * @param f - format of the information (binary or hex)
     * @param n - true for noisy mode, false for quiet mode
     */
    public Simulator(int w, int r, int m, int s, String fn, String f, boolean n) {
        this.wordsize = w;
        this.regcnt = r;
        this.maxmem = m;
        this.sp = s;
        this.filename = fn;
        this.format = f;
        this.noisy = n;

        this.gui = new SimulatorGUI(this);

        this.reset();
    }

    /**
     * Updates the GUI with the current CPU state
     */
    public void drawGUI() {
        this.gui.draw(this.memory, this.registers, this.pc, this.currentInstruction, ArithmeticOperations.getFlags());
    }

    /**
     * Prints the CPU state to the command line
     * @param format - Binary or hex output
     */
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

    /**
     * Reset the simulator to its initial state, reloads from object file
     */
    public void reset() {
        this.registers = new char[this.regcnt][this.wordsize];
        this.memory = new Byte[this.maxmem];

        for (int i = 0; i < this.regcnt; i++) {
            for (int j = 0; j < this.wordsize; j++) {
                this.registers[i][j] = '0';
            }
        }

        for (int i = 0; i < this.maxmem; i++) {
            this.memory[i] = Byte.decode("00000000");
        }

        this.loadFromMemory();
        this.pc = 0;
        this.loadInstruction();

        this.drawGUI();
    }

    /**
     * Writes the memory to a dump file and the CPU state to another dump file
     */
    public void writeToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("data/memory-dump.o"));
            writer.println("#" + this.format + ":WS-" + this.wordsize + ":RC-" + this.regcnt + ":MM-0x" + Integer.toHexString(this.maxmem));
            for (int i = 0; i < this.maxmem; i+=4) {
                writer.println(byteToBinary(this.memory[i]) + "\t"
                        + byteToBinary(this.memory[i+1]) + "\t"
                        + byteToBinary(this.memory[i+2]) + "\t"
                        + byteToBinary(this.memory[i+3]));
            }
            writer.close();

            writer = new PrintWriter(new FileWriter("data/cpu-dump.txt"));
            writer.println("Program Counter: 0x" + Integer.toHexString(this.pc) + " (" + this.pc + ")");
            writer.println("Instruction Register: " + this.currentInstruction.getBinary() + "(" + this.currentInstruction.getPlain() + ")");
            writer.println("Flags: ");
            writer.println("Z - " + ArithmeticOperations.getFlags()[0]);
            writer.println("N - " + ArithmeticOperations.getFlags()[1]);
            writer.println("C - " + ArithmeticOperations.getFlags()[2]);
            writer.println("V - " + ArithmeticOperations.getFlags()[3]);
            writer.println("Registers: ");
            for (int i = 0; i < this.regcnt; i++) writer.println(i + ": " + printCharArray(this.registers[i]));
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Loads the next instruction from memory into the instruction register
     * Equivalent to fetch in the fetch-execute cycle
     */
    public void loadInstruction() {
        if (this.pc >= this.maxmem || this.pc == -1) return;
        String ins = byteToBinary(this.memory[this.pc])
                                    + byteToBinary(this.memory[this.pc+1])
                                    + byteToBinary(this.memory[this.pc+2])
                                    + byteToBinary(this.memory[this.pc+3]);
        String type = InstructionList.getTypeFromMnemonic(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)));
        if (type.equals("R")) {
            this.currentInstruction = new InstructionTypeR(ins);
        } else if (type.equals("I")) {
            this.currentInstruction = new InstructionTypeI(ins);
        } else if (type.equals("M")) {
            this.currentInstruction = new InstructionTypeM(ins);
        } else if (type.equals("B")) {
            this.currentInstruction = new InstructionTypeB(ins);
        } else if (type.equals("O")) {
            this.currentInstruction = new InstructionTypeO(ins);
        }

        if (noisy) System.out.println("LOADED INSTRUCTION " + this.currentInstruction.getPlain() + " AS " + this.currentInstruction.getBinary());
    }

    /**
     * Returns the current instruction in the instruction registers
     * @return the current instruction as an Instruction object
     */
    public Instruction getCurrentInstruction() {
        return this.currentInstruction;
    }

    /**
     * Executes the instruction in the instruction register and updates the program counter
     * Equivalent to execute in the fetch-execute cycle
     */
    public void executeInstruction() {
        if (pc >= this.maxmem || pc == -1) return;

        String ins = this.currentInstruction.getBinary();
        String opcode = ins.substring(0, 6);
        String mnemonic = InstructionList.getMnemonicFromOpcode(opcode);
        String type = InstructionList.getTypeFromMnemonic(mnemonic);

        if (type.equals("R")) {
            // Decodes values from instruction
            int first = Integer.parseInt(ins.substring(6, 11), 2);
            int second = Integer.parseInt(ins.substring(11, 16), 2);
            int target = Integer.parseInt(ins.substring(16, 21), 2);

            char[] firstReg = pad(this.registers[first], 32);
            char[] secondReg = pad(this.registers[second], 32);

            // Handles all R type instructions and executes them
            if (mnemonic.equals("ADD")) setRegister(target, ArithmeticOperations.add(firstReg, secondReg));
            else if (mnemonic.equals("ADDS")) setRegister(target, ArithmeticOperations.add(firstReg, secondReg));
            else if (mnemonic.equals("SUB")) setRegister(target, ArithmeticOperations.subtract(firstReg, secondReg));
            else if (mnemonic.equals("SUBS")) setRegister(target, ArithmeticOperations.subtract(firstReg, secondReg));

            else if (mnemonic.equals("AND")) setRegister(target, LogicalOperations.and(firstReg, secondReg));
            else if (mnemonic.equals("IOR")) setRegister(target, LogicalOperations.ior(firstReg, secondReg));
            else if (mnemonic.equals("EOR")) setRegister(target, LogicalOperations.eor(firstReg, secondReg));

            this.pc += 4;
        } else if (type.equals("I")) {
            // Decodes values from instruction
            int first = Integer.parseInt(ins.substring(6, 11), 2);
            int target = Integer.parseInt(ins.substring(11, 16), 2);
            char[] immediate = ArithmeticOperations.convertString(ins.substring(16));

            char[] firstReg = pad(this.registers[first], 32);

            // Handles all I type instructions and executes them
            if (mnemonic.equals("ADDI")) setRegister(target, ArithmeticOperations.add(firstReg, immediate));
            else if (mnemonic.equals("ADDIS")) setRegister(target, ArithmeticOperations.add(firstReg, immediate));
            else if (mnemonic.equals("SUBI")) setRegister(target, ArithmeticOperations.subtract(firstReg, immediate));
            else if (mnemonic.equals("SUBIS")) setRegister(target, ArithmeticOperations.subtract(firstReg, immediate));

            else if (mnemonic.equals("ANDI")) setRegister(target, LogicalOperations.and(firstReg, immediate));
            else if (mnemonic.equals("IORI")) setRegister(target, LogicalOperations.ior(firstReg, immediate));
            else if (mnemonic.equals("EORI")) setRegister(target, LogicalOperations.eor(firstReg, immediate));
            else if (mnemonic.equals("LSL")) setRegister(target, LogicalOperations.lsl(firstReg, Integer.parseInt(ins.substring(16), 2)));
            else if (mnemonic.equals("LSR")) setRegister(target, LogicalOperations.lsr(firstReg, Integer.parseInt(ins.substring(16), 2)));

            this.pc += 4;
        } else if (type.equals("M")) {
            // Decodes values from instruction
            int first = Integer.parseInt(ins.substring(6, 11), 2);
            int second = Integer.parseInt(ins.substring(11, 16), 2);
            char[] offset = ArithmeticOperations.convertString(ins.substring(16));

            char[] firstReg = pad(this.registers[first], 32);
            char[] secondReg = pad(this.registers[second], 32);

            // Handles all M type instructions and executes them
            if (mnemonic.equals("LDUR")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 32));
            else if (mnemonic.equals("LDURW")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 32));
            else if (mnemonic.equals("LDURH")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 16));
            else if (mnemonic.equals("LDURB")) setRegister(first, MemoryOperations.load(this.memory, secondReg, offset, 8));

            else if (mnemonic.equals("STUR")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 32);
            else if (mnemonic.equals("STURW")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 32);
            else if (mnemonic.equals("STURH")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 16);
            else if (mnemonic.equals("STURB")) MemoryOperations.store(this.memory, firstReg, secondReg, offset, 8);

            this.pc += 4;
        } else if (type.equals("B")) {
            // Decodes values from instruction
            int pc_tmp = pc;
            int first = Integer.parseInt(ins.substring(6, 11), 2);
            char[] address = ArithmeticOperations.convertString(ins.substring(11));

            char[] firstReg = pad(this.registers[first], 32);

            // Handles all B type instructions and executes them
            if (mnemonic.equals("CBZ")) this.pc = BranchOperations.cbz(firstReg, address, pc);
            else if (mnemonic.equals("CBNZ")) this.pc = BranchOperations.cbnz(firstReg, address, pc);
            else if (mnemonic.equals("B")) this.pc = BranchOperations.b(address);
            else if (mnemonic.equals("B.EQ")) {
                boolean[] flags = ArithmeticOperations.getFlags();
                if (flags[0]) this.pc = BranchOperations.b(address);
                else this.pc += 4;
            }
            else if (mnemonic.equals("B.LT")) {
                boolean[] flags = ArithmeticOperations.getFlags();
                if (flags[1] != flags[3]) this.pc = BranchOperations.b(address);
                else this.pc += 4;
            }
            else if (mnemonic.equals("B.GT")) {
                boolean[] flags = ArithmeticOperations.getFlags();
                if (flags[1] == flags[3]) this.pc = BranchOperations.b(address);
                else this.pc += 4;
            }
            else if (mnemonic.equals("BR")) this.pc = BranchOperations.br(firstReg);
            else if (mnemonic.equals("BL")) this.pc = BranchOperations.bl(this.registers, firstReg, pc);

            if (this.noisy) System.out.println("SET PC FROM " + pc_tmp + " TO " + pc);
        } else if (type.equals("O")) {
            // Decodes values from instruction
            int first = Integer.parseInt(ins.substring(6, 11), 2);
            char[] address = ArithmeticOperations.convertString(Integer.toBinaryString(this.sp));
            char[] zero = ArithmeticOperations.convertString("0");

            char[] firstReg = pad(this.registers[first], 32);

            // Handles all O type instructions and executes them
            if (mnemonic.equals("PUSH")) {
                MemoryOperations.store(this.memory, firstReg, address, zero, 32);
                this.sp += 4;
                this.pc += 4;
            }
            else if (mnemonic.equals("POP")) {
                this.sp -= 4;
                this.pc += 4;
            }
            else if (mnemonic.equals("HALT")) this.pc = -1;
            else if (mnemonic.equals("NOP")) this.pc += 4;
        }
        // Loads next instruction and updates GUI
        this.loadInstruction();
        this.drawGUI();
    }

    /**
     * Creates memory in simulator from object file
     */
    private void loadFromMemory() {
        try {
            boolean hex = this.format == "hex";
            Scanner reader = new Scanner(new File(this.filename));
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
        } catch (Exception e) {
            System.out.println("AAA" + e);
        }
    }

    /**
     * Converts a byte object to an 8 bit binary string
     * @param b - Byte object to convert
     * @return 8 bit binary string representation
     */
    private String byteToBinary(Byte b) {
        String binary = Integer.toBinaryString(Integer.parseInt(b.toString()));
        while (binary.length() < 8) binary = "0" + binary;
        if (binary.length() > 8) binary = binary.substring(binary.length() - 8);
        return binary;
    }

    /**
     * Standardizes a character array to 32 bits
     * @param arr - char array that isn't 32 bits
     * @param size - size to pad to (32)
     * @return padded char array
     */
    public char[] pad(char[] arr, int size) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = arr.length - 1; i >= 0; i--) num[i] = arr[i];
        return num;
    }

    /**
     * Gets a string representation of a char array so String methods can be used on it
     * @param arr - array to convert
     * @return String object of char array
     */
    public String printCharArray(char[] arr) {
        String result = "";
        for (int i = 0; i < arr.length; i++) result = result + arr[i];
        return result;
    }

    /**
     * Inserts a value into a CPU registers
     * @param reg - number of the register to change
     * @param val - 32 bit character array to set register to
     */
    public void setRegister(int reg, char[] val) {
        if (noisy) System.out.println("SET REGISTER " + reg + " from " + printCharArray(this.registers[reg]) + " to " + printCharArray(val));
        this.registers[reg] = val;
    }
}
