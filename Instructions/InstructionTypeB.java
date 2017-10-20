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
        if (this.address.substring(0, 2).equals("0x")) {
            return this.opcode + this.decimalToBinary(this.address.substring(2), 26);
        } else {
            return this.opcode + this.address + this.decimalToBinary("0", 26 - this.address.length());
        }
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}