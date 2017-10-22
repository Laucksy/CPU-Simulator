package Operations;

public class BranchOperations {
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

    public static int b(char[] address) {
        String addr = "";
        for (char c : address) addr = addr + c;
        return Integer.parseInt(addr, 2);
    }

    public static int br(char[] reg) {
        String addr = "";
        for (char c : reg) addr = addr + c;
        return Integer.parseInt(addr, 2);
    }

    public static int bl(char[][] registers, char[] reg, int pc) {
        registers[registers.length - 1] = ArithmeticOperations.convertString(Integer.toBinaryString(pc));
        String addr = "";
        for (char c : reg) addr = addr + c;
        return Integer.parseInt(addr, 2);
    }
}
