package com.andrew121410.mc.world16clans;

import com.andrew121410.mc.world16clans.commands.ClanCMD;
import com.andrew121410.mc.world16clans.manager.ClanManager;
import com.andrew121410.mc.world16clans.manager.ClanStorageManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class World16Clans extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(ClanUser.class, "ClanUser");
        ConfigurationSerialization.registerClass(Clan.class, "Clan");
    }

    private static World16Clans plugin;

    private ClanStorageManager clanStorageManager;
    private ClanManager clanManager;

    @Override
    public void onEnable() {
        plugin = this;
        this.clanStorageManager = new ClanStorageManager(this);
        this.clanStorageManager.loadAllClans();
        this.clanManager = new ClanManager(this);
        ClanPlaceHolders clanPlaceHolders = new ClanPlaceHolders(this);
        clanPlaceHolders.register();
        regCommands();
    }

    @Override
    public void onDisable() {
        this.clanStorageManager.saveAllClans();
    }

    public void regCommands() {
        new ClanCMD(this);
    }

    public static World16Clans getPlugin() {
        return plugin;
    }

    public ClanStorageManager getClanStorageManager() {
        return clanStorageManager;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }
}
