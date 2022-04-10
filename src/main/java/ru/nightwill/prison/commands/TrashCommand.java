package ru.nightwill.prison.commands;

import org.bukkit.entity.Player;
import ru.nightwill.prison.menu.TrashMenu;
import ru.xfenilafs.core.command.BukkitCommand;
import ru.xfenilafs.core.command.annotation.CommandCooldown;
import ru.xfenilafs.core.command.sexy.SexyCommand;
import ru.xfenilafs.core.command.sexy.context.CommandContext;

@CommandCooldown(cooldownMillis = 2000L)
public class TrashCommand extends BukkitCommand<Player> {

    public TrashCommand(){
        super("trash");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        new TrashMenu(player);
    }
}
