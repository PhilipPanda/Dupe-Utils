package xyz.dupe_utils.gui.minecraft.panel;

import net.minecraft.client.gui.DrawContext;

public interface IPanel {

    void init();

    void render(DrawContext context, int mouseX, int mouseY, float delta);

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button){
        return false;
    }

    default boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount){
        return false;
    }

    default boolean keyPressed(int keyCode, int scanCode, int modifiers){
        return false;
    }

    default boolean charTyped(char chr, int modifiers) {
        return false;
    }

    boolean isHovered(double mouseX, double mouseY);

    void tick();

    void onClose();
}
