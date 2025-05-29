package xyz.dupe_utils.utils.interfaces;

import xyz.dupe_utils.gui.minecraft.panel.PanelButton;

@FunctionalInterface
public interface PressAction {
    void onPress(PanelButton button);
}