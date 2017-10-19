package Instructions;

public class InstructionTypeM extends Instruction {
    private String first;
    private String second;
    private String offset;

    public InstructionTypeM(String m, String o, String f, String s, String off) {
        super(m, o);
        this.first = f;
        this.second = s.replace("[", "");
        this.offset = off.replace("]", "");
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

    public String getPlain() {
        return this.mnemonic + " " + this.first + ", [" + this.second + ", " + this.offset + "]";
    }

    public String getBinary() {
        return this.opcode + this.decimalToBinary(first.substring(1), 5)
                + this.decimalToBinary(second.substring(1), 5)
                + this.decimalToBinary(offset.substring(1), 16);
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}