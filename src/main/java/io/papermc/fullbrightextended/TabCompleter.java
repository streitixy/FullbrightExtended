package io.papermc.fullbrightextended;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FullbrightCommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            return Bukkit.getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
        } else if (args.length == 2) {

            List<String> list = new ArrayList<>();
            list.add("on");
            list.add("off");
            return list;
        }


        return new ArrayList<>();
    }
}
class LanguageCommand implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            List<String> list = new ArrayList<>();
            list.add("en_US");
            list.add("pt_BR");
            return list;
        }


        return new ArrayList<>();
    }
}