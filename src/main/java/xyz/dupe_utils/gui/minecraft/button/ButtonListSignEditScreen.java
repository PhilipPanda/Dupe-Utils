package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.text.Text;
import xyz.dupe_utils.utils.SharedVariables;

import static xyz.dupe_utils.DupeUtils.mc;

public class ButtonListSignEditScreen extends ButtonListHelper<SignEditScreen> {

    public ButtonListSignEditScreen() {
        super(SignEditScreen.class);

        if (SharedVariables.enabled) {
            add(new Button(Text.of("Close without packet"), (button) -> {
                SharedVariables.shouldEditSign = false;
                mc.setScreen(null);
            }).width(115).position(5, 5));
            add(new Button((Text.of("Disconnect")), (button) -> {
                if (mc.getNetworkHandler() != null) {
                    mc.getNetworkHandler().getConnection().disconnect(Text.of("Disconnecting (DUPE-UTILS)"));
                }
            }).width(115).position(5, 5));
        }
    }
}
