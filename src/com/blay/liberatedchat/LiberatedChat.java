package com.blay.liberatedchat;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public final class LiberatedChat extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message="<"+event.getPlayer().getName()+"> "+event.getMessage();
        getLogger().info(message);
        for(Player player:event.getRecipients()){
            player.sendMessage(message);
        }
        event.setCancelled(true);
    }
}
