package xyz.dupe_utils.gui;

import javax.swing.*;

public class FabricatePacketUI {
    public static void open() {
        JFrame frame = new JFrame("Select Packet Type");
        frame.setSize(460, 120);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JButton clickSlotButton = new JButton("Click Slot Packet");
        clickSlotButton.setBounds(90, 30, 130, 25);
        clickSlotButton.addActionListener(e -> {
            frame.dispose();
            new ClickSlotPacketUI().open();
        });

        JButton buttonClickButton = new JButton("Button Click Packet");
        buttonClickButton.setBounds(240, 30, 140, 25);
        buttonClickButton.addActionListener(e -> {
            frame.dispose();
            new ButtonClickPacketUI().open();
        });

        frame.add(clickSlotButton);
        frame.add(buttonClickButton);
        frame.setVisible(true);
    }
}
