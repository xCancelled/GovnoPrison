package ru.nightwill.prison.menu;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.faction.FactionType;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.inventory.impl.BaseSimpleInventory;
import ru.xfenilafs.core.inventory.item.BaseInventoryClickItem;
import ru.xfenilafs.core.util.ChatUtil;

import java.util.Arrays;

public class FactionMenu extends BaseSimpleInventory {
    public FactionMenu(Player player){
        super(3, "§0Выбор фракции");
        this.openInventory(player);
    }

    @Override
    public void drawInventory(@NonNull Player player) {
        PrisonPlayer data = Prison.getInstance().getPlayerManager().getPlayer(player);

        for (int i = 0; i < 9; i++) {
            this.getInventory().setItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
        }

        for (int i = 18; i < 27; i++) {
            this.getInventory().setItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
        }

        this.addItem(new BaseInventoryClickItem(11, ApiManager.newItemBuilder(FactionType.ASIAN.getMaterial()).setName("§fФракция §7» " + FactionType.ASIAN.getName())
                .setLore("", "§fОнлайн фракции §7» §a" + FactionType.getAllPlayersInFaction("asian"), "", "§aНажмите, чтобы стать членом фракции.")
                .setDurability(4)
                .build()
                , (o, e) -> {
            data.setFactionType(FactionType.ASIAN);
            ChatUtil.sendTitle(player, "&fВы стали членом фракции", "" + FactionType.ASIAN.getName(), 20);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
            player.closeInventory();
        }));

        this.addItem(new BaseInventoryClickItem(13, ApiManager.newItemBuilder(FactionType.WHITE.getMaterial()).setName("§fФракция §7» " + FactionType.WHITE.getName())
                .setLore("", "§fОнлайн фракции §7» §a" + FactionType.getAllPlayersInFaction("white"), "", "§aНажмите, чтобы стать членом фракции.")
                .setDurability(0)
                .build()
                , (o, e) -> {
            data.setFactionType(FactionType.WHITE);
            ChatUtil.sendTitle(player, "&fВы стали членом фракции", "" + FactionType.WHITE.getName(), 20);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
            player.closeInventory();
        }));

        this.addItem(new BaseInventoryClickItem(15, ApiManager.newItemBuilder(FactionType.BLACK.getMaterial()).setName("§fФракция §7» " + FactionType.BLACK.getName())
                .setLore("", "§fОнлайн фракции §7» §a" + FactionType.getAllPlayersInFaction("black"), "", "§aНажмите, чтобы стать членом фракции.")
                .setDurability(15)
                .build()
                , (o, e) -> {
            data.setFactionType(FactionType.BLACK);
            ChatUtil.sendTitle(player, "&fВы стали членом фракции", "" + FactionType.BLACK.getName(), 20);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
            player.closeInventory();
        }));
    }
}
