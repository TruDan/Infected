package me.xxsniperzzxx_sd.infected;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.xxsniperzzxx_sd.infected.Tools.Database;
import me.xxsniperzzxx_sd.infected.Tools.ItemSerialization;
import me.xxsniperzzxx_sd.infected.Tools.Metrics;
import me.xxsniperzzxx_sd.infected.Tools.TagApi;
import me.xxsniperzzxx_sd.infected.Tools.TeleportFix;
import me.xxsniperzzxx_sd.infected.Tools.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class Main extends JavaPlugin
{

    //Initialize all the variables
    public static String bVersion = "1.6.2";
    public static int currentTime = 0;
    public static String v = null;

    //Set up all the needed things for files
    public static YamlConfiguration abilities = null;
    public static File abilitiesFile = null;
    public static YamlConfiguration killT = null;
    public static File killTFile = null;
    public static YamlConfiguration classes = null;
    public static File classesFile = null;
    public static YamlConfiguration arenas = null;
    public static File arenasFile = null;
    public static YamlConfiguration playerF = null;
    public static File playerFile = null;
    public static YamlConfiguration messages = null;
    public static File messagesfile = null;
    public static YamlConfiguration shop = null;
    public static File shopfile = null;
    public static YamlConfiguration grenades = null;
    public static File grenadesfile = null;
    public static FileConfiguration customConfig = null;
    public static File configFile = null;

    //Lists, Strings and Integers Infected needs
    public static int arenaNumber = 0;
    public static ArrayList < String > possibleArenas = new ArrayList < String > ();
    public static ArrayList < String > possibleArenasU = new ArrayList < String > ();
    public static ArrayList < String > Winners = new ArrayList < String > ();
    public static ArrayList < Integer > Score = new ArrayList < Integer > ();
    public static ArrayList < Integer > Grenades = new ArrayList < Integer > ();
    public static ArrayList < String > inLobby = new ArrayList < String > ();
    public static ArrayList < String > zombies = new ArrayList < String > ();
    public static ArrayList < String > humans = new ArrayList < String > ();
    public static ArrayList < String > inGame = new ArrayList < String > ();
    public static HashMap < String, String > humanClasses = new HashMap < String, String > ();
    public static HashMap < String, String > zombieClasses = new HashMap < String, String > ();
    public static HashMap < String, Integer > Leaders = new HashMap < String, Integer > ();
    public static HashMap < String, Boolean > Booleans = new HashMap < String, Boolean > ();
    public static HashMap < String, Long > Timein = new HashMap < String, Long > ();
    public static HashMap < String, String > gamemode = new HashMap < String, String > ();
    public static HashMap < String, String > Lasthit = new HashMap < String, String > ();
    public static HashMap < String, String > Voted4 = new HashMap < String, String > ();
    public static HashMap < String, String > Creating = new HashMap < String, String > ();
    public static HashMap < String, Integer > Votes = new HashMap < String, Integer > ();
    public static HashMap < String, Integer > Levels = new HashMap < String, Integer > ();
    public static HashMap < String, Float > Exp = new HashMap < String, Float > ();
    public static HashMap < String, Integer > KillStreaks = new HashMap < String, Integer > ();
    public static HashMap < String, Double > Health = new HashMap < String, Double > ();
    public static HashMap < String, Integer > Food = new HashMap < String, Integer > ();
    public static HashMap < String, ItemStack[] > Armor = new HashMap < String, ItemStack[] > ();
    public static HashMap < String, ItemStack[] > Inventory = new HashMap < String, ItemStack[] > ();
    public static String I = ChatColor.DARK_RED + "" + "«†" + ChatColor.RESET + ChatColor.DARK_RED + "Infected" + ChatColor.DARK_RED + "†»" + ChatColor.RESET + ChatColor.GRAY + " ";
    public static HashMap < String, Location > Spot = new HashMap < String, Location > ();
    public static String playingin = null;
    public static int timestart;
    public static int queuedtpback;
    public static int queuedtp;
    public static int voteTime;
    public static int Wait;
    public static int GtimeLimit;
    public static int timedCycle;
    public static int timeLimit;
    public static int timeVote;
    public static int timest;
    public static int lastscore;
    public static boolean update = false;
    public static String name = "";
    public static String leader = "";
    public static Plugin me;
    public static File file;
    public static String BV = null;
    public boolean Enabled = true;

    public static Configuration config = null;
    public static Database db = new Database();
    public String currentBukkitVersion = null;
    public String updateBukkitVersion = null;

    //Item Serializer class
    public static ItemSerialization is = null;

    //Scoreboard
    private ScoreboardManager manager;
    public static Scoreboard voteBoard;
    public static Scoreboard playingBoard;
    public static Objective voteList;
    public static Objective playingList;

    //Plugin Addons
    public static boolean tagapi = false;
    public static DisguiseCraftAPI dcAPI;
    //	public NamedItemStack NIS;



    @
    Override
    public void onEnable()
    {

        //Setup the scoreboard
        manager = Bukkit.getScoreboardManager();
        voteBoard = manager.getNewScoreboard();
        playingBoard = manager.getNewScoreboard();
        playingList = playingBoard.registerNewObjective("playing", "dummy");
        voteList = voteBoard.registerNewObjective("votes", "dummy");

        //Item Serialization
        is = new ItemSerialization();

        //Create Configs and files
        Infected.filesGetArenas().options().copyDefaults(true);
        Infected.filesGetKillTypes().options().copyDefaults(true);
        getConfig().options().copyDefaults(true);
        Infected.filesGetShop().options().copyDefaults(true);
        Infected.filesGetPlayers().options().copyDefaults(true);
        Infected.filesGetMessages().options().copyDefaults(true);
        Infected.filesGetGrenades().options().copyDefaults(true);
        Infected.filesGetAbilities().options().copyDefaults(true);
        Infected.filesSafeAllButConfig();
        saveConfig();

        PluginManager pm = getServer().getPluginManager();
        pm = getServer().getPluginManager();
        me = this;
        file = this.getFile();
        Configuration getconfig = getConfig();
        Main.config = getconfig;

        //Check for an update
        PluginDescriptionFile pdf = getDescription();
        Main.v = pdf.getVersion();
        String[] s = Bukkit.getBukkitVersion().split("-");
        currentBukkitVersion = s[0];
        if (getConfig().getBoolean("Check For Updates"))
        {

            Updater updater = new Updater(this, "Infected-Core", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);

            update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
            name = updater.getLatestVersionString();
            updateBukkitVersion = updater.updateBukkitVersion;

            if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(0))) <= Integer.valueOf(String.valueOf(v.charAt(0))))
                if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(2))) <= Integer.valueOf(String.valueOf(v.charAt(2))))
                    if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(4))) <= Integer.valueOf(String.valueOf(v.charAt(4))))
                        update = false;
        }

        //Check if the plugin addons are there
        if (getConfig().getBoolean("DisguiseCraft Support"))
        {
            if (getServer().getPluginManager().getPlugin("ProtocolLib") == null)
            {
                System.out.println(Main.I + "DisguiseCraft was enabled but ProtocolLib wasn't found on the server");
                getConfig().set("DisguiseCraft Support", false);
            }
            else
            {
                if (!(getServer().getPluginManager().getPlugin("DisguiseCraft") == null))
                {
                    setupDisguiseCraft();
                }
                else
                {
                    System.out.println(Main.I + "DisguiseCraft wasn't found on this server, switching to DisguiseCraft Support: false");
                    getConfig().set("DisguiseCraft Support", false);
                    saveConfig();
                }
            }
        }
        if (getServer().getPluginManager().getPlugin("TagAPI") == null)
        {
            getLogger().info(I + "TagAPI was not found...");
        }
        else
        {
            TagApi TagApi = new TagApi(this);
            pm.registerEvents(TagApi, this);
            tagapi = true;
        }

        //On enable set the times form the config
        Main.voteTime = getConfig().getInt("Time.Voting Time");
        Main.Wait = getConfig().getInt("Time.Alpha Zombie Infection");
        Main.GtimeLimit = getConfig().getInt("Time.Game Time Limit");

        //Setup metrics
        try
        {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e)
        {
            System.out.println("Error getting metrics!");
        }

        //Save booleans in an array...(I forget why i did this...)
        Main.Booleans.put("Started", false);
        Main.Booleans.put("BeforeGame", false);
        Main.Booleans.put("BeforeFirstInf", false);

        //Get the Commands class and the Listener
        getCommand("Infected").setExecutor(new Commands(this));
        PlayerListener PlayerListener = new PlayerListener(this);
        TeleportFix TeleportFix = new TeleportFix(this, this.getServer());
        pm.registerEvents(PlayerListener, this);
        pm.registerEvents(TeleportFix, this);

        //Clear the blocks DB because a game couldnt have started before the plugins up
        db.getBlocks().clear();
        db.loadDB("plugins/Infected/Database.db");

        //Do the info signs (Updating the info)
        if (getConfig().getBoolean("Info Signs.Enabled"))
        {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
            {
                public void run()
                {
                    if (!db.getInfoSigns().isEmpty())
                    {
                        for (String loc: db.getInfoSigns().keySet())
                        {
                            String status;
                            if (Infected.booleanIsBeforeGame()) status = "Voting";
                            if (Infected.booleanIsBeforeInfected()) status = "B4 Infected";
                            if (Infected.booleanIsStarted()) status = "Started";
                            else status = "In Lobby";
                            int time = currentTime;
                            if (!db.getInfoSign(loc).equals(null))
                            {
                                if (db.getInfoSign(loc).getType() == Material.SIGN_POST || db.getInfoSign(loc).getType() == Material.WALL_SIGN)
                                {
                                    Sign sign = (Sign) db.getInfoSign(loc).getBlock().getState();
                                    sign.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Infected.listInGame().size()));
                                    sign.setLine(2, ChatColor.GOLD + status);
                                    sign.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));
                                    sign.update();
                                }
                            }
                        }
                    }
                }
            }, 100L, getConfig().getInt("Info Signs.Refresh Time") * 20);
        }

        //Make sure the Infected's CB is the same as the server's CB
        if (!currentBukkitVersion.equalsIgnoreCase(bVersion))
        {
            Bukkit.getPluginManager().disablePlugin(this);
            System.out.println("Infected's Bukkit Version: |" + bVersion + "|");
            System.out.println("Your Bukkit Version: |" + currentBukkitVersion + "|");
            System.out.println("Versions do not match so Infected has been disabled!");
        }

        //Setup the scoreboards
        if (config.getBoolean("ScoreBoard Support"))
        {

            //Votes
            voteList.setDisplaySlot(DisplaySlot.SIDEBAR);
            voteList.setDisplayName("Votes");

            //Playing				
            playingList.setDisplaySlot(DisplaySlot.SIDEBAR);
            playingList.setDisplayName("Playing");

        }
    }


    @
    Override
    public void onDisable()
    {
    	
        //On disable reset players with everything from before
        for (Player players: Bukkit.getServer().getOnlinePlayers())
            if (players != null)
                if (Main.inGame.contains(players.getName()))
                {
                    players.sendMessage(Main.I + "Server was reloaded!");
                }

                //Empty all the hashmaps and database settings(Blocks)
        reset();
        db.getBackups().clear();
        db.getBlocks().clear();
        db.saveDB("plugins/Infected/Database.db");
    }

    //Setup DisguiseCraft
    public void setupDisguiseCraft()
    {
        dcAPI = DisguiseCraft.getAPI();
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
        Methods.resetPlayersInventory(player);
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
                    Methods.updateScoreBoard();
                }
            }
        }
        Main.db.getBlocks().clear();
        Main.KillStreaks.clear();
        Main.possibleArenas.clear();
        Main.inLobby.clear();
        Main.humanClasses.clear();
        Main.zombieClasses.clear();
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
        Main.db.getBlocks().clear();
    }
}