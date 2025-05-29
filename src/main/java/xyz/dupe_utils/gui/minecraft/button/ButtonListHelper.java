package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.text.Text;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.gui.swing.FabricatePacketUI;
import xyz.dupe_utils.utils.SharedVariables;

import static xyz.dupe_utils.DupeUtils.mc;
import static xyz.dupe_utils.utils.DupeUtilsScreen.coloredBoolText;

public class ButtonListHelper<T extends Screen> extends ButtonList<T> {

    public ButtonListHelper(Class<T> screen) {
        super(screen);
    }

    public void createButton() {
        add(new Button(Text.of("Close without packet"), (button) -> {
            mc.setScreen(null);
        }).position(5, 5));

        add(new Button(Text.of("De-Sync"), (button) -> {
            if (mc.getNetworkHandler() != null && mc.player != null) {
                mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            } else {
                DupeUtils.LOGGER.warn("NetworkHandler or player was null in 'De-Sync'");
            }
        }).position(5, 5));

        add(new Button(coloredBoolText("Send packets: ", SharedVariables.sendUIPackets), (button) -> {
            SharedVariables.sendUIPackets = !SharedVariables.sendUIPackets;

            Text newText = coloredBoolText("Send packets: ", SharedVariables.sendUIPackets);
            button.setText(newText);
            clear();
        }).position(5, 5));

        add(new Button(coloredBoolText("Delay packets: ", SharedVariables.delayUIPackets), (button) -> {
            SharedVariables.delayUIPackets = !SharedVariables.delayUIPackets;
            button.setText(coloredBoolText("Delay packets: ", SharedVariables.delayUIPackets));

            if (!SharedVariables.delayUIPackets && !SharedVariables.delayedUIPackets.isEmpty() && mc.getNetworkHandler() != null) {
                SharedVariables.delayedUIPackets.forEach(mc.getNetworkHandler()::sendPacket);
                if (mc.player != null) {
                    mc.player.sendMessage(Text.of("Sent " + SharedVariables.delayedUIPackets.size() + " packets."), false);
                }
                SharedVariables.delayedUIPackets.clear();
            }
            clear();
        }).position(5, 5));

        add(new Button(Text.of("Save GUI"), button -> {
            if (mc.player != null) {
                SharedVariables.storedScreen = mc.currentScreen;
                SharedVariables.storedScreenHandler = mc.player.currentScreenHandler;
            }
        }).position(5, 5));

        add(new Button(Text.of("Disconnect And Send Packets"), b -> {
            SharedVariables.delayUIPackets = false;
            if (mc.getNetworkHandler() != null) {
                SharedVariables.delayedUIPackets.forEach(mc.getNetworkHandler()::sendPacket);
                mc.getNetworkHandler().getConnection().disconnect(Text.of("Disconnecting (DUPE-UTILS)"));
            }
            SharedVariables.delayedUIPackets.clear();
        }).position(5, 5));

        Button fabricatePacketButton = new Button(Text.of("Fabricate Packet"), button -> FabricatePacketUI.open()).position(5, 5);
        fabricatePacketButton.setVisible(!MinecraftClient.IS_SYSTEM_MAC);

        add(fabricatePacketButton);

        add(new Button(Text.of("Copy GUI Title JSON"), button -> {
            try {
                if (mc.currentScreen == null) throw new IllegalStateException("Screen is null");
                mc.keyboard.setClipboard(Text.Serialization.toJsonString(mc.currentScreen.getTitle(), mc.getNetworkHandler() != null ? mc.getNetworkHandler().getRegistryManager() : MinecraftClient.getInstance().getServer().getRegistryManager()));
            }
            catch (Exception e) {
                DupeUtils.LOGGER.error("Failed to copy GUI title JSON", e);
            }
        }).position(5, 5));
    }

    public void clear() {
        getButtons().clear();
        createButton();
        clearAndRecreate(mc.currentScreen);
    }
}
