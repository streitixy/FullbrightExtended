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

import static io.papermc.fullbrightextended.FullbrightExtended.prefix;




public class FullbrightExtended extends JavaPlugin {

    public static String prefix;
    public ConfigManager configManager;
    public LanguageManager languageManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setupConfig();
        languageManager = new LanguageManager(this);
        languageManager.setupConfig();






        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "&6[FullbrightExtended] "));

        this.getCommand("fullbright").setExecutor(new CommandDetector(this));
        this.getCommand("fullbright").setTabCompleter(new FullbrightCommandTabCompleter());

        this.getCommand("fblanguage").setExecutor(new CommandDetector(this));
        this.getCommand("fblanguage").setTabCompleter(new LanguageCommand());


        getServer().getConsoleSender().sendMessage(prefix + " " + ChatColor.GREEN + "Plugin on");
    }

    @Override
    public void onDisable() {
        languageManager = new LanguageManager(this);
        languageManager.setupConfig();
        getServer().getConsoleSender().sendMessage(prefix + " " + ChatColor.RED + "Plugin off");
    }
}




class CommandDetector implements CommandExecutor {
    public FullbrightExtended plugin;


    String language;
    ConfigManager config;


    public CommandDetector(FullbrightExtended plugin) {
        this.plugin = plugin;

        config = new ConfigManager(plugin);
        config.setupConfig();
        config.reloadConfig();
        this.language = this.config.getConfig().getString("language", "en_US");


    }

    public String sendMessage(String language, String messageKey) {

        plugin.reloadConfig();
        return plugin.languageManager.getConfig().getString(language + "." + messageKey);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.languageManager.reloadConfig();
        language = plugin.languageManager.getConfig().getString("language", "en_US");
        if (cmd.getName().equalsIgnoreCase("fullbright")) {
            if (!(sender instanceof Player))
            {

                    sender.sendMessage( prefix + " " + ChatColor.RED + sendMessage(language, "console") );
                    return true;
            }
            else if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
                if (effect == null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                    sender.sendMessage(prefix + " " + ChatColor.GREEN + sendMessage(language, "fb_on"));
                } else {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "fb_off"));
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    if (sender.hasPermission("FullbrightExtended.command.fullbrightextendedarguments")) {
                        PotionEffect effect = target.getPotionEffect(PotionEffectType.NIGHT_VISION);
                        if (effect == null) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                            sender.sendMessage(prefix + " " + ChatColor.GREEN +  sendMessage(language, "fb_on_other").replace("{player}", args[0]));
                        } else {
                            target.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "fb_off_other").replace("{player}", args[0]));
                        }
                    } else {
                        sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "permission"));
                    }
                } else {
                    sender.sendMessage(prefix + " " + ChatColor.GOLD + sendMessage(language, "fb_player_not_found").replace("{player}", args[0]));
                }
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    if (sender.hasPermission("FullbrightExtended.command.fullbrightextendedarguments")) {
                        PotionEffect effect = target.getPotionEffect(PotionEffectType.NIGHT_VISION);
                        if (effect == null) {
                            if (args[1].equals("off")) {
                                sender.sendMessage(prefix + " " + ChatColor.RED +  sendMessage(language, "fb_off_other").replace("{player}", args[0]));
                            } else if (args[1].equals("on")) {
                                target.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                                sender.sendMessage(prefix + " " + ChatColor.GREEN +  sendMessage(language, "fb_off_other").replace("{player}", args[0]));
                            } else {
                                sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "parameter_state"));
                            }
                        } else {
                            if (args[1].equals("off")) {
                                target.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                sender.sendMessage(prefix + " " + ChatColor.GREEN + sendMessage(language, "fb_already_on_other").replace("{player}", args[0]));
                            } else if (args[1].equals("on")) {
                                sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "fb_alreay_off_other").replace("{player}", args[0]));
                            } else {
                                sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "parameter_state"));
                            }
                        }
                    } else {
                        sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "permission"));
                    }
                } else {
                    sender.sendMessage(prefix + " " + ChatColor.GOLD + sendMessage(language, "fb_player_not_found").replace("{player}", args[0]));
                }
            }
            if (args.length > 2) {
                sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "parameter_toomuch"));
                return false;
            }
        }
        }
            else if (cmd.getName().equalsIgnoreCase("fblanguage")) {
                if (sender.hasPermission("FullbrightExtended.language.language")) {
                    if (args.length > 0) {
                        if (args[0].equals("pt_BR")) {
                            config.getConfig().set("language", "pt_BR");
                            config.saveConfig();
                            config.reloadConfig();

                            language = "pt_BR";
                        } else if (args[0].equals("en_US")) {
                            config.getConfig().set("language", "en_US");
                            config.saveConfig();
                            config.reloadConfig();
                            language = "en_US";
                        } else {
                            sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "language_invalid"));
                            return true;
                        }


                        sender.sendMessage(prefix + " " + ChatColor.GREEN + sendMessage(language, "language_changed"));
                    } else {
                        return false;
                    }
                }
                else{
                    sender.sendMessage(prefix + " " + ChatColor.RED + sendMessage(language, "permission"));
            }



                }
        return true;
    }






}




