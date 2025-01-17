package net.arcanecloud.backpack.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.arcanecloud.backpack.Backpack;
import net.arcanecloud.backpack.menus.BackpackMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerData extends YamlConfig {

    private static HashMap<UUID, PlayerData> playerData = new HashMap<>();

    private final UUID uuid;

    private Integer capacity;
    private HashMap<Integer, BackpackItem> items;

    private Integer capacityTier;
    private Integer mutliTier;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;

        this.loadConfiguration(NO_DEFAULT, "players/" + uuid + ".yml");

        this.save();
    }

    @Override
    protected void onLoad() {
        this.capacityTier = this.getInteger("capacity_tier", 1);
        this.mutliTier = this.getInteger("multi_tier", 1);

        this.capacity = this.getInteger("capacity");
        this.items = this.getMap("items", Integer.class, BackpackItem.class);
    }

    @Override
    protected void onSave() {
        this.set("capacity_tier", this.capacityTier);
        this.set("multi_tier", this.mutliTier);
        this.set("capacity", this.capacity);
        this.set("items", this.items);
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;

        this.save();
    }

    public void setCapacityTier(Integer capacityTier) {
        this.capacityTier = capacityTier;

        this.save();
    }

    public void setMutliTier(Integer mutliTier) {
        this.mutliTier = mutliTier;

        this.save();
    }

    public void setBackpackItem(Material material, Integer amount) {
        for(int slot : Backpack.slots) {
            if(this.items.get(slot) == null) {

                if(material == Material.BEETROOTS) {
                    this.items.put(slot, new BackpackItem(Material.BEETROOT, amount));
                    break;
                } else if(material == Material.CARROTS) {
                    this.items.put(slot, new BackpackItem(Material.CARROT, amount));
                    break;
                } else {
                    this.items.put(slot, new BackpackItem(material, amount));
                    break;
                }
            }
        }

        this.save();
    }

    public boolean checkIfBackpackItemExists(Material material) {
        for(Integer key : getItems().keySet()) {
            if(getItems().get(key).getMaterial() == material) {
                return true;
            }
        }
        return false;
    }

    public void addAmount(Integer amount, Material material) {

        if(getItems() == null) return;

        for(Integer key : getItems().keySet()) {
            if(getItems().get(key).getMaterial() == material) {
                this.items.get(key).amount = this.items.get(key).amount + amount;
            }
        }

        this.save();
    }

    public static PlayerData from(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = playerData.get(uuid);

        if (data == null) {
            data = new PlayerData(uuid);

            playerData.put(uuid, data);
        }

        return data;
    }

    public static void remove(Player player) {
        playerData.remove(player.getUniqueId());
    }

    @Getter
    @AllArgsConstructor(access =  AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BackpackItem implements ConfigSerializable {

        private Material material;
        private Integer amount;


        @Override
        public SerializedMap serialize() {
            SerializedMap map = new SerializedMap();

            map.put("material", this.material);
            map.put("amount", this.amount);

            return map;
        }

        public static BackpackItem deserialize(SerializedMap map) {
            BackpackItem backpackItem = new BackpackItem();

            backpackItem.material = Material.getMaterial(map.getString("material"));
            backpackItem.amount = map.getInteger("amount");

            return backpackItem;
        }
    }

    public Menu toMenu(Player player) {
        if(player == null || !player.isOnline()) {
            return null;
        }

        BackpackMenu menu = new BackpackMenu(player);


        if(getItems() != null) {
            for(Integer key : getItems().keySet()) {
                BackpackItem item = getItems().get(key);

                if(item != null) {
                    ItemStack newItem = ItemCreator
                            .of(ItemStack.of(getItems().get(key).getMaterial()))
                            .name(getItems().get(key).getMaterial().name() + " " + getItems().get(key).getAmount() + "x")
                            .make();

                    menu.addItem(key, newItem);
                }
            }
        }

        return menu;
    }
}
