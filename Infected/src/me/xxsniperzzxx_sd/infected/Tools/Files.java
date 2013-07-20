package me.xxsniperzzxx_sd.infected.Tools;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import me.xxsniperzzxx_sd.infected.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
public class Files
{
    //Reload Kills File
    public static void reloadKills()
    {
        if (Main.killTFile == null)
            Main.killTFile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Kills.yml");
        Main.killT = YamlConfiguration.loadConfiguration(Main.killTFile);
        //Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Kills.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.killT.setDefaults(defConfig);
        }
    }
    //Get Kills file
    public static FileConfiguration getKills()
    {
        if (Main.killT == null)
            reloadKills();
        return Main.killT;
    }
    //Safe Kills File
    public static void saveKills()
    {
        if (Main.killT == null || Main.killTFile == null)
            return;
        try
        {
            getKills().save(Main.killTFile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.killTFile, ex);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Reload Abilities File
    public static void reloadAbilities()
    {
        if (Main.abilitiesFile == null)
            Main.abilitiesFile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Abilities.yml");
        Main.abilities = YamlConfiguration.loadConfiguration(Main.abilitiesFile);
        //Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Abilities.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.abilities.setDefaults(defConfig);
        }
    }
    //Get Abilities file
    public static FileConfiguration getAbilities()
    {
        if (Main.abilities == null)
            reloadAbilities();
        return Main.abilities;
    }
    //Safe Abilities File
    public static void saveAbilities()
    {
        if (Main.abilities == null || Main.abilitiesFile == null)
            return;
        try
        {
            getAbilities().save(Main.abilitiesFile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.abilitiesFile, ex);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void reloadConfig()
    {
        if (Main.configFile == null)
            Main.configFile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Config.yml");
        Main.killT = YamlConfiguration.loadConfiguration(Main.configFile);
        //Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Config.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.customConfig.setDefaults(defConfig);
        }
    }
    //Get Kills file
    public static FileConfiguration getConfig()
    {
        if (Main.customConfig == null)
            reloadConfig();
        return Main.customConfig;
    }
    //Safe Kills File
    public static void saveConfig()
    {
        if (Main.customConfig == null || Main.configFile == null)
            return;
        try
        {
            getConfig().save(Main.configFile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.configFile, ex);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Reload Arenas File
    public static void reloadArenas()
    {
        if (Main.arenasFile == null)
            Main.arenasFile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Arenas.yml");
        Main.arenas = YamlConfiguration.loadConfiguration(Main.arenasFile);
        // Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Arenas.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.arenas.setDefaults(defConfig);
        }
    }
    //Get Arenas File
    public static FileConfiguration getArenas()
    {
        if (Main.arenas == null)
            reloadArenas();
        return Main.arenas;
    }
    //Safe Arenas File
    public static void saveArenas()
    {
        if (Main.arenas == null || Main.arenasFile == null)
            return;
        try
        {
            getArenas().save(Main.arenasFile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.arenasFile, ex);
        }
    }
    //Reload Arenas File
    public static void reloadGrenades()
    {
        if (Main.grenades == null)
            Main.grenadesfile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Grenades.yml");
        Main.grenades = YamlConfiguration.loadConfiguration(Main.grenadesfile);
        // Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Grenades.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.grenades.setDefaults(defConfig);
        }
    }
    //Get Arenas File
    public static FileConfiguration getGrenades()
    {
        if (Main.grenades == null)
            reloadGrenades();
        return Main.grenades;
    }
    //Safe Arenas File
    public static void saveGrenades()
    {
        if (Main.grenades == null || Main.grenadesfile == null)
            return;
        try
        {
            getGrenades().save(Main.grenadesfile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.grenadesfile, ex);
        }
    }
    //Reload Arenas File
    public static void reloadShop()
    {
        if (Main.shop == null)
            Main.shopfile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Shop.yml");
        Main.shop = YamlConfiguration.loadConfiguration(Main.shopfile);
        // Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Shop.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.shop.setDefaults(defConfig);
        }
    }
    //Get Arenas File
    public static FileConfiguration getShop()
    {
        if (Main.shop == null)
            reloadShop();
        return Main.shop;
    }
    //Safe Arenas File
    public static void saveShop()
    {
        if (Main.shop == null || Main.shopfile == null)
            return;
        try
        {
            getShop().save(Main.shopfile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.shopfile, ex);
        }
    }
    //Reload Arenas File
    public static void reloadMessages()
    {
        if (Main.messages == null)
            Main.messagesfile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Messages.yml");
        Main.messages = YamlConfiguration.loadConfiguration(Main.messagesfile);
        // Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Messages.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.messages.setDefaults(defConfig);
        }
    }
    //Get Arenas File
    public static FileConfiguration getMessages()
    {
        if (Main.messages == null)
            reloadMessages();
        return Main.messages;
    }
    //Safe Arenas File
    public static void saveMessages()
    {
        if (Main.messages == null || Main.messagesfile == null)
            return;
        try
        {
            getMessages().save(Main.messagesfile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.messagesfile, ex);
        }
    }
    //Reload Kills File
    public static void reloadPlayers()
    {
        if (Main.playerFile == null)
            Main.playerFile = new File(Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Players.yml");
        Main.playerF = YamlConfiguration.loadConfiguration(Main.playerFile);
        //Look for defaults in the jar
        InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Players.yml");
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            Main.playerF.setDefaults(defConfig);
        }
    }
    //Get Kills file
    public static FileConfiguration getPlayers()
    {
        if (Main.playerF == null)
        {
            reloadPlayers();
            savePlayers();
        }
        return Main.playerF;
    }
    //Save Kills File
    public static void savePlayers()
    {
        if (Main.playerF == null || Main.playerFile == null)
            return;
        try
        {
            getPlayers().save(Main.playerFile);
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Main.playerFile, ex);
        }
    }
}