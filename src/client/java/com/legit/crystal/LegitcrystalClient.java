package com.legit.crystal;

import com.legit.crystal.modules.CrystalPlace;
import net.fabricmc.api.ClientModInitializer;

public class LegitcrystalClient implements ClientModInitializer {
	public static String MODID = "legit-crystal";
	@Override
	public void onInitializeClient() {
		CrystalPlace.registerModule();
	}
}