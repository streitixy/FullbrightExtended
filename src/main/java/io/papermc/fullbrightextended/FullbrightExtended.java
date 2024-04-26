package io.papermc.fullbrightextended;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.papermc.fullbrightextended.FullbrightExtended.prefix;


public class FullbrightExtended extends JavaPlugin {

    private ConfigManager configManager;
    public static String prefix;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setupConfig();

        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "&6[FullbrightExtended] "));

        this.getCommand("fullbright").setExecutor(new FullbrightCommand());
        this.getCommand("fullbright").setTabCompleter(new FullbrightCommandTabCompleter());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FullbrightExtended]: Plugin is enabled");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FullbrightExtended]: Plugin is disabled");
    }
}


class FullbrightCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
                if (effect == null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                    sender.sendMessage(prefix + ChatColor.GREEN + "Turned fullbright on for you.");
                } else {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    sender.sendMessage(prefix + ChatColor.RED + "Turned fullbright off for you.");
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    if (sender.hasPermission("FullbrightExtended.command.fullbrightextendedarguments")) {
                        PotionEffect effect = target.getPotionEffect(PotionEffectType.NIGHT_VISION);
                        if (effect == null) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                            sender.sendMessage(prefix + ChatColor.GREEN + "Turned fullbright on for " + args[0] + ".");
                        } else {
                            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            sender.sendMessage(prefix + ChatColor.RED + "Turned fullbright off for " + args[0] + ".");
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + "You do not have permission to use this command.");
                    }
                } else {
                    sender.sendMessage(prefix + ChatColor.GOLD + "The player " + args[0] + " is not online/does not exist!");
                }
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    if (sender.hasPermission("FullbrightExtended.command.fullbrightextendedarguments")) {
                        PotionEffect effect = target.getPotionEffect(PotionEffectType.NIGHT_VISION);
                        if (effect == null) {
                            if (args[1].equals("off")) {
                                sender.sendMessage(prefix + ChatColor.RED + "Fullbright is already off for " + args[0] + "!");
                            } else if (args[1].equals("on")) {
                                target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                                sender.sendMessage(prefix + ChatColor.GREEN + "Turned fullbright on for " + args[0] + ".");
                            } else {
                                sender.sendMessage(prefix + ChatColor.RED + "Parameter 'state' invalid.");
                            }
                        } else {
                            if (args[1].equals("off")) {
                                target.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                sender.sendMessage(prefix + ChatColor.GREEN + "Turned fullbright off for " + args[0] + ".");
                            } else if (args[1].equals("on")) {
                                sender.sendMessage(prefix + ChatColor.RED + "Fullbright is already on for " + args[0] + "!");
                            } else {
                                sender.sendMessage(prefix + ChatColor.RED + "Parameter 'state' invalid.");
                            }
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + "You do not have permission to use this command.");
                    }
                } else {
                    sender.sendMessage(prefix + ChatColor.GOLD + "The player " + args[0] + " is not online/does not exist!");
                }
            }
            if (args.length > 2) {
                sender.sendMessage(prefix + ChatColor.RED + "You added too many parameters.");
                return false;
            }
        }
        return true;
    }
}


class FullbrightCommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // If the first argument, return a list of online players
            return Bukkit.getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            // If the second argument, return a list of "on" and "off"
            List<String> list = new ArrayList<>();
            list.add("on");
            list.add("off");
            return list;
        }

        // If more than two arguments, return an empty list
        return new ArrayList<>();
    }
}