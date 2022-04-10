package ru.nightwill.prison.player;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.faction.FactionType;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.CorePlugin;
import ru.xfenilafs.core.database.RemoteDatabaseTable;
import ru.xfenilafs.core.database.query.RemoteDatabaseRowType;
import ru.xfenilafs.core.database.query.row.TypedQueryRow;
import ru.xfenilafs.core.database.query.row.ValueQueryRow;
import ru.xfenilafs.core.util.ChatUtil;
import ru.xfenilafs.core.util.JsonUtil;
import sexy.kostya.mineos.network.client.SexyClient;
import sexy.kostya.mineos.network.packets.Packet29MultiThrow;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public class PlayerManager extends BukkitRunnable {
    private static String tableName = "prison_players";

    private final Map<String, PrisonPlayer> cache = new ConcurrentHashMap<>();
    private final Type blocks_log = (new TypeToken<ConcurrentMap<String, Integer>>() {
    }).getType();

    public PlayerManager(CorePlugin corePlugin){

        Prison.getInstance().getHandler().newDatabaseQuery(tableName)
                .createTableQuery().setCanCheckExists(true)
                .queryRow(this.queryRow("id", RemoteDatabaseRowType.INT).index(TypedQueryRow.IndexType.PRIMARY).index(TypedQueryRow.IndexType.AUTO_INCREMENT))
                .queryRow(this.queryRow("name", RemoteDatabaseRowType.TEXT))
                .queryRow(this.queryRow("level", RemoteDatabaseRowType.INT))
                .queryRow(this.queryRow("balance", RemoteDatabaseRowType.DOUBLE))
                .queryRow(this.queryRow("totalblocks", RemoteDatabaseRowType.INT))
                .queryRow(this.queryRow("kills", RemoteDatabaseRowType.INT))
                .queryRow(this.queryRow("deaths", RemoteDatabaseRowType.INT))
                .queryRow(this.queryRow("needs", RemoteDatabaseRowType.TINY_INT))
                .queryRow(this.queryRow("vault", RemoteDatabaseRowType.TINY_INT))
                .queryRow(this.queryRow("autosell", RemoteDatabaseRowType.TINY_INT))
                .queryRow(this.queryRow("faction", RemoteDatabaseRowType.TEXT))
                .queryRow(this.queryRow("blocks_log", RemoteDatabaseRowType.TEXT))
                .executeSync(Prison.getInstance().getHandler());

        this.runTaskTimerAsynchronously(corePlugin, 1L, 20L * TimeUnit.MINUTES.toSeconds(5L));
    }

    @Override
    public void run() {
        this.cache.values().forEach(this::save);
    }

    public void load(Player player) {
        Prison.getInstance().getHandler().getTable(tableName).newDatabaseQuery().selectQuery().queryRow(this.valueRow("name", player.getName()))
                .executeQueryAsync(Prison.getInstance().getHandler())
                .thenAccept(result -> {
                    PrisonPlayer.PrisonPlayerBuilder prisonPlayerBuilder = PrisonPlayer.builder();
                    PrisonPlayer prisonPlayer;

                    ConcurrentHashMap<String, Integer> blocks_log = new ConcurrentHashMap<>();
                    Prison.getInstance().getBlocksPriceManager().getCache().forEach((key, value) -> blocks_log.put(value.getId(), 0));

                    try {
                        if (result.next()) {
                            prisonPlayer = prisonPlayerBuilder
                                    .player(player)
                                    .level(result.getInt("level"))
                                    .balance(result.getDouble("balance"))
                                    .totalblocks(result.getInt("totalblocks"))
                                    .kills(result.getInt("kills"))
                                    .deaths(result.getInt("deaths"))
                                    .needs(result.getBoolean("needs"))
                                    .vault(result.getBoolean("vault"))
                                    .autosell(result.getBoolean("autosell"))
                                    .factionType(FactionType.getID(result.getString("faction")))
                                    .blocks_log(JsonUtil.GSON.fromJson(result.getString("blocks_log"), this.blocks_log))
                                    .build();
                        } else {
                            prisonPlayer = PrisonPlayer.builder()
                                    .player(player)
                                    .level(1)
                                    .balance(0)
                                    .totalblocks(0)
                                    .kills(0)
                                    .deaths(0)
                                    .needs(false)
                                    .vault(false)
                                    .autosell(false)
                                    .factionType(FactionType.NORMAL)
                                    .blocks_log(blocks_log)
                                    .build();
                            this.insert(prisonPlayer);
                        }
                        this.cache.put(player.getName().toLowerCase(), prisonPlayer);
                    } catch (Exception exception) {
                        SexyClient.instance.sendPacket(new Packet29MultiThrow("hub1", Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList())));
                        ChatUtil.sendMessage(player, "&cПроизошла ошибка при загрузке ваших данных!");
                        exception.printStackTrace();
                    }
                });
    }

    public void insert(PrisonPlayer prisonPlayer){
        Prison.getInstance().getHandler().newDatabaseQuery(tableName).insertQuery()
                .queryRow(this.valueRow("name", prisonPlayer.getPlayer().getName()))
                .queryRow(this.valueRow("level", prisonPlayer.getLevel()))
                .queryRow(this.valueRow("balance", prisonPlayer.getBalance()))
                .queryRow(this.valueRow("totalblocks", prisonPlayer.getTotalblocks()))
                .queryRow(this.valueRow("kills", prisonPlayer.getKills()))
                .queryRow(this.valueRow("deaths", prisonPlayer.getDeaths()))
                .queryRow(this.valueRow("needs", prisonPlayer.isNeeds()))
                .queryRow(this.valueRow("vault", prisonPlayer.isVault()))
                .queryRow(this.valueRow("autosell", prisonPlayer.isAutosell()))
                .queryRow(this.valueRow("faction", prisonPlayer.getFactionType().getId()))
                .queryRow(this.valueRow("blocks_log", JsonUtil.toJson(prisonPlayer.getBlocks_log())))
                .executeAsync(Prison.getInstance().getHandler());
    }

    public void save(PrisonPlayer prisonPlayer){
        Prison.getInstance().getHandler().getExecuteHandler().executeUpdate(true,//language=SQL
                "UPDATE " + tableName + " SET `level` = ?, `balance` = ?, `totalblocks` = ?, `kills` = ?, `deaths` = ?, `needs` = ?, `vault` = ?, `autosell` = ?, `blocks_log` = ?, `faction` = ? WHERE `name` = ?",
                prisonPlayer.getLevel(), prisonPlayer.getBalance(), prisonPlayer.getTotalblocks(), prisonPlayer.getKills(), prisonPlayer.getDeaths(), prisonPlayer.isNeeds(), prisonPlayer.isVault(), prisonPlayer.isAutosell(), JsonUtil.toJson(prisonPlayer.getBlocks_log()), prisonPlayer.getFactionType().getId(), prisonPlayer.getPlayer().getName()
        );
    }

    public void unload(Player player){
        save(cache.remove(player.getName().toLowerCase()));
    }

    public PrisonPlayer getPlayer(Player player) {
        return getPlayer(player.getName());
    }

    public PrisonPlayer getPlayer(String name) {
        return cache.get(name.toLowerCase());
    }

    private TypedQueryRow queryRow(String name, RemoteDatabaseRowType type) {
        return (new TypedQueryRow(type, name)).index(TypedQueryRow.IndexType.NOT_NULL);
    }

    private ValueQueryRow valueRow(String name, Object value) {
        return new ValueQueryRow(name, value);
    }
}
