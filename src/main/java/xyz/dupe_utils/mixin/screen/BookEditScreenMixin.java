package xyz.dupe_utils.mixin.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.gui.swing.DupeUtilsScreen;
import xyz.dupe_utils.utils.CommandManager;
import xyz.dupe_utils.utils.SharedVariables;

import java.util.regex.Pattern;

import static xyz.dupe_utils.DupeUtils.mc;

@Mixin(BookEditScreen.class)
public class BookEditScreenMixin extends Screen {
    protected BookEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo ci) {
        if (SharedVariables.enabled) {
            DupeUtilsScreen.createWidgets(mc, this);

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
                            DupeUtils.LOGGER.warn("Minecraft network handler (mc.getNetworkHandler()) was null while trying to send chat message from Dupe Utils.");
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
