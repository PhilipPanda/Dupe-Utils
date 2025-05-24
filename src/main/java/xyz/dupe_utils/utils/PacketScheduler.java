package xyz.dupe_utils.utils;

import java.util.Timer;
import java.util.TimerTask;

import static xyz.dupe_utils.DupeUtils.mc;

public class PacketScheduler {
    public static void queue(Runnable runnable, long delayMs) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mc.send(runnable);
            }
        }, delayMs);
    }
}