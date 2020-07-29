package fr.enzias.easyscore;

import fr.enzias.easyscore.listeners.*;
import fr.enzias.easyscore.scoreboard.*;
import fr.enzias.easyscore.util.PlaceHolderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyScore extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();
        if(getConfig().getBoolean("settings.enable")) {
            registerEvents();
            registerCommands();
            scoreToOnline();
        }

        PlaceHolderAPIHook placeHolderAPIHook = new PlaceHolderAPIHook();
        placeHolderAPIHook.setupPlugin();
        if(PlaceHolderAPIHook.hasValidPlugin())
            getLogger().info("[EasyScore] Successfully hooked with PlaceholderAPI.");
        else getLogger().info("[EasyScore] PlaceholderAPI wasn't find.");

        Bukkit.getServer().getLogger().info("[EasyScore] Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {

        if(getConfig().getBoolean("settings.enable"))
        stopScoreForOnline();

        Bukkit.getServer().getLogger().info("[EasyScore] Plugin successfully disabled.");
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChangeWorldEvent(this), this);
    }

    public void registerCommands(){
        getCommand("easyscore").setExecutor(new ScoreBoardCommand(this));
    }

    public void scoreToOnline(){
        if(!Bukkit.getOnlinePlayers().isEmpty())
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(getConfig().getStringList("settings.disabled-worlds").contains(player.getWorld().getName()))
                    return;
                ScoreBoard scoreBoard = new ScoreBoard(this);
                scoreBoard.createBoard(player.getWorld().getName(), player);
                scoreBoard.start(player);
                scoreBoard.setBoard(player);
            }
    }

    public void stopScoreForOnline(){
        if(!Bukkit.getOnlinePlayers().isEmpty())
            for (Player player : Bukkit.getOnlinePlayers()){
                ScoreBoardManager manager = new ScoreBoardManager(player.getUniqueId());
                ScoreBoard scoreBoard = new ScoreBoard(this);
                if(manager.hasID())
                    manager.stop();
                scoreBoard.setEmptyBoard(player);
            }
    }

}
