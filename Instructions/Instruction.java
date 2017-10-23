package Instructions;

public abstract class Instruction {
    protected String mnemonic;
    protected String opcode;

    /**
     * Creates an instruction with the given parameters
     * @param m - mnemonic of the instruction
     * @param o - opcode of the instruction
     */
    public Instruction(String m, String o) {
        this.mnemonic = m;
        this.opcode = o;
    }

    /**
     * Gets the mnemonic of the instruction
     * @return mnemonic of the instruction
     */
    public String getMnemonic() {
        return this.mnemonic;
    }

    /**
     * Gets the opcode of the instruction
     * @return opcode of the instruction
     */
    public String getOpcode() {
        return this.opcode;
    }

    /**
     * Converts a decimal string to a binary string with certain number of bits
     * @param dec - decimal string to convert
     * @param bits - number of bits for output string
     * @return formatted binary string representation of decimal value
     */
    public String decimalToBinary(String dec, int bits) {
        if (dec.equals("")) return "";
        String unpadded = Integer.toBinaryString(Integer.parseInt(dec));
        while (unpadded.length() < bits) unpadded = "0" + unpadded;
        return unpadded;
    }

    /**
     * Gets all of the paramters for the instruction
     * @return String array of all the paramters
     */
    public abstract String[] getParams();

    /**
     * Gets Assembly representation of the instruction
     * @return String of Assembly code of instruction
     */
    public abstract String getPlain();

    /**
     * Gets binary representation of the instruction
     * @return 32 bit binary string of instruction
     */
    public abstract String getBinary();

    /**
     * Gets hex representation of the instruction
     * @return 32 bit hex string of instruction
     */
    public abstract String getHex();
}

