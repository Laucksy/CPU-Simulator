package Instructions;

public class InstructionTypeO extends Instruction {
    private String value;

    /**
     * Creates O type instruction with the given parameters
     * @param m - mnemonic of the instruction
     * @param o - opcode of the instruction
     * @param v - value of the register (not for all instructions)
     */
    public InstructionTypeO(String m, String o, String v) {
        super(m, o);
        this.value = v;
    }

    /**
     * Creates O type instruction by extracting values from binary string
     * @param ins - 32 bit binary string of instruction
     */
    public InstructionTypeO(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.value = "x" + Integer.parseInt(ins.substring(6, 11), 2);
    }

    /**
     * Gets register value of the instruction
     * @return Decimal value of the register number
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns all parameters of the instruction
     * @return String array of the parameters
     */
    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.value};
    }

    /**
     * Gets Assembly representation of the instruction
     * @return String of Assembly code of instruction
     */
    public String getPlain() {
        return this.mnemonic + " " + this.value;
    }

    /**
     * Gets binary representation of the instruction
     * @return 32 bit binary string of instruction
     */
    public String getBinary() {
        return this.opcode + this.decimalToBinary(value.substring(1), 5) + this.decimalToBinary("0", 21);
    }

    /**
     * Gets hex representation of the instruction
     * @return 32 bit hex string of instruction
     */
    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}