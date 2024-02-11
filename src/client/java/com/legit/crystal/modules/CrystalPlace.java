package com.legit.crystal.modules;

import com.legit.crystal.keybinds.Keybinds;
import com.legit.crystal.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class CrystalPlace {


    public static final String KEY_HIT_CRYSTAL = "key.legit-crystal.HitCrystal";
    private static int step = 0;
    private static final int STARTING_STEP = 0;
    private static final int OBSIDIAN_STEP = 1;
    private static final int CRYSTAL_STEP = 2;
    public static KeyBinding placeCrystal;

    private static void registerKeyInput() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            int crystalSlot = -1;
            int obsidianSlot = -1;
            if (placeCrystal.wasPressed() && step == STARTING_STEP)
                step++;
            if (step != STARTING_STEP) {
                PlayerInventory inventory = client.player.getInventory();
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = inventory.getStack(i);
                    String itemName = itemStack.getItem().getName().getString();
                    if (Objects.equals(itemName, "Obsidian")) {
                        obsidianSlot = i;
                    } else if (Objects.equals(itemName, "End Crystal")) {
                        crystalSlot = i;
                    }
                }
            }

            // place obsidian 1 tick, then place the crystal
            if (step == OBSIDIAN_STEP && obsidianSlot != -1) {
                changeSelectedSlot(client, obsidianSlot);
                placeBlock(client);
            }
            if (step == CRYSTAL_STEP && crystalSlot != -1) {
                changeSelectedSlot(client, crystalSlot);
                placeBlock(client);
            }

            if (step != STARTING_STEP) {
                step++;
            }
            step = step > CRYSTAL_STEP ? STARTING_STEP : step;
        });
    }

    private static void placeBlock(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null)
            return;
        try {
            BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHitResult);
        } catch (Exception e) {
            client.player.sendMessage(Text.of("error occurred but its ok"));
        }

    }

    // Method to change the selected slot of the player
    public static void changeSelectedSlot(MinecraftClient client, int slotIndex) {
        // Ensure the slot index is within valid bounds (0-8)
        if (slotIndex < 0 || slotIndex > 8) {
            return; // Or handle the error as per your requirement
        }
        PlayerInventory inventory = client.player.getInventory();
        // Set the player's selected slot to the specified index
        inventory.selectedSlot = slotIndex;

        // Inform the client about the change in selected slot
        client.player.playerScreenHandler.sendContentUpdates();
    }

    public static void registerModule() {
        placeCrystal = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_HIT_CRYSTAL,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                Keybinds.CRYSTAL_PVP_CATEGORY
        ));

        registerKeyInput();
    }

}
