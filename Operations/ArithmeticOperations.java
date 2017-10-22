package Operations;

public class ArithmeticOperations {
    private static boolean flagZ = false;
    private static boolean flagN = false;
    private static boolean flagC = false;
    private static boolean flagV = false;

    public static boolean[] getFlags() {
        return new boolean[] {flagZ, flagN, flagC, flagV};
    }

    public static char[] convertString(String s) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        int iterations = s.length() > 32 ? 32 : s.length();
        for (int i = 1; i <= iterations; i++) num[32-i] = s.charAt(s.length()-i);
        return num;
    }

    public static char[] complement(char[] num) {
        for(int i = 0; i < num.length; i++) {
            if (num[i] == '0') num[i] = '1';
            else num[i] = '0';
        }
        return add(num, convertString("1"));
    }

    public static char[] add(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        char carry = '0';
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

    public static char[] subtract(char[] one, char[] two) {
        return add(one, complement(two));
    }
}
