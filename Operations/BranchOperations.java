package Operations;

public class BranchOperations {
    /**
     * Does a conditional branch zero on a register
     * @param reg - register to check value of
     * @param address - address to branch to upon condition being met
     * @param pc - current program counter
     * @return new program counter after branch
     */
    public static int cbz(char[] reg, char[] address, int pc) {
        int numOnes = 0;
        for (int i = 0; i < reg.length; i++) {
            if (reg[i] == '1') numOnes++;
        }

        if (numOnes == 0) {
            String addr = "";
            for (char c : address) addr = addr + c;
            return Integer.parseInt(addr, 2);
        } else return pc;
    }

    /**
     * Does a conditional branch not zero on a register
     * @param reg - register to check value of
     * @param address - address to branch to upon condition being met
     * @param pc - current program counter
     * @return new program counter after branch
     */
    public static int cbnz(char[] reg, char[] address, int pc) {
        int numOnes = 0;
        for (int i = 0; i < reg.length; i++) {
            if (reg[i] == '1') numOnes++;
        }

        if (numOnes != 0) {
            String addr = "";
            for (char c : address) addr = addr + c;
            return Integer.parseInt(addr, 2);
        } else return pc;
    }

    /**
     * Branches to a new location
     * @param address - address to branch to
     * @return decimal integer value of address
     */
    public static int b(char[] address) {
        String addr = "";
        for (char c : address) addr = addr + c;
        return Integer.parseInt(addr, 2);
    }

    /**
     * Branches to value of a register
     * @param reg - register to get address from
     * @return decimal integer value of address
     */
    public static int br(char[] reg) {
        String addr = "";
        for (char c : reg) addr = addr + c;
        return Integer.parseInt(addr, 2);
    }

    /**
     * Stores current program counter in link register (last register) and branches to new location
     * @param registers - list of registers in CPU
     * @param reg - register to get address from
     * @param pc - current program counter
     * @return decimal integer value of address
     */
    public static int bl(char[][] registers, char[] reg, int pc) {
        registers[registers.length - 1] = ArithmeticOperations.convertString(Integer.toBinaryString(pc));
        String addr = "";
        for (char c : reg) addr = addr + c;
        return Integer.parseInt(addr, 2);
    }
}
