package Operations;

public class ArithmeticOperations {
    private static boolean flagZ = false;
    private static boolean flagN = false;
    private static boolean flagC = false;
    private static boolean flagV = false;

    /**
     * Gets the current CPU flags
     * @return boolean array of the flag values
     */
    public static boolean[] getFlags() {
        return new boolean[] {flagZ, flagN, flagC, flagV};
    }

    /**
     * Converts a binary string to a 32 bit char array
     * @param s - binary string to convert
     * @return 32 bit char array representation
     */
    public static char[] convertString(String s) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        int iterations = s.length() > 32 ? 32 : s.length();
        for (int i = 1; i <= iterations; i++) num[32-i] = s.charAt(s.length()-i);
        return num;
    }

    /**
     * Takes the two's complement of a number
     * @param num - number to take two's complement of
     * @return two's complement of the number
     */
    public static char[] complement(char[] num) {
        for(int i = 0; i < num.length; i++) {
            if (num[i] == '0') num[i] = '1';
            else num[i] = '0';
        }
        return add(num, convertString("1"));
    }

    /**
     * Adds two numbers together
     * @param one - first number to add
     * @param two - second number to add
     * @return char array of their sum
     */
    public static char[] add(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        char carry = '0';
        // Covers all possible additive cases and sets carry bit
        for (int i = 31; i >= 0; i--) {
            if (one[i] == '0' && two[i] == '0' && carry == '0') {
                num[i] = '0';
                carry = '0';
            } else if (one[i] == '0' && two[i] == '0' && carry == '1') {
                num[i] = '1';
                carry = '0';
            } else if (one[i] == '0' && two[i] == '1' && carry == '0') {
                num[i] = '1';
                carry = '0';
            } else if (one[i] == '0' && two[i] == '1' && carry == '1') {
                num[i] = '0';
                carry = '1';
            } else if (one[i] == '1' && two[i] == '0' && carry == '0') {
                num[i] = '1';
                carry = '0';
            } else if (one[i] == '1' && two[i] == '0' && carry == '1') {
                num[i] = '0';
                carry = '1';
            } else if (one[i] == '1' && two[i] == '1' && carry == '0') {
                num[i] = '0';
                carry = '1';
            } else if (one[i] == '1' && two[i] == '1' && carry == '1') {
                num[i] = '1';
                carry = '1';
            }
        }

        int numOnes = 0;
        for (int i = 0; i < 32; i++) {
            if (num[i] == '1') numOnes++;
        }

        // Sets flags after addition
        if (numOnes == 0) flagZ = true;
        else flagZ = false;

        if (num[0] == '1') flagN = true;
        else flagN = false;

        if (carry == '1') flagC = true;
        else flagC = false;

        if ((one[0] == '0' && two[0] == '0' && num[0] == '1') || (one[0] == '1' && two[0] == '1' && num[0] == '0')) flagV = true;
        else flagV = false;

        return num;
    }

    /**
     * Substracts two numbers
     * @param one - number to subtract from
     * @param two - number to subtract
     * @return char array of their difference
     */
    public static char[] subtract(char[] one, char[] two) {
        return add(one, complement(two));
    }
}
