package ru.nightwill.prison.level;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.blocksprice.BlocksPriceInfo;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.JsonUtil;
import ru.xfenilafs.core.util.function.Logger;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class LevelManager {
    private static final @Getter Map<Integer, LevelInfo> cache = new ConcurrentHashMap<>();
    private final Type blocksType = (new TypeToken<ConcurrentMap<String, Integer>>() {
    }).getType();

    public LevelManager(){
        Prison.getInstance().getHandler().getExecuteHandler().executeQuery(true, "SELECT * FROM prison_level")
                .thenAccept(result -> {
                    while (result.next()){
                        int level = result.getInt("level");
                        double price = result.getDouble("price");

                        cache.put(level, new LevelInfo(level, price, JsonUtil.GSON.fromJson(result.getString("blocks"), this.blocksType)));
                    }
                });
        Logger.info("[Prison] Загружено %s уровней!", cache.size());
    }

    public int getMaxLevel(){
        return Collections.max(cache.keySet());
    }

    public LevelInfo getNextLevel(Player player){
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);
        return LevelManager.getCache().get(prisonPlayer.getLevel() + 1);
    }

    public boolean containsRequirements(Player player){
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);
        LevelInfo levelInfo = Prison.getInstance().getLevelManager().getNextLevel(player);
        AtomicBoolean access = new AtomicBoolean(false);

        levelInfo.getBlocks().forEach((key, value) -> cache.forEach((key1, value1) -> {
            BlocksPriceInfo blocksPriceInfo = Prison.getInstance().getBlocksPriceManager().getBlockPrice(key);
            if(prisonPlayer.getBalance() >= value1.getPrice() && prisonPlayer.getBlockCount(blocksPriceInfo) >= value){
                access.set(true);
            }
        }));
        return access.get();
    }

    public void levelUp(Player player){
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);
        LevelInfo prisonLevel = Prison.getInstance().getLevelManager().getNextLevel(player);

        prisonPlayer.takeBalance(prisonLevel.getPrice());
        prisonPlayer.addLevel(1);
        player.sendTitle("Поздравляем!", "Вы повысили");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        player.closeInventory();
    }
}
