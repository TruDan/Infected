package me.xxsniperzzxx_sd.infected;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Events.InfectedGameStartEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedVoteStartEvent;
import me.xxsniperzzxx_sd.infected.Tools.TeleportFix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

public class Game
{

    public static void START()
    {

        Main.Winners.clear();
        Infected.filesReloadArenas();

        Main.db.getBlocks().clear();
        for (Player playing: Bukkit.getServer().getOnlinePlayers())
            if (Main.inGame.contains(playing.getName()))
            {

                if (Main.config.getBoolean("ScoreBoard Support"))
                {
                    playing.setScoreboard(Main.voteBoard);
                    Methods.updateScoreBoard();
                }
                playing.sendMessage(Methods.sendMessage("Vote_Time", null, Methods.getTime(Long.valueOf(Main.voteTime)), null));
                playing.sendMessage(Methods.sendMessage("Vote_HowToVote", null, null, null));

                Infected.filesReloadArenas();
                Main.possibleArenas.clear();

                for (String parenas: Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
                {
                    //Check if the string matchs an arena

                    if (Main.possibleArenas.contains(parenas))
                    {
                        Main.possibleArenas.remove(parenas);
                    }
                    if (!parenas.contains("."))
                    {
                        Main.possibleArenas.add(parenas);
                    }
                    if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
                    {
                        Main.possibleArenas.remove(parenas);
                    }
                    if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns") && !parenas.contains("."))
                    {
                        Main.possibleArenasU.add(parenas);
                    }
                }

                StringBuilder possible = new StringBuilder();
                for (Object o: Main.possibleArenas)
                {
                    possible.append(o.toString());
                    if (Main.possibleArenas.size() > 1) possible.append(", ");
                }
                playing.sendMessage(Main.I + ChatColor.GRAY + "Arenas: " + ChatColor.GREEN + possible.toString());

                playing.sendMessage(Main.I + ChatColor.DARK_RED + "Possible Arenas: " + ChatColor.GOLD + possible.toString());
                playing.sendMessage(Main.I + ChatColor.YELLOW + "Or you could just vote for \"/Inf Vote Random\"");
                Main.Winners.add(playing.getName());
            }

            //ING TIME
        Bukkit.getServer().getPluginManager().callEvent(new InfectedVoteStartEvent(Main.inGame, Main.voteTime));

        Main.Booleans.put("BeforeGame", true);
        Main.timeVote = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
        {
            int timeleft = Main.voteTime;@
            Override
            public void run()
            {
                if (timeleft != -1)
                {
                    timeleft -= 1;
                    Main.currentTime = timeleft;
                    if(timeleft == 5 || timeleft == 4 ||timeleft == 3||timeleft == 2||timeleft == 1){
                   	 for (Player playing: Bukkit.getServer().getOnlinePlayers())
                        {
                            if (Main.inGame.contains(playing.getName()))
                            {
                              playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
                            }
                        }
                   }
                    if (timeleft == 60 || timeleft == 50 || timeleft == 40 || timeleft == 30 || timeleft == 20 || timeleft == 10 || timeleft == 9 || timeleft == 8 || timeleft == 7 || timeleft == 6 || timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
                    {
                        for (Player playing: Bukkit.getServer().getOnlinePlayers())
                            if (Main.inGame.contains(playing.getName()))
                            {
                                playing.sendMessage(Methods.sendMessage("Vote_TimeLeft", null, Methods.getTime(Long.valueOf(timeleft)), null));
                            }

                    }
                    else if (timeleft == -1)
                    {
                        Main.possibleArenas.clear();

                        for (String parenas: Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
                        {
                            //Check if the string matchs an arena

                            if (Main.possibleArenas.contains(parenas))
                            {
                                Main.possibleArenas.remove(parenas);
                            }
                            if (!parenas.contains("."))
                            {
                                Main.possibleArenas.add(parenas);
                            }
                            if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
                            {
                                Main.possibleArenas.remove(parenas);
                            }
                            if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns") && !parenas.contains("."))
                            {
                                Main.possibleArenasU.add(parenas);
                            }
                        }

                        if (Main.Votes.isEmpty())
                        {
                            Random r = new Random();
                            int i = r.nextInt(Main.possibleArenas.size());
                            Main.playingin = Main.possibleArenas.get(i);
                        }
                        else
                        {
                            //Determine the most voted for map
                            Main.playingin = Methods.countdown(Main.Votes);
                        }
                        if (Main.playingin.equalsIgnoreCase("random"))
                        {
                            Main.possibleArenas.remove("random");
                            Random r = new Random();
                            int i = r.nextInt(Main.possibleArenas.size());
                            Main.playingin = Main.possibleArenas.get(i);
                        }
                        for (Player p: Bukkit.getServer().getOnlinePlayers())
                            if (Main.inGame.contains(p.getName()))
                            {
                                p.sendMessage(Main.I + ChatColor.GOLD + "Map: " + ChatColor.WHITE + Main.playingin);
                                p.sendMessage(Main.I + "Game Starting in 10 Seconds.");
                            }
                        for (String loc: Infected.filesGetArenas().getStringList("Arenas." + Main.playingin + ".Spawns"))
                        {
                            String[] floc = loc.split(",");
                            World world = Bukkit.getServer().getWorld(floc[0]);
                            Location Loc = new Location(world, Integer.valueOf(floc[1]), Integer.valueOf(floc[2]), Integer.valueOf(floc[3]));
                            if (!Bukkit.getServer().getWorld(world.getName()).getChunkAt(Loc).isLoaded()) Bukkit.getServer().getWorld(world.getName()).getChunkAt(Loc).load();
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
                        {@
                            Override
                            public void run()
                            {
                                //Get Players in lobby setup
                                Main.inLobby.clear();
                                for (Player p: Bukkit.getServer().getOnlinePlayers())
                                {
                                    if (Main.inGame.contains(p.getName()))
                                    {
                                        p.setHealth(20);
                                        p.setFoodLevel(20);
                                        p.sendMessage(Methods.sendMessage("Game_FirstInfectedIn", null, Methods.getTime(Long.valueOf(Main.Wait)), null));
                                        if (Main.config.getBoolean("ScoreBoard Support"))
                                        {
                                            p.setScoreboard(Main.playingBoard);
                                            Methods.updateScoreBoard();
                                        }
                                        Methods.respawn(p);
                                        Methods.equipHumans(p);
                                        if (Main.config.getBoolean("Allow Breaking Certain Blocks"))
                                            p.setGameMode(GameMode.SURVIVAL);
                                        else
                                            p.setGameMode(GameMode.ADVENTURE);
                                        if (!Main.KillStreaks.containsKey(p.getName()))
                                            Main.KillStreaks.put(p.getName(), Integer.valueOf("0"));
                                    }
                                }
                                Main.Voted4.clear();
                                Main.Votes.clear();
                                Main.Booleans.put("BeforeFirstInf", true);
                                Main.Booleans.put("BeforeGame", false);
                                Main.timestart = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
                                {
                                    int timeleft = Main.Wait;@
                                    Override
                                    public void run()
                                    {
                                        if (timeleft != -1)
                                        {
                                            timeleft -= 1;
                                            Main.currentTime = timeleft;
                                            if(timeleft == 5 || timeleft == 4 ||timeleft == 3||timeleft == 2||timeleft == 1){
                                            	 for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                                 {
                                                     if (Main.inGame.contains(playing.getName()))
                                                     {
                                                       playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
                                                     }
                                                 }
                                            }
                                            if (timeleft == 60 || timeleft == 50 || timeleft == 40 || timeleft == 30 || timeleft == 20 || timeleft == 10 || timeleft == 9 || timeleft == 8 || timeleft == 7 || timeleft == 6 || timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
                                            {
                                                for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                                {
                                                    if (Main.inGame.contains(playing.getName()))
                                                    {
                                                        playing.sendMessage(Methods.sendMessage("Game_InfectionTimer", null, Methods.getTime(Long.valueOf(timeleft)), null));
                                                    }
                                                }
                                            }
                                            else if (timeleft == -1)
                                            {
                                                //Choose the first infected
                                                Main.Booleans.put("BeforeFirstInf", false);
                                                Main.Booleans.put("Started", true);
                                                for (Player players: Bukkit.getServer().getOnlinePlayers())
                                                {
                                                    if (Main.inGame.contains(players.getName()))
                                                    {
                                                        if (!Main.Winners.contains(players.getName()))
                                                        {
                                                            Main.Winners.add(players.getName());
                                                        }
                                                        Main.Timein.put(players.getName(), System.currentTimeMillis() / 1000);
                                                        Main.inLobby.remove(players.getName());
                                                    }
                                                }
                                                Methods.newZombieSetUpEveryOne();
                                                Bukkit.getServer().getPluginManager().callEvent(new InfectedGameStartEvent(Main.inGame, Main.Wait, Bukkit.getPlayer(Main.zombies.get(0))));
                                                if (Main.config.getBoolean("Debug"))
                                                {
                                                    System.out.println("humans: " + Main.humans.toString());
                                                    System.out.println("zombies: " + Main.zombies.toString());
                                                    System.out.println("InGame " + Main.inGame.toString());
                                                }
                                                //Set the game's time limit
                                                Main.timeLimit = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
                                                {
                                                    int timeleft = Main.GtimeLimit;@
                                                    Override
                                                    public void run()
                                                    {
                                                        if (timeleft != -1)
                                                        {
                                                            timeleft -= 1;
                                                            Main.currentTime = timeleft;

                                                            for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                                            {
                                                                if (Main.inGame.contains(playing.getName()))
                                                                	playing.setLevel(timeleft);
                                                            }
                                                            if (Main.GtimeLimit - timeleft == 10)
                                                                for(final Player playing : Bukkit.getOnlinePlayers()){
                                                                	if(Main.inGame.contains(playing.getName()) || Main.inLobby.contains(playing.getName())){

                                                                        
                                                                		TeleportFix.updateEntities(TeleportFix.getPlayersWithin(playing, Bukkit.getServer().getViewDistance() * 16));
                                                                	}
                                                                }
                                                            if(timeleft == 5 || timeleft == 4 ||timeleft == 3||timeleft == 2||timeleft == 1){
                                                           	 for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                                                {
                                                                    if (Main.inGame.contains(playing.getName()))
                                                                    {
                                                                      playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
                                                                    }
                                                                }
                                                           }
                                                            if (timeleft == (Main.GtimeLimit / 4) * 3 || timeleft == Main.GtimeLimit / 2 || timeleft == Main.GtimeLimit / 4 || timeleft == 60 || timeleft == 10 || timeleft == 9 || timeleft == 8 || timeleft == 7 || timeleft == 6 || timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
                                                            {
                                                                for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                                                {
                                                                    if (Main.inGame.contains(playing.getName()))
                                                                        if (timeleft > 61)
                                                                        {
                                                                            playing.sendMessage(Methods.sendMessage("Game_TimeLeft", null, Methods.getTime(Long.valueOf(timeleft)), null));
                                                                            playing.sendMessage(Main.I + ChatColor.GREEN + "Humans Left: " + ChatColor.YELLOW + Main.humans.size() + ChatColor.GREEN + " |" + ChatColor.RED + "| Total Zombies: " + ChatColor.YELLOW + Main.zombies.size());
                                                                        }
                                                                        else
                                                                        {
                                                                            playing.sendMessage(Methods.sendMessage("Game_TimeLeft", null, Methods.getTime(Long.valueOf(timeleft)), null));
                                                                        }
                                                                }
                                                            }
                                                            else if (timeleft == -1)
                                                            {
                                                                Methods.endGame(true);

                                                            }
                                                        }
                                                    }
                                                }, 0L, 20L);
                                            }
                                        }
                                    }
                                }, 0L, 20L);
                            }
                        }, 200L);
                    }
                }
            }
        }, 0L, 20L);
    }
}