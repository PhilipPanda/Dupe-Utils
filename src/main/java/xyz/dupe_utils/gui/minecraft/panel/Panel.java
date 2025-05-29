package xyz.dupe_utils.gui.minecraft.panel;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import xyz.dupe_utils.utils.RenderEngine;

import java.awt.*;

import static xyz.dupe_utils.DupeUtils.mc;

public abstract class Panel implements IPanel {
    final Text title;
    private float width;
    private float height;
    private final boolean scissor;

    private float x;
    private float y;

    protected float scroll;

    private boolean opened;
    protected boolean hovered;

    private boolean modifiable;
    private boolean visible;

    private float hoverProgress = 0f;

    public Panel(Text title, float width, float height, boolean scissor) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.scissor = scissor;

        this.hovered = false;
        this.opened = false;
        this.modifiable = true;
        this.visible = true;
    }

    @Override
    public void init() { }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        MatrixStack matrices = context.getMatrices();
        hovered = isHovered(mouseX, mouseY);

        float target = hovered ? 1f : 0f;
        float speed = 0.1f;
        hoverProgress += (target - hoverProgress) * speed;

        Color color = RenderEngine.colorInterpolate(
                new Color(255, 225, 160, 230),
                new Color(45, 45, 45, 230),
                1f - hoverProgress
        );
        RenderEngine.drawRound(matrices, x, y, width, height, 3, color);

        if (scissor) {
            context.enableScissor((int)x, (int)y, (int)(x + width), (int)(y + height));
        }

        matrices.push();
        renderContent(context, mouseX, mouseY, deltaTime);
        matrices.pop();

        context.drawText(mc.textRenderer, getTitle(), (int)(x + width / 2f - mc.textRenderer.getWidth(getTitle()) / 2f), (int)(y + height / 2f - mc.textRenderer.fontHeight / 2f), -1, true);

        if (scissor) {
            context.disableScissor();
        }
    }

    public abstract void renderContent(DrawContext context, int mouseX, int mouseY, float deltaTime);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                opened = !opened;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizonAmount, double verticalAmount) {
        if (opened && isHovered(mouseX, mouseY)) {
            scroll += (float)(verticalAmount * 3.0f);
            return true;
        }
        return false;
    }

    @Override
    public void tick() { }

    @Override
    public void onClose() { }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public Text getTitle() {
        return title;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
