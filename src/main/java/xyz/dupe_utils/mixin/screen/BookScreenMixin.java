package xyz.dupe_utils.mixin.screen;

import xyz.dupe_utils.gui.DupeUtilsScreen;
import xyz.dupe_utils.utils.CommandManager;
import xyz.dupe_utils.utils.SharedVariables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.dupe_utils.DupeUtils;

import java.util.regex.Pattern;

@Mixin(BookScreen.class)
public class BookScreenMixin extends Screen {
    protected BookScreenMixin(Text title) {
        super(title);
    }
    @Unique
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo ci) {
        if (SharedVariables.enabled) {
            DupeUtilsScreen.createWidgets(mc, this);

            // create chat box
            TextFieldWidget addressField = new TextFieldWidget(textRenderer, 5, 245, 160, 20, Text.of("Chat ...")) {
                @Override
                public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                    if (keyCode == GLFW.GLFW_KEY_ENTER) {
                        if (this.getText().startsWith(".")) {
                            CommandManager.handle(this.getText());
                            this.setText("");
                            return true;
                        }


                        if (mc.getNetworkHandler() != null) {
                            if (this.getText().startsWith("/")) {
                                mc.getNetworkHandler().sendChatCommand(this.getText().replaceFirst(Pattern.quote("/"), ""));
                            } else {
                                mc.getNetworkHandler().sendChatMessage(this.getText());
                            }
                        } else {
                            DupeUtils.LOGGER.warn("Minecraft network handler (mc.getNetworkHandler()) was null while trying to send chat message from UI Utils.");
                        }

                        this.setText("");
                    }
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
            };
            addressField.setText("");
            addressField.setMaxLength(255);

            this.addDrawableChild(addressField);
        }
    }
}
