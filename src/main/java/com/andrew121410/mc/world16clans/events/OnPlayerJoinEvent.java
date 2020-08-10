package com.andrew121410.mc.world16clans.events;

import com.andrew121410.mc.world16clans.Clan;
import com.andrew121410.mc.world16clans.ClanUser;
import com.andrew121410.mc.world16clans.World16Clans;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerJoinEvent implements Listener {

    private Map<String, Clan> clanMap;
    private Map<UUID, ClanUser> clanUserMap;

    private World16Clans plugin;

    public OnPlayerJoinEvent(World16Clans plugin) {
        this.plugin = plugin;
        this.clanMap = this.plugin.getClanStorageManager().getClansMap();
        this.clanUserMap = this.plugin.getClanStorageManager().getClanUserMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ClanUser clanUser = this.clanUserMap.get(player.getUniqueId());
        if (clanUser != null) {
            Clan clan = clanUser.getClan();
            String motd = clan.getMotd();
            if (motd == null) return;
            if (motd.equals("")) return;
            player.sendMessage(Translate.color(motd));
        }
    }
}
