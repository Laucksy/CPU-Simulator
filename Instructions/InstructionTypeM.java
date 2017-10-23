package Instructions;

public class InstructionTypeM extends Instruction {
    private String first;
    private String second;
    private String offset;

    /**
     * Creates M type instruction with the parameters given
     * @param m - Mnemonic of the instruction
     * @param o - Opcode of the instruction
     * @param f - First register of the instruction
     * @param s - Second register of the instruction
     * @param off - Offset of the instruction
     */
    public InstructionTypeM(String m, String o, String f, String s, String off) {
        super(m, o);
        this.first = f;
        this.second = s.replace("[", "");
        this.offset = off.replace("]", "");
    }

    /**
     * Creates M type instruction by extracting values from binary string
     * @param ins - 32 bit binary string of instruction
     */
    public InstructionTypeM(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.first = "x" + Integer.parseInt(ins.substring(6, 11), 2);
        this.second = "x" + Integer.parseInt(ins.substring(11, 16), 2);
        this.offset = "#" + Integer.parseInt(ins.substring(16), 2);
    }

    /**
     * Get the first register in the instruction
     * @return String value of register number
     */
    public String getFirst() {
        return this.first;
    }

    /**
     * Get the second register in the instruction
     * @return String value of register number
     */
    public String getSecond() {
        return this.second;
    }

    /**
     * Get the offset of the instruction
     * @return String value of offset in decimal
     */
    public String getOffset() {
        return this.offset;
    }

    /**
     * Returns all parameters of the instruction
     * @return String array of parameters
     */
    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.second, this.offset};
    }

    /**
     * Gets Assembly representation of the instruction
     * @return String of Assembly code of instruction
     */
    public String getPlain() {
        return this.mnemonic + " " + this.first + ", [" + this.second + ", " + this.offset + "]";
    }

    /**
     * Get binary representation of instruction
     * @return 32 bit binary string of instruction
     */
    public String getBinary() {
        return this.opcode + this.decimalToBinary(first.substring(1), 5)
                + this.decimalToBinary(second.substring(1), 5)
                + this.decimalToBinary(offset.substring(1), 16);
    }

    /**
     * Get hex representation of instruction
     * @return 32 bit hex string of instruction
     */
    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}