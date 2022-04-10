package ru.nightwill.prison.commands;

import org.bukkit.entity.Player;
import ru.nightwill.prison.menu.MinesMenu;
import ru.xfenilafs.core.command.BukkitCommand;
import ru.xfenilafs.core.command.annotation.CommandCooldown;

@CommandCooldown(cooldownMillis = 2000L)
public class MineCommand extends BukkitCommand<Player> {

    public MineCommand(){
        super("mine", "mines");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        new MinesMenu(player);
    }
}
