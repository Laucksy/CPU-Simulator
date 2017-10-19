public class InstructionTypeI extends Instruction {
    private String first;
    private String target;
    private String immediate;

    public InstructionTypeI(String m, String o, String f, String t, String i) {
        super(m, o);
        this.first = f;
        this.target = t;
        this.immediate = i;
    }

    public String getFirst() {
        return this.first;
    }

    public String getTarget() {
        return this.target;
    }

    public String getImmediate() {
        return this.immediate;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.target, this.immediate};
    }

    public String getBinary() {
        return "binary";
    }

    public String getHex() {
        return "hex";
    }
}