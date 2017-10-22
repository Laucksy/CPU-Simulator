package Instructions;

public class InstructionTypeO extends Instruction {
    private String value;

    public InstructionTypeO(String o, String n, String v) {
        super(o, n);
        this.value = v;
    }

    public InstructionTypeO(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.value = "" + Integer.parseInt(ins.substring(6), 2);
    }

    public String getValue() {
        return this.value;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.value};
    }

    public String getPlain() {
        return this.mnemonic + " " + this.value;
    }

    public String getBinary() {
        return this.opcode + this.decimalToBinary(value, 26);
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}