package Instructions;

public class InstructionTypeR extends Instruction {
    private String first;
    private String second;
    private String target;
    private String shift;

    /**
     * Creates an R type instruction from the given parameters
     * @param m - mnemonic of the instruction
     * @param o - opcode of the instruction
     * @param f - first register of the instruction
     * @param s - second register of the instruction
     * @param t - target register of the instruction
     * @param sh - shift value of the instruction
     */
    public InstructionTypeR(String m, String o, String f, String s, String t, String sh) {
        super(m, o);
        this.first = f;
        this.second = s;
        this.target = t;
        this.shift = sh;
    }

    /**
     * Creates R type instruction by extracting values from binary string
     * @param ins - 32 bit binary string of instruction
     */
    public InstructionTypeR(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.first = "x" + Integer.parseInt(ins.substring(6, 11), 2);
        this.second = "x" + Integer.parseInt(ins.substring(11, 16), 2);
        this.target = "x" + Integer.parseInt(ins.substring(16, 21), 2);
        this.shift = "" + Integer.parseInt(ins.substring(21), 2);
    }

    /**
     * Gets the first register of the instruction
     * @return Decimal value of register number
     */
    public String getFirst() {
        return this.first;
    }

    /**
     * Gets the second register of the instruction
     * @return Decimal value of register number
     */
    public String getSecond() {
        return this.second;
    }

    /**
     * Gets the target register of the instruction
     * @return Decimal value of register number
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * Gets the shift value of the instruction
     * @return Shift value of the instruction
     */
    public String getShift() {
        return this.shift;
    }

    /**
     * Returns all parameters of the instruction
     * @return String array of the parameters
     */
    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.second, this.target, this.shift};
    }

    /**
     * Gets the Assembly representation of the instruction
     * @return String of Assembly code of instruction
     */
    public String getPlain() {
        return this.mnemonic + " " + this.first + ", " + this.second + ", " + this.target;
    }

    /**
     * Gets the binary representation of the instruction
     * @return 32 bit binary string of instruction
     */
    public String getBinary() {
        return this.opcode + this.decimalToBinary(first.substring(1), 5)
                            + this.decimalToBinary(second.substring(1), 5)
                            + this.decimalToBinary(target.substring(1), 5)
                            + this.decimalToBinary(shift, 11);
    }

    /**
     * Gets the hex representation of the instruction
     * @return 32 bit hex string of instruction
     */
    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}