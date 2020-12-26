package org.akadia.template;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;


public class Main extends Plugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("Yay! It loads!");

        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new Lobby());


        getProxy().getScheduler().runAsync(this, () -> System.out.println("Hello from Runnable"));
        getProxy().getScheduler().schedule(this, () -> {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(new TextComponent(String.format("Currently online players count: %s", getProxy().getOnlineCount())));
            }
        }, 0, 10, TimeUnit.SECONDS);

    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent(event.getPlayer().getName() + " has joined the network."));
        }
    }
}