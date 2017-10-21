package Simulator;

import java.util.*;
import java.io.*;

public class Simulator {
    private int wordsize;
    private int regcnt;
    private int maxmem;
    private char[][] registers;
    private Byte[] memory;

    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(new File(args[0]));
            String firstLine = reader.nextLine();
            String[] params = firstLine.split(":");

            String format = params[0].substring(1);
            int wordsize = Integer.parseInt(params[1].substring(3));
            int regcnt = Integer.parseInt(params[2].substring(3));
            int maxmem = Integer.parseInt(params[3].substring(5), 16);

            System.out.println(format + "," + wordsize + "," + regcnt + "," + maxmem);

            Simulator sim = new Simulator(wordsize, regcnt, maxmem);
            sim.execute(args[0], format);
        } catch (FileNotFoundException e) {
            System.out.println("BBB" + e);
        }
    }

    public Simulator(int w, int r, int m) {
        this.wordsize = w;
        this.regcnt = r;
        this.maxmem = m;
        this.registers = new char[r][w];
        this.memory = new Byte[1024];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < w; j++) {
                this.registers[i][j] = '0';
            }
        }

        for (int i = 0; i < 1024; i++) {
            this.memory[i] = Byte.decode("00000000");
        }
    }

    public void printState() {
        System.out.println("Wordsize: " + this.wordsize);
        System.out.println("Registers: " + this.regcnt);
        System.out.println("Max Memory: " + this.maxmem);
        System.out.println("Register List: ");
        for (char[] reg : this.registers) {
            for (char c : reg) System.out.print(c);
            System.out.println();
        }

        int printed = 0;
        System.out.println("Memory: ");
        for (Byte b : this.memory) {
            System.out.print(b);
            printed++;
            if (printed % 8 == 0 && printed % 32 != 0) System.out.print("\t");
            if (printed % 32 == 0) System.out.println();
        }
    }

    public void execute(String filename, String format) {
        try {
            Scanner reader = new Scanner(new File(filename));
            reader.nextLine();
            int bytesLoaded = 0;
            while (reader.hasNextLine()) {
                String[] line = reader.nextLine().split("\t");
                for (String l : line) {
                    // System.out.println(l);
                    if (l.charAt(0) == '0') {
                        // System.out.println(Byte.parseByte(l, 2));
                        this.memory[bytesLoaded] = Byte.parseByte(l, 2);
                    } else {
                        // System.out.println(Byte.parseByte(""+ (Integer.parseInt(l, 2) - 256), 10));
                        this.memory[bytesLoaded] = Byte.parseByte(""+ (Integer.parseInt(l, 2) - 256), 10);
                    }
                    bytesLoaded++;
                }
            }
            this.printState();
        } catch (Exception e) {
            System.out.println("AAA" + e);
        }
    }
}
