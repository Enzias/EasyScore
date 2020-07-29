package fr.enzias.easyscore.tasks;

import fr.enzias.easyscore.EasyScore;
import fr.enzias.easyscore.scoreboard.ScoreBoard;
import fr.enzias.easyscore.scoreboard.ScoreBoardManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RefreshingTask extends BukkitRunnable {

    private final EasyScore plugin;
    private Player player;

    public RefreshingTask(EasyScore plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {

        ScoreBoardManager manager = new ScoreBoardManager(player.getUniqueId());
        ScoreBoard scoreBoard = new ScoreBoard(plugin);
        if(!manager.hasID())
            manager.setID(this);

        scoreBoard.updateScoreboard(player, player.getScoreboard(), player.getWorld().getName());
    }
}
