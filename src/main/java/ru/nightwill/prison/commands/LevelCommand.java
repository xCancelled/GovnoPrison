package ru.nightwill.prison.commands;

import org.bukkit.entity.Player;
import ru.nightwill.prison.menu.LevelMenu;
import ru.xfenilafs.core.command.BukkitCommand;

public class LevelCommand extends BukkitCommand<Player> {
    public LevelCommand(){
        super("level");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        new LevelMenu(player);
    }
}
