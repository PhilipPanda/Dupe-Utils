package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.text.Text;
import xyz.dupe_utils.utils.SharedVariables;

import static xyz.dupe_utils.DupeUtils.mc;
import static xyz.dupe_utils.utils.DupeUtilsScreen.coloredBoolText;

public class ButtonListMultiplayerScreen extends ButtonListHelper<MultiplayerScreen> {

    public ButtonListMultiplayerScreen() {
        super(MultiplayerScreen.class);
        createButton();
    }

    @Override
    public void createButton() {
        if (SharedVariables.enabled) {
            add(new Button(coloredBoolText("Force Deny: ", SharedVariables.resourcePackForceDeny), (button) -> {
                SharedVariables.resourcePackForceDeny = !SharedVariables.resourcePackForceDeny;
                button.setText(coloredBoolText("Force Deny: ", SharedVariables.resourcePackForceDeny));
                clear();
            }).position(5, 5));
        }
    }

    @Override
    public void clear() {
        getButtons().clear();
        createButton();
        clearAndRecreate(mc.currentScreen);
    }
}