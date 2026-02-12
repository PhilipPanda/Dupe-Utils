package xyz.dupe_utils.gui.minecraft.panel;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.CharInput;
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
    public boolean mouseClicked(Click click, boolean doubled) {
        panel.setModifiable(false);
        boolean value = panel.isVisible() && panel.mouseClicked((int) Math.floor(click.x()), (int) Math.floor(click.y()), click.button());
        panel.setModifiable(true);

        return value;
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (panel.isVisible()) {
            panel.mouseReleased(click.x(), click.y(), click.button());
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
    public boolean keyPressed(KeyInput input) {
        return panel.isVisible() && panel.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (panel.isVisible()) {
            panel.charTyped((char) input.codepoint(), input.modifiers());
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