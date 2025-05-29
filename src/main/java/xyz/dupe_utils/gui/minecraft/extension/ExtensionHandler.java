package xyz.dupe_utils.gui.minecraft.extension;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screen.Screen;
import xyz.dupe_utils.event.ScreenEvent;
import xyz.dupe_utils.gui.minecraft.button.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExtensionHandler {
    private final List<ExtensionScreener<?>> list = new CopyOnWriteArrayList<>();

    public ExtensionHandler() {
        add(
                new ButtonListBookEdit(),
                new ButtonListBookScreen(),
                new ButtonListHandledScreen(),
                new ButtonListMultiplayerScreen(),
                new ButtonListScreen(),
                new ButtonListSignEditScreen(),
                new ButtonListSleepingChatScreen()
        );
    }

    @Subscribe
    public void onAddChild(ScreenEvent.Child event) {
        for (ExtensionScreener<?> ext : list) {
            if (ext.getScreen().isAssignableFrom(event.getScreen().getClass())) {
                createElements(ext, event.getScreen());
            }
        }
    }

    public final void add(ExtensionScreener<?>... objects) {
        for (ExtensionScreener<?> obj : objects) {
            insert(obj, list.size());
        }
    }

    public void insert(ExtensionScreener<?> obj, int index) {
        if (!list.contains(obj)) {
            list.add(index, obj);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Screen> void createElements(ExtensionScreener<T> ext, Screen screen) {
        ext.createElements((T) screen);
    }
}
