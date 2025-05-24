package xyz.dupe_utils.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.utils.SharedVariables;

public class DupeUtilsScreen {
    public static void createText(MinecraftClient mc, DrawContext context, TextRenderer textRenderer) {
        if (mc.player == null || mc.player.currentScreenHandler == null) return;

        int textX = 180;
        int textY = 15;
        int spacing = 20;

        context.drawText(textRenderer, "Sync Id: " + mc.player.currentScreenHandler.syncId, textX, textY, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Revision: " + mc.player.currentScreenHandler.getRevision(), textX, textY + spacing, 0xFFFFFFFF, false);
    }


    public static void createWidgets(MinecraftClient mc, Screen screen) {
        screen.addDrawableChild(ButtonWidget.builder(Text.of("Close without packet"), b -> mc.setScreen(null)).width(115).position(5, 5).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("De-sync"), b -> {
            if (mc.getNetworkHandler() != null && mc.player != null) {
                mc.getNetworkHandler().sendPacket(new net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            } else {
                DupeUtils.LOGGER.warn("NetworkHandler or player was null in 'De-sync'");
            }
        }).width(115).position(5, 35).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Send packets: " + SharedVariables.sendUIPackets), b -> {
            SharedVariables.sendUIPackets = !SharedVariables.sendUIPackets;
            b.setMessage(Text.of("Send packets: " + SharedVariables.sendUIPackets));
        }).width(115).position(5, 65).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Delay packets: " + SharedVariables.delayUIPackets), b -> {
            SharedVariables.delayUIPackets = !SharedVariables.delayUIPackets;
            b.setMessage(Text.of("Delay packets: " + SharedVariables.delayUIPackets));
            if (!SharedVariables.delayUIPackets && !SharedVariables.delayedUIPackets.isEmpty() && mc.getNetworkHandler() != null) {
                SharedVariables.delayedUIPackets.forEach(mc.getNetworkHandler()::sendPacket);
                if (mc.player != null) {
                    mc.player.sendMessage(Text.of("Sent " + SharedVariables.delayedUIPackets.size() + " packets."), false);
                }
                SharedVariables.delayedUIPackets.clear();
            }
        }).width(115).position(5, 95).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Save GUI"), b -> {
            if (mc.player != null) {
                SharedVariables.storedScreen = mc.currentScreen;
                SharedVariables.storedScreenHandler = mc.player.currentScreenHandler;
            }
        }).width(115).position(5, 125).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Disconnect and send packets"), b -> {
            SharedVariables.delayUIPackets = false;
            if (mc.getNetworkHandler() != null) {
                SharedVariables.delayedUIPackets.forEach(mc.getNetworkHandler()::sendPacket);
                mc.getNetworkHandler().getConnection().disconnect(Text.of("Disconnecting (UI-UTILS)"));
            }
            SharedVariables.delayedUIPackets.clear();
        }).width(160).position(5, 155).build());

        ButtonWidget fabricatePacketButton = ButtonWidget.builder(Text.of("Fabricate packet"), b -> FabricatePacketUI.open()).width(115).position(5, 185).build();
        fabricatePacketButton.active = !MinecraftClient.IS_SYSTEM_MAC;
        screen.addDrawableChild(fabricatePacketButton);

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Copy GUI Title JSON"), b -> {
            try {
                if (mc.currentScreen == null) throw new IllegalStateException("Screen is null");
                mc.keyboard.setClipboard(Text.Serialization.toJsonString(
                        mc.currentScreen.getTitle(),
                        MinecraftClient.getInstance().getServer().getRegistryManager()
                ));
            } catch (Exception e) {
                DupeUtils.LOGGER.error("Failed to copy GUI title JSON", e);
            }
        }).width(115).position(5, 215).build());
    }
}