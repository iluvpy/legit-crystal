package com.legit.crystal.modules;

import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class Modules {

    private static final ArrayList<Pair<String, Supplier<Boolean>>> moduleList = new ArrayList<>();

    public static void addModule(String name, Supplier<Boolean> getModuleActive) {
        Pair<String, Supplier<Boolean>> pair = new Pair<String, Supplier<Boolean>>(name, getModuleActive);
        moduleList.add(pair);
    }

    public static ArrayList<Pair<String, Supplier<Boolean>>> getModules() {
        return moduleList;
    }
}
