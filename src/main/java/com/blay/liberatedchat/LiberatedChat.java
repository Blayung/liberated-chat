package com.blay.liberatedchat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Arrays;

public final class LiberatedChat extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().warning("Make sure to disable enforce-secure-profile in server.properties, or the plugin will break!");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        String message = "<" + event.getPlayer().getName() + "> " + event.getMessage();
        getLogger().info(message);
        for (Player player : event.getRecipients()) {
            player.sendMessage(message);
        }
    }

    public static final List<String> whisperCommands = Arrays.asList("/w", "/minecraft:w", "/msg", "/minecraft:msg", "/tell", "/minecraft:tell", "/teammsg", "/minecraft:teammsg");

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String args[] = event.getMessage().split(" ");
        if(!whisperCommands.contains(args[0]) || args.length < 3){
            return;
        }

        event.setCancelled(true);

        Player sender = event.getPlayer();

        if(args[1].charAt(0)=='@') {
            sender.sendMessage("§cSelectors (@s, @a...) are not allowed. It is caused by the liberated chat plugin, but we are aiming to fix it soon.");
            return;
        }

        Player receiver = Bukkit.getPlayer(args[1]);
        if (receiver == null) {
            sender.sendMessage("§cPlayer not found");
            return;
        }

        String message="";
        for(int i=2; i<args.length; i++){
            message += args[i];
        }
        sender.sendMessage("§7You whisper to " + receiver.getName() + ": " + message);
        receiver.sendMessage("§7" + sender.getName() + " whispers to you: " + message);
    }
}
