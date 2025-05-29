package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.ingame.BookScreen;

public class ButtonListBookScreen extends ButtonListHelper<BookScreen> {

    public ButtonListBookScreen() {
        super(BookScreen.class);
        createButton();
    }
}
