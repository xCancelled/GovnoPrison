package ru.nightwill.prison;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import ru.nightwill.prison.blocksprice.BlocksPriceManager;
import ru.nightwill.prison.commands.*;
import ru.nightwill.prison.level.LevelManager;
import ru.nightwill.prison.listeners.CancelledListener;
import ru.nightwill.prison.listeners.GlobalListener;
import ru.nightwill.prison.listeners.PlayerListener;
import ru.nightwill.prison.mines.MinesManager;
import ru.nightwill.prison.npc.NpcManager;
import ru.nightwill.prison.player.PlayerManager;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.CorePlugin;
import ru.xfenilafs.core.database.RemoteDatabaseConnectionHandler;
import ru.xfenilafs.core.util.TeleportUtil;

import java.util.Arrays;

@Plugin(name = "Prison", version = "1.0")
@Author(value = "NightWill")
@Dependency("SMCS")
@Getter
public class Prison extends CorePlugin {
    private static @Getter Prison instance;
    private RemoteDatabaseConnectionHandler handler;
    private PlayerManager playerManager;
    private NpcManager npcManager;
    private MinesManager minesManager;
    private TeleportUtil teleportUtil;
    private BlocksPriceManager blocksPriceManager;
    private LevelManager levelManager;

    @Override
    protected void onPluginEnable() {
        instance = this;
        handler = ApiManager.createHikariConnection("65.108.4.69", "root", "MySqlASFJhjhljl123****%%jhhjafjh#########qjhdhjkfakquk123jkh132khj@@danglpidor@", "Prison");
        playerManager = new PlayerManager(this);
        npcManager = new NpcManager();
        minesManager = new MinesManager();
        teleportUtil = new TeleportUtil(this);
        blocksPriceManager = new BlocksPriceManager();
        levelManager = new LevelManager();

        registerListeners(
                new PlayerListener(),
                new CancelledListener(),
                new GlobalListener()
        );

        registerCommands(
                new TrashCommand(),
                new MineCommand(),
                new TestCommand(),
                new FactionCommand(),
                new FactionLeaveCommand(),
                new BaseCommand(),
                new LevelCommand()
        );
    }

    @Override
    protected void onPluginDisable() {
        Bukkit.getOnlinePlayers().forEach(getPlayerManager()::unload);
        handler.handleDisconnect();
    }
}
