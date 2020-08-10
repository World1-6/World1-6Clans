package com.andrew121410.mc.world16clans.events;

import com.andrew121410.mc.world16clans.Clan;
import com.andrew121410.mc.world16clans.ClanUser;
import com.andrew121410.mc.world16clans.World16Clans;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnAsyncPlayerChatEvent implements Listener {

    private Map<String, Clan> clanMap;
    private Map<UUID, ClanUser> clanUserMap;

    private World16Clans plugin;

    public OnAsyncPlayerChatEvent(World16Clans plugin) {
        this.plugin = plugin;
        this.clanMap = this.plugin.getClanStorageManager().getClansMap();
        this.clanUserMap = this.plugin.getClanStorageManager().getClanUserMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ClanUser clanUser = clanUserMap.get(player.getUniqueId());
        if (clanUser != null) {
            Clan clan = clanUser.getClan();
            if (clanUser.getInClanChat() || clanUser.getInAllyChat()) {
                String message = event.getMessage();
                event.setCancelled(true);
                //Handle clan chat
                if (clanUser.getInClanChat()) {
                    clan.getClanUserMap().forEach(((uuid, clanUser1) -> {
                        Player player1 = clanUser1.getPlayer();
                        if (player1 == null) return;
                        player1.sendMessage(Translate.color("&9[CLAN] &7<" + clan.getTag() + "> " + message));
                    }));
                } else {
                    List<Clan> clans = new ArrayList<>();
                    for (String c : clan.getClanAlliesList()) {
                        clans.add(this.clanMap.get(c));
                    }
                    for (Clan clan1 : clans) {
                        for (Map.Entry<UUID, ClanUser> entry : clan1.getClanUserMap().entrySet()) {
                            UUID k = entry.getKey();
                            ClanUser v = entry.getValue();
                            Player player1 = v.getPlayer();
                            if (player1 == null) continue;
                            player1.sendMessage(Translate.color("&9[CLAN] &7<" + clan.getTag() + "> " + message));
                        }
                    }
                }
            }
        } else {
            //Player isn't in clan.
            return;
        }
    }
}
