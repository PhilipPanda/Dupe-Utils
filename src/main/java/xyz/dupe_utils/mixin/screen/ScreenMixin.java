package xyz.dupe_utils.mixin.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.event.ScreenEvent;
import xyz.dupe_utils.gui.minecraft.panel.ClickableWidgetPanel;
import xyz.dupe_utils.utils.DupeUtilsScreen;
import xyz.dupe_utils.mixin.accessor.ScreenAccessor;
import xyz.dupe_utils.utils.CommandManager;
import xyz.dupe_utils.utils.SharedVariables;
import xyz.dupe_utils.utils.interfaces.IScreen;

import java.util.List;
import java.util.regex.Pattern;

import static xyz.dupe_utils.DupeUtils.EVENT_BUS;
import static xyz.dupe_utils.DupeUtils.mc;

@SuppressWarnings("all")
@Mixin(Screen.class)
public abstract class ScreenMixin implements IScreen {

    @Shadow
    protected abstract <T extends Element & Drawable> T addDrawableChild(T drawableElement);

    @Shadow
    public abstract List<? extends Element> children();

    @Shadow
    private boolean screenInitialized;

    @Shadow
    protected abstract void clearAndInit();

    @Unique
    private TextFieldWidget addressField;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickClickableWidgetPanel(CallbackInfo ci) {
        for (Element child : this.children()) {
            if (child instanceof ClickableWidgetPanel clickableWidgetPanel) {
                clickableWidgetPanel.tick();
            }
        }
    }

    @Inject(
            method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;init()V", shift = At.Shift.AFTER)
    )
    private void onInitFirstInvokeHook(MinecraftClient client, int width, int height, CallbackInfo ci) {
        EVENT_BUS.post(new ScreenEvent.Child((Screen)(Object)this));

        for (Element child : this.children()) {
            if (child instanceof ClickableWidgetPanel clickableWidgetPanel) {
                clickableWidgetPanel.init();
            }
        }
    }

    @Inject(method = "refreshWidgetPositions", at = @At("RETURN"))
    public void onRefreshWidgetPositionsReturnHook(CallbackInfo ci) {
        if (this.screenInitialized) {
            EVENT_BUS.post(new ScreenEvent.Child((Screen)(Object)this));
        }
    }

    // inject at the end of the render method (if instanceof LecternScreen)
    @Inject(at = @At("TAIL"), method = "init(Lnet/minecraft/client/MinecraftClient;II)V")
    public void init(MinecraftClient client, int width, int height, CallbackInfo ci) {
        // check if the current gui is a lectern gui and if ui-utils is enabled
        if (mc.currentScreen instanceof LecternScreen screen && SharedVariables.enabled) {
            // setup widgets
            if (screenInitialized) { // bro why did you do this cxg :skull:
                // check if the current gui is a lectern gui and ui-utils is enabled
                // if you do not message me about this @coderx-gamer you are not reading my commits
                // why would you read them anyway tbh
                // ill clean this up later if you dont fix it

                TextRenderer textRenderer = ((ScreenAccessor) this).getTextRenderer();
                //DupeUtilsScreen.createWidgets(mc, screen);

                // create chat box
                this.addressField = new TextFieldWidget(textRenderer, 5, 245, 160, 20, Text.of("Chat ...")) {
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
                this.addressField.setText("");
                this.addressField.setMaxLength(255);

                this.addDrawableChild(this.addressField);
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // display sync id, revision, if ui utils is enabled
        if (SharedVariables.enabled && mc.player != null && mc.currentScreen instanceof LecternScreen) {
            DupeUtilsScreen.createText(mc, context, ((ScreenAccessor) this).getTextRenderer());
        }
    }

    @Override
    public <T> void dupe_utils$addDrawableChild(T t) {
        this.addDrawableChild((Element & Drawable)t);
    }

    @Override
    public void dupe_utils$clearDrawableChildren() {
        this.clearAndInit();
    }
}
