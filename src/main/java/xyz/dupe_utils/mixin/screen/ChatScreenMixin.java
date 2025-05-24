package xyz.dupe_utils.mixin.screen;

import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.dupe_utils.utils.CommandManager;

import static xyz.dupe_utils.DupeUtils.mc;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
        if (chatText.startsWith(".")) {
            CommandManager.handle(chatText);
            mc.inGameHud.getChatHud().addToMessageHistory(chatText);
            mc.setScreen(null);
            ci.cancel();
        }
    }
}
