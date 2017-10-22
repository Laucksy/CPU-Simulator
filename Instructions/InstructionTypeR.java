package Instructions;

public class InstructionTypeR extends Instruction {
    private String first;
    private String second;
    private String target;
    private String shift;

    public InstructionTypeR(String m, String o, String f, String s, String t, String sh) {
        super(m, o);
        this.first = f;
        this.second = s;
        this.target = t;
        this.shift = sh;
    }

    public InstructionTypeR(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.first = "x" + Integer.parseInt(ins.substring(6, 11), 2);
        this.second = "x" + Integer.parseInt(ins.substring(11, 16), 2);
        this.target = "x" + Integer.parseInt(ins.substring(16, 21), 2);
        this.shift = "" + Integer.parseInt(ins.substring(21), 2);
    }

    public String getFirst() {
        return this.first;
    }

    public String getSecond() {
        return this.second;
    }

    public String getTarget() {
        return this.target;
    }

    public String getShift() {
        return this.shift;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.second, this.target, this.shift};
    }

    public String getPlain() {
        return this.mnemonic + " " + this.first + ", " + this.second + ", " + this.target;
    }

    public String getBinary() {
        return this.opcode + this.decimalToBinary(first.substring(1), 5)
                            + this.decimalToBinary(second.substring(1), 5)
                            + this.decimalToBinary(target.substring(1), 5)
                            + this.decimalToBinary(shift, 11);
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}