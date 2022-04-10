package ru.nightwill.prison.commands;

import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.faction.FactionType;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.command.BukkitCommand;
import ru.xfenilafs.core.util.ChatUtil;

public class BaseCommand extends BukkitCommand<Player> {
    public BaseCommand(){
        super("base");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);

        if(!prisonPlayer.getFactionType().equals(FactionType.NORMAL)){
            Prison.getInstance().getTeleportUtil().teleport(player, prisonPlayer.getFactionType().getLocation(), "", "§aТелепортация...");
        } else {
            ChatUtil.sendMessage(player, "&cВы должны находиться во фракции!");
        }
    }
}
