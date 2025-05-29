package xyz.dupe_utils.gui.minecraft.panel;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import xyz.dupe_utils.utils.interfaces.PressAction;

import static xyz.dupe_utils.DupeUtils.mc;

public class PanelButton extends Panel {

    private final float x;
    private final float y;

    private float width;
    private float height;
    private Text text;
    private final PressAction pressAction;

    public PanelButton(float x, float y, float width, float height, Text text, PressAction pressAction) {
        super(text, width, height, true);
        this.width = width;
        this.height = height;
        this.text = text;
        this.pressAction = pressAction;

        this.x = x;
        this.y = y;
    }

    public static ClickableWidgetPanel createButtonWidget(float x, float y, float width, float height, Text text, PressAction pressAction) {
        return new ClickableWidgetPanel(new PanelButton(x, y, width, height, text, pressAction), true);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderContent(DrawContext context, int mouseX, int mouseY, float delta) {
        // U can add more render stuff here like icon
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1F));
            pressAction.onPress(this);
            return true;
        }
        return false;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }
}

