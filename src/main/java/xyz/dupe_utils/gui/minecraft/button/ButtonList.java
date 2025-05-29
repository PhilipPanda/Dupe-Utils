package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import xyz.dupe_utils.DupeUtils;
import xyz.dupe_utils.gui.minecraft.extension.ExtensionScreener;
import xyz.dupe_utils.gui.minecraft.panel.PanelButton;
import xyz.dupe_utils.utils.interfaces.IScreen;
import xyz.dupe_utils.utils.interfaces.PressAction;

import java.util.ArrayList;
import java.util.List;

public abstract class ButtonList<T extends Screen> extends ExtensionScreener<T> {
    public static final int BUTTON_PADDING = 5;
    public static final int BUTTON_WIDTH = 160;
    public static final int BUTTON_HEIGHT = 20;

    private final List<Button> buttons = new ArrayList<>();
    private final Class<? extends T> screenClass;

    public ButtonList(Class<? extends T> screen) {
        super(screen);
        this.screenClass = screen;
    }

    public void add(Button button) {
        buttons.add(button);
    }

    @Override
    public void createElements(T screen) {
        float y = 0;

        for (Button button : buttons) {
            if (!button.isVisible()) {
                continue;
            }
            var panelButton = PanelButton.createButtonWidget(
                    button.x, button.y + y, button.width, button.height, button.getText(),
                    button.getPressAction()
            );

            ((IScreen)screen).dupe_utils$addDrawableChild(panelButton);

            if (button.autoPadding) {
                y += panelButton.getHeight() + BUTTON_PADDING;
            }
        }
    }

    public void clearAndRecreate(Screen screen) {
        if (screen instanceof IScreen iScreen) {
            iScreen.dupe_utils$clearDrawableChildren();
            this.createElements((T) screen);
        } else {
            DupeUtils.LOGGER.warn("Screen is not instance of IScreen");
        }
    }

    public T get() {
        try {
            return screenClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            DupeUtils.LOGGER.error("Failed to instantiate screen", e);
            return null;
        }
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public static class Button {
        private Text text;
        private final PressAction pressAction;

        private float x;
        private float y;
        private float width = BUTTON_WIDTH;
        private float height = BUTTON_HEIGHT;
        private boolean autoPadding = true;
        private boolean visible = true;

        public Button(Text text, PressAction pressAction) {
            this.text = text;
            this.pressAction = pressAction;
        }

        public Button position(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Button setY(float y) {
            this.y = y;
            return this;
        }

        public Button setX(float x) {
            this.x = x;
            return this;
        }

        public Button width(float width) {
            this.width = width;
            return this;
        }

        public Button height(float height) {
            this.height = height;
            return this;
        }

        public Button size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Button dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public Button setPadding(boolean autoPadding) {
            this.autoPadding = autoPadding;
            return this;
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public PressAction getPressAction() {
            return pressAction;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
    }
}