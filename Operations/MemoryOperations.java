package Operations;

public class MemoryOperations {
    public static char[] load(Byte[] memory, char[] reg, char[] offset, int bits) {
        System.out.println("LOADING " + printCharArray(reg) + " " + printCharArray(offset) + " " + bits);
        char[] address = ArithmeticOperations.add(reg, offset);
        String addr = "";
        for (int i = 0; i < address.length; i++) addr = addr + address[i];

        System.out.println(addr + " " + Integer.parseInt(addr, 2));

        String byte1 = byteToBinary(memory[Integer.parseInt(addr, 2)]);
        String byte2 = byteToBinary(memory[Integer.parseInt(addr, 2)+1]);
        String byte3 = byteToBinary(memory[Integer.parseInt(addr, 2)+2]);
        String byte4 = byteToBinary(memory[Integer.parseInt(addr, 2)+3]);

        if (bits == 8) return ArithmeticOperations.convertString(byte1);
        else if (bits == 16) return ArithmeticOperations.convertString(byte1 + byte2);
        else return ArithmeticOperations.convertString(byte1 + byte2 + byte3 + byte4);
    }

    public static String byteToBinary(Byte b) {
        String binary = Integer.toBinaryString(Integer.parseInt(b.toString()));
        while (binary.length() < 8) binary = "0" + binary;
        if (binary.length() > 8) binary = binary.substring(binary.length() - 8);
        return binary;
    }

    public static String printCharArray(char[] arr) {
        String result = "";
        for (int i = 0; i < arr.length; i++) result = result + arr[i];
        return result;
    }
}
