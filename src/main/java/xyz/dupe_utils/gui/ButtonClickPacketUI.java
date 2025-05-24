package xyz.dupe_utils.gui;

import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.utils.PacketScheduler;

import javax.swing.*;
import java.awt.*;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ButtonClickC2SPacket;

public class ButtonClickPacketUI {
    public void open() {
        JFrame frame = new JFrame("Send Button Click Packet");
        frame.setSize(500, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        int y = 20;
        JTextField syncIdField = new JTextField();
        JTextField buttonIdField = new JTextField();
        JTextField timesField = new JTextField("1");

        JCheckBox delayCheckbox = new JCheckBox("Delay");
        JLabel statusLabel = new JLabel();

        y = addLabeledInput(frame, "Sync ID:", syncIdField, y);
        y = addLabeledInput(frame, "Button ID:", buttonIdField, y);

        delayCheckbox.setBounds(280, y - 35, 100, 20);
        frame.add(delayCheckbox);

        y = addLabeledInput(frame, "Repeat Count:", timesField, y);

        JButton sendBtn = new JButton("Send Packet");
        sendBtn.setBounds(280, y, 160, 30);
        frame.add(sendBtn);

        statusLabel.setBounds(30, y + 35, 410, 25);
        frame.add(statusLabel);

        sendBtn.addActionListener(e -> {
            try {
                int syncId = Integer.parseInt(syncIdField.getText());
                int buttonId = Integer.parseInt(buttonIdField.getText());
                int times = Integer.parseInt(timesField.getText());

                Packet<?> packet = new ButtonClickC2SPacket(syncId, buttonId);
                Runnable task = () -> {
                    if (DupeUtils.mc.getNetworkHandler() != null) {
                        DupeUtils.mc.getNetworkHandler().sendPacket(packet);
                    }
                };

                for (int i = 0; i < times; i++) {
                    if (delayCheckbox.isSelected()) PacketScheduler.queue(task, i * 100L);
                    else task.run();
                }

                statusLabel.setText("✔ Sent successfully");
                statusLabel.setForeground(Color.GREEN.darker());
            } catch (Exception ex) {
                statusLabel.setText("✘ Invalid input or not connected");
                statusLabel.setForeground(Color.RED.darker());
            }
        });

        frame.setVisible(true);
    }

    private int addLabeledInput(JFrame frame, String label, JTextField field, int y) {
        JLabel jlabel = new JLabel(label);
        jlabel.setBounds(30, y, 100, 20);
        field.setBounds(140, y, 120, 24);
        frame.add(jlabel);
        frame.add(field);
        return y + 35;
    }
}