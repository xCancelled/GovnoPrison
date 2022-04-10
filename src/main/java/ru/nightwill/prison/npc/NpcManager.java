package ru.nightwill.prison.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import ru.nightwill.prison.Prison;
import ru.xfenilafs.core.protocollib.entity.impl.FakePlayer;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.LocationUtil;
import ru.xfenilafs.core.util.function.AsyncUtil;
import ru.xfenilafs.core.util.function.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NpcManager {
    private static final @Getter Map<String, NpcInfo> cache = new ConcurrentHashMap<>();

    public NpcManager() {
        Prison.getInstance().getHandler().getExecuteHandler().executeQuery(true, "SELECT * FROM prison_npc")
                .thenAccept(result -> {
                    String name = ChatUtil.color(result.getString("name"));
                    String skin = result.getString("skin");
                    Location location = LocationUtil.fromString(result.getString("location"));
                    EntityType entityType = EntityType.valueOf(result.getString("entityType"));

                    AsyncUtil.submitAsync(() -> {
                        switch (entityType){
                            case PLAYER:
                                FakePlayer fakePlayer = new FakePlayer(location, name);
                                fakePlayer.setCustomName(name);
                                fakePlayer.setSkin(skin);

                                fakePlayer.spawn();
                                break;
                        }
                    });

                    cache.put(name, new NpcInfo(name, skin, location, entityType));
                });
        Logger.info("[Prison] Загружено %s нпс!", cache.size());
    }
}
