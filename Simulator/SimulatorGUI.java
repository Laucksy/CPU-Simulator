package Simulator;

import java.awt.*;
import java.awt.event.*;

public class SimulatorGUI {
    private Simulator simulator;
    private Frame stateFrame;
    private Frame memFrame;

    public SimulatorGUI(Simulator s) {
        this.simulator = s;

        this.stateFrame = new Frame("CPU Simulator State");
        this.stateFrame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        this.stateFrame.setSize(800,600);
        this.stateFrame.setLayout(null);
        this.stateFrame.setVisible(true);
    }

    public void draw(Byte[] memory, char[][] registers, int pc, String instruction, boolean[] flags) {
        this.showState(registers, pc, instruction, flags);
        this.showMemory(memory);
    }

    public void showState(char[][] registers, int pc, String instruction, boolean[] flags) {
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

        Label pcLabel = new Label("Program Counter: " + Integer.toHexString(pc) + " (" + pc + ")");
        pcLabel.setBounds(470,10, 200,20);
        this.stateFrame.add(pcLabel);

        Label instructionHeaderLabel = new Label("Current Instruction: ");
        instructionHeaderLabel.setBounds(470,40, 320,15);
        this.stateFrame.add(instructionHeaderLabel);

        Label instructionLabel = new Label(instruction);
        instructionLabel.setBounds(470,55, 320,15);
        this.stateFrame.add(instructionLabel);

        Label flagHeaderLabel = new Label("Flags:");
        flagHeaderLabel.setBounds(470,80, 320,15);
        this.stateFrame.add(flagHeaderLabel);

        Label flagZLabel = new Label("Z: " + (flags[0] ? "1" : "0"));
        flagZLabel.setBounds(470,95, 320,15);
        this.stateFrame.add(flagZLabel);

        Label flagNLabel = new Label("N: " + (flags[1] ? "1" : "0"));
        flagNLabel.setBounds(470,110, 320,15);
        this.stateFrame.add(flagNLabel);

        Label flagCLabel = new Label("C: " + (flags[2] ? "1" : "0"));
        flagCLabel.setBounds(470,125, 320,15);
        this.stateFrame.add(flagCLabel);

        Label flagVLabel = new Label("V: " + (flags[3] ? "1" : "0"));
        flagVLabel.setBounds(470,140, 320,15);
        this.stateFrame.add(flagVLabel);

        Button stepOne = new Button("Step One Instruction");
        stepOne.setBounds(470,160,300,30);
        stepOne.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                simulator.executeInstruction();
            }
        });
        this.stateFrame.add(stepOne);
    }

    public void showMemory(Byte[] memory) {

    }
}
