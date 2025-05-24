package xyz.dupe_utils.mixin.screen;

import xyz.dupe_utils.utils.CommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
        if (chatText.startsWith(".")) {
            CommandManager.handle(chatText);
            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(chatText);
            MinecraftClient.getInstance().setScreen(null);
            ci.cancel();
        }
    }
}
