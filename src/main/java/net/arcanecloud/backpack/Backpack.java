package net.arcanecloud.backpack;

import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class Backpack extends SimplePlugin {

    public static int[] slots = {11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33};

    @Override
    protected String[] getStartupLogo() {
        return new String[] {
                        "&6   ___            _                     _",
                        "&6  / __\\ __ _  ___| | ___ __   __ _  ___| | __" ,
                        "&6 /__\\/// _` |/ __| |/ / '_ \\ / _` |/ __| |/ /",
                        "&6/ \\/  \\ (_| | (__|   <| |_) | (_| | (__|   <",
                        "&6\\_____/\\__,_|\\___|_|\\_\\ .__/ \\__,_|\\___|_|\\_\\",
                        "&6                      |_|"
        };
    }

    @Override
    protected void onPluginStart() {
        System.out.println("Starting backpack");

//        if(HookManager.isVaultLoaded()) {
//            System.out.println("Vault: Hooked!");
//        }
    }

    @Override
    public int getFoundedYear() {
        return 2025;
    }
}
