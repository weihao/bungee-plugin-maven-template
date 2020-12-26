package org.akadia.template;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin implements Listener {
    private static Configuration configuration;

    @Override
    public void onEnable() {
        getLogger().info("Yay! It loads!");

        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new Lobby());


        getProxy().getScheduler().runAsync(this, () -> System.out.println("Hello from Runnable"));
        getProxy().getScheduler().schedule(this, () -> {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(
                        new ComponentBuilder("Currently online players count: ")
                                .color(ChatColor.GRAY)
                                .append(String.valueOf(getProxy().getOnlineCount()))
                                .color(ChatColor.AQUA)
                                .create());
            }
        }, 0, 10, TimeUnit.SECONDS);


        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(configuration.getInt("version"));

    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent(event.getPlayer().getName() + " has joined the network."));
        }
    }
}