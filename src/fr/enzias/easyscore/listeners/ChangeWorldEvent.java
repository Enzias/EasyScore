package fr.enzias.easyscore.listeners;

import fr.enzias.easyscore.EasyScore;
import fr.enzias.easyscore.scoreboard.ScoreBoard;
import fr.enzias.easyscore.scoreboard.ScoreBoardManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ChangeWorldEvent implements Listener {

    private final EasyScore plugin;
    public ChangeWorldEvent(EasyScore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event){

        Player player = event.getPlayer();
        World world = player.getWorld();
        ScoreBoard scoreBoard = new ScoreBoard(plugin);
        ScoreBoardManager manager = new ScoreBoardManager(player.getUniqueId());
        if(plugin.getConfig().getStringList("settings.disabled-worlds").contains(world.getName())) {
            if(manager.hasID()) {
                manager.stop();
                scoreBoard.setEmptyBoard(player);
            }
            return;
        }

        if(manager.hasID())
            manager.stop();

        scoreBoard.createBoard(player.getWorld().getName(), player);
        scoreBoard.start(player);
        scoreBoard.setBoard(player);

    }

    @EventHandler
    public void onRevive(PlayerRespawnEvent event){

        Player player = event.getPlayer();
        World world = event.getRespawnLocation().getWorld();
        ScoreBoard scoreBoard = new ScoreBoard(plugin);
        ScoreBoardManager manager = new ScoreBoardManager(player.getUniqueId());
        if(plugin.getConfig().getStringList("settings.disabled-worlds").contains(world.getName())) {
            if(manager.hasID()) {
                manager.stop();
                scoreBoard.setEmptyBoard(player);
            }
            return;
        }

        if(manager.hasID())
            manager.stop();

        scoreBoard.createBoard(world.getName(), player);
        scoreBoard.start(player);
        scoreBoard.setBoard(player);

    }


}
