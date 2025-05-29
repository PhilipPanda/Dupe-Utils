package xyz.dupe_utils.event;

import net.minecraft.client.gui.screen.Screen;

public class ScreenEvent {
    public static class Child extends ScreenEvent {
        private Screen screen;

        public Child(Screen screen) {
            this.screen = screen;
        }

        public Screen getScreen() {
            return screen;
        }

        public void setScreen(Screen screen) {
            this.screen = screen;
        }
    }
}
