package fr.enzias.easyscore.listeners;

import fr.enzias.easyscore.EasyScore;
import fr.enzias.easyscore.scoreboard.ScoreBoard;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private final EasyScore plugin;
    public JoinEvent(EasyScore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        World world = player.getWorld();
        if(plugin.getConfig().getStringList("settings.disabled-worlds").contains(world.getName()))
            return;

        ScoreBoard scoreBoard = new ScoreBoard(plugin);

        scoreBoard.createBoard(world.getName(), player);
        scoreBoard.start(player);
        scoreBoard.setBoard(player);

    }

}
