package com.blay.liberatedchat;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LiberatedChat extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().warning("Make sure to disable enforce-secure-profile in server.properties!");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = "<" + event.getPlayer().getName() + "> " + event.getMessage();
        getLogger().info(message);
        for (Player player : event.getRecipients()) {
            player.sendMessage(message);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        List<String> msgCommands = new ArrayList<>(Arrays.asList("msg", "minecraft:msg", "tell", "minecraft:tell", "w", "minecraft:w"));
        String command = event.getMessage().substring(1).toLowerCase().split("\\s+")[0];
        if(!msgCommands.contains(command) || event.getMessage().substring(1).split("\\s+").length < 3) return;
        String receiverName = event.getMessage().substring(1).split("\\s+")[1];
        Player receiver = Bukkit.getPlayer(receiverName);
        String message = event.getMessage().substring(1).split("\\s+")[2];
        event.setCancelled(true);

        if(receiverName.contains("@")) {
            player.sendMessage(ChatColor.RED + "Selectors are not allowed");
        } else if (receiver == null) {
            player.sendMessage(ChatColor.RED + "Player not found");
        } else {
            player.sendMessage("§7§oYou whisper to " + receiverName + ": " + message);
            receiver.sendMessage("§7§o" + player.getName() + " whispers to you: " + message);
        }
    }

}
