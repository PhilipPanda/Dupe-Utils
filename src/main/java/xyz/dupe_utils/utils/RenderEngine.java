package xyz.dupe_utils.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderEngine {

    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void prepare() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
    }

    public static void release() {
        RenderSystem.depthMask(true);
        RenderSystem.disableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public static Color colorInterpolate(
            Color start, Color end, float delta
    ) {
        int red = MathHelper.clamp(MathHelper.lerp(delta, start.getRed(), end.getRed()), 0, 255);
        int green = MathHelper.clamp(MathHelper.lerp(delta, start.getGreen(), end.getGreen()), 0, 255);
        int blue = MathHelper.clamp(MathHelper.lerp(delta, start.getBlue(), end.getBlue()), 0, 255);
        int alpha = MathHelper.clamp(MathHelper.lerp(delta, start.getAlpha(), end.getAlpha()), 0, 255);

        return new Color(red, green, blue, alpha);
    }

    public static void drawRound(
            MatrixStack matrices,
            float x, float y,
            float width, float height,
            float radius, Color color
    ) {
        prepare();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        roundQuadInternal(matrices.peek().getPositionMatrix(), x, y, x + width, y + height, radius, color.getRGB(), 4);
        release();
    }

    public static void roundQuadInternal(
            Matrix4f matrix,
            float fromX, float fromY,
            float toX, float toY,
            float radius, int color,
            float samples
    ) {
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        float[][] map = new float[][]{
                new float[] { toX - radius, toY - radius, radius },
                new float[] { toX - radius, fromY + radius, radius },
                new float[] { fromX + radius, fromY + radius, radius },
                new float[] { fromX + radius, toY - radius, radius }
        };
        for (int i = 0; i < 4; i++) {
            float[] current = map[i];
            float rad = current[2];

            for (float r = i * 90f; r < (360 / 4f + i * 90f); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, current[0] + sin, current[1] + cos, 0.0F).color(color);
            }

            float rad1 = (float) Math.toRadians((360 / 4f + i * 90f));
            float sin = (float) (Math.sin(rad1) * rad);
            float cos = (float) (Math.cos(rad1) * rad);

            bufferBuilder.vertex(matrix, current[0] + sin, current[1] + cos, 0.0F).color(color);
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}
