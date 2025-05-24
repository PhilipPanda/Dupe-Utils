package xyz.dupe_utils.gui.swing;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.utils.PacketScheduler;

import javax.swing.*;
import java.awt.*;

public class ClickSlotPacketUI {
    public void open() {
        JFrame frame = new JFrame("Send Click Slot Packet");
        frame.setSize(500, 430);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        int y = 20;
        JTextField syncIdField = new JTextField();
        JTextField revisionField = new JTextField();
        JTextField slotField = new JTextField();
        JTextField buttonField = new JTextField();
        JTextField timesField = new JTextField("1");

        JComboBox<String> actionCombo = new JComboBox<>(new String[] {
                "PICKUP", "QUICK_MOVE", "SWAP", "CLONE", "THROW", "QUICK_CRAFT", "PICKUP_ALL"
        });

        JCheckBox delayCheckbox = new JCheckBox("Delay");
        JLabel statusLabel = new JLabel();

        y = addLabeledInput(frame, "Sync ID:", syncIdField, y);
        y = addLabeledInput(frame, "Revision:", revisionField, y);
        y = addLabeledInput(frame, "Slot:", slotField, y);
        y = addLabeledInput(frame, "Button:", buttonField, y);

        JLabel actionLabel = new JLabel("Action:");
        actionLabel.setBounds(30, y, 100, 20);
        actionCombo.setBounds(140, y, 120, 24);
        frame.add(actionLabel);
        frame.add(actionCombo);
        y += 35;

        delayCheckbox.setBounds(280, y - 35, 100, 20);
        frame.add(delayCheckbox);

        y = addLabeledInput(frame, "Repeat Count:", timesField, y);

        JButton sendBtn = new JButton("Send Packet");
        sendBtn.setBounds(280, y, 160, 30);
        frame.add(sendBtn);

        statusLabel.setBounds(30, y + 35, 420, 25);
        frame.add(statusLabel);

        sendBtn.addActionListener(e -> {
            try {
                int syncId = Integer.parseInt(syncIdField.getText());
                int revision = Integer.parseInt(revisionField.getText());
                int slot = Integer.parseInt(slotField.getText());
                int button = Integer.parseInt(buttonField.getText());
                int times = Integer.parseInt(timesField.getText());
                SlotActionType action = SlotActionType.valueOf((String) actionCombo.getSelectedItem());

                Packet<?> packet = new ClickSlotC2SPacket(syncId, revision, slot, button, action, ItemStack.EMPTY, new Int2ObjectArrayMap<>());
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