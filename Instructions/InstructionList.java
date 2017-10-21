package Instructions;

public class InstructionList {
    private static String[][] list = {
            {"NOP", "O", "000000", "nop"},
            {"HALT", "O", "000001", "halt"},
            {"PUSH", "O", "000010", "push"},
            {"POP", "O", "000011", "pop"},

            {"ADD", "R", "001000", "add"},
            {"SUB", "R", "001001", "subtract"},
            {"ADDI", "I", "001010", "add immediate"},
            {"SUBI", "I", "001011", "subtract immediate"},
            {"ADDS", "R", "001100", "add & set flags"},
            {"SUBS", "R", "001101", "subtract & set flags"},
            {"ADDIS", "I", "001110", "add immediate & set flags"},
            {"SUBIS", "I", "001111", "subtract immediate & set flags"},

            {"AND", "R", "010000", "and"},
            {"IOR", "R", "010001", "inclusive or"},
            {"EOR", "R", "010010", "exclusive or"},
            {"ANDI", "I", "010011", "and immediate"},
            {"IORI", "I", "010100", "inclusive or immediate"},
            {"EORI", "I", "010101", "exclusive or immediate"},
            {"LSL", "I", "010110", "logical shift left"},
            {"LSR", "I", "010111", "logical shift right"},

            {"LDUR", "M", "011000", "load register"},
            {"STUR", "M", "011001", "store register"},
            {"LDURW", "M", "011010", "load word"},
            {"STURW", "M", "011011", "store word"},
            {"LDURH", "M", "011100", "load half"},
            {"STURH", "M", "011101", "store half"},
            {"LDURB", "M", "011110", "load byte"},
            {"STURB", "M", "011111", "store byte"},

            {"CBZ", "B", "100000", "compare and branch on equal 0"},
            {"CBNZ", "B", "100001", "compare and branch on not equal 0"},
            {"B.cond", "B", "100010", "branch conditionally"},

            {"B", "B", "100011", "branch"},
            {"BR", "B", "100100", "branch to register"},
            {"BL", "B", "100101", "branch with link"}
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

    public static String getMnemonicFromOpcode(String opcode) {
        for(String[] instruction : list) {
            if (instruction[2].equals(opcode)) return instruction[0];
        }
        return "";
    }
}
