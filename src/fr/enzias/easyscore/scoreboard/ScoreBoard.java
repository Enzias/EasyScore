package fr.enzias.easyscore.scoreboard;

import fr.enzias.easyscore.EasyScore;
import fr.enzias.easyscore.tasks.RefreshingTask;
import fr.enzias.easyscore.util.PlaceHolderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;


import java.util.ArrayList;

public class ScoreBoard {

    private final EasyScore plugin;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard scoreboard = manager.getNewScoreboard();


    public ScoreBoard(EasyScore plugin) {
        this.plugin = plugin;
    }

    public void createBoard(String world, Player player){

        if(!(plugin.getConfig().get("scoreboards." + world) instanceof ConfigurationSection)) {
            world = "default";
        }
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("scoreboards." + world);
        Objective objective = scoreboard.registerNewObjective(world, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String title = section.getString("title");
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&',title));

        section = section.getConfigurationSection("lines");

        for(String number : section.getKeys(false)){
            String line = section.getString(number);

            if(PlaceHolderAPIHook.hasPlaceholders(line)){
                line = PlaceHolderAPIHook.setPlaceholders(line, player);
            }

            line = ChatColor.translateAlternateColorCodes('&',line);

            ArrayList<String> lines = subString(line);
            Team team = scoreboard.registerNewTeam(number);
            String entry = ChatColor.translateAlternateColorCodes('&', (number.length() == 2 ? '&'+number.substring(0,1)+'&'+number.substring(1,2) : '&'+number));

            team.addEntry(entry);
            team.setPrefix(lines.get(0));
            team.setSuffix(getLastColor(lines.get(0)) + lines.get(1));
            objective.getScore(entry).setScore(Integer.parseInt(number));
        }

    }

    public void setBoard(Player player){

        player.setScoreboard(scoreboard);
    }

    public void setEmptyBoard(Player player){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
    }

    public void start(Player player){


        RefreshingTask refreshingTask = new RefreshingTask(plugin, player);
        /*if(plugin.getConfig().getBoolean("settings.async-update")) {
            refreshingTask.runTaskTimerAsynchronously(plugin, 0, plugin.getConfig().getInt("settings.update-interval"));
        }else */ //TODO --> ASync Way
        refreshingTask.runTaskTimer(plugin, 0, plugin.getConfig().getInt("settings.update-interval"));

    }

    public void updateScoreboard(Player player, Scoreboard scoreboard, String world){

        for(Team team : scoreboard.getTeams()) {

            String teamName = team.getName();
            if(!(plugin.getConfig().get("scoreboards." + world) instanceof ConfigurationSection)) {
                world = "default";
            }
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("scoreboards." + world + ".lines");

                String line = section.getString(teamName);

                if(PlaceHolderAPIHook.hasPlaceholders(line)){
                    line = PlaceHolderAPIHook.setPlaceholders(line, player);
                }

                line = ChatColor.translateAlternateColorCodes('&',line);

                ArrayList<String> lines = subString(line);

                team.setPrefix(lines.get(0));
                team.setSuffix(getLastColor(lines.get(0)) + lines.get(1));
        }
    }

    public ArrayList<String> subString(String s){

        ArrayList<String> list = new ArrayList<>();

        if(s.length() <= 16) {
            list.add(s);
            list.add("");
        }
        else if(s.length()  > 32)
            plugin.getLogger().warning("You can't set a line with more than 32 characters !");
        else {
            int length = s.length();
            String s1 = s.substring(0, 16);
            String s2 = s.substring(16, length);
            list.add(s1);
            list.add(s2);
        }
        return list;
    }

    public String getLastColor(String s){
        String color = ChatColor.getLastColors(s);
        if(color == null)
            return "";
        return color;
    }

}
