package xyz.dupe_utils.gui.minecraft.panel;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;

public class ClickableWidgetPanel extends ClickableWidget implements Element {
    protected final Panel panel;
    private final boolean update;

    public ClickableWidgetPanel(Panel panel, boolean update) {
        super((int)panel.getX(), (int)panel.getY(), (int)panel.getWidth(), (int)panel.getHeight(), Text.of(panel.getTitle()));
        this.panel = panel;
        this.update = update;
        panel.init();
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        if (builder != null) {
            builder.put(NarrationPart.TITLE, panel.getTitle());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        panel.setModifiable(false);
        boolean value = panel.isVisible() && panel.mouseClicked((int) Math.floor(mouseX), (int) Math.floor(mouseY), button);
        panel.setModifiable(true);

        return value;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (panel.isVisible()) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean opened = panel.isOpened();
        panel.setOpened(true);
        boolean value = panel.isVisible() && panel.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        panel.setOpened(opened);
        return value;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return panel.isVisible() && panel.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (panel.isVisible()) {
            panel.charTyped(chr, modifiers);
        }
        return false;
    }

    public void init() {
        panel.init();
    }

    public void tick() {
        panel.tick();
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean opened = panel.isOpened();
        panel.setOpened(true);

        if (update) {
            panel.setX(this.getX());
            panel.setY(this.getY());
            panel.setWidth(this.getWidth());
            panel.setHeight(this.getHeight());
        }

        if (panel.isVisible()) {
            panel.render(context, mouseX, mouseY, delta);
        }

        panel.setOpened(opened);
    }

    @Override
    public boolean isHovered() {
        return true;
    }
}