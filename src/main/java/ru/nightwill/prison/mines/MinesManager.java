package ru.nightwill.prison.mines;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.nightwill.prison.Prison;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.LocationUtil;
import ru.xfenilafs.core.util.function.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MinesManager {
    private static final @Getter Map<String, MinesInfo> cache = new ConcurrentHashMap<>();

    public MinesManager(){
        Prison.getInstance().getHandler().getExecuteHandler().executeQuery(true, "SELECT * FROM prison_mines")
                .thenAccept(result -> {
                   while (result.next()){
                       String id = result.getString("id");
                       String name = ChatUtil.color(result.getString("name"));
                       String lore = ChatUtil.color(result.getString("lore"));
                       Material material = Material.valueOf(result.getString("material"));
                       int data = result.getInt("data");
                       Location location = LocationUtil.fromString(result.getString("location"));
                       int level = result.getInt("level");
                       int slot = result.getInt("slot");
                       boolean vault = result.getBoolean("vault");

                       cache.put(id, new MinesInfo(id, name, lore, material, data, location, level, slot, vault));
                   }
                });
        Logger.info("[Prison] Загружено %s шахт!", cache.size());
    }

    public static ItemStack createItem(ItemStack itemStack, String name){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        return itemStack;
    }
}
