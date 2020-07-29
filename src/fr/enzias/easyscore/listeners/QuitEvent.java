package fr.enzias.easyscore.listeners;

import fr.enzias.easyscore.EasyScore;
import fr.enzias.easyscore.scoreboard.ScoreBoard;
import fr.enzias.easyscore.scoreboard.ScoreBoardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    private final EasyScore plugin;
    public QuitEvent(EasyScore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        ScoreBoardManager manager = new ScoreBoardManager(event.getPlayer().getUniqueId());
        ScoreBoard scoreBoard = new ScoreBoard(plugin);
        if(manager.hasID()) {
            manager.stop();
            scoreBoard.setEmptyBoard(event.getPlayer());
        }
    }

}
