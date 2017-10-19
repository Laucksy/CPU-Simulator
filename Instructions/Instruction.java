package Instructions;

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

    public String decimalToBinary(String dec, int bits) {
        if (dec.equals("")) return "";
        String unpadded = Integer.toBinaryString(Integer.parseInt(dec));
        while (unpadded.length() < bits) unpadded = "0" + unpadded;
        return unpadded;
    }

    public abstract String[] getParams();

    public abstract String getPlain();

    public abstract String getBinary();

    public abstract String getHex();
}

