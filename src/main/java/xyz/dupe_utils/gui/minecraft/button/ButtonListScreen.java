package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import xyz.dupe_utils.utils.SharedVariables;

import static xyz.dupe_utils.DupeUtils.mc;

public class ButtonListScreen extends ButtonListHelper<Screen> {

    public ButtonListScreen() {
        super(Screen.class);

        if (mc.currentScreen instanceof LecternScreen && SharedVariables.enabled) {
            createButton();
        }
    }
}
