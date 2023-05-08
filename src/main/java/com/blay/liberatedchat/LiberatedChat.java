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

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        boolean shouldOverride=false;
        try{
            String command=event.getMessage();
            System.out.println("recieved event: "+command);

            // I had to do this monstrosity, or it won't work otherwise, fucking bullshit
            shouldOverride=false;

            for(char character : command.toCharArray()){
                System.out.println(character);
            }

            System.out.println(command.substring(0,4).toCharArray().length);
            for(char character : command.substring(0,4).toCharArray()){
                System.out.println(character);
            }

            if(command.substring(0, 1) == "/w"){
                shouldOverride=true;
            }else if(command.substring(0, 2).toCharArray() == new char[]{'/','m','e'}){
                shouldOverride=true;
            }else if(command.substring(0, 3).toCharArray() == new char[]{'/','m','s','g'}){
                shouldOverride=true;
            }else if(command.substring(0, 4).toCharArray() == new char[]{'/','t','e','l','l'}){
                shouldOverride=true;
            }else if(command.substring(0, 11).toCharArray() == new char[]{'/','m','i','n','e','c','r','a','f','t',':','w'}){
                shouldOverride=true;
            }else if(command.substring(0, 12).toCharArray() == new char[]{'/','m','i','n','e','c','r','a','f','t',':','m','e'}){
                shouldOverride=true;
            }else if(command.substring(0, 13).toCharArray() == new char[]{'/','m','i','n','e','c','r','a','f','t',':','m','s','g'}){
                shouldOverride=true;
            }else if(command.substring(0, 14).toCharArray() == new char[]{'/','m','i','n','e','c','r','a','f','t',':','t','e','l','l'}){
                shouldOverride=true;
            }

            if(shouldOverride){
                System.out.println("msg found");
                Player sender = event.getPlayer();
                String[] args = command.split(" ");
                if (args.length<3) {
                    System.out.println("bad args length");
                    sender.sendMessage(ChatColor.RED + "Unknown or incomplete command, see below for error");
                    sender.sendMessage(ChatColor.GRAY + command.substring(1) + ChatColor.RED + "" + ChatColor.ITALIC + "<--[HERE]");
                } else {
                    System.out.println("good args length");
                    Player reciever = Bukkit.getPlayer(args[1]);
                    if (reciever == null) {
                        System.out.println("player not found");
                        sender.sendMessage(ChatColor.RED + "No player was found");
                    } else {
                        System.out.println("player found");
                        String message="";
                        for(int i=2; i<args.length; i++) {
                            message += args[i];
                        }
                        System.out.println("sending msg: "+message);
                        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You whisper to " + reciever.getName() + ": " + message);
                        reciever.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + sender.getName() + " whispers to you: " + message);
                    }
                }
                System.out.println("cancelling event");
                event.setCancelled(true);
            }
        }catch(StringIndexOutOfBoundsException e){System.out.println("shitty exception, "+shouldOverride);e.printStackTrace();}
    }
}
