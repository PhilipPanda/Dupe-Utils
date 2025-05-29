package xyz.dupe_utils.gui.minecraft.extension;

import net.minecraft.client.gui.screen.Screen;

public abstract class ExtensionScreener<T extends Screen> {

    private final Class<? extends T> screen;

    public ExtensionScreener(Class<? extends T> screen) {
        this.screen = screen;
    }

    public Class<? extends T> getScreen() {
        return screen;
    }

    public abstract void createElements(T screen);
}
