package ru.nightwill.prison.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.blocksprice.BlocksPriceInfo;
import ru.nightwill.prison.blocksprice.BlocksPriceManager;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.nightwill.prison.utils.TextUtils;
import ru.xfenilafs.core.util.ChatUtil;

import java.util.Collection;

public class GlobalListener implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);

        if(player.getGameMode() != GameMode.SURVIVAL){
            event.setCancelled(true);
        }

        if(player.getInventory().firstEmpty() == - 1){
            ChatUtil.sendTitle(player, "&cУ вас полный инвентарь!", "&fПродайте все блоки", 25);
            event.setCancelled(true);
        }

        Collection<ItemStack> collectionDrops = event.getBlock().getDrops();
        collectionDrops.forEach(drops -> {
            BlocksPriceInfo blocksPriceInfo = Prison.getInstance().getBlocksPriceManager().getBlockPrice(drops.getType(), drops.getData().getData());

            if(Prison.getInstance().getBlocksPriceManager().getBlockPrice(drops.getType(), drops.getData().getData()) != null){
                if(prisonPlayer.isAutosell()){
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getDrops().clear();
                    prisonPlayer.addBalance(blocksPriceInfo.getPrice());
                    prisonPlayer.addTotalBlocks(1);
                    prisonPlayer.addBlockCount(blocksPriceInfo, 1);
                } else {
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getDrops().clear();
                    player.getInventory().addItem(blocksPriceInfo.getItemStack());
                    prisonPlayer.addTotalBlocks(1);
                    prisonPlayer.addBlockCount(blocksPriceInfo, 1);
                }
            }

            event.getBlock().setType(Material.AIR);
            event.getBlock().getDrops().clear();
        });

    }
}
