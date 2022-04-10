package ru.nightwill.prison.commands;

import lombok.val;
import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.faction.FactionType;
import ru.nightwill.prison.menu.FactionMenu;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.command.BukkitCommand;
import ru.xfenilafs.core.util.ChatUtil;

public class FactionCommand extends BukkitCommand<Player> {

    public FactionCommand() {
        super("faction");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);

        if(prisonPlayer.getLevel() >= 5){
            if(prisonPlayer.getFactionType().equals(FactionType.NORMAL)){
                new FactionMenu(player);
            } else {
                ChatUtil.sendMessage(player, "&cВы уже состоите во фракции!");
            }
        } else {
            ChatUtil.sendMessage(player, "&cВам необходимо достигнуть 5-го уровня!");
        }
    }
}
