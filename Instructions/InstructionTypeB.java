package Instructions;

public class InstructionTypeB extends Instruction {
    private String first;
    private String address;

    public InstructionTypeB(String o, String n, String f, String a) {
        super(o, n);
        this.first = f;
        this.address = a;
    }

    public InstructionTypeB(String ins) {
        super(InstructionList.getMnemonicFromOpcode(ins.substring(0, 6)), ins.substring(0, 6));
        this.first = "" + Integer.parseInt(ins.substring(6, 11), 2);
        this.address = "0x" + Integer.parseInt(ins.substring(11), 2);
    }

    public String getFirst() {
        return this.first;
    }

    public String getAddress() {
        return this.address;
    }

    public String[] getParams() {
        return new String[] {this.mnemonic, this.opcode, this.first, this.address};
    }

    public String getPlain() {
        return this.mnemonic + " " + this.first + " " + this.address;
    }

    public String getBinary() {
        if (this.address.substring(0, 2).equals("0x")) {
            return this.opcode + this.decimalToBinary(this.first.substring(1), 5) + this.decimalToBinary(this.address.substring(2), 21);
        } else {
            return this.opcode + this.decimalToBinary(this.first.substring(1), 5) + this.address + this.decimalToBinary("0", 21 - this.address.length());
        }
    }

    public String getHex() {
        return Integer.toHexString(Integer.parseInt(this.getBinary(), 2));
    }
}