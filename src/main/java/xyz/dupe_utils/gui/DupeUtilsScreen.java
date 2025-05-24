package xyz.dupe_utils.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.utils.SharedVariables;

public class DupeUtilsScreen {
    private static final int BUTTON_WIDTH = 160;

    public static void createText(MinecraftClient mc, DrawContext context, TextRenderer textRenderer) {
        if (mc.player == null || mc.player.currentScreenHandler == null) return;

        int textX = 180;
        int textY = 15;
        int spacing = 20;

        context.drawText(textRenderer, "Sync Id: " + mc.player.currentScreenHandler.syncId, textX, textY, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Revision: " + mc.player.currentScreenHandler.getRevision(), textX, textY + spacing, 0xFFFFFFFF, false);
    }

    private static Text coloredBoolText(String prefix, boolean value) {
        return Text.literal(prefix)
                .append(Text.literal(value ? "True" : "False")
                        .formatted(value ? Formatting.GREEN : Formatting.RED));
    }

    public static void createWidgets(MinecraftClient mc, Screen screen) {
        screen.addDrawableChild(ButtonWidget.builder(Text.of("Close Without Packet"), b -> mc.setScreen(null))
                .width(BUTTON_WIDTH).position(5, 5).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("De-Sync"), b -> {
            if (mc.getNetworkHandler() != null && mc.player != null) {
                mc.getNetworkHandler().sendPacket(new net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            } else {
                DupeUtils.LOGGER.warn("NetworkHandler or player was null in 'De-Sync'");
            }
        }).width(BUTTON_WIDTH).position(5, 35).build());

        screen.addDrawableChild(ButtonWidget.builder(coloredBoolText("Send Packets: ", SharedVariables.sendUIPackets), b -> {
            SharedVariables.sendUIPackets = !SharedVariables.sendUIPackets;
            b.setMessage(coloredBoolText("Send Packets: ", SharedVariables.sendUIPackets));
        }).width(BUTTON_WIDTH).position(5, 65).build());

        screen.addDrawableChild(ButtonWidget.builder(coloredBoolText("Delay Packets: ", SharedVariables.delayUIPackets), b -> {
            SharedVariables.delayUIPackets = !SharedVariables.delayUIPackets;
            b.setMessage(coloredBoolText("Delay Packets: ", SharedVariables.delayUIPackets));
            if (!SharedVariables.delayUIPackets && !SharedVariables.delayedUIPackets.isEmpty() && mc.getNetworkHandler() != null) {
                SharedVariables.delayedUIPackets.forEach(mc.getNetworkHandler()::sendPacket);
                if (mc.player != null) {
                    mc.player.sendMessage(Text.of("Sent " + SharedVariables.delayedUIPackets.size() + " packets."), false);
                }
                SharedVariables.delayedUIPackets.clear();
            }
        }).width(BUTTON_WIDTH).position(5, 95).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Save GUI"), b -> {
            if (mc.player != null) {
                SharedVariables.storedScreen = mc.currentScreen;
                SharedVariables.storedScreenHandler = mc.player.currentScreenHandler;
            }
        }).width(BUTTON_WIDTH).position(5, 125).build());

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Disconnect And Send Packets"), b -> {
            SharedVariables.delayUIPackets = false;
            if (mc.getNetworkHandler() != null) {
                SharedVariables.delayedUIPackets.forEach(mc.getNetworkHandler()::sendPacket);
                mc.getNetworkHandler().getConnection().disconnect(Text.of("Disconnecting (DUPE-UTILS)"));
            }
            SharedVariables.delayedUIPackets.clear();
        }).width(BUTTON_WIDTH).position(5, 155).build());

        ButtonWidget fabricatePacketButton = ButtonWidget.builder(Text.of("Fabricate Packet"), b -> FabricatePacketUI.open())
                .width(BUTTON_WIDTH).position(5, 185).build();
        fabricatePacketButton.active = !MinecraftClient.IS_SYSTEM_MAC;
        screen.addDrawableChild(fabricatePacketButton);

        screen.addDrawableChild(ButtonWidget.builder(Text.of("Copy GUI Title JSON"), b -> {
            try {
                if (mc.currentScreen == null) throw new IllegalStateException("Screen is null");
                mc.keyboard.setClipboard(Text.Serialization.toJsonString(
                        mc.currentScreen.getTitle(),
                        mc.getNetworkHandler() != null ? mc.getNetworkHandler().getRegistryManager() : MinecraftClient.getInstance().getServer().getRegistryManager()
                ));
            } catch (Exception e) {
                DupeUtils.LOGGER.error("Failed to copy GUI title JSON", e);
            }
        }).width(BUTTON_WIDTH).position(5, 215).build());
    }
}
