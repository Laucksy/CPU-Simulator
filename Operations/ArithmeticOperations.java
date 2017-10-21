package Operations;

public class ArithmeticOperations {
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

        return num;
    }

    public static char[] subtract(char[] one, char[] two) {
        return add(one, complement(two));
    }
}
