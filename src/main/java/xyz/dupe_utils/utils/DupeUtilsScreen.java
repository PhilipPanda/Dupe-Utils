package xyz.dupe_utils.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.gui.swing.FabricatePacketUI;

public class DupeUtilsScreen {

    public static void createText(MinecraftClient mc, DrawContext context, TextRenderer textRenderer) {
        if (mc.player == null || mc.player.currentScreenHandler == null) return;

        int textX = 180;
        int textY = 15;
        int spacing = 20;

        context.drawText(textRenderer, "Sync Id: " + mc.player.currentScreenHandler.syncId, textX, textY, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Revision: " + mc.player.currentScreenHandler.getRevision(), textX, textY + spacing, 0xFFFFFFFF, false);
    }

    public static Text coloredBoolText(String prefix, boolean value) {
        return Text.literal(prefix)
                .append(Text.literal(value ? "True" : "False")
                                .formatted(value ? Formatting.GREEN : Formatting.RED));
    }
}
