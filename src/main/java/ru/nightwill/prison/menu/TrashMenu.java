package ru.nightwill.prison.menu;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.xfenilafs.core.inventory.impl.BaseSimpleInventory;

public class TrashMenu extends BaseSimpleInventory {
    public TrashMenu(Player player){
        super(6, "§0Мусорка");
        this.openInventory(player);
    }

    @Override
    public void drawInventory(@NonNull Player player) {

    }
}
