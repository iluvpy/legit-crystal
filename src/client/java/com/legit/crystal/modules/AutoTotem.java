package com.legit.crystal.modules;

import com.legit.crystal.keybinds.Keybinds;
import com.legit.crystal.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class AutoTotem {
    public static boolean moduleActive = false;

    private static KeyBinding autoTotemKey;
    private static String KEY_AUTO_TOTEM = "key.legit-crystal.AutoTotem";

    public static void registerModule() {
        registerKeyBinding();
        registerTickEvents();
    }

    private static void registerKeyBinding() {
        autoTotemKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_AUTO_TOTEM,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                Keybinds.CRYSTAL_PVP_CATEGORY
        ));
    }

    private static void registerTickEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (autoTotemKey.wasPressed()) {
                moduleActive = !moduleActive;
                Utils.sendClientMsg("module " + "Auto Totem " + (moduleActive ? "enabled" : "disabled"));
            }

            if (client.player != null && moduleActive) {
                if (!totemInOffHand(client)) {
                    int totemSlot = getTotemSlot(client);
                    if (totemSlot != -1 && client.interactionManager != null && isInventoryOpen(client)) {
                        client.interactionManager.clickSlot(
                                0,
                                getServerSlot(totemSlot),
                                PlayerInventory.OFF_HAND_SLOT,
                                SlotActionType.SWAP,
                                client.player);
                    }
                }
            }
        });
    }

    private static boolean totemInOffHand(MinecraftClient client) {
        return getInventory(client).getStack(PlayerInventory.OFF_HAND_SLOT).isOf(Items.TOTEM_OF_UNDYING);
    }

    private static int getServerSlot(int slot) {
        if (slot >= 0 && slot <= 8) return slot + 36;
        if (slot >= 36 && slot <= 39) return 39 - slot + 5;
        if (slot >= 9 && slot <= 35) return slot;
        if (slot == 40) return 45;
        return -1;
    }

    private static int getTotemSlot(MinecraftClient client) {
        PlayerInventory inventory = getInventory(client);
        int totemSlot = -1;
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.getStack(i).getItem();
            if (item.equals(Items.TOTEM_OF_UNDYING)) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }

    private static PlayerInventory getInventory(MinecraftClient client) {
        return client.player.getInventory();
    }

    public static boolean isInventoryOpen(MinecraftClient client) {
        return client.currentScreen instanceof InventoryScreen;
    }
}
