package ru.nightwill.prison.listeners;

import net.minecraft.server.v1_12_R1.BlockBanner;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class CancelledListener implements Listener {

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPhysicsBlock(BlockPhysicsEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
            event.setCancelled(true);
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.DROWNING){
            event.setCancelled(true);
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setDamage(100);
        }
    }

    @EventHandler
    public void onInteractPlayer(PlayerInteractEvent event){
        List<Material> materialList = Arrays.asList(Material.ANVIL, Material.ENCHANTMENT_TABLE, Material.FURNACE, Material.BURNING_FURNACE, Material.OBSERVER, Material.DROPPER, Material.DISPENSER);
        if(event.getClickedBlock() != null){
            if(materialList.contains(event.getClickedBlock().getType())){
                event.setCancelled(true);
            }
        }
    }
}
