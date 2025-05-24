package xyz.dupe_utils.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public class CommandManager {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void handle(String input) {
        if (!input.startsWith(".")) return;

        String[] args = input.substring(1).trim().split(" ");
        if (args.length == 0) return;

        String command = args[0].toLowerCase(Locale.ROOT);

        if (command.equals("dupeutils")) {
            if (args.length < 2) {
                sendUsage();
                return;
            }

            String action = args[1].toLowerCase(Locale.ROOT);
            if (action.equals("enable")) {
                SharedVariables.enabled = true;
                sendToggleMessage(true);
            } else if (action.equals("disable")) {
                SharedVariables.enabled = false;
                sendToggleMessage(false);
            } else {
                sendUsage();
            }
        }
    }

    private static void sendUsage() {
        if (mc.player == null) return;

        Text msg = Text.literal("[")
                .formatted(Formatting.WHITE)
                .append(Text.literal("DupeUtils").formatted(Formatting.GOLD))
                .append(Text.literal("] Usage: ").formatted(Formatting.WHITE))
                .append(Text.literal(".dupeutils enable | disable").formatted(Formatting.GRAY));

        mc.player.sendMessage(msg, false);
    }

    private static void sendToggleMessage(boolean enabled) {
        if (mc.player == null) return;

        Text message = Text.literal("[")
                .formatted(Formatting.WHITE)
                .append(Text.literal("DupeUtils").formatted(Formatting.GOLD))
                .append(Text.literal("] ").formatted(Formatting.WHITE))
                .append(Text.literal(enabled ? "Enabled" : "Disabled").formatted(enabled ? Formatting.GREEN : Formatting.RED));

        mc.player.sendMessage(message, false);
    }

    private static void sendMsg(String msg, Formatting color) {
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal(msg).formatted(color), false);
        }
    }
}
