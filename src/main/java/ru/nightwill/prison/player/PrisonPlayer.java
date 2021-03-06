package ru.nightwill.prison.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.nightwill.prison.Prison;
import ru.nightwill.prison.blocksprice.BlocksPriceInfo;
import ru.nightwill.prison.blocksprice.BlocksPriceManager;
import ru.nightwill.prison.faction.FactionType;
import ru.xfenilafs.core.ApiManager;
import ru.xfenilafs.core.database.query.row.ValueQueryRow;
import ru.xfenilafs.core.scoreboard.BaseScoreboardBuilder;
import ru.xfenilafs.core.scoreboard.BaseScoreboardScope;
import ru.xfenilafs.core.scoreboard.animation.ScoreboardDisplayFlickAnimation;
import ru.xfenilafs.core.util.ChatUtil;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static ru.nightwill.prison.utils.DoubleFormatter.chars;
import static ru.nightwill.prison.utils.DoubleFormatter.comma;

@AllArgsConstructor
@Getter @Setter @Builder
public class PrisonPlayer {
    private Player player;
    private int level;
    private double balance;
    private int totalblocks;
    private int kills;
    private int deaths;
    private boolean vault;
    private boolean needs;
    private boolean autosell;
    private FactionType factionType;
    private final ConcurrentMap<String, Integer> blocks_log;

    public int getBlockCount(BlocksPriceInfo blocksPriceInfo) {
        return blocks_log.getOrDefault(blocksPriceInfo.getId(), blocks_log.get(blocksPriceInfo.getId()));
    }

    public void addBlockCount(BlocksPriceInfo blocksPriceInfo, int count) {
        if (blocks_log.containsKey(blocksPriceInfo.getId())){
            blocks_log.put(blocksPriceInfo.getId(), blocks_log.get(blocksPriceInfo.getId()) + count);
        } else{
            blocks_log.put(blocksPriceInfo.getId(), count);
        }
    }

    public void setBlockCount(BlocksPriceInfo blocksPriceInfo, int count) {
        this.blocks_log.put(blocksPriceInfo.getId(), count);
    }

    public void addLevel(int level) {
        this.level += level;
    }

    public void addBalance(double balance){
        this.balance += balance;
    }

    public void takeBalance(double balance){
        if(this.balance <= balance){
            this.balance = 0;
        }
        this.balance -= balance;
    }

    public void addKills(int kills){
        this.kills += kills;
    }

    public void addDeaths(int deaths){
        this.deaths += deaths;
    }

    public void addTotalBlocks(int totalblocks){
        this.totalblocks += totalblocks;
    }

    public void loadScoreboard() {
        BaseScoreboardBuilder scoreboardBuilder = ApiManager.newScoreboardBuilder();
        ScoreboardDisplayFlickAnimation displayFlickAnimation = new ScoreboardDisplayFlickAnimation();
        displayFlickAnimation.addColor(ChatColor.WHITE);
        displayFlickAnimation.addColor(ChatColor.GOLD);
        displayFlickAnimation.addTextToAnimation("??lPRISON");
        scoreboardBuilder.scoreboardDisplay(displayFlickAnimation);
        scoreboardBuilder.scoreboardScope(BaseScoreboardScope.PROTOTYPE);
        scoreboardBuilder.scoreboardLine(14, "");
        scoreboardBuilder.scoreboardLine(13, " ??6??????????");
        scoreboardBuilder.scoreboardLine(12, " ??f??????????????: ??e????????????????");
        scoreboardBuilder.scoreboardLine(11, " ??f??????????: ??e????????????????");
        scoreboardBuilder.scoreboardLine(10, " ??f??????????????: ??e????????????????");
        scoreboardBuilder.scoreboardLine(9, "");
        scoreboardBuilder.scoreboardLine(8, " ??6????????????????????");
        scoreboardBuilder.scoreboardLine(7, " ??f???????????? ????????????: ??e????????????????");
        scoreboardBuilder.scoreboardLine(6, " ??f??????????????: ??e????????????????");
        scoreboardBuilder.scoreboardLine(5, " ??f??????????????: ??e????????????????");
        scoreboardBuilder.scoreboardLine(4, "");
        scoreboardBuilder.scoreboardLine(3, " ??6????????????????????");
        scoreboardBuilder.scoreboardLine(2, " ??f????????????: ??e" + Bukkit.getOnlinePlayers().size());
        scoreboardBuilder.scoreboardLine(1, "");
        scoreboardBuilder.scoreboardLine(0, "??ewww.starfarm.fun");

        scoreboardBuilder.scoreboardUpdater((baseScoreboard, boardPlayer) -> {
            PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(boardPlayer);

            baseScoreboard.updateScoreboardLine(12, boardPlayer, " ??f??????????????: ??e" + prisonPlayer.getLevel());
            baseScoreboard.updateScoreboardLine(11, boardPlayer, " ??f??????????: ??e" + chars(prisonPlayer.getBalance()) + " ???");
            baseScoreboard.updateScoreboardLine(10, boardPlayer, " ??f??????????????: " + (prisonPlayer.getFactionType().getName()));
            baseScoreboard.updateScoreboardLine(7, boardPlayer, " ??f???????????? ????????????: ??e" + chars(prisonPlayer.getTotalblocks()));
            baseScoreboard.updateScoreboardLine(6, boardPlayer, " ??f??????????????: ??e" + comma(prisonPlayer.getKills()));
            baseScoreboard.updateScoreboardLine(5, boardPlayer, " ??f??????????????: ??e" + comma(prisonPlayer.getDeaths()));
            baseScoreboard.updateScoreboardLine(2, boardPlayer, " ??f????????????: ??e" + Bukkit.getOnlinePlayers().size());
        }, 20);

        player.setLevel(getLevel());
        scoreboardBuilder.build().setScoreboardToPlayer(player);
    }

    public void sellBlocks(){
        PrisonPlayer prisonPlayer = Prison.getInstance().getPlayerManager().getPlayer(player);
        double totalMoney = 0.0D;
        int totalBlocks = 0;

        ItemStack[] itemStacks = player.getInventory().getContents();
        for(int l = itemStacks.length, i = 0; i < l; i++){
            ItemStack itemStack = itemStacks[i];
            if(itemStack != null && Prison.getInstance().getBlocksPriceManager().getBlockPrice(itemStack.getType(), itemStack.getData().getData()) != null){
                BlocksPriceInfo blocksPriceInfo = Prison.getInstance().getBlocksPriceManager().getBlockPrice(itemStack.getType(), itemStack.getData().getData());
                totalMoney += blocksPriceInfo.getPrice() * itemStack.getAmount();
                totalBlocks += itemStack.getAmount();
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }
        if(totalMoney > 0){
            prisonPlayer.addBalance(totalMoney);
            ChatUtil.sendMessage(player, "&f???? ???????????????? &a%s ??? &f???? &a%s &f????????????!", chars(totalMoney), chars(totalBlocks));
        } else {
            ChatUtil.sendMessage(player,  "&c?? ??????????????????, ?? ?????? ???????? ???????????? ?????????? ???? ??????????????. ?????? ?????????? ???? ???????????????? ?? ??????????!");
        }
    }
}
