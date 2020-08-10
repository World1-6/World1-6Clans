package com.andrew121410.mc.world16clans.commands;

import com.andrew121410.mc.world16clans.Clan;
import com.andrew121410.mc.world16clans.ClanUser;
import com.andrew121410.mc.world16clans.World16Clans;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ClanTAB implements TabCompleter {

    private Map<String, Clan> clanMap;
    private Map<UUID, ClanUser> clanUserMap;

    private World16Clans plugin;

    private List<String> args;

    public ClanTAB(World16Clans plugin) {
        this.plugin = plugin;
        this.clanMap = this.plugin.getClanStorageManager().getClansMap();
        this.clanUserMap = this.plugin.getClanStorageManager().getClanUserMap();

        this.args = Arrays.asList("create", "invite", "info", "promote", "demote", "levelup", "kick", "pass", "leave", "disband", "sethome", "home", "alliance", "tag", "motd", "c", "ca", "ct", "accept", "deny");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String ailes, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("clan")) {
            return null;
        }

        ClanUser clanUser = this.clanUserMap.get(player.getUniqueId());

        List<String> clansList = new ArrayList<>(this.clanMap.keySet());
        List<String> playersList = this.plugin.getServer().getOnlinePlayers().stream().map(Player::getDisplayName).collect(Collectors.toList());
        List<String> clanMembers = null;
        if (clanUser != null) {
            clanMembers = clanUser.getClan().getClanUserMap().values().stream().map(ClanUser::getPlayerName).collect(Collectors.toList());
        }

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], this.args, new ArrayList<>());
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length == 2) {
                return StringUtil.copyPartialMatches(args[1], playersList, new ArrayList<>());
            }
            return null;
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (args.length == 2 && clanMembers != null) {
                return StringUtil.copyPartialMatches(args[1], clanMembers, new ArrayList<>());
            }
            return null;
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (args.length == 2 && clanMembers != null) {
                return StringUtil.copyPartialMatches(args[1], clanMembers, new ArrayList<>());
            }
            return null;
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 2 && clanMembers != null) {
                return StringUtil.copyPartialMatches(args[1], clanMembers, new ArrayList<>());
            }
            return null;
        } else if (args[0].equalsIgnoreCase("pass")) {
            if (args.length == 2) {
                return StringUtil.copyPartialMatches(args[1], playersList, new ArrayList<>());
            }
            return null;
        } else if (args[0].equalsIgnoreCase("alliance")) {
            if (args.length == 2) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("ally", "dally", "accept"), new ArrayList<>());
            } else if (args.length == 3 && args[1].equalsIgnoreCase("ally")) {
                return StringUtil.copyPartialMatches(args[2], clansList, new ArrayList<>());
            } else if (args.length == 3 && args[1].equalsIgnoreCase("dally")) {
                return StringUtil.copyPartialMatches(args[2], clansList, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
