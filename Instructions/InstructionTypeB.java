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

    public String getBinary() {
        return "binary";
    }

    public String getHex() {
        return "hex";
    }
}