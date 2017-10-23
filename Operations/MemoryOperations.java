package Operations;

public class MemoryOperations {
    /**
     * Loads a value from memory into a register
     * @param memory - Byte array of complete memory of the CPU
     * @param reg - address of memory value to load
     * @param offset - offset of the address
     * @param bits - number of bits to load (8, 16, 32)
     * @return 32 bit char array to load into register
     */
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

    /**
     * Stores a value from register into memory
     * @param memory - Byte array of complete memory of the CPU
     * @param value - register value to store
     * @param reg - address in memory to store at
     * @param offset - offset from the address
     * @param bits - number of bits to store (8. 16, 32)
     */
    public static void store(Byte[] memory, char[] value, char[] reg, char[] offset, int bits) {
        char[] address = ArithmeticOperations.add(reg, offset);
        String addr = "";
        for (int i = 0; i < address.length; i++) addr = addr + address[i];


        int mem = Integer.parseInt(addr, 2);

        String val = "";
        for (int i = 0; i < value.length; i++) val = val + value[i];
        Byte byte1 = binaryToByte(val.substring(0, 8));
        Byte byte2 = binaryToByte(val.substring(8, 16));
        Byte byte3 = binaryToByte(val.substring(16, 24));
        Byte byte4 = binaryToByte(val.substring(24));

        if (bits == 8) memory[mem] = byte4;
        else if (bits == 16) {
            memory[mem] = byte3;
            memory[mem+1] = byte4;
        }
        else {
            memory[mem] = byte1;
            memory[mem+1] = byte2;
            memory[mem+2] = byte3;
            memory[mem+3] = byte4;
        }
    }

    /**
     * Converts a Byte object to an 8 bit binary string
     * @param b - Byte object to convert
     * @return 8 bit binary string representation of Byte
     */
    public static String byteToBinary(Byte b) {
        String binary = Integer.toBinaryString(Integer.parseInt(b.toString()));
        while (binary.length() < 8) binary = "0" + binary;
        if (binary.length() > 8) binary = binary.substring(binary.length() - 8);
        return binary;
    }

    /**
     * Converts an 8 bit binary string to a Byte object
     * @param b - 8 bit binary string to convert
     * @return Byte object representation of binary string
     */
    public static Byte binaryToByte(String b) {
        if (b.charAt(0) == '0') return Byte.parseByte(b, 2);
        else return Byte.parseByte(""+ (Integer.parseInt(b, 2) - 256), 10);
    }

    /**
     * Converts a char array to a String
     * @param arr - char array to convert
     * @return String representation of char array
     */
    public static String printCharArray(char[] arr) {
        String result = "";
        for (int i = 0; i < arr.length; i++) result = result + arr[i];
        return result;
    }
}
