package Visualizer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Visualizer {
    private Frame memFrame;

    public static void main(String[] args) {
        try {
            ArrayList<Byte> memory = new ArrayList<Byte>();

            Scanner reader = new Scanner(new File(args[0]));
            reader.nextLine(); // Skip line of parameters
            // Fill memory array
            while (reader.hasNextLine()) {
                String[] line = reader.nextLine().split("\t");
                for (String l : line) {
                    if (l.charAt(0) == '0') {
                        memory.add(Byte.parseByte(l, 2));
                    } else {
                        memory.add(Byte.parseByte(""+ (Integer.parseInt(l, 2) - 256), 10));
                    }
                }
            }

            boolean hex = false;
            if (args.length == 2 && args[1].equals("hex")) hex = true;

            Visualizer v = new Visualizer();
            v.showMemory(memory, "0x" + Integer.toHexString(0), "0x" + Integer.toHexString(memory.size()), hex);
        } catch (FileNotFoundException e) {
            System.out.println("BBB" + e);
        }
    }

    /**
     * Creates Visualizer object and initializes AWT Frame
     */
    public Visualizer() {
        this.memFrame = new Frame("Memory State");
        this.memFrame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        this.memFrame.setSize(400,800);
        this.memFrame.setLayout(null);
        this.memFrame.setVisible(true);
    }

    /**
     * Adds all of the GUI components to the AWT Frame
     * @param memory - Byte array from the simulator that represents the entirety of memory
     * @param low - The start point for displaying the bounded memory
     * @param high - The end point for displaying the bounded memory
     * @param hex - True to display the memory as hex values, false to display as binary
     */
    private void showMemory(ArrayList<Byte> memory, String low, String high, boolean hex) {
        this.memFrame.removeAll();

        Label memLabel = new Label("Memory:");
        memLabel.setBounds(10, 10, 100, 20);
        this.memFrame.add(memLabel);

        TextField lowerRange = new TextField(low);
        lowerRange.setBounds(10,40, 100,30);
        this.memFrame.add(lowerRange);

        TextField higherRange = new TextField(high);
        higherRange.setBounds(120,40, 100,30);
        this.memFrame.add(higherRange);

        Button button = new Button("Update");
        button.setBounds(230,40,100,30);
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                showMemory(memory, lowerRange.getText(), higherRange.getText(), hex);
            }
        });
        this.memFrame.add(button);

        String memData = "";
        for (int i = Integer.parseInt(low.substring(2), 16); i < Integer.parseInt(high.substring(2), 16); i++) {
            if (hex) {
                String hexValue = Integer.toHexString(Integer.parseInt(memory.get(i).toString()));
                while (hexValue.length() < 2) hexValue = "0" + hexValue;
                if (hexValue.length() > 2) hexValue = hexValue.substring(hexValue.length() - 2);
                memData = memData + "0x" + Integer.toHexString(i) + ":\t0x" + hexValue + "\n";
            } else {
                String binary = Integer.toBinaryString(Integer.parseInt(memory.get(i).toString()));
                while (binary.length() < 8) binary = "0" + binary;
                if (binary.length() > 8) binary = binary.substring(binary.length() - 8);
                memData = memData + "0x" + Integer.toHexString(i) + ":\t" + binary + "\n";
            }
        }
        TextArea memoryDisplay = new TextArea(memData);
        memoryDisplay.setBounds(10,80, 380,710);
        this.memFrame.add(memoryDisplay);
    }
}
