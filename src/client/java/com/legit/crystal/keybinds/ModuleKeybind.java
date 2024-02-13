package com.legit.crystal.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class ModuleKeybind {
    private final KeyBinding keyBinding;
    public static final String CRYSTAL_PVP_CATEGORY= "key.category.legit-crystal.CrystalPVP";


    public ModuleKeybind(String name,  int keyCode) {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                name,
                InputUtil.Type.KEYSYM,
                keyCode,
                CRYSTAL_PVP_CATEGORY
        ));
    }


    public void onWasPressed(Consumer<MinecraftClient> func) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) func.accept(client);
        });
    }

    public boolean wasPressed() { return keyBinding.wasPressed(); }
}
