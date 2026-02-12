package xyz.dupe_utils.utils;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class RenderEngine {

    public static void prepare() {
        // Rendering setup - temporarily disabled for 1.21.11 API migration
    }

    public static void release() {
        // Rendering cleanup - temporarily disabled for 1.21.11 API migration
    }

    public static Color colorInterpolate(
            Color start, Color end, float delta
    ) {
        int red = MathHelper.clamp((int)MathHelper.lerp(delta, start.getRed(), end.getRed()), 0, 255);
        int green = MathHelper.clamp((int)MathHelper.lerp(delta, start.getGreen(), end.getGreen()), 0, 255);
        int blue = MathHelper.clamp((int)MathHelper.lerp(delta, start.getBlue(), end.getBlue()), 0, 255);
        int alpha = MathHelper.clamp((int)MathHelper.lerp(delta, start.getAlpha(), end.getAlpha()), 0, 255);

        return new Color(red, green, blue, alpha);
    }

    public static void drawRound(
            MatrixStack matrices,
            float x, float y,
            float width, float height,
            float radius, Color color
    ) {
        // Custom rendering temporarily disabled for 1.21.11 API migration
        // The rendering system has changed significantly and requires refactoring
    }
}
