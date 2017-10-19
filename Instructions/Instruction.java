public abstract class Instruction {
    protected String mnemonic;
    protected String opcode;

    public Instruction(String m, String o) {
        this.mnemonic = m;
        this.opcode = o;
    }

    public String getMnemonic() {
        return this.mnemonic;
    }

    public String getOpcode() {
        return this.opcode;
    }

    public abstract String[] getParams();

    public abstract String getBinary();

    public abstract String getHex();
}

