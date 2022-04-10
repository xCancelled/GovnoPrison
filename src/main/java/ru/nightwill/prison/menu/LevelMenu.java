package ru.nightwill.prison.menu;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.blocksprice.BlocksPriceInfo;
import ru.nightwill.prison.level.LevelInfo;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.inventory.impl.BaseSimpleInventory;
import ru.xfenilafs.core.inventory.item.BaseInventoryClickItem;
import ru.xfenilafs.core.util.ChatUtil;

import java.util.List;

public class LevelMenu extends BaseSimpleInventory {
    public LevelMenu(Player player) {
        super(4, "§0Повышение уровня");

        if (Prison.getInstance().getPlayerManager().getPlayer(player).getLevel() <= Prison.getInstance().getLevelManager().getMaxLevel()){
            this.openInventory(player);
        } else{
            ChatUtil.sendMessage(player, "&cВы уже достигли максимального уровня!");
        }
    }

    @Override
    public void drawInventory(@NonNull Player player){
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);
        LevelInfo levelInfo = Prison.getInstance().getLevelManager().getNextLevel(player);

        for(int i = 0; i < this.inventory.getSize(); i++){
            if(this.inventory.getItem(i) == null){
                this.addItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setDurability(15).build());
            }

            if(i == 12 || i == 14 || i == 16){
                this.addItem(i, (Prison.getInstance().getLevelManager().containsRequirements(player)
                        ? ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setDurability(12).build()
                        : ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).build()));
            }
        }

        List<String> information = Lists.newArrayList();
        ChatColor moneyColor = (prisonPlayer.getBalance() >= levelInfo.getPrice() ? ChatColor.GREEN : ChatColor.RED);
        information.add("");
        information.add("§7▪ §fМонет §7» " + moneyColor + prisonPlayer.getBalance() + "/" + levelInfo.getPrice());
        information.add("");
        information.add("§eНеобходимые блоки");
        levelInfo.getBlocks().forEach((key, value) -> {
            BlocksPriceInfo blocksPriceInfo = Prison.getInstance().getBlocksPriceManager().getBlockPrice(key);
            ChatColor blocksColor = (prisonPlayer.getBlockCount(blocksPriceInfo) >= value ? ChatColor.GREEN : ChatColor.RED);
            information.add("§7▪ " + blocksPriceInfo.getName() + " §7» " + blocksColor +  prisonPlayer.getBlockCount(blocksPriceInfo) + "/" + value);
        });

        this.addItem(new BaseInventoryClickItem(21, ApiManager.newItemBuilder(Material.EXP_BOTTLE)
                .setName("§eПовысить уровень")
                .build(),
                (o, e) -> {

                }));

        this.addItem(new BaseInventoryClickItem(23, ApiManager.newItemBuilder(Material.BOOK)
                .setName("§eТребования")
                .setLore(information)
                .build(),
                (o, e) -> {

        }));
    }
}
