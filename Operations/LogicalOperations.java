package Operations;

public class LogicalOperations {
    /**
     * Performs a bitwise AND on two numbers
     * @param one - first number in operation
     * @param two - second number in operation
     * @return bitwise AND of one and two
     */
    public static char[] and(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if (one[i] == '1' && two[i] == '1') num[i] = '1';
            else num[i] = '0';
        }
        return num;
    }

    /**
     * Performs a bitwise inclusive OR on two numbers
     * @param one - first number in operation
     * @param two - second number in operation
     * @return bitwise inclusive OR of one and two
     */
    public static char[] ior(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if (one[i] == '1' || two[i] == '1') num[i] = '1';
            else num[i] = '0';
        }
        return num;
    }

    /**
     * Performs a bitwise exclusive OR on two numbers
     * @param one - first number in operation
     * @param two - second number in operation
     * @return bitwise exclusive OR of one and two
     */
    public static char[] eor(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if ((one[i] == '1' && two[i] == '0') || (one[i] == '0' && two[i] == '1')) num[i] = '1';
            else num[i] = '0';
        }
        return num;
    }

    /**
     * Performs a logical shift left on a number
     * @param one - number to shift
     * @param shift - number of bits to shift
     * @return one shifted left shift times
     */
    public static char[] lsl(char[] one, int shift) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if (i + shift < one.length) num[i] = one[i + shift];
            else num[i] = '0';
        }
        return num;
    }

    /**
     * Performs a logical shift right on a number
     * @param one - number to shift
     * @param shift - number of bits to shift
     * @return one shifted right shift times
     */
    public static char[] lsr(char[] one, int shift) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 31; i >= 0; i--) {
            if (i - shift >= 0) num[i] = one[i - shift];
            else num[i] = '0';
        }
        return num;
    }
}
