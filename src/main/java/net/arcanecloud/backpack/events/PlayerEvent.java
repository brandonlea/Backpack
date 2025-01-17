package net.arcanecloud.backpack.events;

import me.rivaldev.harvesterhoes.api.events.HoeItemReceiveEvent;
import me.rivaldev.harvesterhoes.api.events.RivalBlockBreakEvent;
import net.arcanecloud.backpack.menus.BackpackMenu;
import net.arcanecloud.backpack.settings.PlayerData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.SkullCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompMetadata;
import org.mineacademy.fo.remain.Remain;

@AutoRegister
public final class PlayerEvent implements Listener {

    @EventHandler
    public void onStart(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!hasBackpack(player)) {
            ItemCreator.of(CompMaterial.PLAYER_HEAD)
                    .skullUrl("http://textures.minecraft.net/texture/3e5abdb7374553d0565cbcb3295aed515a897ebce9e0bc60f1c1f8ae54c749df")
                    .tag("PlayerUUID", player.getUniqueId().toString())
                    .give(player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerData.remove(player);
    }

    @EventHandler
    public void onRightClickBackpackEvent(PlayerInteractEvent event) {
        if(!Remain.isInteractEventPrimaryHand(event))
            return;

        Player player = event.getPlayer();
        ItemStack hand = event.getItem();

        String PlayerUUID = hand != null ? CompMetadata.getMetadata(hand, "PlayerUUID") : null;

        if(PlayerUUID != null) {
            PlayerData.from(player).toMenu(player).displayTo(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakRivalHoe(RivalBlockBreakEvent event) {
        Player player = event.getPlayer();




        if(!PlayerData.from(player).checkIfBackpackItemExists(event.getCrop().getDrops().stream().findAny().get().getType())) {
            PlayerData.from(player).setBackpackItem(event.getCrop().getDrops().stream().findAny().get().getType(), event.getAmount());
        } else {
            PlayerData.from(player).addAmount(event.getAmount(), event.getCrop().getDrops().stream().findAny().get().getType());
        }
    }

    @EventHandler
    public void hoeItemReceiveEvent(HoeItemReceiveEvent event) {
        event.setGiveToInventory(false);
    }

    private boolean hasBackpack(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != CompMaterial.AIR.getMaterial()) {
                if (CompMetadata.hasMetadata(item, "PlayerUUID")) {
                    return true;
                }
            }
        }
        return false;
    }
}
