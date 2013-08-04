package me.xxsniperzzxx_sd.infected;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.ItemSerialization;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.kitteh.tag.TagAPI;

import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;


public class Methods
{

    public static HashMap < String, Integer > Stats = new HashMap < String, Integer > ();
    public static void rewardPoints(Player player, String PointsCause)
    {
        if (Main.config.getBoolean("Points.Use"))
        {
            if (Main.config.getBoolean("Debug")) System.out.print(Main.KillStreaks.toString());
            if (Main.KillStreaks.containsKey(player.getName()))
            {
                if (!(Main.KillStreaks.get(player.getName()) == 0))
                {
                    int times = Main.KillStreaks.get(player.getName()) / 2;
                    Main.timest = times;
                }
                else
                {
                    int times = 1;
                    Main.timest = times;
                }
            }
            else
                Main.timest = 1;
            int score = Main.config.getInt("Score." + PointsCause) * Main.timest;
            int reward = Main.config.getInt("Points." + PointsCause);


            reward = Main.config.getInt("Points." + PointsCause);
            if (getPoints(player) > Main.config.getInt("Points.Max Points"))
                player.sendMessage(Main.I + ChatColor.RED + "You have exceded the max points!");
            if (getScore(player) > Main.config.getInt("Score.Max Score"))
                player.sendMessage(Main.I + ChatColor.RED + "You have exceded the max score!");
            else
            {
                setPoints(player, Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Points") + reward);
                setScore(player, Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Score") + score);
                Files.savePlayers();
                player.sendMessage(Main.I + ChatColor.AQUA + "Points +" + reward);
                Files.savePlayers();
            }
        }
    }
    public static void applyAbilities(Player player)
    {
        Random r = new Random();
        int n = 0;
        for (String abilities: Infected.filesGetAbilities().getConfigurationSection("Abilities").getKeys(true))
        {
            if (!abilities.contains("."))
            {
                n += Infected.filesGetAbilities().getInt("Abilities." + abilities + ".Chance");
            }
        }
        int i = r.nextInt(n);
        n = 0;
        for (String abilities: Infected.filesGetAbilities().getConfigurationSection("Abilities").getKeys(true))
        {
            n += Infected.filesGetAbilities().getInt("Abilities." + abilities + ".Chance");
            if (n > i)
            {
                Integer id = 0;
                Integer time = 0;
                Integer power = 0;
                int max = Infected.filesGetAbilities().getStringList("Abilities." + abilities + ".Potion Effects").size();
                for (int x = 0; x < max; x++)
                {
                    String path = Infected.filesGetAbilities().getStringList("Abilities." + abilities + ".Potion Effects").get(x);
                    String[] strings = path.split(":");
                    id = Integer.valueOf(strings[0]);
                    time = Integer.valueOf(strings[1]) * 20;
                    power = Integer.valueOf(strings[2]);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.getById(id), time, power));
                }
                player.sendMessage(Main.I + abilities);
                break;
            }
        }
    }
    public static void applyClassAbility(Player player){
	
    	for (PotionEffect reffect: player.getActivePotionEffects())
    	{
    		player.removePotionEffect(reffect.getType());
    	}
    	Integer id = 0;
    	Integer time = 0;
    	Integer power = 0;
    	int max = 0;
    	String path = "";
    	if(Infected.isPlayerZombie(player))
    		max = Infected.filesGetClasses().getStringList("Classes."+Main.zombieClasses.get(player.getName())+".Potion Effects").size();
    	
    	else
    		max = Infected.filesGetClasses().getStringList("Classes."+Main.humanClasses.get(player.getName())+".Potion Effects").size();
    	
    	for (int x = 0; x < max; x++)
    	{
    		if(Infected.isPlayerZombie(player))
    			path = Infected.filesGetClasses().getStringList("Classes."+Main.zombieClasses.get(player.getName())+".Potion Effects").get(x);	
    		else
    			path = Infected.filesGetClasses().getStringList("Classes."+Main.humanClasses.get(player.getName())+".Potion Effects").get(x);
    		String[] strings = path.split(":");
    		id = Integer.valueOf(strings[0]);
    		time = Integer.valueOf(strings[1]) * 20;
    		power = Integer.valueOf(strings[2]);
    		player.addPotionEffect(new PotionEffect(PotionEffectType.getById(id), time, power));
    	}
    }
    public static void disguisePlayer(Player player)
    {
        if (Main.config.getBoolean("DisguiseCraft Support") == true)
        {
        	if(Main.zombieClasses.containsKey(player.getName()))
        	{
        		if (!Main.dcAPI.isDisguised(player))
	            {
        			if(!(DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(player.getName())+".Disguise")) == null)){
        				Main.dcAPI.disguisePlayer(player, new Disguise(Main.dcAPI.newEntityID(), DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(player.getName())+".Disguise"))).addSingleData("noarmor"));
        			}
	            }
        		else
        		{
	            	Main.dcAPI.undisguisePlayer(player);
	            	disguisePlayer(player);
        		}
        	}
        	else
        	{
	            // https://gitorious.org/disguisecraft/disguisecraft/blobs/master/src/pgDev/bukkit/DisguiseCraft/disguise/Disguise.java#line234
	            Random ra = new Random();
	            int chance = ra.nextInt(100);
	            if (!Main.dcAPI.isDisguised(player))
	            {
	                if (chance <= Main.config.getInt("Chance To Be Pig Zombie"))
	                {
	                    Main.dcAPI.disguisePlayer(player, new Disguise(Main.dcAPI.newEntityID(), DisguiseType.PigZombie).addSingleData("noarmor"));
	                    if (Main.config.getBoolean("Debug"))
	                    {
	                        System.out.println("Choosing new zombie: " + player.getName() + " = pigzombie");
	                    }
	                }
	                else if (chance <= (Main.config.getInt("Chance To Be NPC Zombie") + Main.config.getInt("Chance To Be NPC Zombie")))
	                {
	                    if (Main.config.getBoolean("Debug"))
	                    {
	                        System.out.println("Choosing new zombie: " + player.getName() + " = infected zombie");
	                    }
	                    Main.dcAPI.disguisePlayer(player, new Disguise(Main.dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("infected").addSingleData("noarmor"));
	                }
	                else
	                {
	                    Main.dcAPI.disguisePlayer(player, new Disguise(Main.dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));
	                    if (Main.config.getBoolean("Debug"))
	                    {
	                        System.out.println("Choosing new zombie: " + player.getName() + " = zombie");
	                    }
	                }
	            }else{
	            	Main.dcAPI.undisguisePlayer(player);
	            	disguisePlayer(player);
	            }
	        }
        }
    }
    
    public static void zombifyPlayer(Player player)
    {
    	if(Main.zombieClasses.containsKey(player.getName()))
    	{
    	applyClassAbility(player);	
    	}
    	else if (Main.config.getBoolean("Zombie Abilities") == true)
        {
            applyAbilities(player);
        }
        disguisePlayer(player);
        
    }
    public static String grenadeGetName(Integer id)
    {
        return Files.getGrenades().getString(id + ".Name");
    }
    public static boolean grenadeTakeAfter(Integer id)
    {
        return Files.getGrenades().getBoolean(id + ".Take After Thrown");
    }
    public static int grenadeGetDamage(Integer id)
    {
        return Files.getGrenades().getInt(id + ".Damage");
    }
    public static int grenadeGetCost(Integer id)
    {
        return Files.getGrenades().getInt(id + ".Cost");
    }
    public static int grenadeGetRange(Integer id)
    {
        return Files.getGrenades().getInt(id + ".Range");
    }
    public static int grenadeGetDelay(Integer id)
    {
        return Files.getGrenades().getInt(id + ".Delay") * 20;
    }
    public static void grenadeAddPotion(Player player, Integer Itemid)
    {
        Integer id = 0;
        Integer time = 0;
        Integer power = 0;
        int max = Files.getGrenades().getStringList(Itemid + ".Effects").size();
        for (int x = 0; x < max; x = x + 1)
        {
            String path = Files.getGrenades().getStringList(Itemid + ".Effects").get(x);
            String[] strings = path.split(":");
            id = Integer.valueOf(strings[0]);
            time = Integer.valueOf(strings[1]) * 20;
            power = Integer.valueOf(strings[2]);
            player.addPotionEffect(new PotionEffect(PotionEffectType.getById(id), time, power));
        }

    }
    public static void saveInventory(Player player, String loc)
    {
        String data = ItemSerialization.toBase64(player.getInventory());
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + "." + loc, data);
        Files.savePlayers();
    }
    public static void addItemToInventory(Player player, String loc, ItemStack items)
    {
        if (Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc) == null)
        {
            saveInventory(player, loc);
        }
        Inventory copy = ItemSerialization.fromBase64(Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc));
        copy.addItem(items);
        String done = ItemSerialization.toBase64(copy);
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + "." + loc, done);
        Files.savePlayers();
    }
    public static ItemStack[] getInventory(Player player, String loc)
    {
        if (Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc) == null)
        {
            saveInventory(player, loc);
        }
        String data = Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc);
        Inventory copy = ItemSerialization.fromBase64(data);
        return copy.getContents();
    }
    public static void SetOnlineTime(Player player)
    {
        long time = Main.Timein.get(player.getName());
        long timeon = (System.currentTimeMillis() / 1000) - time;
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Time", Files.getPlayers().getLong("Players." + player.getName().toLowerCase() + ".Time") + timeon);
    }

    public static String getOnlineTime(String player)
    {
        Long time = Files.getPlayers().getLong("Players." + player.toLowerCase() + ".Time");
        Long seconds = time;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        String times = days + "D, " + hours + "H, " + minutes + "M " + seconds + "S";
        return times;

    }



    public static void newZombieSetUpEveryOne()
    {
        Random r = new Random();
        if (Main.inGame.size() <= 0)
        {
            Infected.resetPlugin();
        }
        else
        {
            for (Player online: Bukkit.getServer().getOnlinePlayers())
            {
                if (Infected.isPlayerInGame(online) && Main.config.getBoolean("DisguiseCraft Support"))
                {
                    if (Main.dcAPI.isDisguised(online)) Main.dcAPI.undisguisePlayer(online);
                }
            }
            int alpha = r.nextInt(Main.inGame.size());
            String name = Main.inGame.get(alpha);
            Player zombie = Bukkit.getServer().getPlayer(name);
            zombie.sendMessage(Methods.sendMessage("Game_YouAreFirstInfected", null, null, null));
            Main.zombies.clear();
            Main.humans.clear();
            Main.zombies.add(zombie.getName());
            Main.Winners.remove(zombie.getName());
            if (Main.config.getBoolean("New Zombie Tp")) Methods.respawn(zombie);
            zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            if (Main.config.getBoolean("Zombie Abilities") == true)
            {
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 2), true);
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2000, 1), true);
            }
            Methods.zombifyPlayer(zombie);
            zombie.setHealth(20);
            Methods.equipZombies(zombie);
            //Inform humans of infected, prepare them
            for (Player online: Bukkit.getServer().getOnlinePlayers())
            {
                if (Infected.isPlayerInGame(online))
                {
                    if (Main.config.getBoolean("ScoreBoard Support"))
                        if (!Main.KillStreaks.containsKey(online.getName()))
                            Main.KillStreaks.put(online.getName(), Integer.valueOf("0"));
                    int timeleft = Main.GtimeLimit;
                    online.sendMessage(Main.I + ChatColor.WHITE + "You have " + ChatColor.YELLOW + getTime(Long.valueOf(timeleft)) + ChatColor.WHITE + ". Good luck!");
                    if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
                    {
                        //if(Main.humans.contains(online)) {
                        Main.humans.add(online.getName());
                        if (!Main.Winners.contains(online.getName()))
                        {
                            Main.Winners.add(online.getName());
                        }
                        online.sendMessage(Methods.sendMessage("Game_FirstInfected", zombie, null, null));
                        online.setHealth(20);
                        online.playEffect(online.getLocation(), Effect.SMOKE, 1);

                    }
                }
            }
        }
        if (Main.config.getBoolean("ScoreBoard Support"))
        {
            updateScoreBoard();
        }
    }
    public static void updateScoreBoard()
    {
        if (Main.config.getBoolean("ScoreBoard Support"))
        {
	        if (Infected.booleanIsStarted())
	        {
	            Score score = Main.playingList.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Humans:"));
	            score.setScore(Main.humans.size());
	            Score score2 = Main.playingList.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Zombies:"));
	            score2.setScore(Main.zombies.size());
	
	
	            //Resetting Votes
	            Main.possibleArenas.clear();
	            for (String parenas: Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
	            {
	                //Check if the string matchs an arena
	
	                if (Main.possibleArenas.contains(parenas))
	                {
	                    Main.possibleArenas.remove(parenas);
	                }
	                if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
	                {
	                    Main.possibleArenas.remove(parenas);
	                }
	                else if (!parenas.contains("."))
	                {
	                    Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(parenas));
	                }
	            }
	        }
	        else
	        {
	            Main.possibleArenas.clear();
	            for (String parenas: Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
	            {
	                //Check if the string matchs an arena
	
	                if (Main.possibleArenas.contains(parenas))
	                {
	                    Main.possibleArenas.remove(parenas);
	                }
	                if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
	                {
	                    Main.possibleArenas.remove(parenas);
	                }
	                else if (!parenas.contains("."))
	                {
	                    for (Entry < String, Integer > mapName: Main.Votes.entrySet())
	                    {
	                        if (parenas.equalsIgnoreCase(mapName.getKey()))
	                        {
	                            Score score = Main.voteList.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + parenas));
	                            score.setScore(mapName.getValue());
	                        }
	                    }
	                }
	            }
	
	            //Reset Team board
	            Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Humans:"));
	            Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + "Zombies:"));
	
	        }
        }
    }
    public static int getPlayerKills(Player user)
    {
        return Files.getPlayers().getInt("Players." + user.getName().toLowerCase() + ".Kills");
    }
    public static int getPlayerDeaths(Player user)
    {
        return Files.getPlayers().getInt("Players." + user.getName().toLowerCase() + ".Deaths");
    }
    public static void setPlayerKills(Player user, Integer Int)
    {
        Files.getPlayers().set("Players." + "Players." + user.getName().toLowerCase() + ".Kills", Int);
        Files.savePlayers();
    }
    public static void setPlayerDeaths(Player user, Integer Int)
    {
        Files.getPlayers().set("Players." + "Players." + user.getName().toLowerCase() + ".Deaths", Int);
        Files.savePlayers();
    }
    public static Double KD(Player player)
    {
        int kills = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
        int deaths = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
        double ratio = Math.round(((double) kills / (double) deaths) * 100.0D) / 100.0D;
        if (deaths == 0)
            ratio = kills;
        else if (kills == 0)
            ratio = 0.00;
        return ratio;
    }
    public static void stats(Player player, Integer Kills, Integer Deaths)
    {
        int kills = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
        int deaths = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
        if (kills == 0) kills = 0;
        if (deaths == 0) deaths = 0;
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Kills", kills + Kills);
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Deaths", deaths + Deaths);
        Files.savePlayers();
    }
    public static void grenadeKill(Player Killer, Player Killed)
    {
        Methods.stats(Killer, 1, 0);
        Methods.rewardPoints(Killer, "Kill");
        String kill = getKillType(getGroup(Killer) + "s", Killer.getName(), Killed.getName());
        for (Player playing: Bukkit.getServer().getOnlinePlayers())
            if (Main.inGame.contains(playing.getName()))
            {
                playing.sendMessage(kill);
            }
        Main.KillStreaks.put(Killer.getName(), Main.KillStreaks.get(Killer.getName()) + 1);
        Files.getPlayers().set("Players." + Killer.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(Killer.getName()));
        if (Main.KillStreaks.get(Killer.getName()) > 2)
            for (Player playing: Bukkit.getServer().getOnlinePlayers())
                if (Main.inGame.contains(playing.getName()))
                {
                    playing.sendMessage(Main.I + ChatColor.GREEN + Killer.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + Main.KillStreaks.get(Killer.getName()));
                }
        if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(Main.KillStreaks.get(Killer.getName())))))
        {
            String command = null;
            command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + Main.KillStreaks.get(Killer.getName()))).replaceAll("<player>", Killer.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        Methods.stats(Killed, 0, 1);
        if (Main.KillStreaks.containsKey(Killed.getName()))
        {
            if (Main.KillStreaks.get(Killed.getName()) > Files.getPlayers().getInt("Players." + Killed.getName().toLowerCase() + ".KillStreak"))
            {
                Files.getPlayers().set("Players." + Killed.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(Killed.getName()));
                Files.savePlayers();
            }

            Main.KillStreaks.put(Killed.getName(), 0);
        }
        Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(Killer, Killed, Infected.playerGetGroup(Killed), Infected.isPlayerHuman(Killed) ? true : false));
        Killed.setHealth(20);
        Killed.setFallDistance(0F);
        Killed.setFoodLevel(20);
        Methods.respawn(Killed);
        Killed.setFallDistance(0F);
        Main.humans.remove(Killed.getName());
        Main.Lasthit.remove(Killed.getName());
        if (Main.humans.size() == 0)
        {
            Methods.endGame(false);
        }
        else
        {
            Methods.equipZombies(Killed);
            Methods.zombifyPlayer(Killed);
        }
    }
    public static int getPoints(Player player)
    {
        return Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Points");

    }
    public static void setPoints(Player player, Integer Int)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Points", Int);
        Files.savePlayers();
    }
    public static void delPoints(Player player)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Points", null);
        Files.savePlayers();
    }
    public static int getScore(Player player)
    {
        return Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Score");

    }
    public static void setScore(Player player, Integer Int)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Score", Int);
        Files.savePlayers();
    }
    public static void delScore(Player player)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Score", null);
        Files.savePlayers();
    }
    public static void addPlayer(Player player)
    {
        Main.inGame.add(player.getName());
    }
    public static void delPlayer(Player player)
    {
        Main.inGame.remove(player.getName());
    }
    public static void clearPlayers()
    {
        Main.inGame.clear();
    }
    public static boolean isInLobby(Player player)
    {
        return Main.inLobby.contains(player.getName());
    }
    public static void addInLobby(Player player)
    {
        Main.inLobby.add(player.getName());
    }
    public static void delInLobby(Player player)
    {
        Main.inLobby.remove(player.getName());
    }
    public static void clearInLobby()
    {
        Main.inLobby.clear();
    }@
    SuppressWarnings("deprecation")
    public static void equipHumans(Player human)
    {
        if (Main.tagapi)
            TagAPI.refreshPlayer(human);
        if(Main.humanClasses.containsKey(human.getName())){
        	for (String s: Infected.filesGetClasses().getStringList("Classes."+Main.humanClasses.get(human.getName())+".Items"))
	        {
	            human.getInventory().addItem(getItemStack(s));
	            human.updateInventory();
	        }
	        if (Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Head") != null) human.getInventory().setHelmet(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Head")));
	        if (Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Chest") != null) human.getInventory().setChestplate(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Chest")));
	        if (Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Legs") != null) human.getInventory().setLeggings(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Legs")));
	        if (Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Feet") != null) human.getInventory().setBoots(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.humanClasses.get(human.getName())+".Feet")));
        }else{
	        for (String s: Main.config.getStringList("Armor.Human.Items"))
	        {
	            human.getInventory().addItem(getItemStack(s));
	            human.updateInventory();
	        }
	        if (Main.config.getString("Armor.Human.Head") != null) human.getInventory().setHelmet(getItemStack(Main.config.getString("Armor.Human.Head")));
	        if (Main.config.getString("Armor.Human.Chest") != null) human.getInventory().setChestplate(getItemStack(Main.config.getString("Armor.Human.Chest")));
	        if (Main.config.getString("Armor.Human.Legs") != null) human.getInventory().setLeggings(getItemStack(Main.config.getString("Armor.Human.Legs")));
	        if (Main.config.getString("Armor.Human.Feet") != null) human.getInventory().setBoots(getItemStack(Main.config.getString("Armor.Human.Feet")));
	        }
        human.updateInventory();
        applyClassAbility(human);
    }@
    SuppressWarnings("deprecation")
    public static void equipZombies(Player zombie)
    {
    	updateScoreBoard();
        if (Main.tagapi)
            TagAPI.refreshPlayer(zombie);
        //Give infected their armor

        //Take away humans items
        for (String s: Main.config.getStringList("Armor.Human.Items"))
        {
            if (zombie.getInventory().contains(getItem(s).getType()))
            {
                zombie.getInventory().remove(getItem(s).getType());
            }
        }
        //Take away any items from their human class
        if(Main.humanClasses.containsKey(zombie.getName())){
        	for (String s: Infected.filesGetClasses().getStringList("Classes."+Main.humanClasses.get(zombie.getName())+".Items"))
	        {
	            if (zombie.getInventory().contains(getItem(s).getType()))
	            {
	                zombie.getInventory().remove(getItem(s).getType());
	            }
	        }
        }
        for (ItemStack armor: zombie.getInventory().getArmorContents())
        {
            if (!(armor == null || armor.getType() == Material.AIR) && (armor == getItem(Main.config.getString("Armor.Zombie.Head")) || armor == getItem(Main.config.getString("Armor.Zombie.Chest")) || armor == getItem(Main.config.getString("Armor.Zombie.Legs")) || armor == getItem(Main.config.getString("Armor.Zombie.Feet"))))
            {
                zombie.getInventory().addItem(armor);
            }
        }
        
        //Add armor from the zombie class
        if(Main.zombieClasses.containsKey(zombie.getName())){
        	for (String s: Infected.filesGetClasses().getStringList("Classes."+Main.zombieClasses.get(zombie.getName())+".Items"))
	        {
        		if (!zombie.getInventory().contains(getItem(s))) zombie.getInventory().addItem(getItemStack(s));
	            zombie.updateInventory();
	        }
	        if (Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Head") != null) zombie.getInventory().setHelmet(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Head")));
	        if (Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Chest") != null) zombie.getInventory().setChestplate(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Chest")));
	        if (Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Legs") != null) zombie.getInventory().setLeggings(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Legs")));
	        if (Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Feet") != null) zombie.getInventory().setBoots(getItemStack(Infected.filesGetClasses().getString("Classes."+Main.zombieClasses.get(zombie.getName())+".Feet")));
        }else{
	        for (String s: Main.config.getStringList("Armor.Zombie.Items"))
	        {
	            if (!zombie.getInventory().contains(getItem(s))) zombie.getInventory().addItem(getItemStack(s));
	        }
	        if (Main.config.getString("Armor.Zombie.Head") != null) zombie.getInventory().setHelmet(getItemStack(Main.config.getString("Armor.Zombie.Head")));
	        if (Main.config.getString("Armor.Zombie.Chest") != null) zombie.getInventory().setChestplate(getItemStack(Main.config.getString("Armor.Zombie.Chest")));
	        if (Main.config.getString("Armor.Zombie.Legs") != null) zombie.getInventory().setLeggings(getItemStack(Main.config.getString("Armor.Zombie.Legs")));
	        if (Main.config.getString("Armor.Zombie.Feet") != null) zombie.getInventory().setBoots(getItemStack(Main.config.getString("Armor.Zombie.Feet")));
        }
        zombie.updateInventory();
    }
    public static String sendMessage(String message, Player player, String Time, String List)
    {
        String msg = String.valueOf(Files.getMessages().getString(message));
        String msg1 = msg;
        if (msg1.contains("<player>") && !(player == null)) /**/ msg1 = msg1.replaceAll("<player>", player.getName());

        if (msg1.contains("<timeleft>") && !(Time == null)) /**/ msg1 = msg1.replaceAll("<timeleft>", String.valueOf(Time));

        if (msg1.contains("<list>") && !(List == null)) /**/ msg1 = msg1.replaceAll("<list>", List);

        if (msg.contains("<humansize>")) /**/ msg1 = msg1.replaceAll("<humansize>", String.valueOf(Main.humans.size())).replaceAll("<zombiesize>", String.valueOf(Main.zombies.size())).replaceAll("<map>", Main.playingin);

        if (msg.contains("&")) /**/ msg1 = ChatColor.translateAlternateColorCodes('&', msg1);

        String newMsg = Main.I + msg1;
        return newMsg;
    }

    public static ItemStack getItemStack(String Path)
    {
        ItemStack is = new ItemStack(getItem(Path));
        is.setDurability(getItemData(Path));
        return is;
    }
    private static Integer getItemID(String Path)
    {
        String itemid = null;
        String string = Path;
        if (string.contains(":"))
        {
            String[] ss = string.split(":");
            itemid = ss[0];
        }
        else if (string.contains(","))
        {
            String[] ss = string.split(",");
            itemid = ss[0];
        }
        else if (string.contains("-"))
        {
            String[] ss = string.split("-");
            itemid = ss[0];
        }
        else if (string.contains("@"))
        {
            String[] ss = string.split("@");
            itemid = ss[0];
        }
        else if (string.contains("%"))
        {
            String[] ss = string.split("%");
            itemid = ss[0];
        }
        else
            itemid = string;
        int i = Integer.valueOf(itemid);
        return i;
    }

    private static Short getItemData(String Path)
    {
        String itemdata = null;
        String string = Path;
        if (string.contains(":"))
        {
            String[] s = string.split(":");
            if (s[1].contains(","))
            {
                String[] ss = s[1].split(",");
                itemdata = ss[0];
            }
            else if (s[1].contains("-"))
            {
                String[] ss = s[1].split("-");
                itemdata = ss[0];
            }
            else if (s[1].contains("@"))
            {
                String[] ss = s[1].split("@");
                itemdata = ss[0];
            }
            else if (s[1].contains("%"))
            {
                String[] ss = s[1].split("%");
                itemdata = ss[0];
            }
            else
                itemdata = "0";

        }
        else
            itemdata = "0";
        Short s = Short.valueOf(itemdata);
        return s;
    }
    private static Integer getItemAmount(String Path)
    {
        String itemdata = null;
        String string = Path;
        if (string.contains(","))
        {
            String[] s = string.split(",");
            if (s[1].contains("-"))
            {
                String[] ss = s[1].split("-");
                itemdata = ss[0];
            }
            else if (s[1].contains("@"))
            {
                String[] ss = s[1].split("@");
                itemdata = ss[0];
            }
            else if (s[1].contains("%"))
            {
                String[] ss = s[1].split("%");
                itemdata = ss[0];
            }
            else
                itemdata = s[1];
        }
        else
            itemdata = "1";
        return Integer.valueOf(itemdata);
    }

    private static int getItemEnchant(String Path)
    {
        String itemdata = null;
        String string = Path;
        if (string.contains("-"))
        {
            String[] s = string.split("-");
            if (s[1].contains("@"))
            {
                String[] ss = s[1].split("@");
                itemdata = ss[0];
            }
            else if (s[1].contains("%"))
            {
                String[] ss = s[1].split("%");
                itemdata = ss[0];
            }
            else
                itemdata = "0";

        }
        else
            itemdata = "0";
        return Integer.valueOf(itemdata);
    }
    private static int getItemEnchantLvl(String Path)
    {
        String itemdata = null;
        String string = Path;
        if (string.contains("@"))
        {
            String[] s = string.split("@");
            if (s[1].contains("%"))
            {
                String[] ss = s[1].split("%");
                itemdata = ss[0];
            }
            else
                itemdata = "1";

        }
        else
            itemdata = "1";
        return Integer.valueOf(itemdata);
    }
    private static String getItemName(String Path)
    {
        String itemName = null;
        if (Path.contains("%"))
        {
            String[] ss = Path.split("%");
            itemName = ChatColor.translateAlternateColorCodes('&', ss[1]);
        }
        else
        {
            itemName = null;
        }
        return itemName;
    }
    public static ItemStack getItem(String location)
    {
        ItemStack is = null;
        if (Material.getMaterial(getItemID(location)) != null)
            is = new ItemStack(Material.getMaterial(getItemID(location)), getItemAmount(String.valueOf(location)));
        else
            is = new ItemStack(Material.AIR);

        is.setDurability(getItemData(location));

        if (!(getItemName(location) == null))
        {
            ItemMeta im = is.getItemMeta();
            String name = getItemName(location).replaceAll("_", " ");
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            is.setItemMeta(im);
        }
        if (getItemEnchant(location) > 0)
        {
            is.addUnsafeEnchantment(Enchantment.getById(getItemEnchant(location)), getItemEnchantLvl(location));
        }
        return is;

    }
    public static void joinInfectHuman(Player player)
    {
        Player newzombie = player;
        if (!Main.Timein.containsKey(newzombie.getName())) Main.Timein.put(newzombie.getName(), System.currentTimeMillis() / 1000);
        Main.humans.remove(newzombie.getName());
        if (!Main.KillStreaks.containsKey(newzombie.getName()))
            Main.KillStreaks.put(newzombie.getName(), Integer.valueOf("0"));
        newzombie.sendMessage(Main.I + "You have became infected!");
        Methods.equipZombies(newzombie);
        newzombie.setHealth(20);
        newzombie.setFoodLevel(20);
        Main.KillStreaks.remove(newzombie.getName());
        for (Player playing: Bukkit.getServer().getOnlinePlayers())
        {
            if ((!(playing == newzombie)) && Main.inGame.contains(playing.getName()))
                playing.sendMessage(Methods.sendMessage("Game_GotInfected", newzombie, null, null));
        }
        newzombie.setFallDistance(0F);
        Methods.respawn(newzombie);
        newzombie.setFallDistance(0F);

        Main.zombies.add(newzombie.getName());
        Main.Winners.remove(newzombie.getName());
        Main.inLobby.remove(newzombie.getName());
        newzombie.playEffect(newzombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
        Methods.zombifyPlayer(newzombie);
        newzombie.setHealth(20);
        Methods.equipZombies(newzombie);
    }
    public static String getGroup(Player player)
    {
        String group = null;
        if (Methods.isHuman(player)) group = "Human";
        if (Methods.isZombie(player)) group = "Zombie";
        return group;
    }
    public static boolean isPlayer(Player player)
    {
        return Main.inGame.contains(player.getName());
    }
    public static void addZombie(Player player)
    {
        Main.zombies.add(player.getName());
    }
    public static void delZombie(Player player)
    {
        Main.zombies.remove(player.getName());
    }
    public static void clearZombies()
    {
        Main.zombies.clear();
    }
    public static boolean isZombie(Player player)
    {
        return Main.zombies.contains(player.getName());
    }
    public static void addHuman(Player player)
    {
        Main.zombies.add(player.getName());
    }
    public static void delHuman(Player player)
    {
        Main.zombies.remove(player.getName());
    }
    public static void clearHumans()
    {
        Main.zombies.clear();
    }@
    SuppressWarnings("deprecation")
    public static void endGame(Boolean DidHumansWin)
    {
        for (Player players: Bukkit.getServer().getOnlinePlayers())
        {
            if (Infected.isPlayerInGame(players))
            {
                if (Main.KillStreaks.containsKey(players.getName()))
                {
                    if (Main.KillStreaks.get(players.getName()) > Files.getPlayers().getInt("Players." + players.getName().toLowerCase() + ".KillStreak"))
                    {
                        Files.getPlayers().set("Players." + players.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(players.getName()));
                        Files.savePlayers();
                    }
                }
            }
        }
        if (DidHumansWin)
        {
        	if(Main.config.getString("Vault Support.Reward") != null){
        		int rewardMoney = Main.config.getInt("Vault.Support");

                for (Player players: Bukkit.getOnlinePlayers())
                    if (Main.Winners.contains(players.getName()))
                    	Main.economy.depositPlayer(players.getName(), rewardMoney);
        	}
            if (!(Main.config.getString("Command Reward").equalsIgnoreCase(null) || Main.config.getString("Command Reward").equalsIgnoreCase("[]")))
            {
                for (Player players: Bukkit.getOnlinePlayers())
                {
                    if (Main.Winners.contains(players.getName()))
                    {
                        String s = Main.config.getString("Command Reward").replaceAll("<player>", players.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                    }
                }
            }
            for (String s: Main.config.getStringList("Rewards"))
            {
                for (Player players: Bukkit.getOnlinePlayers())
                {
                    if (Main.Winners.contains(players.getName()))
                    {
                        players.getInventory().setContents(Main.Inventory.get(players.getName()));
                        players.updateInventory();
                        players.getInventory().addItem(Methods.getItemStack(s));
                        players.updateInventory();
                        Main.Inventory.put(players.getName(), players.getInventory().getContents());
                        resetPlayersInventory(players);
                        players.updateInventory();
                    }
                }
            }
            for (Player players: Bukkit.getServer().getOnlinePlayers())
            {
                if (Main.inGame.contains(players.getName()))
                {
                    Methods.rewardPoints(players, "Game Over");
                    players.sendMessage(Methods.sendMessage("AfterGame_HumansWin", null, null, null));
                    StringBuilder winners = new StringBuilder();
                    for (Object o: Main.Winners)
                    {
                        winners.append(o.toString());
                        winners.append(", ");
                    }
                    players.sendMessage(Main.I + "Winners: " + winners.toString());
                    players.sendMessage(Main.I + "Total Points: " + Files.getPlayers().getInt("Players." + players.getName().toLowerCase() + ".Points"));
                    Methods.SetOnlineTime(players);
                    Files.savePlayers();
                    if (Main.config.getBoolean("DisguiseCraft Support") == true)
                        if (Main.dcAPI.isDisguised(players))
                        {
                            Main.dcAPI.undisguisePlayer(players);
                        }
                    for (PotionEffect reffect: players.getActivePotionEffects())
                    {
                        players.removePotionEffect(reffect.getType());
                    }
                    Methods.tp2LobbyAfter(players);
                }
            }
        }
        else
        {
            for (Player players: Bukkit.getServer().getOnlinePlayers())
                if (Main.inGame.contains(players.getName()))
                {
                    if (Main.config.getBoolean("Debug"))
                        System.out.println(players.getName() + " KillStreaks: " + Main.KillStreaks.get(players.getName()));
                    Methods.rewardPoints(players, "Game Over");
                    Methods.SetOnlineTime(players);
                    if (Main.config.getBoolean("Debug"))
                    {
                        System.out.println("Zombie Kills Human");
                        System.out.println("Humans: " + Main.humans.toString());
                        System.out.println("Zombies: " + Main.zombies.toString());
                        System.out.println("InGame:" + Main.inGame.toString());
                    }
                    Files.savePlayers();
                    players.sendMessage(Methods.sendMessage("AfterGame_ZombiesWin", null, null, null));
                    players.sendMessage(Main.I + "Total Points: " + Files.getPlayers().getInt("Players." + players.getName().toLowerCase() + ".Points"));
                    for (PotionEffect reffect: players.getActivePotionEffects())
                        players.removePotionEffect(reffect.getType());
                    Methods.tp2LobbyAfter(players);
                }
        }
        updateScoreBoard();
        Main.Winners.clear();
        if (Main.inGame.size() >= Main.config.getInt("Automatic Start.Minimum Players") && Infected.booleanIsStarted() == false && Infected.booleanIsBeforeGame() == false && Infected.booleanIsBeforeInfected() == false && Main.config.getBoolean("Automatic Start.Use"))
        {
            Game.START();
        }
    }
    public static String getTime(Long Time)
    {
        String times = null;
        Long time = Time;
        Long seconds = time;
        long minutes = seconds / 60;
        seconds %= 60;
        if (seconds == 0)
        {
            if (minutes <= 1) times = minutes + " Minute";
            else times = minutes + " Minutes";
        }
        else if (minutes == 0)
        {
            if (seconds <= 1) times = seconds + " Second";
            else times = seconds + " Seconds";
        }
        else
        {
            times = minutes + " Minutes " + seconds + " Seconds";
        }
        return times;
    }
    public static boolean isHuman(Player player)
    {
        return Main.humans.contains(player.getName());
    }
    public static String getKillType(String group, String human, String zombie)
    {
        Random r = new Random();
        int i = r.nextInt(Files.getKills().getStringList(group).size());
        String killtype = ChatColor.GRAY + Files.getKills().getStringList(group).get(i);
        String msg = null;
        msg = killtype.replaceAll("<zombie>", ChatColor.RED + zombie).replaceAll("<human>", ChatColor.GREEN + human);
        String cmsg = ChatColor.translateAlternateColorCodes('&', msg);
        return cmsg;
    }
    @
    SuppressWarnings("deprecation")
    public static void resetPlayersInventory(Player player)
    {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.updateInventory();
    }
    @
    SuppressWarnings("deprecation")
    public static void tp2LobbyAfter(Player player)
    {
        if (!Main.db.getBlocks().isEmpty())
        {
            for (Location loc: Main.db.getBlocks().keySet())
            {
                loc.getBlock().setType(Main.db.getBlocks().get(loc));
            }
        }
		if(!Main.db.getChests().isEmpty()){
			for(Location loc : Main.db.getChests().keySet()){
				if(loc.getBlock().getTypeId()==54) {
					Chest chest = (Chest) loc.getBlock().getState();
					chest.getBlockInventory().setContents(ItemSerialization.fromBase64(Main.db.getChests().get(loc)).getContents());
				}
			}
		}
        if (Main.config.getBoolean("ScoreBoard Support"))
        {

            player.setScoreboard(Main.voteBoard);
            Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Humans:"));
            Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + "Zombies:"));
        }
        Main.db.getBlocks().clear();
        Main.db.clearChests();
        player.teleport(Methods.getLocation(Main.config.getString("Lobby")));
        resetPlayersInventory(player);
        if (Infected.filesGetShop().getBoolean("Save Items")) player.getInventory().setContents(Infected.playerGetShopInventory(player));
        Main.Lasthit.remove(player.getName());
        Main.humanClasses.remove(player.getName());
        Main.zombieClasses.remove(player.getName());
        player.setGameMode(GameMode.ADVENTURE);
        player.updateInventory();
        player.setLevel(0);
        Main.Votes.clear();
        Main.inLobby.add(player.getName());
        Main.zombies.clear();
        Main.humans.clear();
        player.setFireTicks(0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        Main.KillStreaks.remove(player.getName());
        Main.Booleans.put("Started", false);
        Main.Booleans.put("BeforeGame", false);
        Main.Booleans.put("BeforeFirstInf", false);
        Main.Voted4.clear();
        Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
        Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
        Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
        Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
        if (Main.config.getBoolean("DisguiseCraft Support"))
            if (Main.dcAPI.isDisguised(player) == true)
                Main.dcAPI.undisguisePlayer(player);
        if (Main.inGame.size() == 0)
            Main.Winners.clear();

    }

    public static void resetInf()
    {
        Infected.booleanBeforeGame(false);
        Infected.booleanBeforeInfected(false);
        Infected.booleanStarted(false);
        Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
        Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
        Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
        Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
        Main.Winners.clear();
        Main.humans.clear();
        Main.zombies.clear();
        Main.KillStreaks.clear();
        if (!Main.db.getBlocks().isEmpty())
        {
            for (Location loc: Main.db.getBlocks().keySet())
            {
                loc.getBlock().setType(Main.db.getBlocks().get(loc));
            }
        }
		if(!Main.db.getChests().isEmpty()){
			for(Location loc : Main.db.getChests().keySet()){
				if(loc.getBlock().getTypeId()==54) {
					Chest chest = (Chest) loc.getBlock().getState();
					chest.getBlockInventory().setContents(ItemSerialization.fromBase64(Main.db.getChests().get(loc)).getContents());
				}
			}
		}
		Main.db.clearChests();
        Main.db.getBlocks().clear();
    }
    
    @
    SuppressWarnings("deprecation")
    public static void resetp(Player player)
    {

        if (Main.config.getBoolean("ScoreBoard Support"))
        {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            board.registerNewObjective("empty", "dummy");

            Objective objective = board.getObjective("empty");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(board);
        }
        player.setHealth(20.0);
        player.setFoodLevel(20);
        for (PotionEffect reffect: player.getActivePotionEffects())
        {
            player.removePotionEffect(reffect.getType());
        }
        resetPlayersInventory(player);
        player.updateInventory();
        player.setGameMode(GameMode.valueOf(Main.gamemode.get(player.getName())));
        Main.Lasthit.remove(player.getName());
        if (Main.Inventory.containsKey(player.getName())) player.getInventory().setContents(Main.Inventory.get(player.getName()));
        if (Main.Armor.containsKey(player.getName())) player.getInventory().setArmorContents(Main.Armor.get(player.getName()));
        player.updateInventory();
        player.setExp(Main.Exp.get(player.getName()));
        player.setLevel(Main.Levels.get(player.getName()));
        if (Main.Spot.containsKey(player.getName())) player.teleport(Main.Spot.get(player.getName()));
        if (Main.Food.containsKey(player.getName())) player.setFoodLevel(Main.Food.get(player.getName()));
        if (Main.Health.containsKey(player.getName())) player.setHealth(Main.Health.get(player.getName()));
        Main.humanClasses.remove(player.getName());
        Main.zombieClasses.remove(player.getName());
        Main.inLobby.remove(player.getName());
        Main.zombies.remove(player.getName());
        Main.KillStreaks.remove(player.getName());
        Main.humans.remove(player.getName());
        Main.inGame.remove(player.getName());
        Main.Creating.remove(player.getName());
        Main.Health.remove(player.getName());
        Main.Food.remove(player.getName());
        Main.Armor.remove(player.getName());
        Main.Inventory.remove(player.getName());
        Main.Spot.remove(player.getName());
        Main.Winners.remove(player.getName());
        if (Main.config.getBoolean("DisguiseCraft Support"))
        {
            if (Main.dcAPI.isDisguised(player)) Main.dcAPI.undisguisePlayer(player);
        }
        if (Main.Voted4.containsKey(player.getName()))
        {
            if (Main.Votes.containsKey(Main.Voted4.get(player.getName()))) Main.Votes.put(Main.Voted4.get(player.getName()).toString(), Main.Votes.get(Main.Voted4.get(player.getName())) - 1);
            Main.Voted4.remove(player.getName());

        }
    }

    //Reset the game(Method)
    public static void reset()
    {
        for (Player players: Bukkit.getOnlinePlayers())
        {
            if (Infected.isPlayerInGame(players))
            {
                Infected.resetPlayer(players);
                if (Main.config.getBoolean("ScoreBoard Support"))
                {
                    updateScoreBoard();
                }
            }
        }
        Main.db.getBlocks().clear();
        Main.db.clearChests();
        Main.KillStreaks.clear();
        Main.possibleArenas.clear();
        Main.inLobby.clear();
        Main.zombieClasses.clear();
        Main.humanClasses.clear();
        Main.Winners.clear();
        Main.zombies.clear();
        Main.humans.clear();
        Main.inGame.clear();
        Main.Voted4.clear();
        Main.Votes.clear();
        Main.Health.clear();
        Main.Food.clear();
        Main.Inventory.clear();
        Main.Spot.clear();
        Main.Armor.clear();
        Main.Booleans.put("Started", false);
        Main.Booleans.put("BeforeGame", false);
        Main.Booleans.put("BeforeFirstInf", false);
        Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
        Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
        Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
        Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
        resetInf();
    }
    public static String countdown(HashMap < String, Integer > map)
    {
        String top = null;
        int maxValueInMap = (Collections.max(map.values())); // This will return max value in the Hashmap
        for (Entry < String, Integer > entry: map.entrySet())
        { // Itrate through hashmap
            if (entry.getValue() == maxValueInMap)
            { // Print the key with max value
                top = entry.getKey();
            }
        }

        return top;
    }
    public static String[] getTop5(String stat)
    {
        String Stat = stat;
        char[] stringArray = Stat.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        Stat = new String(stringArray);
        for (String user: Files.getPlayers().getConfigurationSection("Players").getKeys(true))
        {
            if (!user.contains("."))
            {
                Stats.put(user, Files.getPlayers().getInt("Players." + user + "." + Stat));
            }
        }
        if (Stats.size() < 6)
        {
            Stats.put(" ", 0);
            Stats.put("  ", 0);
            Stats.put("   ", 0);
            Stats.put("    ", 0);
            Stats.put("     ", 0);
        }
        String name1 = Methods.countdown(Stats);
        Stats.remove(name1);
        String name2 = Methods.countdown(Stats);
        Stats.remove(name2);
        String name3 = Methods.countdown(Stats);
        Stats.remove(name3);
        String name4 = Methods.countdown(Stats);
        Stats.remove(name4);
        String name5 = Methods.countdown(Stats);
        Stats.remove(name5);
        String[] top = {
            name1, name2, name3, name4, name5
        };
        Stats.clear();
        return top;
    }
    public static void saveLocation(Location loc, String saveto)
    {
        int ix = (int) loc.getX();
        int iy = (int) loc.getY();
        int iz = (int) loc.getZ();
        World world = loc.getWorld();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        String s = world.getName() + "," + ix + "," + iy + "," + iz + "," + yaw + "," + pitch;
        Main.config.set(saveto, s);
    }
    public static Location getLocation(String loc)
    {
        String[] floc = loc.split(",");
        World world = Bukkit.getServer().getWorld(floc[0]);
        Location Loc = new Location(world, Integer.valueOf(floc[1])+.5, Integer.valueOf(floc[2])+.5, Integer.valueOf(floc[3])+.5, Float.valueOf(floc[4]), Float.valueOf(floc[5]));
        return Loc;
    }
    public static void respawn(Player player)
    {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        Random r = new Random();
        int i = r.nextInt(Files.getArenas().getStringList("Arenas." + Main.playingin + ".Spawns").size());
        String loc = Files.getArenas().getStringList("Arenas." + Main.playingin + ".Spawns").get(i);
        String[] floc = loc.split(",");
        World world = Bukkit.getServer().getWorld(floc[0]);
        Location Loc = new Location(world, Integer.valueOf(floc[1])+.5, Integer.valueOf(floc[2])+.5, Integer.valueOf(floc[3])+.5, Float.valueOf(floc[4]), Float.valueOf(floc[5]));
        player.teleport(Loc);
        Main.Lasthit.remove(player.getName());
        if (Main.config.getBoolean("ScoreBoard Support"))
        {
            updateScoreBoard();
        }
    }
}