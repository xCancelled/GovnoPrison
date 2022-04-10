package ru.nightwill.prison.faction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.nightwill.prison.Prison;
import ru.xfenilafs.core.ApiManager;

@Getter
@AllArgsConstructor
public enum FactionType {
    NORMAL("normal", "§cНет",  "§cНет", "§6", null, null),
    ASIAN("asian", "§eАзиаты","§eАзиат","§e", new Location(Bukkit.getWorld("world"), 300,300,300), Material.CONCRETE_POWDER),
    BLACK("black", "§8Ниггеры","§8Ниггер", "§8", new Location(Bukkit.getWorld("world"), 200,200,200), Material.CONCRETE_POWDER),
    WHITE("white", "§fБелые", "§fБелый", "§f", new Location(Bukkit.getWorld("world"), 100,100,100), Material.CONCRETE_POWDER);

    private final String id;
    private final String name;
    private final String prefix;
    private final String color;
    private final Location location;
    private final Material material;

    public static FactionType getID(String id) {
        for (FactionType faction : values()) {
            if (faction.getId().equalsIgnoreCase(id))
                return faction;
        }
        return null;
    }

    public static int getAllPlayersInFaction(String id){
        return (int) Bukkit.getOnlinePlayers().stream()
                .map(players -> Prison.getInstance().getPlayerManager().getPlayer(players))
                .filter(data -> data.getFactionType().getId().contains(id))
                .count();
    }

    public ItemStack getItemStack(){
        return ApiManager.newItemBuilder(material)
                .setName("§fФракция §7» " + this.name)
                .setLore(
                        "",
                        "§fОнлайн фракции: §a" + getAllPlayersInFaction(id),
                        "")
                .build();
    }
}