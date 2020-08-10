package com.andrew121410.mc.world16clans;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs("Clan")
public class Clan implements ConfigurationSerializable {

    private String clanName;
    private String tag;
    private Integer level;
    private Location homeLocation;
    private String motd;
    private Map<UUID, ClanUser> clanUserMap;

    private List<String> clanAlliesList;

    public Clan(String clanName, String tag, Integer level, Location homeLocation, String motd, Map<UUID, ClanUser> clanUserMap, List<String> clanAlliesList) {
        this.clanName = clanName;
        this.tag = tag;
        this.level = level;
        this.homeLocation = homeLocation;
        this.motd = motd;
        this.clanUserMap = clanUserMap;
        this.clanAlliesList = clanAlliesList;
        this.clanUserMap.forEach(((uuid, clanUser) -> clanUser.setClan(this)));
    }

    public Clan(String name, String tag) {
        this(name, tag, 1, null, null, new HashMap<>(), new ArrayList<>());
    }

    //Getters
    public String getClanName() {
        return clanName;
    }

    public String getTag() {
        return tag;
    }

    public Integer getLevel() {
        return level;
    }

    public Location getHomeLocation() {
        return homeLocation;
    }

    public String getMotd() {
        return motd;
    }

    public Map<UUID, ClanUser> getClanUserMap() {
        return clanUserMap;
    }

    public List<String> getClanAlliesList() {
        return clanAlliesList;
    }

    //Setters
    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.clanName);
        map.put("Tag", this.tag);
        map.put("Level", this.level);
        map.put("HomeLocation", this.homeLocation);
        map.put("MOTD", this.motd);
        map.put("ClanUserMap", this.clanUserMap);
        map.put("ClanAlliesList", this.clanAlliesList);
        return map;
    }

    public static Clan deserialize(Map<String, Object> map) {
        return new Clan((String) map.get("Name"), (String) map.get("Tag"), (Integer) map.get("Level"), (Location) map.get("HomeLocation"), (String) map.get("MOTD"), (Map<UUID, ClanUser>) map.get("ClanUsersList"), (List<String>) map.get("ClanAlliesList"));
    }
}
