package Instructions;

public class InstructionList {
    private static String[][] list = {
            {"ADD", "R", "000000", "add"},
            {"SUB", "R", "000001", "subtract"},
            {"ADDI", "I", "000010", "add immediate"},
            {"SUBI", "I", "000011", "subtract immediate"},
            {"ADDS", "R", "000100", "add & set flags"},
            {"SUBS", "R", "000101", "subtract & set flags"},
            {"ADDIS", "I", "000110", "add immediate & set flags"},
            {"SUBIS", "I", "000111", "subtract immediate & set flags"},

            {"AND", "R", "001000", "and"},
            {"IOR", "R", "001001", "inclusive or"},
            {"EOR", "R", "001010", "exclusive or"},
            {"ANDI", "I", "001011", "and immediate"},
            {"IORI", "I", "001100", "inclusive or immediate"},
            {"EORI", "I", "001101", "exclusive or immediate"},
            {"LSL", "R", "001110", "logical shift left"},
            {"LSR", "R", "001111", "logical shift right"},

            {"LDUR", "M", "010000", "load register"},
            {"STUR", "M", "010001", "store register"},
            {"LDURW", "M", "010010", "load signed word"},
            {"STURW", "M", "010011", "store word"},
            {"LDURH", "M", "010100", "load half"},
            {"STURH", "M", "010101", "store half"},
            {"LDURB", "M", "010110", "load byte"},
            {"STURB", "M", "010111", "store byte"},

            {"CBZ", "B", "011000", "compare and branch on equal 0"},
            {"CBNZ", "B", "011001", "compare and branch on not equal 0"},
            {"B.cond", "B", "011010", "branch conditionally"},

            {"B", "B", "011011", "branch"},
            {"BR", "B", "011100", "branch to register"},
            {"BL", "B", "011101", "branch with link"},

            {"PUSH", "O", "100000", "push"},
            {"POP", "O", "100001", "pop"},
            {"HALT", "O", "100010", "halt"},
            {"NOP", "O", "100100", "nop"}
    };

    public static String getOpcodeFromMnemonic(String mnemonic) {
        for(String[] instruction : list) {
            if (instruction[0].equals(mnemonic)) return instruction[2];
        }
        return "";
    }

    public static String getTypeFromMnemonic(String mnemonic) {
        for(String[] instruction : list) {
            if (instruction[0].equals(mnemonic)) return instruction[1];
        }
        return "";
    }
}
