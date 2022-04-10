package ru.nightwill.prison.commands;

import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.menu.BlocksPriceInformationMenu;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.command.BukkitCommand;

public class TestCommand extends BukkitCommand<Player> {
    public TestCommand(){
        super("test");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        new BlocksPriceInformationMenu(player);
    }
}
