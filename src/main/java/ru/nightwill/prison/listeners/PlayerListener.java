package ru.nightwill.prison.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.player.PrisonPlayer;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);

        Prison.getInstance().getPlayerManager().load(event.getPlayer());
        Prison.getInstance().getPlayerManager().getPlayer(event.getPlayer()).loadScoreboard();;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);

        Prison.getInstance().getPlayerManager().unload(event.getPlayer());
    }
}
