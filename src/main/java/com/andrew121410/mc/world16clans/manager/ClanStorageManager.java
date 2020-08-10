package com.andrew121410.mc.world16clans.manager;

import com.andrew121410.mc.world16clans.Clan;
import com.andrew121410.mc.world16clans.ClanUser;
import com.andrew121410.mc.world16clans.World16Clans;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanStorageManager {

    private Map<String, Clan> clansMap;
    private Map<UUID, ClanUser> clanUserMap;

    private World16Clans plugin;
    private CustomYmlManager clansConfig;

    public ClanStorageManager(World16Clans plugin) {
        this.plugin = plugin;
        this.clansMap = new HashMap<>();
        this.clanUserMap = new HashMap<>();
        //clans.yml
        this.clansConfig = new CustomYmlManager(this.plugin, false);
        this.clansConfig.setup("clans.yml");
        this.clansConfig.saveConfig();
        this.clansConfig.reloadConfig();
        //...
        setupConfigSelection();
    }

    private ConfigurationSection setupConfigSelection() {
        //This runs when clans.yml is first created.
        ConfigurationSection elevatorControllersSection = this.clansConfig.getConfig().getConfigurationSection("Clans");
        if (elevatorControllersSection == null) {
            this.clansConfig.getConfig().createSection("Clans");
            this.clansConfig.saveConfig();
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[ClanStorageManager]&r&6 Clans section has been created."));
        }
        return elevatorControllersSection;
    }

    public Clan loadClan(String key) {
        ConfigurationSection clanSelection = setupConfigSelection();
        if (clanSelection.contains(key)) {
            return (Clan) clanSelection.get(key);
        }
        return null;
    }

    public void saveClan(Clan clan) {
        ConfigurationSection clanSelection = setupConfigSelection();
        clanSelection.set(clan.getClanName(), clan);
    }

    public void loadAllClans() {
        ConfigurationSection clanSelection = setupConfigSelection();
        for (String key : clanSelection.getKeys(false)) {
            Clan clan = loadClan(key);
            clan.getClanUserMap().forEach(((uuid, clanUser) -> this.clanUserMap.putIfAbsent(uuid, clanUser)));
            this.clansMap.put(key, clan);
        }
    }

    public void saveAllClans() {
        ConfigurationSection clanSelection = setupConfigSelection();
        this.clansMap.forEach(clanSelection::set);
    }

    public void deleteClan(String key) {
        ConfigurationSection clanSelection = setupConfigSelection();
        clanSelection.set(key, null);
    }

    public Map<String, Clan> getClansMap() {
        return clansMap;
    }

    public Map<UUID, ClanUser> getClanUserMap() {
        return clanUserMap;
    }
}
