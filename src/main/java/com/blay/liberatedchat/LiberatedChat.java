package com.blay.liberatedchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class LiberatedChat extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        setServerProperty("enforce-secure-profile", "false");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        String message = "<" + event.getPlayer().getName() + "> " + event.getMessage();
        getLogger().info(message);
        Bukkit.broadcastMessage(message);
    }

    public static final List<String> whisperCommands = Arrays.asList("/w", "/minecraft:w", "/msg", "/minecraft:msg", "/tell", "/minecraft:tell", "/teammsg", "/minecraft:teammsg");

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        if (!whisperCommands.contains(args[0]) || args.length < 3) {
            return;
        }

        event.setCancelled(true);

        Player sender = event.getPlayer();

        if (args[1].charAt(0) == '@') {
            sender.sendMessage("§cSelectors (@s, @a...) are not allowed in that command. It is caused by the liberated chat plugin, but we are aiming to fix it soon.");
            return;
        }

        Player receiver = Bukkit.getPlayer(args[1]);
        if (receiver == null) {
            sender.sendMessage("§cPlayer not found");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++)
            message.append(args[i]);

        sender.sendMessage("§7You whisper to " + receiver.getName() + ": " + message);
        receiver.sendMessage("§7" + sender.getName() + " whispers to you: " + message);
    }

    private void setServerProperty(String propertyName, String value) {
        Path path = Paths.get(getDataFolder().getParentFile().getAbsolutePath()).getParent().resolve("server.properties");

        try {
            List<String> lines = Files.readAllLines(path).stream()
                    .map(line -> {
                        if (line.startsWith(propertyName + "="))
                            return propertyName + "=" + value;
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
