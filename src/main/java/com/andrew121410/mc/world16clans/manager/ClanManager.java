package com.andrew121410.mc.world16clans.manager;

import com.andrew121410.mc.world16clans.Clan;
import com.andrew121410.mc.world16clans.ClanRank;
import com.andrew121410.mc.world16clans.ClanUser;
import com.andrew121410.mc.world16clans.World16Clans;

import java.util.Map;
import java.util.UUID;

public class ClanManager {

    private Map<String, Clan> clanMap;
    private Map<UUID, ClanUser> clanUserMap;

    private World16Clans plugin;

    public ClanManager(World16Clans plugin) {
        this.plugin = plugin;
        this.clanMap = this.plugin.getClanStorageManager().getClansMap();
        this.clanUserMap = this.plugin.getClanStorageManager().getClanUserMap();
    }

    public void createClan(String name, String tag, UUID owner) {
        Clan clan = new Clan(name, tag);
        ClanUser clanUser = new ClanUser(owner);
        clanUser.setClanRank(ClanRank.LEADER);
        clanUser.setClan(clan);
        clan.getClanUserMap().put(owner, clanUser);
        this.clanUserMap.put(owner, clanUser);
        this.clanMap.put(name, clan);
    }

    public void deleteClan(String name) {
        Clan clan = this.clanMap.get(name);
        for (Map.Entry<UUID, ClanUser> uuidClanUserEntry : clan.getClanUserMap().entrySet()) {
            this.clanUserMap.remove(uuidClanUserEntry.getKey());
        }
        this.clanMap.remove(name);
        this.plugin.getClanStorageManager().deleteClan(name);
    }

    public void kickPlayerFromClan(String name, UUID uuid) {
        Clan clan = this.clanMap.get(name);
        ClanUser clanUser = this.clanUserMap.get(uuid);
        clan.getClanUserMap().remove(uuid);
        this.clanUserMap.remove(uuid);
    }

    public void addPlayerToClan(String name, UUID uuid) {
        Clan clan = this.clanMap.get(name);
        ClanUser clanUser = new ClanUser(uuid);
        clan.getClanUserMap().put(uuid, clanUser);
        this.clanUserMap.put(uuid, clanUser);
    }
}
