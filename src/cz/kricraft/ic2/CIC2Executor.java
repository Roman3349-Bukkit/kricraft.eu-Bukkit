package cz.kricraft.ic2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CIC2Executor implements CommandExecutor {

    private final Main plugin;

    public CIC2Executor(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender.hasPermission("CraftujIC2.command") || sender.isOp())) {
            sender.sendMessage(ChatColor.RED + "Nemas povoleni! (CraftujIC2.command)");
            return true;
        }
        if (args.length <= 0) {
            listCommands(sender);
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.storage.loadConfig();
            sender.sendMessage(ChatColor.GREEN + "Config.yml nacten.");
            return true;
        }
        return false;
    }

    private void listCommands(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "CraftujIC2 Pr�kazy:");
        cmd(sender, "reload", "nacte config.yml");

    }

    private void cmd(CommandSender sender, String args, String desc) {
        sender.sendMessage(ChatColor.WHITE + "/cic2 " + args + ChatColor.GRAY + " - " + desc);
    }

}
