package Instructions;

public class InstructionTypeB extends Instruction {
    private String first;
    private String address;

    /**
     * Creates B type instruction from the given parameters
     * @param m - mnemonic of the instruction
     * @param o - opcode of the instruction
     * @param f - first register of the instruction
     * @param a - address for the instruction
     */
    public InstructionTypeB(String m, String o, String f, String a) {
        super(m, o);
        this.first = f;
        this.address = a;
    }

    /**
     * Creates B type instruction by extracting values from binary string
     * @param ins - 32 bit binary string of instruction
     */
    public InstructionTypeB(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.first = "" + Integer.parseInt(ins.substring(6, 11), 2);
        this.address = "0x" + Integer.parseInt(ins.substring(11), 2);
    }

    /**
     * Gets the first register of the instruction
     * @return Decimal value of register number
     */
    public String getFirst() {
        return this.first;
    }

    /**
     * Gets the address for the instruction
     * @return Decimal value of address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Gets all parameters for instruction
     * @return String array of parameters
     */
    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.address};
    }

    /**
     * Gets Assembly representation of instruction
     * @return String of Assembly code of instruction
     */
    public String getPlain() {
        return this.mnemonic + " " + this.first + " " + this.address;
    }

    /**
     * Gets binary representation of instruction
     * @return 32 bit binary string of instruction
     */
    public String getBinary() {
        if (this.address.substring(0, 2).equals("0x")) {
            return this.opcode + this.decimalToBinary(this.first.substring(1), 5) + this.decimalToBinary(this.address.substring(2), 21);
        } else {
            return this.opcode + this.decimalToBinary(this.first.substring(1), 5) + this.address + this.decimalToBinary("0", 21 - this.address.length());
        }
    }

    /**
     * Gets hex representation of instruction
     * @return 32 bit hex string of instruction
     */
    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}