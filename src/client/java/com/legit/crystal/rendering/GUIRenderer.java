package com.legit.crystal.rendering;

import com.legit.crystal.modules.Modules;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class GUIRenderer {

    private static BiConsumer<MatrixStack, Float>[] drawables;

    private static boolean GUIopen = true;

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (GUIopen) {

                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

                ArrayList<Pair<String, Supplier<Boolean>>> moduleList = Modules.getModules();
                int moduleNamesDrawn = 0;
                float startingX = 20;
                float startingY = 20;
                for (Pair<String, Supplier<Boolean>> module : moduleList) { // if active draw module
                    if (module.getB().get()) {
                        textRenderer.draw(drawContext,
                                module.getA(),
                                startingX,
                                startingY + moduleNamesDrawn * textRenderer.fontHeight,
                                0xFFFFFF);
                        moduleNamesDrawn++;
                    }
                }

            }
        });

    }

    private static void drawRect(MatrixStack drawContext, float x, float y, float width, float height, Color color) {

        Matrix4f positionMatrix = drawContext.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        float x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = x;
        y1 = y;
        x2 = x;
        y2 = y+height;
        x3 = x + width;
        y3 = y + height;
        x4 = x + width;
        y4 = y;
        buffer.vertex(positionMatrix, x1, y1, 0).color(color.r, color.g, color.b, color.a).next();
        buffer.vertex(positionMatrix, x2, y2, 0).color(color.r, color.g, color.b, color.a).next();
        buffer.vertex(positionMatrix, x3, y3, 0).color(color.r, color.g, color.b, color.a).next();
        buffer.vertex(positionMatrix, x4, y4, 0).color(color.r, color.g, color.b, color.a).next();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //RenderSystem.setShaderTexture(0, new Identifier("legit-crystal", "icon.png"));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        tessellator.draw();
    }
}
