package com.andrew121410.mc.world16clans;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class ClanPlaceHolders extends PlaceholderExpansion {

    private Map<String, Clan> clanMap;
    private Map<UUID, ClanUser> clanUserMap;

    private World16Clans plugin;

    public ClanPlaceHolders(World16Clans plugin) {
        this.plugin = plugin;
        this.clanMap = this.plugin.getClanStorageManager().getClansMap();
        this.clanUserMap = this.plugin.getClanStorageManager().getClanUserMap();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "world16clans";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Andrew121410";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] args = params.split("_");
        ClanUser clanUser = this.clanUserMap.get(player.getUniqueId());
        if (clanUser == null) return "";

        if (args[0].equals("clan")) {
            switch (args[1]) {
                case "level":
                    return String.valueOf(clanUser.getClan().getLevel());
                case "tag":
                    return clanUser.getClan().getTag();
                case "name":
                    return clanUser.getClan().getClanName();
            }
        } else if (args[0].equals("user")) {
            switch (args[1]) {
                case "rank":
                    return clanUser.getClanRank().name();
            }
        }
        return "";
    }
}
