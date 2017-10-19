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

    public String getBinary() {
        return "binary";
    }

    public String getHex() {
        return "hex";
    }
}