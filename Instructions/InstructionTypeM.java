public class InstructionTypeM extends Instruction {
    private String first;
    private String second;
    private String offset;

    public InstructionTypeM(String m, String o, String f, String s, String off) {
        super(m, o);
        this.first = f;
        this.second = s;
        this.offset = off;
    }

    public String getFirst() {
        return this.first;
    }

    public String getSecond() {
        return this.second;
    }

    public String getOffset() {
        return this.offset;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.second, this.offset};
    }

    public String getBinary() {
        return "binary";
    }

    public String getHex() {
        return "hex";
    }
}