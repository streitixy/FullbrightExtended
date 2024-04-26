package io.papermc.fullbrightextended;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static io.papermc.fullbrightextended.FullbrightExtended.prefix;

public string sendMessage(string language, string messageKey){
    if (language.equals("en_US")){
        return(getConfig().getString(language+"."+messageKey))



    }

}
public class FullbrightExtended extends JavaPlugin {

    private ConfigManager configManager;
    private LanguageManager languageManager;
    public static String prefix;
    public static String language;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setupConfig();
        languageManager = new LanguageManager(this);
        languageManager.setupConfig();

        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "&6[FullbrightExtended] "));
        language = getConfig().getString("language", "en_US");

        this.getCommand("fullbright").setExecutor(new Command());
        this.getCommand("fullbright").setTabCompleter(new FullbrightCommandTabCompleter());

        this.getCommand("fblanguage").setExecutor(new Command());
        this.getCommand("fblanguage").setTabCompleter(new LanguageCommand());


        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FullbrightExtended]: Plugin is enabled");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FullbrightExtended]: Plugin is disabled");
    }
}


class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fullbright")) {
            if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
                if (effect == null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                    sender.sendMessage(prefix + ChatColor.GREEN + sendMessage(language, "fb_on"));
                } else {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "fb_off"));
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    if (sender.hasPermission("FullbrightExtended.command.fullbrightextendedarguments")) {
                        PotionEffect effect = target.getPotionEffect(PotionEffectType.NIGHT_VISION);
                        if (effect == null) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                            sender.sendMessage(prefix + ChatColor.GREEN +  sendMessage(language, "fb_on_other").replace("{player}", args[0]));
                        } else {
                            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "fb_off_other").replace("{player}", args[0]));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "permission");
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
                                sender.sendMessage(prefix + ChatColor.RED +  sendMessage(language, "fb_off_other").replace("{player}", args[0]));
                            } else if (args[1].equals("on")) {
                                target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                                sender.sendMessage(prefix + ChatColor.GREEN +  sendMessage(language, "fb_off_other").replace("{player}", args[0]));
                            } else {
                                sender.sendMessage(prefix + ChatColor.RED + "Parameter 'state' invalid.");
                            }
                        } else {
                            if (args[1].equals("off")) {
                                target.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "fb_already_on_other").replace("{player}", args[0]));
                            } else if (args[1].equals("on")) {
                                sender.sendMessage(prefix + ChatColor.GREEN + sendMessage(language, "fb_alreay_off_other").replace("{player}", args[0]));
                            } else {
                                sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "parameter_state"));
                            }
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "permission"));
                    }
                } else {
                    sender.sendMessage(prefix + ChatColor.GOLD + sendMessage(language, "fb_player_not_found").replace("{player}", args[0]));
                }
            }
            if (args.length > 2) {
                sender.sendMessage(prefix + ChatColor.RED + sendMessage(language, "parameter_toomuch"));
                return false;
            }
        }
            else if (cmd.getName().equalsIgnoreCase("fblanguage")) {
                if (args[0].equals("pt_BR")){
                    getConfig().set("language", "pt_BR");
                }

                else if (args[0].equals("en_US")){
                    getConfig().set("language", "en_US");
                }



            }
        }
        return true;

    }
}


