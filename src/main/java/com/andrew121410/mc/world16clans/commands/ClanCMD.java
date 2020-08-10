package com.andrew121410.mc.world16clans.commands;

import com.andrew121410.mc.world16clans.Clan;
import com.andrew121410.mc.world16clans.ClanRank;
import com.andrew121410.mc.world16clans.ClanUser;
import com.andrew121410.mc.world16clans.World16Clans;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanCMD implements CommandExecutor {

    private Map<String, Clan> clanMap;
    private Map<UUID, ClanUser> clanUserMap;

    private Map<UUID, String> clanInviteRequest;
    private Map<String, String> clanAllyRequest;

    private World16Clans plugin;

    public ClanCMD(World16Clans plugin) {
        this.plugin = plugin;
        this.clanMap = this.plugin.getClanStorageManager().getClansMap();
        this.clanUserMap = this.plugin.getClanStorageManager().getClanUserMap();

        this.clanInviteRequest = new HashMap<>();
        this.clanAllyRequest = new HashMap<>();
        this.plugin.getCommand("clan").setExecutor(this);
        this.plugin.getCommand("clan").setTabCompleter(new ClanTAB(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        ClanUser clanUser = this.clanUserMap.get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Translate.color("&e/clan create <Name> <Tag> &r- Create a clan."));
            player.sendMessage(Translate.color("&e/clan invite <Player> &r- Invite player to clan."));
            player.sendMessage(Translate.color("&e/clan info &r- Show clan info."));
            player.sendMessage(Translate.color("&e/clan promote <Player> &r- Promote player in clan."));
            player.sendMessage(Translate.color("&e/clan demote <Player> &r- Demote player in clan."));
            player.sendMessage(Translate.color("&e/clan levelup &r- Level up your clan."));
            player.sendMessage(Translate.color("&e/clan kick <Player> &r- Kick player from clan."));
            player.sendMessage(Translate.color("&e/clan pass <Player> &r- Pass ownership to player."));
            player.sendMessage(Translate.color("&e/clan leave &r- Leave the clan your in."));
            player.sendMessage(Translate.color("&e/clan disband &r- Deletes the clan."));
            player.sendMessage(Translate.color("&e/clan sethome &r- Set clan home."));
            player.sendMessage(Translate.color("&e/clan home &r- Goes to clan home."));
            player.sendMessage(Translate.color("&e/clan alliance &r- Shows help for alliance command."));
            player.sendMessage(Translate.color("&e/clan tag <Name> &r- Sets tag of clan."));
            player.sendMessage(Translate.color("&e/clan motd <Message> &r- Set's message of the day."));
            player.sendMessage(Translate.color("&e/clan c <Message> &r- Sends a message to all online clan members"));
            player.sendMessage(Translate.color("&e/clan ca <Message> &r- Sends a message to all allies members."));
            player.sendMessage(Translate.color("&e/clan ct &r- Toggles between clan and clan allies and global chat."));
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1 || args.length == 2) {
                player.sendMessage(Translate.color("&e/clan create <Clan> <Tag>"));
                return true;
            } else if (args.length == 3) {
                String clanName = args[1];
                String clanTag = args[2];

                if (clanUser != null) {
                    player.sendMessage(Translate.color("&cLooks like you already in a clan."));
                    return true;
                }

                if (this.clanMap.containsKey(clanName)) {
                    player.sendMessage(Translate.color("&cClan with that name already exists."));
                    return true;
                }

                this.plugin.getClanManager().createClan(clanName, clanTag, player.getUniqueId());
                player.sendMessage(Translate.color("&6Clan: " + clanName + " has been created!"));
                return true;
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
            String playerName = args[1];

            if (clanUser == null) {
                player.sendMessage("&cLooks like you aren't in a clan.");
                return true;
            }

            Player targetPlayer = this.plugin.getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                player.sendMessage(Translate.color("&cCouldn't find player with that name."));
                return true;
            }

            ClanUser targetClanUser = this.clanUserMap.get(targetPlayer.getUniqueId());
            if (targetClanUser != null) {
                player.sendMessage(Translate.color("&cThat player is already in the clan: " + targetClanUser.getClan().getClanName()));
                return true;
            }
            this.clanInviteRequest.remove(targetPlayer.getUniqueId());
            this.clanInviteRequest.put(targetPlayer.getUniqueId(), clanUser.getClan().getClanName());
            targetPlayer.sendMessage(Translate.color("&9&l[CLAN] &r&6Clan: " + clanUser.getClan().getClanName() + " has invited you &c/clan accept OR /clan deny"));
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }
            Clan clan = clanUser.getClan();
            ClanUser leaderUser = null;
            List<ClanUser> officersUserList = new ArrayList<>();
            List<ClanUser> membersUserList = new ArrayList<>();
            List<ClanUser> newbiesUserList = new ArrayList<>();
            for (Map.Entry<UUID, ClanUser> entry : clan.getClanUserMap().entrySet()) {
                UUID uuid = entry.getKey();
                ClanUser clanUser1 = entry.getValue();
                if (clanUser1.getClanRank() == ClanRank.LEADER) {
                    leaderUser = clanUser1;
                } else if (clanUser1.getClanRank() == ClanRank.OFFICERS) {
                    officersUserList.add(clanUser1);
                } else if (clanUser1.getClanRank() == ClanRank.MEMBERS) {
                    membersUserList.add(clanUser1);
                } else if (clanUser1.getClanRank() == ClanRank.NEWBIES) {
                    newbiesUserList.add(clanUser1);
                }
            }
            ComponentBuilder componentBuilder = new ComponentBuilder();
            player.sendMessage(Translate.color("&6Clan: " + clan.getClanName()) + " &bLevel: " + clan.getLevel() + " &5Tag:" + clan.getTag());
            componentBuilder.append("Leader: ").color(ChatColor.GRAY).append(leaderUser.getPlayerName());
            componentBuilder.append("\n");
            componentBuilder.append("Officers: ").color(ChatColor.GRAY);
            for (ClanUser user : officersUserList) {
                componentBuilder.append(" " + user.getPlayerName()).color(ChatColor.RED);
            }
            componentBuilder.append("\n");
            componentBuilder.append("Members: ").color(ChatColor.GRAY);
            for (ClanUser user : membersUserList) {
                componentBuilder.append(" " + user.getPlayerName()).color(ChatColor.RED);
            }
            componentBuilder.append("\n");
            componentBuilder.append("Newbies: ").color(ChatColor.GRAY);
            for (ClanUser user : newbiesUserList) {
                componentBuilder.append(" " + user.getPlayerName()).color(ChatColor.RED);
            }
            componentBuilder.append("\n");
            for (String s : clan.getClanAlliesList()) {
                componentBuilder.append(" " + s).color(ChatColor.WHITE);
            }
            player.spigot().sendMessage(componentBuilder.create());
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("promote")) {
            String playerName = args[1];

            if (clanUser == null) {
                player.sendMessage("&cLooks like you aren't in a clan.");
                return true;
            }

            Player targetPlayer = this.plugin.getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                player.sendMessage(Translate.color("&cCouldn't find player with that name."));
                return true;
            }

            ClanUser targetUser = clanUser.getClan().getClanUserMap().get(targetPlayer.getUniqueId());
            if (targetUser == null) {
                player.sendMessage(Translate.color("&cPlayer isn't in clan."));
                return true;
            }

            switch (targetUser.getClanRank()) {
                case NEWBIES:
                    targetUser.setClanRank(ClanRank.MEMBERS);
                    this.plugin.getServer().broadcastMessage("&6[Clan] &7<" + clanUser.getClan().getTag() + "> &a" + targetPlayer.getDisplayName() + " has been promoted to MEMBER");
                    return true;
                case MEMBERS:
                    targetUser.setClanRank(ClanRank.OFFICERS);
                    this.plugin.getServer().broadcastMessage("&6[Clan] &7<" + clanUser.getClan().getTag() + "> &a" + targetPlayer.getDisplayName() + " has been promoted to OFFICER");
                    return true;
                case OFFICERS:
                    player.sendMessage(Translate.color("&cCannot promote to leader only 1 leader is allowed."));
                    return true;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("demote")) {
            String playerName = args[1];

            if (clanUser == null) {
                player.sendMessage("&cLooks like you aren't in a clan.");
                return true;
            }

            Player targetPlayer = this.plugin.getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                player.sendMessage(Translate.color("&cCouldn't find player with that name."));
                return true;
            }

            ClanUser targetUser = clanUser.getClan().getClanUserMap().get(targetPlayer.getUniqueId());
            if (targetUser == null) {
                player.sendMessage(Translate.color("&cPlayer isn't in clan."));
                return true;
            }

            switch (targetUser.getClanRank()) {
                case NEWBIES:
                    player.sendMessage(Translate.color("&cPlayer is already at the lowest rank."));
                    return true;
                case MEMBERS:
                    targetUser.setClanRank(ClanRank.NEWBIES);
                    this.plugin.getServer().broadcastMessage("&6[Clan] &7<" + clanUser.getClan().getTag() + "> &4" + targetPlayer.getDisplayName() + " has been demoted to NEWBIE");
                    return true;
                case OFFICERS:
                    targetUser.setClanRank(ClanRank.MEMBERS);
                    this.plugin.getServer().broadcastMessage("&6[Clan] &7<" + clanUser.getClan().getTag() + "> &4" + targetPlayer.getDisplayName() + " has been demoted to MEMBER");
                    return true;
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("levelup")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (player.getInventory().contains(Material.ENDER_EYE)) {
                player.getInventory().remove(Material.ENDER_EYE);
                clanUser.getClan().setLevel(clanUser.getClan().getLevel() + 1);
                this.plugin.getServer().broadcastMessage(Translate.color("&9[Clan] &7<" + clanUser.getClan().getTag() + "> has level up to " + clanUser.getClan().getLevel()));
                return true;
            } else {
                player.sendMessage(Translate.color("&cNeed ender eye to upgrade clan level."));
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {
            String playerName = args[1];

            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            Map.Entry<UUID, ClanUser> targetClanUser = clanUser.getClan().getClanUserMap().entrySet().stream().filter(uuidClanUserEntry -> uuidClanUserEntry.getValue().getPlayerName().equals(playerName)).findFirst().orElse(null);
            if (targetClanUser == null) {
                player.sendMessage(Translate.color("&cUser wasn't found in clan."));
                return true;
            }

            this.plugin.getClanManager().kickPlayerFromClan(clanUser.getClan().getClanName(), targetClanUser.getKey());
            player.sendMessage(Translate.color("Player: " + playerName + " has been kicked from the clan."));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("pass")) {
            String playerName = args[1];

            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() != ClanRank.LEADER) {
                player.sendMessage(Translate.color("&cYou have to be owner of clan to pass it to another user."));
                return true;
            }

            Player targetPlayer = this.plugin.getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                player.sendMessage(Translate.color("&cUser isn't online."));
                return true;
            }

            ClanUser targetClanUser = clanUser.getClan().getClanUserMap().get(targetPlayer.getUniqueId());
            if (targetClanUser == null) {
                player.sendMessage(Translate.color("&cUser isn't in your clan."));
                return true;
            }

            clanUser.setClanRank(ClanRank.OFFICERS);
            targetClanUser.setClanRank(ClanRank.LEADER);
            this.plugin.getServer().broadcastMessage(Translate.color("&9[Clan] &e" + playerName + " is the new leader of " + clanUser.getClan().getTag()));
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() == ClanRank.LEADER) {
                player.sendMessage(Translate.color("&cSince your the owner of this clan you have to disband the clan."));
                return true;
            }

            this.plugin.getClanManager().kickPlayerFromClan(clanUser.getClan().getClanName(), player.getUniqueId());
            player.sendMessage("&6You have left the clan.");
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("disband")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() != ClanRank.LEADER) {
                player.sendMessage(Translate.color("&cYou aren't the leader of the clan."));
                return true;
            }

            String clanName = clanUser.getClan().getClanName();

            this.plugin.getClanManager().deleteClan(clanName);
            Bukkit.getServer().broadcastMessage(Translate.color("&9[Clan] &eClan " + clanName + " has been disband"));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("sethome")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() != ClanRank.OFFICERS || clanUser.getClanRank() != ClanRank.LEADER) {
                player.sendMessage(Translate.color("&cMust be a officer or leader to sethome for clan."));
                return true;
            }

            Integer level = clanUser.getClan().getLevel();
            if (level == null) {
                throw new NullPointerException("level == null");
            }

            if (level < 3) {
                player.sendMessage(Translate.color("&cCan't set a clan sethome because clan isn't at level 3 yet."));
                return true;
            }

            clanUser.getClan().setHomeLocation(player.getLocation());
            player.sendMessage("&aClan sethome has been set.");
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("home")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() == ClanRank.NEWBIES) {
                player.sendMessage(Translate.color("&cMust be a member or higher to go to clan home."));
                return true;
            }

            if (clanUser.getClan().getHomeLocation() == null) {
                player.sendMessage(Translate.color("&cNo home has been set for the clan."));
                return true;
            }

            player.teleport(clanUser.getClan().getHomeLocation());
            player.sendMessage(Translate.color("&6Teleporting..."));
            return true;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("alliance")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() != ClanRank.OFFICERS || clanUser.getClanRank() != ClanRank.LEADER) {
                player.sendMessage(Translate.color("&cNeed to be an officer or leader to ally a clan."));
                return true;
            }

            if (args.length == 1) {
                player.sendMessage(Translate.color("&e/clan alliance ally <Clan> &r- Allys clan."));
                player.sendMessage(Translate.color("&e/clan alliance dally <Clan> &r- Makes ally clan no longer a ally."));
                player.sendMessage(Translate.color("&e/clan alliance accept &r- Accept ally request from the latest request."));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("ally")) {
                String clanName = args[2];
                Clan targetClan = this.clanMap.get(clanName);
                if (targetClan == null) {
                    player.sendMessage(Translate.color("&cCan't find the target clan."));
                    return true;
                }
                this.clanAllyRequest.remove(clanName);
                this.clanAllyRequest.put(clanName, clanUser.getClan().getClanName());
                player.sendMessage(Translate.color("&6Sent a request to " + clanName + " to ally with them."));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("dally")) {
                String clanName = args[2];
                Clan targetClan = this.clanMap.get(clanName);
                if (targetClan == null) {
                    player.sendMessage(Translate.color("&cCan't find the target clan."));
                    return true;
                }

                this.plugin.getServer().broadcastMessage("&9[Clan] &4" + clanUser.getClan().getClanName() + " is no longer allys with " + clanName);
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("accept")) {
                String clanThatIsAllyWith = this.clanAllyRequest.get(clanUser.getClan().getClanName());
                if (clanThatIsAllyWith == null) {
                    player.sendMessage(Translate.color("&cThere is no clan ally request."));
                    return true;
                }
                Clan targetClan = this.clanMap.get(clanThatIsAllyWith);
                targetClan.getClanAlliesList().add(clanUser.getClan().getClanName());
                clanUser.getClan().getClanAlliesList().add(clanThatIsAllyWith);
                this.plugin.getServer().broadcastMessage("&9[Clan] &6" + clanThatIsAllyWith + " is now allied with " + clanUser.getClan().getClanName());
                return true;
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("motd")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getClanRank() != ClanRank.OFFICERS || clanUser.getClanRank() != ClanRank.LEADER) {
                player.sendMessage(Translate.color("&cMust be an officer or leader to set motd for clan."));
                return true;
            }

            clanUser.getClan().setMotd(args[1]);
            player.sendMessage(Translate.color("&6The message of the day has been set."));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("c")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            this.plugin.getClanManager().sendMessageToClanMembers(player, args[1]);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("ca")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            this.plugin.getClanManager().sendMessageToAlliesMembers(player, args[1]);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("ct")) {
            if (clanUser == null) {
                player.sendMessage(Translate.color("&cYou aren't in a clan."));
                return true;
            }

            if (clanUser.getInClanChat()) {
                clanUser.setInClanChat(false);
                clanUser.setInAllyChat(true);
                player.sendMessage(Translate.color("&3&lSwitched to ally chat."));
            } else if (clanUser.getInAllyChat()) {
                clanUser.setInAllyChat(false);
                player.sendMessage(Translate.color("&3&lSwitched to global chat."));
            } else {
                clanUser.setInClanChat(true);
                player.sendMessage(Translate.color("&3&lSwitched to clan chat."));
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {
            if (clanUser != null) {
                player.sendMessage(Translate.color("&cLooks like you already in a clan."));
                return true;
            }
            String clanName = this.clanInviteRequest.get(player.getUniqueId());
            this.plugin.getClanManager().addPlayerToClan(clanName, player.getUniqueId());
            this.plugin.getServer().broadcastMessage("&9[Clan] &6" + player.getDisplayName() + " has joined the clan " + clanName);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("deny")) {
            this.clanInviteRequest.remove(player.getUniqueId());
            player.sendMessage(Translate.color("&6Denied clan invite request."));
            return true;
        }
        return false;
    }
}
