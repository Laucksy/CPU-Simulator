package Operations;

public class LogicalOperations {
    public static char[] and(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if (one[i] == '1' && two[i] == '1') num[i] = '1';
            else num[i] = '0';
        }
        return num;
    }

    public static char[] ior(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if (one[i] == '1' || two[i] == '1') num[i] = '1';
            else num[i] = '0';
        }
        return num;
    }

    public static char[] eor(char[] one, char[] two) {
        char[] num = new char[32];
        for (int i = 0; i < 32; i++) num[i] = '0';

        for (int i = 0; i < 32; i++) {
            if ((one[i] == '1' && two[i] == '0') || (one[i] == '0' && two[i] == '1')) num[i] = '1';
            else num[i] = '0';
        }
        return num;
    }
}
