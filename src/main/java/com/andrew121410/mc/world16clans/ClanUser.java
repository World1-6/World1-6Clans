package com.andrew121410.mc.world16clans;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("ClanUser")
public class ClanUser implements ConfigurationSerializable {

    private UUID uuid;
    private String playerName;
    private Clan clan;
    private ClanRank clanRank;

    private Boolean isInClanChat;
    private Boolean isInAllyChat;

    public ClanUser(UUID uuid, String playerName, ClanRank clanRank, Boolean isInClanChat, Boolean isInAllyChat) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.clanRank = clanRank;
        this.isInClanChat = isInClanChat;
        this.isInAllyChat = isInAllyChat;
    }

    public ClanUser(UUID uuid) {
        this(uuid, Bukkit.getServer().getPlayer(uuid).getDisplayName(), ClanRank.NEWBIES, false, false);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public ClanRank getClanRank() {
        return clanRank;
    }

    public Clan getClan() {
        return clan;
    }

    public Boolean getInClanChat() {
        return isInClanChat;
    }

    public Boolean getInAllyChat() {
        return isInAllyChat;
    }

    //Dumb but oh well.
    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public void setClanRank(ClanRank clanRank) {
        this.clanRank = clanRank;
    }

    public void setInClanChat(Boolean inClanChat) {
        isInClanChat = inClanChat;
    }

    public void setInAllyChat(Boolean inAllyChat) {
        isInAllyChat = inAllyChat;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("UUID", this.uuid);
        map.put("PlayerName", this.playerName);
        map.put("ClanRank", this.clanRank.name());
        map.put("IsInClanChat", this.isInClanChat);
        map.put("IsInAllyChat", this.isInAllyChat);
        return map;
    }

    public static ClanUser deserialize(Map<String, Object> map) {
        return new ClanUser((UUID) map.get("UUID"), (String) map.get("PlayerName"), ClanRank.valueOf((String) map.get("ClanRank")), (Boolean) map.get("IsInClanChat"), (Boolean) map.get("IsInAllyChat"));
    }
}
