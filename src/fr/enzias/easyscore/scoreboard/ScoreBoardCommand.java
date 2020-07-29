package fr.enzias.easyscore.scoreboard;

import fr.enzias.easyscore.EasyScore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreBoardCommand implements CommandExecutor {

    private final EasyScore plugin;
    public ScoreBoardCommand(EasyScore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;

        if(!player.hasPermission("easyscore.use")) {
            String noPerm = plugin.getConfig().getString("messages.no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerm));
            return true;
        }
        if(args.length == 0){
            ScoreBoard scoreBoard = new ScoreBoard(plugin);
            ScoreBoardManager manager = new ScoreBoardManager(player.getUniqueId());

            if(manager.hasID()){
                manager.stop();
                scoreBoard.setEmptyBoard(player);

                String noScoreBoard = plugin.getConfig().getString("messages.no-scoreboard");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noScoreBoard));
            }
            else{
                scoreBoard.createBoard(player.getWorld().getName(), player);
                scoreBoard.start(player);
                scoreBoard.setBoard(player);

                String yesScoreBoard = plugin.getConfig().getString("messages.yes-scoreboard");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', yesScoreBoard));
            }
            return true;
        }
        else if(args.length > 1){
            String manyArgs = plugin.getConfig().getString("messages.too-many-args");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', manyArgs));
            return true;
        }
        else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("easyscore.reload")) {
                    plugin.saveDefaultConfig();
                    plugin.stopScoreForOnline();
                    plugin.scoreToOnline();

                    String configReload = plugin.getConfig().getString("messages.config-reload");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', configReload));
                } else {
                    String noPerm = plugin.getConfig().getString("messages.no-permission");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerm));
                }
                return true;
            } else if (Bukkit.getPlayerExact(args[0]) != null) {
                if(!player.hasPermission("easyscore.admin")){
                    String noPerm = plugin.getConfig().getString("messages.no-permission");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerm));
                    return true;
                }
                String targetName = args[0];

                if(targetName.equalsIgnoreCase(player.getName())){
                    String useMainCommand = plugin.getConfig().getString("messages.use-main-command");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', useMainCommand));
                    return true;
                }
                Player target = Bukkit.getPlayer(targetName);

                ScoreBoard scoreBoard = new ScoreBoard(plugin);
                ScoreBoardManager manager = new ScoreBoardManager(target.getUniqueId());

                if (manager.hasID()) {
                    manager.stop();
                    scoreBoard.setEmptyBoard(target);

                    String noScoreBoard = plugin.getConfig().getString("messages.no-scoreboard");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', noScoreBoard));
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', noScoreBoard));
                } else {
                    scoreBoard.createBoard(target.getWorld().getName(), target);
                    scoreBoard.start(target);
                    scoreBoard.setBoard(target);

                    String yesScoreBoard = plugin.getConfig().getString("messages.yes-scoreboard");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', yesScoreBoard));
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', yesScoreBoard));
                }
                return true;
            } else {
                String offlinePlayer = plugin.getConfig().getString("messages.offline-player").replace("%player%", args[0]);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', offlinePlayer));
                return true;
            }
        }
    }
}
