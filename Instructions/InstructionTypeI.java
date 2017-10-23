package Instructions;

public class InstructionTypeI extends Instruction {
    private String first;
    private String target;
    private String immediate;

    /**
     * Creates an I type instruction from the given parameters
     * @param m - mnemonic of the instruction
     * @param o - opcode of the instruction
     * @param f - first register of the instruction
     * @param t - target register of the instruction
     * @param i - immediate value of the instruction
     */
    public InstructionTypeI(String m, String o, String f, String t, String i) {
        super(m, o);
        this.first = f;
        this.target = t;
        this.immediate = i;
    }

    /**
     * Creates I type instruction by extracting values from binary string
     * @param ins - 32 bit binary string of instruction
     */
    public InstructionTypeI(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.first = "x" + Integer.parseInt(ins.substring(6, 11), 2);
        this.target = "x" + Integer.parseInt(ins.substring(11, 16), 2);
        this.immediate = "#" + Integer.parseInt(ins.substring(16), 2);
    }

    /**
     * Gets the first register of the instruction
     * @return Decimal value of register number
     */
    public String getFirst() {
        return this.first;
    }

    /**
     * Gets the target register of the instruction
     * @return Decimal value of register number
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * Gets the immediate value of the instruction
     * @return Decimal value of immediate
     */
    public String getImmediate() {
        return this.immediate;
    }

    /**
     * Returns all paramters of the instruction
     * @return String array of parameters
     */
    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.target, this.immediate};
    }

    /**
     * Gets Assembly representation of instruction
     * @return String of Assembly code of instruction
     */
    public String getPlain() {
        return this.mnemonic + " " + this.first + ", " + this.target + ", " + this.immediate;
    }

    /**
     * Gets binary representation of instruction
     * @return 32 bit binary string of instruction
     */
    public String getBinary() {
        return this.opcode + this.decimalToBinary(first.substring(1), 5)
                + this.decimalToBinary(target.substring(1), 5)
                + this.decimalToBinary(immediate.substring(1), 16);
    }

    /**
     * Gets hex representation of instruction
     * @return 32 bit hex string of instruction
     */
    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}