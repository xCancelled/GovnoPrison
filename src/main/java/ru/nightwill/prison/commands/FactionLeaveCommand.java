package ru.nightwill.prison.commands;

import org.bukkit.entity.Player;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.faction.FactionType;
import ru.nightwill.prison.player.PrisonPlayer;
import ru.xfenilafs.core.command.BukkitCommand;
import ru.xfenilafs.core.util.ChatUtil;

public class FactionLeaveCommand extends BukkitCommand<Player> {
    public FactionLeaveCommand() {
        super("factionleave", "fleave");
    }

    @Override
    protected void onExecute(Player player, String[] strings) {
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);

        if(!prisonPlayer.getFactionType().equals(FactionType.NORMAL)){
           if(prisonPlayer.getBalance() >= 2500){
               prisonPlayer.setFactionType(FactionType.NORMAL);
           } else {
               ChatUtil.sendMessage(player, "&cУ вас недостаточно монет!");
           }
        } else {
            ChatUtil.sendMessage(player, "&cВы не состоите во фракции!");
        }
    }
}
