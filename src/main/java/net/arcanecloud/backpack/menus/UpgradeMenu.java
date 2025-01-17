package net.arcanecloud.backpack.menus;

import net.arcanecloud.backpack.settings.PlayerData;
import net.arcanecloud.backpack.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class UpgradeMenu extends Menu {

    @Position(12)
    private final Button capacityButton;

    @Position(14)
    private final Button mutliButton;

    public UpgradeMenu(Player player) {
        Settings settings = new Settings();
        this.setSlotNumbersVisible(); //Disable this when used
        this.setSize(9 * 3);
        this.setTitle("Upgrade Menu");


        this.capacityButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(settings.CAPACITY_UPGRADES.get(PlayerData.from(player).getCapacityTier() + 1) != null) {
                    PlayerData.from(player).setCapacityTier(PlayerData.from(player).getCapacityTier() + 1);
                    menu.restartMenu();
                }
            }

            @Override
            public ItemStack getItem() {
                if(settings.CAPACITY_UPGRADES.get(PlayerData.from(player).getCapacityTier() + 1) == null) {
                    return ItemCreator.of(CompMaterial.BARRIER, "Maxed Upgrade", "", "Maxed out upgrade", "").make();
                } else {
                    return ItemCreator.
                            of(CompMaterial.CHEST, "Upgrade to next Tier!",
                                    "",
                                    "Capacity: ",
                                    "Tier: " + (PlayerData.from(player).getCapacityTier() + 1),
                                    "Cost: " + settings.CAPACITY_UPGRADES.get(PlayerData.from(player).getCapacityTier() + 1).getCost()
                            ).make();
                }
            }
        };

        this.mutliButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(settings.MULTI_UPGRADES.get(PlayerData.from(player).getMutliTier() + 1) != null) {
                    PlayerData.from(player).setMutliTier(PlayerData.from(player).getMutliTier() + 1);
                    menu.restartMenu();
                }
            }

            @Override
            public ItemStack getItem() {
                if(settings.MULTI_UPGRADES.get(PlayerData.from(player).getMutliTier() + 1) == null) {
                    return ItemCreator.of(CompMaterial.BARRIER, "Maxed Upgrade", "", "Maxed out upgrade", "").make();
                } else {
                    return ItemCreator.
                            of(CompMaterial.EMERALD, "Upgrade to next Tier!",
                                    "",
                                    "Multi: ",
                                    "Tier: " + (PlayerData.from(player).getMutliTier() + 1),
                                    "Cost: " + settings.MULTI_UPGRADES.get(PlayerData.from(player).getMutliTier() + 1).getCost()
                            ).make();
                }
            }
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, " ").make();
    }
}
