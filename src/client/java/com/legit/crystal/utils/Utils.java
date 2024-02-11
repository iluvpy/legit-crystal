package com.legit.crystal.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class Utils {
    public static void sendChatMessage(String str) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player != null)
            player.sendChatMessage(str);
    }

    public static void sendClientMsg(String str) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player != null)
            player.sendMessage(Text.of(str), true);
    }
}
