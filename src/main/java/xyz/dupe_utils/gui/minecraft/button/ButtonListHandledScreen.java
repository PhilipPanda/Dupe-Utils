package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class ButtonListHandledScreen extends ButtonListHelper<HandledScreen> {

    public ButtonListHandledScreen() {
        super(HandledScreen.class);
        createButton();
    }
}
