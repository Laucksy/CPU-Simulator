public class InstructionTypeO extends Instruction {
    private String value;

    public InstructionTypeO(String o, String n, String v) {
        super(o, n);
        this.value = v;
    }

    public String getValue() {
        return this.value;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this,value};
    }

    public String getBinary() {
        return "binary";
    }

    public String getHex() {
        return "hex";
    }
}