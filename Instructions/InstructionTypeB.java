package Instructions;

public class InstructionTypeB extends Instruction {
    private String address;

    public InstructionTypeB(String o, String n, String a) {
        super(o, n);
        this.address = a;
    }

    public String getAddress() {
        return this.address;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.address};
    }

    public String getPlain() {
        return this.mnemonic + " " + this.address;
    }

    public String getBinary() {
        return this.opcode + this.decimalToBinary(address.substring(26), 5);
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}