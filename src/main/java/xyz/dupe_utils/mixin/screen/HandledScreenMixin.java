package xyz.dupe_utils.mixin.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.utils.DupeUtilsScreen;
import xyz.dupe_utils.utils.CommandManager;
import xyz.dupe_utils.utils.SharedVariables;

import java.util.regex.Pattern;

import static xyz.dupe_utils.DupeUtils.mc;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
    private HandledScreenMixin() {
        super(null);
    }

    @Shadow
    protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Unique
    private TextFieldWidget addressField;

    // called when creating a HandledScreen
    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo ci) {
        if (SharedVariables.enabled) {
            // create chat box
            this.addressField = new TextFieldWidget(this.textRenderer, 5, 245, 160, 20, Text.of("Chat ...")) {
                @Override
                public boolean keyPressed(KeyInput input) {
                    if (input.key() == GLFW.GLFW_KEY_ENTER) {
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
                    return super.keyPressed(input);
                }
            };
            this.addressField.setText("");
            this.addressField.setMaxLength(256);

            this.addDrawableChild(this.addressField);
        }
    }

    @Inject(at = @At("HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        if (super.keyPressed(input)) {
            cir.setReturnValue(true);
        } else if (mc.options.inventoryKey.matchesKey(input) && (this.addressField == null || !this.addressField.isSelected())) {
            // Crashes if address field does not exist (because of ui utils disabled, this is a temporary fix.)
            this.close();
            cir.setReturnValue(true);
        } else {
            // handleHotbarKeyPressed was removed in 1.21.11 - hotbar keys are now handled by vanilla
            if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
                if (mc.options.pickItemKey.matchesKey(input)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 0, SlotActionType.CLONE);
                } else if (mc.options.dropKey.matchesKey(input)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, (input.modifiers() & GLFW.GLFW_MOD_CONTROL) != 0 ? 1 : 0, SlotActionType.THROW);
                }
            }

            cir.setReturnValue(true);
        }
    }

    // inject at the end of the render method
    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // display sync id, revision, if ui utils is enabled
        // this hurts me physically to look at this in a render method :(
        // im too lazy to fix it tho :D
        if (SharedVariables.enabled) {
            DupeUtilsScreen.createText(mc, context, this.textRenderer);
        }
    }
}
