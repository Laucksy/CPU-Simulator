package Simulator;

import Instructions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimulatorGUI {
    private Simulator simulator;
    private Frame stateFrame;
    private Frame memFrame;
    private Timer timer;

    /**
     * Initializes Simulator GUI and creates the two AWT Frames (one for the memory display and one for CPU state
     * @param s - Simulator that is being represented (used for passing commands to the simulator like execute)
     */
    public SimulatorGUI(Simulator s) {
        this.simulator = s;

        this.stateFrame = new Frame("CPU Simulator State");
        this.stateFrame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        this.stateFrame.setSize(800,600);
        this.stateFrame.setLayout(null);
        this.stateFrame.setVisible(true);

        this.memFrame = new Frame("Memory State");
        this.memFrame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        this.memFrame.setSize(400,800);
        this.memFrame.setLayout(null);
        this.memFrame.setVisible(true);

        this.timer = null;
    }

    /**
     * Updates both AWT Frames with new information from the simulator
     * @param memory - Byte array of the entire memory in the simulator
     * @param registers - Array of all of the registers in the CPU and their values
     * @param pc - Current program counter in the CPU
     * @param instruction - Current instruction in the CPU
     * @param flags - Array of flags in the CPU
     */
    public void draw(Byte[] memory, char[][] registers, int pc, Instruction instruction, boolean[] flags) {
        this.showState(registers, pc, instruction, flags);
        this.showMemory(memory);
    }

    /**
     * Updates the GUI Components in the State AWT Frame
     * @param registers - Array of all the registers in the CPU and their values
     * @param pc - Current program counter in the CPU
     * @param instruction - Current instruction in the CPU
     * @param flags - Array of flags in the CPU
     */
    private void showState(char[][] registers, int pc, Instruction instruction, boolean[] flags) {
        this.stateFrame.removeAll();

        Label regLabel = new Label("Registers:");
        regLabel.setBounds(10, 10, 100, 20);
        this.stateFrame.add(regLabel);

        String regs = "";
        for (int i = 0; i < registers.length; i++) {
            String r = "";
            for (char c : registers[i]) r = r + c;
            regs = regs + i + ":\t" + r + "\n";
        }
        TextArea registerDisplay = new TextArea(regs);
        registerDisplay.setBounds(10,40, 450,300);
        this.stateFrame.add(registerDisplay);

        Label pcLabel = new Label("Program Counter: 0x" + Integer.toHexString(pc) + " (" + pc + ")");
        pcLabel.setBounds(470,10, 200,20);
        this.stateFrame.add(pcLabel);

        Label instructionHeaderLabel = new Label("Current Instruction: ");
        instructionHeaderLabel.setBounds(470,40, 320,15);
        this.stateFrame.add(instructionHeaderLabel);

        Label instructionLabel = new Label(instruction.getBinary());
        instructionLabel.setBounds(470,55, 320,15);
        this.stateFrame.add(instructionLabel);

        Label instructionPlainLabel = new Label(instruction.getPlain());
        instructionPlainLabel.setBounds(470,70, 320,15);
        this.stateFrame.add(instructionPlainLabel);

        Label flagHeaderLabel = new Label("Flags:");
        flagHeaderLabel.setBounds(470,90, 320,15);
        this.stateFrame.add(flagHeaderLabel);

        Label flagZLabel = new Label("Z: " + (flags[0] ? "1" : "0"));
        flagZLabel.setBounds(470,105, 320,15);
        this.stateFrame.add(flagZLabel);

        Label flagNLabel = new Label("N: " + (flags[1] ? "1" : "0"));
        flagNLabel.setBounds(470,120, 320,15);
        this.stateFrame.add(flagNLabel);

        Label flagCLabel = new Label("C: " + (flags[2] ? "1" : "0"));
        flagCLabel.setBounds(470,135, 320,15);
        this.stateFrame.add(flagCLabel);

        Label flagVLabel = new Label("V: " + (flags[3] ? "1" : "0"));
        flagVLabel.setBounds(470,150, 320,15);
        this.stateFrame.add(flagVLabel);

        Button stepOne = new Button("Step One Instruction");
        stepOne.setBounds(470,170,300,30);
        stepOne.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                simulator.executeInstruction();
                if (simulator.getCurrentInstruction().getMnemonic().equals("HALT")) {
                    if (timer != null) timer.stop();
                    simulator.writeToFile();
                }
            }
        });
        this.stateFrame.add(stepOne);

        Button fastButton = new Button("Run Fast");
        fastButton.setBounds(480,210,80,30);
        fastButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ActionListener interval = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        simulator.executeInstruction();
                        if (simulator.getCurrentInstruction().getMnemonic().equals("HALT")) {
                            if (timer != null) timer.stop();
                            simulator.writeToFile();
                        }
                    }
                };
                timer = new Timer(250, interval);
                timer.start();
            }
        });
        this.stateFrame.add(fastButton);

        Button mediumButton = new Button("Run Medium");
        mediumButton.setBounds(570,210,100,30);
        mediumButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ActionListener interval = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        simulator.executeInstruction();
                        if (simulator.getCurrentInstruction().getMnemonic().equals("HALT")) {
                            if (timer != null) timer.stop();
                            simulator.writeToFile();
                        }
                    }
                };
                timer = new Timer(500, interval);
                timer.start();
            }
        });
        this.stateFrame.add(mediumButton);

        Button slowButton = new Button("Run Slow");
        slowButton.setBounds(680,210,80,30);
        slowButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ActionListener interval = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        simulator.executeInstruction();
                        if (simulator.getCurrentInstruction().getMnemonic().equals("HALT")) {
                            if (timer != null) timer.stop();
                            simulator.writeToFile();
                        }
                    }
                };
                timer = new Timer(1000, interval);
                timer.start();
            }
        });
        this.stateFrame.add(slowButton);

        Button stopButton = new Button("Stop");
        stopButton.setBounds(480,250,80,30);
        stopButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (timer != null) timer.stop();
            }
        });
        this.stateFrame.add(stopButton);

        Button resetButton = new Button("Reset");
        resetButton.setBounds(680,250,80,30);
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (timer != null) timer.stop();
                simulator.reset();
            }
        });
        this.stateFrame.add(resetButton);

        Button writeButton = new Button("Write to Image File");
        writeButton.setBounds(470,290,300,30);
        writeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                simulator.writeToFile();
            }
        });
        this.stateFrame.add(writeButton);
    }

    /**
     * Updates the GUI Components in the Memory AWT Frame
     * @param memory - Byte array of the entire memory in the simulator
     */
    private void showMemory(Byte[] memory) {
        this.memFrame.removeAll();

        Label memLabel = new Label("Memory:");
        memLabel.setBounds(10, 10, 100, 20);
        this.memFrame.add(memLabel);

        String memData = "";
        for (int i = 0; i < memory.length; i++) {
            String binary = Integer.toBinaryString(Integer.parseInt(memory[i].toString()));
            while (binary.length() < 8) binary = "0" + binary;
            if (binary.length() > 8) binary = binary.substring(binary.length() - 8);
            memData = memData + "0x" +  Integer.toHexString(i) + ":\t" + binary + "\n";
        }
        TextArea memoryDisplay = new TextArea(memData);
        memoryDisplay.setBounds(10,40, 380,750);
        this.memFrame.add(memoryDisplay);
    }
}
