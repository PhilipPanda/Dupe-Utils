package xyz.dupe_utils;

import com.google.common.eventbus.EventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dupe_utils.gui.minecraft.extension.ExtensionHandler;
import xyz.dupe_utils.utils.SharedVariables;

import java.awt.*;

public class DupeUtils implements ClientModInitializer {
    public static Font monospace;
    public static Color darkWhite;

    public static KeyBinding restoreKey;
    public static KeyBinding unloadChunksKey;


    public static final Logger LOGGER = LoggerFactory.getLogger("dupe_utils");
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public static EventBus EVENT_BUS;

    @Override
    public void onInitializeClient() {
        restoreKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("Restore Saved GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "Dupe Utils")
        );

        unloadChunksKey = KeyBindingHelper.registerKeyBinding( // ✅ Register new key
                new KeyBinding("Unload Rendered Chunks", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "Dupe Utils")
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (restoreKey.wasPressed()) {
                if (SharedVariables.storedScreen != null && SharedVariables.storedScreenHandler != null && client.player != null) {
                    client.setScreen(SharedVariables.storedScreen);
                    client.player.currentScreenHandler = SharedVariables.storedScreenHandler;
                }
            }

            while (unloadChunksKey.wasPressed()) {
                if (mc.worldRenderer != null) {
                    mc.worldRenderer.reload();
                    if (mc.player != null) {
                        mc.player.sendMessage(Text.of("Chunks unloaded (render reload triggered)."), false);
                    }
                }
            }
        });

        EVENT_BUS = new EventBus();
        EVENT_BUS.register(new ExtensionHandler());

        if (!MinecraftClient.IS_SYSTEM_MAC) {
            System.setProperty("java.awt.headless", "false");
            monospace = new Font(Font.MONOSPACED, Font.PLAIN, 10);
            darkWhite = new Color(220, 220, 220);
        }
    }
}