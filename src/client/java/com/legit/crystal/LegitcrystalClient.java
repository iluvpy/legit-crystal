package com.legit.crystal;

import com.legit.crystal.GUI.MainGUI;
import com.legit.crystal.modules.AutoTotem;
import com.legit.crystal.modules.CrystalPlace;
import com.legit.crystal.rendering.GUIRenderer;
import net.fabricmc.api.ClientModInitializer;

public class LegitcrystalClient implements ClientModInitializer {
	public static String MODID = "legit-crystal";
	@Override
	public void onInitializeClient() {
		CrystalPlace.registerModule();
		AutoTotem.registerModule();
		GUIRenderer.register();
	}
}