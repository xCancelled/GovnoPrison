package ru.nightwill.prison.menu;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.nightwill.prison.blocksprice.BlocksPriceManager;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.inventory.impl.BaseSimpleInventory;

import java.util.Comparator;

public class BlocksPriceInformationMenu extends BaseSimpleInventory {

    public BlocksPriceInformationMenu(Player player){
        super(6, "§0Стоимость блоков");
        this.openInventory(player);
    }

    @Override
    public void drawInventory(@NonNull Player player) {

        for (int i = 0; i < 9; i++) {
            this.getInventory().setItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
        }

        for (int i = 45; i < 54; i++) {
            this.getInventory().setItem(i, ApiManager.newItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
        }

        BlocksPriceManager.getCache().entrySet().stream()
                .sorted(Comparator.comparingInt(sorted -> Integer.parseInt(sorted.getValue().getId())))
                .forEach(blocksInfo -> {
                    this.getInventory().addItem(ApiManager.newItemBuilder(blocksInfo.getValue().getItemStack())
                            .setLore(
                                    "",
                                    "§fЦена §a(x1) §7» §a" + blocksInfo.getValue().getPrice() + "$",
                                    "§fЦена §a(x64) §7» §a" + blocksInfo.getValue().getPrice() * 64 + "$"
                            )
                            .build());
                });
    }
}
