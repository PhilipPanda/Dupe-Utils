package xyz.dupe_utils.mixin.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.dupe_utils.utils.SharedVariables;

import static xyz.dupe_utils.DupeUtils.mc;

@Mixin(SignEditScreen.class)
public class SignEditScreenMixin extends Screen {
    protected SignEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo ci) {

        if (SharedVariables.enabled) {
            addDrawableChild(ButtonWidget.builder(Text.of("Close without packet"), (button) -> {
                SharedVariables.shouldEditSign = false;
                mc.setScreen(null);
            }).width(115).position(5, 5).build());
            addDrawableChild(ButtonWidget.builder(Text.of("Disconnect"), (button) -> {
                if (mc.getNetworkHandler() != null) {
                    mc.getNetworkHandler().getConnection().disconnect(Text.of("Disconnecting (UI-UTILS)"));
                }
            }).width(115).position(5, 35).build());
        }
    }
}