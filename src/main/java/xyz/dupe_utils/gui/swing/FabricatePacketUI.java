package xyz.dupe_utils.gui.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FabricatePacketUI {

    public static void open() {
        JFrame frame = new JFrame("DupeUtils - Fabricate Packet");
        frame.setSize(460, 150);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        try {
            Image icon = ImageIO.read(FabricatePacketUI.class.getResourceAsStream("/assets/dupe_utils/icon.png"));
            frame.setIconImage(icon);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Icon image not found or invalid.");
        }

        applyDarkTheme();

        JButton clickSlotButton = new JButton("Click Slot Packet");
        clickSlotButton.setBounds(90, 40, 130, 30);
        styleButton(clickSlotButton);
        clickSlotButton.addActionListener(e -> {
            frame.dispose();
            new ClickSlotPacketUI().open();
        });

        JButton buttonClickButton = new JButton("Button Click Packet");
        buttonClickButton.setBounds(240, 40, 140, 30);
        styleButton(buttonClickButton);
        buttonClickButton.addActionListener(e -> {
            frame.dispose();
            new ButtonClickPacketUI().open();
        });

        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.add(clickSlotButton);
        frame.add(buttonClickButton);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button) {
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void applyDarkTheme() {
        UIManager.put("Button.background", new Color(50, 50, 50));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Panel.background", new Color(30, 30, 30));
        UIManager.put("Label.foreground", Color.LIGHT_GRAY);
        UIManager.put("TextField.background", new Color(45, 45, 45));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.caretForeground", Color.WHITE);
        UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(80, 80, 80)));
        UIManager.put("CheckBox.background", new Color(30, 30, 30));
        UIManager.put("CheckBox.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(45, 45, 45));
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("ComboBox.buttonBackground", new Color(50, 50, 50));
    }
}
