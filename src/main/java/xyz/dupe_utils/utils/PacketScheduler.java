package xyz.dupe_utils.utils;

import net.minecraft.client.MinecraftClient;

import java.util.Timer;
import java.util.TimerTask;

public class PacketScheduler {
    public static void queue(Runnable runnable, long delayMs) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MinecraftClient.getInstance().send(runnable);
            }
        }, delayMs);
    }
}