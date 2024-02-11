package com.legit.crystal.modules;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoTotem {
    public static final int OFF_HAND_SLOT = -106;
    private static boolean totemAtStart = false;
    public static void registerModule() {

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                totemAtStart = totemInOffHand(client);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean totemAtEnd = totemInOffHand(client);
            if (totemAtStart && !totemAtEnd) {
                // TODO: find totem in inventory and set it to the offhand slot
            }
        });
    }

    private static boolean totemInOffHand(MinecraftClient client) {
        return client.player.getInventory().getStack(OFF_HAND_SLOT).getItem() == Items.TOTEM_OF_UNDYING;
    }
}
