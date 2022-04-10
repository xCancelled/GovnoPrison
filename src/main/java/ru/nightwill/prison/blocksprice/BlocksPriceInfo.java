package ru.nightwill.prison.blocksprice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.xfenilafs.core.ApiManager;

@Getter
@AllArgsConstructor
public class BlocksPriceInfo {
    public String id;
    public String name;
    public Material material;
    public int data;
    public double price;

    public ItemStack getItemStack(){
        return ApiManager.newItemBuilder(material)
                .setName(name)
                .setDurability(data)
                .build();
    }
}
