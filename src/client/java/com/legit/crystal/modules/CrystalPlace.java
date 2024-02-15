package com.legit.crystal.modules;

import com.legit.crystal.keybinds.ModuleKeybind;
import com.legit.crystal.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class CrystalPlace {

    private static final int STARTING_STEP = 0;
    private static int step = STARTING_STEP;
    private static final int OBSIDIAN_STEP = 1;
    private static final int CRYSTAL_STEP = 2;
    private static int initialSlot = 0;
    private static long lastPlace = 0;
    private static boolean didPlace = false;
    private static int lastPlaceRandomDelay = 0;
    public static ModuleKeybind placeCrystalKey;

    private static void registerKeyInput() {
        ClientTickEvents.END_CLIENT_TICK.register(CrystalPlace::placeCrystal);
    }

    private static void placeCrystal(MinecraftClient client) {
        if (client.player == null) return;
        if (client.world == null) return;

        int crystalSlot = -1;
        int obsidianSlot = -1;
        boolean lookingAtObsidian = false;

        if (placeCrystalKey.wasPressed() && step == STARTING_STEP) {
            step = OBSIDIAN_STEP;
            initialSlot = client.player.getInventory().selectedSlot;
        }

        if (step != STARTING_STEP) {
            PlayerInventory inventory = client.player.getInventory();
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = inventory.getStack(i);
                Item item = itemStack.getItem();
                if (item == Items.OBSIDIAN) {
                    obsidianSlot = i;
                } else if (item == Items.END_CRYSTAL) {
                    crystalSlot = i;
                }
            }
            try {
                assert client.crosshairTarget != null;
                BlockHitResult result = (BlockHitResult) client.crosshairTarget;
                Block targetedBlock = client.world.getBlockState(result.getBlockPos()).getBlock();
                lookingAtObsidian = targetedBlock.equals(Blocks.OBSIDIAN);
            } catch (Exception ignored) {}

            if (lookingAtObsidian && step == OBSIDIAN_STEP) {
                step = CRYSTAL_STEP;
            }


        }

        // place obsidian 1 tick, place crystal the next
        if (step == OBSIDIAN_STEP && obsidianSlot != -1) {
            changeSelectedSlot(client, obsidianSlot);
            if (placeDelayPassed()) {
                placeBlock(client);
            }


        }
        if (step == CRYSTAL_STEP && crystalSlot != -1) {
            changeSelectedSlot(client, crystalSlot);
            if (placeDelayPassed()) {
                placeBlock(client);
            }
        }

        if (step != STARTING_STEP && didPlace) {
            step++;
            didPlace = false;
            if (step > CRYSTAL_STEP) {
                step = STARTING_STEP;
                changeSelectedSlot(client, initialSlot);
                initialSlot = 0;
            }
        }

    }

    private static boolean isValidPlacement(BlockHitResult blockHitResult, MinecraftClient client) {
        assert client.player != null;
        assert client.world != null;
        Block block = client.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
        return !blockHitResult.isInsideBlock() &&
                blockHitResult.squaredDistanceTo(client.player) <= 17 &&
                block != Blocks.AIR;
    }

    private static void placeBlock(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null)
            return;
        try {

            BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
            if (blockHitResult == null) return;
            if (!isValidPlacement(blockHitResult, client)) return;

            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHitResult);
            didPlace = true;
            lastPlace = System.currentTimeMillis();
            lastPlaceRandomDelay = Utils.getRandInt(0, 50); // delay to seem more legit
        } catch (Exception ignored) { }
    }

    public static boolean placeDelayPassed() {
        return System.currentTimeMillis() - lastPlace > lastPlaceRandomDelay;
    }

    // Method to change the selected slot of the player
    public static void changeSelectedSlot(MinecraftClient client, int slotIndex) {
        assert client.player != null;
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
        Modules.addModule("Crystal Place", () -> true);
        registerKeyBinding();
        registerKeyInput();
    }

    private static void registerKeyBinding() {
        placeCrystalKey = new ModuleKeybind("key.legit-crystal.CrystalPlace", GLFW.GLFW_KEY_R);
    }

}
