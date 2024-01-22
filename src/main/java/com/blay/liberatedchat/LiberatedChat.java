package com.blay.liberatedchat;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.Arrays;

public final class LiberatedChat extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        try {
            Path path = Paths.get(getDataFolder().getParentFile().getAbsolutePath()).getParent().resolve("server.properties");

            boolean[] replaced = {false};

            List<String> lines = Files.readAllLines(path).stream()
                .map(line -> {
                    if (line.equals("enforce-secure-profile=true")) {
                        replaced[0] = true;
                        return "enforce-secure-profile=false";
                    }
                    return line;
                })
                .collect(Collectors.toList());

            if (replaced[0]) {
                Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
                getLogger().warning("enforce-secure-profile in server.properties was set to true. The liberated chat plugin automatically changed it to false and now it restarts the server.");
                getServer().spigot().restart();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        getServer().broadcastMessage('<' + event.getPlayer().getName() + "> " + event.getMessage());
    }

    private static final List<String> whisperCommands = Arrays.asList("/w", "/minecraft:w", "/msg", "/minecraft:msg", "/tell", "/minecraft:tell");

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");

        if (!whisperCommands.contains(args[0]) || args.length < 3) {
            return;
        }

        Player sender = event.getPlayer();

        if (args[1].charAt(0) == '@') {
            sender.sendMessage("" + ChatColor.RED + "Selectors (@s, @a...) are not allowed in that command. It is caused by the liberated chat plugin, but we are aiming to fix it soon.");
            event.setCancelled(true);
            return;
        }

        Player receiver = getServer().getPlayer(args[1]);
        if (receiver == null) {
            return;
        }

        event.setCancelled(true);

        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            message.append(args[i]).append(' ');
        }

        sender.spigot().sendMessage(new ComponentBuilder(new TranslatableComponent("commands.message.display.outgoing", receiver.getName(), message)).color(ChatColor.GRAY).create());
        receiver.spigot().sendMessage(new ComponentBuilder(new TranslatableComponent("commands.message.display.incoming", sender.getName(), message)).color(ChatColor.GRAY).create());
    }
}
