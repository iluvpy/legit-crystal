package com.legit.crystal.modules;

import com.legit.crystal.keybinds.Keybinds;
import com.legit.crystal.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class CrystalPlace {


    public static final String KEY_HIT_CRYSTAL = "key.legit-crystal.CrystalPlace";

    private static final int STARTING_STEP = 0;
    private static int step = STARTING_STEP;
    private static final int OBSIDIAN_STEP = 1;
    private static final int CRYSTAL_STEP = 2;
    private static int initialSlot = 0;
    public static KeyBinding placeCrystal;

    private static void registerKeyInput() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> placeCrystal(client));
    }

    private static void placeCrystal(MinecraftClient client) {
        if (client.player == null) return;
        if (client.world == null) return;

        int crystalSlot = -1;
        int obsidianSlot = -1;
        boolean lookingAtObsidian = false;

        if (placeCrystal.wasPressed() && step == STARTING_STEP) {
            step = OBSIDIAN_STEP;
            initialSlot = client.player.getInventory().selectedSlot;
        }

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
            try {
                Vec3d pos = client.crosshairTarget.getPos();
                BlockState bState = client.world.getBlockState(new BlockPos(pos));
                lookingAtObsidian = bState.getBlock().getDefaultState() == Blocks.OBSIDIAN.getDefaultState();
            } catch (Exception ignored) {}

            if (lookingAtObsidian && step == OBSIDIAN_STEP) {
                step = CRYSTAL_STEP;
            }
        }



        // place obsidian 1 tick, place crystal the next
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
            if (step > CRYSTAL_STEP) {
                step = STARTING_STEP;
                changeSelectedSlot(client, initialSlot);
                initialSlot = 0;
            }
        }

    }

    private static void placeBlock(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null)
            return;
        try {
            BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
            if (blockHitResult == null ||
                    blockHitResult.isInsideBlock() ||
                    client.player.world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.AIR ||
                    blockHitResult.squaredDistanceTo((Entity) client.player) > 13) {
                return;
            }
            
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHitResult);
        } catch (Exception ignored) {}

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
