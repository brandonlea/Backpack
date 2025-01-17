package net.arcanecloud.backpack.menus;

import net.arcanecloud.backpack.Backpack;
import net.arcanecloud.backpack.settings.PlayerData;
import net.arcanecloud.backpack.settings.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.List;

public class BackpackMenu extends Menu {

    @Position(38)
    private final Button upgradeButton;

    @Position(40)
    private final Button sellButton;

    @Position(42)
    private final Button autoSellButton;

    private final HashMap<Integer, ItemStack> buttons;

    public BackpackMenu(Player player) {
        Settings settings = new Settings();

        this.setSlotNumbersVisible(); //Disable this when used
        this.setSize(9 * 5);
        this.setTitle(settings.TITLE);
        buttons = new HashMap<>();


        ItemStack upgradeItem = settings.BACKPACK_ITEMS.get("upgrade").buildItem().makeMenuTool();
        this.upgradeButton = new ButtonMenu(new UpgradeMenu(player), upgradeItem);
        this.sellButton = Button.makeDummy(settings.BACKPACK_ITEMS.get("sell").buildItem());
        this.autoSellButton = Button.makeDummy(settings.BACKPACK_ITEMS.get("autosell").buildItem());
    }

    @Override
    public ItemStack getItemAt(int slot) {

        for(Integer key : buttons.keySet()) {
            if(key == slot) {
                return buttons.get(key);
            }
        }

        for(int islot : Backpack.slots) {
            if(islot == slot) {
                return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE).name(" ").make();
            }
        }

        return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE).name(" ").make();
    }

    public void addItem(int slot, ItemStack item) {
        this.buttons.put(slot, item);
    }
}
