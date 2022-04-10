package ru.nightwill.prison.menu;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.inventory.impl.BaseSimpleInventory;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.PlayerCooldownUtil;

public class MinesMenu extends BaseSimpleInventory {
    public MinesMenu(Player player){
        super(6,"§0Список локаций");
        this.openInventory(player);
    }

    @Override
    public void drawInventory(@NonNull Player player) {
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);

        for(int i = 0; i < 9; i++){
            this.getInventory().setItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
        }

        for(int i = 45; i < 54; i++){
            this.getInventory().setItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
        }

        Prison.getInstance().getMinesManager().getCache().forEach((key, value) -> {
            this.addItem(value.getSlot(), value.getIcon(prisonPlayer), (o, e) -> {
                if(prisonPlayer.getLevel() <= value.getLevel()){
                    ChatUtil.sendMessage(player, "&cВаш уровень меньше необходимого!");
                    player.closeInventory();
                    return;
                }

                if(prisonPlayer.isVault() != value.isVault()){
                    ChatUtil.sendMessage(player, "&cУ вас нету доступа в подвал!");
                    player.closeInventory();
                    return;
                }

                if(!PlayerCooldownUtil.hasCooldown("click_mines", player)){
                    player.teleport(value.getLocation());
                    player.closeInventory();
                }
                PlayerCooldownUtil.putCooldown("click_mines", player, 2000L);
            });
        });
    }
}

