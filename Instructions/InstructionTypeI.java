package Instructions;

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

    public String getPlain() {
        return this.mnemonic + " " + this.first + ", " + this.target + ", " + this.immediate;
    }

    public String getBinary() {
        return this.opcode + this.decimalToBinary(first.substring(1), 5)
                + this.decimalToBinary(target.substring(1), 5)
                + this.decimalToBinary(immediate.substring(1), 16);
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}