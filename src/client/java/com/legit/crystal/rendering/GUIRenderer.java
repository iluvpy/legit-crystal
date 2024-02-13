package com.legit.crystal.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import java.util.function.BiConsumer;

public class GUIRenderer {

    private static BiConsumer<MatrixStack, Float>[] drawables;

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            Matrix4f positionMatrix = drawContext.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
/*
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            buffer.vertex(positionMatrix, 20, 20, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
            buffer.vertex(positionMatrix, 20, 60, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
            buffer.vertex(positionMatrix, 60, 60, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
            buffer.vertex(positionMatrix, 60, 20, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();

            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.setShaderTexture(0, new Identifier("legit-crystal", "icon.png"));
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
*/
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            buffer.vertex(positionMatrix, 20, 20, 0).color(1f, 1f, 1f, .4f).next();
            buffer.vertex(positionMatrix, 20, 60, 0).color(1f, 1f, 1f, .4f).next();
            buffer.vertex(positionMatrix, 60, 60, 0).color(1f, 1f, 1f, .4f).next();
            buffer.vertex(positionMatrix, 60, 20, 0).color(1f, 1f, 1f, .4f).next();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            //RenderSystem.setShaderTexture(0, new Identifier("legit-crystal", "icon.png"));
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            tessellator.draw();
        });

    }



}
