package xyz.dupe_utils.gui.minecraft.button;

import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.text.Text;
import xyz.dupe_utils.utils.SharedVariables;

import static xyz.dupe_utils.DupeUtils.mc;

public class ButtonListSleepingChatScreen extends ButtonListHelper<SleepingChatScreen> {

    public ButtonListSleepingChatScreen() {
        super(SleepingChatScreen.class);

        if (SharedVariables.enabled) {
            add(new Button(Text.of("Client wake up"), button -> {
                if (mc != null && mc.player != null) {
                    mc.player.wakeUp();
                    mc.setScreen(null);
                }
            }).width(115).position(5, 5));
        }
    }
}
