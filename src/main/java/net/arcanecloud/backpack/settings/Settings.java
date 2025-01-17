package net.arcanecloud.backpack.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class Settings extends YamlConfig {

    public String TITLE;
    public HashMap<String, MenuItem> BACKPACK_ITEMS;
    public HashMap<Integer, Upgrade> CAPACITY_UPGRADES;
    public HashMap<Integer, Upgrade> MULTI_UPGRADES;

    public Settings() {
        this.loadConfiguration("settings.yml");


        this.save();
    }

    @Override
    protected void onLoad() {
        this.TITLE = this.getString("title");
        this.BACKPACK_ITEMS = this.getMap("backpack-items", String.class, MenuItem.class);

        this.CAPACITY_UPGRADES = this.getMap("upgrades.storage", Integer.class, Upgrade.class);
        this.MULTI_UPGRADES = this.getMap("upgrades.multi", Integer.class, Upgrade.class);
    }



    @Getter
    @AllArgsConstructor(access =  AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class MenuItem implements ConfigSerializable {

        private CompMaterial item;
        private String name;
        private List<String> lore;

        @Override
        public SerializedMap serialize() {
            SerializedMap map = new SerializedMap();
            map.put("item", this.item);
            map.put("name", this.name);
            map.put("lore", this.lore);

            return map;
        }

        public static MenuItem deserialize(SerializedMap map) {
           MenuItem menuItem = new MenuItem();

            menuItem.item = map.getMaterial("item");
            menuItem.name = map.getString("name");
            menuItem.lore = map.getStringList("lore");

            return menuItem;
        }

        public ItemCreator buildItem() {
            return ItemCreator.of(getItem())
                    .name(getName().replace("{value}", "0"))
                    .lore(getLore());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Upgrade implements ConfigSerializable {

        private Integer capacity;
        private Integer cost;
        private Double multi;


        @Override
        public SerializedMap serialize() {
            SerializedMap map = new SerializedMap();

            if(this.capacity != null) {
                map.put("capacity", this.capacity);
            } else if(this.multi != null) {
                map.put("multi", this.multi);
            }

            map.put("cost", this.cost);

            return map;
        }

        public static Upgrade deserialize(SerializedMap map) {
            Upgrade upgrade = new Upgrade();

            if(upgrade.getCapacity() != null) {
                upgrade.capacity = map.getInteger("capacity");
            } else if(upgrade.getMulti() != null) {
                upgrade.multi = map.getDouble("multi");
            }

            upgrade.cost = map.getInteger("cost");

            return upgrade;
        }

    }


}
