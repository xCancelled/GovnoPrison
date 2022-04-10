package ru.nightwill.prison.blocksprice;

import jdk.internal.net.http.hpack.HPACK;
import lombok.Getter;
import org.bukkit.Material;
import ru.nightwill.prison.Prison;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.function.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlocksPriceManager {
    private static final @Getter Map<String, BlocksPriceInfo> cache = new ConcurrentHashMap<>();

    public BlocksPriceManager(){
        Prison.getInstance().getHandler().getExecuteHandler().executeQuery(true, "SELECT * FROM prison_blocks_price")
                .thenAccept(result -> {
                    while (result.next()){
                        String id = result.getString("id");
                        String name = ChatUtil.color(result.getString("name"));
                        Material material = Material.valueOf(result.getString("material"));
                        int data = result.getInt("data");
                        double price = result.getDouble("price");

                        cache.put(id, new BlocksPriceInfo(id, name, material, data, price));
                    }
                });
        Logger.info("[Prison] Загружено %s блоков для продажи!", cache.size());
    }

    public BlocksPriceInfo getBlockPrice(Material material, int data){
        for(BlocksPriceInfo blocksPriceInfo : BlocksPriceManager.getCache().values()){
            if(blocksPriceInfo.getMaterial() == material && blocksPriceInfo.getData() == data){
                return blocksPriceInfo;
            }
        }
        return null;
    }

    public BlocksPriceInfo getBlockPrice(String id){
        return BlocksPriceManager.getCache().get(id);
    }
}
