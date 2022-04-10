package ru.nightwill.prison.mines;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.ItemUtil;

@AllArgsConstructor
@Getter
public class MinesInfo{
    public String id;
    public String name;
    public String lore;
    public Material material;
    public int data;
    public Location location;
    public int level;
    public int slot;
    public boolean vault;

    public ItemStack getIcon(PrisonPlayer prisonPlayer){
        ItemUtil.ItemBuilder itemBuilder = ApiManager.newItemBuilder(material);

        itemBuilder.setName(this.name);
        itemBuilder.setDurability(this.data);

        if(prisonPlayer.getLevel() >= this.level){
            itemBuilder.setMaterial(material);
        } else {
            itemBuilder.setMaterial(Material.CONCRETE).setDurability(14);
        }

        if(prisonPlayer.getLevel() >= this.getLevel()){
            if(this.lore != null){
                String[] loreString = this.lore.split("\\r?\\n");
                itemBuilder.addLore("");
                for(String s : loreString){
                    itemBuilder.addLore(ChatUtil.color(s));
                }
            }
        } else {
            itemBuilder.setLore(
                    "",
                    "§cУ вас маленький уровень,",
                    "§cповысьте свой уровень!",
                    "",
                    "§cНеобходимый уровень " + this.level);
        }

        if(prisonPlayer.getLevel() >= this.getLevel()){
            itemBuilder.addLore("", "§aДоступно");
        } else {
            itemBuilder.addLore("", "§cНедоступно");
        }

        return itemBuilder.build();
    }
}