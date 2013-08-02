package me.xxsniperzzxx_sd.infected;

import java.util.ArrayList;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Infected
{
    public static void filesSafeAllButConfig()
    {
        filesSaveArenas();
        filesSavePlayers();
        filesSaveKillTypes();
        filesSaveShop();
        filesSaveGrenades();
        filesSaveMessages();
        filesSaveAbilities();
        filesSaveClasses();
    }
    public static void filesReloadAllButConfig()
    {
        filesReloadArenas();
        filesReloadPlayers();
        filesReloadKillTypes();
        filesReloadShop();
        filesReloadGrenades();
        filesReloadMessages();
        filesReloadAbilities();
        filesReloadClasses();

    }
    public static void playerSaveShopInventory(Player player)
    {
        Methods.saveInventory(player, "Shop Inventory");
    }
    public static void playerAddToShopInventory(Player player, ItemStack items)
    {
        Methods.addItemToInventory(player, "Shop Inventory", items);
    }
    public static ItemStack[] playerGetShopInventory(Player player)
    {
        return Methods.getInventory(player, "Shop Inventory");
    }
    public static void clearZombie()
    {
        Methods.clearZombies();
    }
    public static ArrayList < String > listZombies()
    {
        return Main.zombies;
    }
    public static void clearHuman()
    {
        Methods.clearHumans();
    }
    public static ArrayList < String > listHumans()
    {
        return Main.humans;
    }
    public static void clearInGame()
    {
        Methods.clearPlayers();
    }
    public static ArrayList < String > listInGame()
    {
        return Main.inGame;
    }
    public static void clearInLobby()
    {
        Methods.clearInLobby();
    }
    public static ArrayList < String > listInLobby()
    {
        return Main.inLobby;
    }
    public static void addPlayerZombie(Player player)
    {
        Methods.addZombie(player);
    }
    public static void addPlayerHuman(Player player)
    {
        Methods.addHuman(player);
    }
    public static void addPlayerInGame(Player player)
    {
        Methods.addPlayer(player);
    }
    public static void addPlayerInLobby(Player player)
    {
        Methods.addInLobby(player);
    }
    public static void delPlayerZombie(Player player)
    {
        Methods.delZombie(player);
    }
    public static void delPlayerHuman(Player player)
    {
        Methods.delHuman(player);
    }
    public static void delPlayerInGame(Player player)
    {
        Methods.delPlayer(player);
    }
    public static void delPlayerInLobby(Player player)
    {
        Methods.delInLobby(player);
    }
    public static boolean isPlayerZombie(Player player)
    {
        return Methods.isZombie(player);
    }
    public static boolean isPlayerHuman(Player player)
    {
        return Methods.isHuman(player);
    }
    public static boolean isPlayerInGame(Player player)
    {
        return Methods.isPlayer(player);
    }
    public static boolean isPlayerInLobby(Player player)
    {
        return Methods.isInLobby(player);
    }
    public static boolean booleanIsBeforeGame()
    {
        return Main.Booleans.get("BeforeGame");
    }
    public static void booleanBeforeGame(Boolean value)
    {
        Main.Booleans.put("BeforeGame", value);
    }
    public static boolean booleanIsBeforeInfected()
    {
        return Main.Booleans.get("BeforeFirstInf");
    }
    public static void booleanBeforeInfected(Boolean value)
    {
        Main.Booleans.put("BeforeFirstInf", value);
    }
    public static boolean booleanIsStarted()
    {
        return Main.Booleans.get("Started");
    }
    public static void booleanStarted(Boolean value)
    {
        Main.Booleans.put("Started", value);
    }
    public static String playerCreatingArena(Player player)
    {
        return Main.Creating.get(player);
    }
    public static String gameArenaPlayingIn()
    {
        return Main.playingin;
    }
    public static ArrayList < String > gameWinners()
    {
        return Main.Winners;
    }
    public static boolean gameisWinner(Player player)
    {
        return Main.Winners.contains(player.getName());
    }
    public static void gameDelWinners(Player player)
    {
        Main.Winners.remove(player.getName());
    }
    public static void gameClearWinners(Player player)
    {
        Main.Winners.clear();
    }
    public static void gameAddWinners(Player player)
    {
        Main.Winners.add(player.getName());
    }
    public static boolean playerHasPlayed(Player player)
    {
        return Files.getPlayers().contains(player.getName().toLowerCase());
    }
    public static void playerSetTime(Player player)
    {
        Methods.SetOnlineTime(player);
    }
    public static String playerGetTime(Player player)
    {
        return Methods.getOnlineTime(player.getName().toLowerCase());
    }
    public static void playerDelTime(Player player)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Time", null);
    }
    public static void playerSetKills(Player player, Integer kills)
    {
        Methods.setPlayerKills(player, kills);
    }
    public static int playerGetKills(Player player)
    {
        return Methods.getPlayerKills(player);
    }
    public static void playerDelKills(Player player)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Kills", null);
    }
    public static void playerSetDeaths(Player player, Integer deaths)
    {
        Methods.setPlayerDeaths(player, deaths);
    }
    public static int playerGetDeaths(Player player)
    {
        return Methods.getPlayerDeaths(player);
    }
    public static void playerDelDeaths(Player player)
    {
        Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Deaths", null);
    }
    public static void playerSetPoints(Player player, Integer points)
    {
        Methods.setPoints(player, points);
    }
    public static int playerGetPoints(Player player)
    {
        return Methods.getPoints(player);
    }
    public static void playerDelPoints(Player player)
    {
        Methods.delPoints(player);
    }
    public static void playerSetScore(Player player, Integer score)
    {
        Methods.setScore(player, score);
    }
    public static int playerGetScore(Player player)
    {
        return Methods.getScore(player);
    }
    public static void playerDelScore(Player player)
    {
        Methods.delScore(player);
    }
    public static void inventoryClearItems()
    {
        Main.Inventory.clear();
    }
    public static void inventorySetItems(Player player)
    {
        Main.Inventory.put(player.getName(), player.getInventory().getContents());
    }
    public static ItemStack[] inventoryDelItems(Player player)
    {
        return Main.Inventory.remove(player.getName());
    }
    public static ItemStack[] inventoryGetItems(Player player)
    {
        return Main.Inventory.get(player.getName());
    }
    public static void inventoryClearArmor()
    {
        Main.Armor.clear();
    }
    public static void inventorySetArmor(Player player)
    {
        Main.Armor.put(player.getName(), player.getInventory().getArmorContents());
    }
    public static ItemStack[] inventoryDelArmor(Player player)
    {
        return Main.Armor.remove(player.getName());
    }
    public static ItemStack[] inventoryGetArmor(Player player)
    {
        return Main.Armor.get(player.getName());
    }
    public static void playerClearHealth()
    {
        Main.Health.clear();
    }
    public static void playerSetHealth(Player player)
    {
        Main.Health.put(player.getName(), player.getHealth());
    }
    public static void playerDelHealth(Player player)
    {
        Main.Health.remove(player.getName());
    }
    public static double playergetHealth(Player player)
    {
        return Main.Health.get(player.getName());
    }
    public static void playerClearFoodlevel()
    {
        Main.Food.clear();
    }
    public static void playerSetFoodLevel(Player player)
    {
        Main.Food.put(player.getName(), player.getFoodLevel());
    }
    public static void playerDelFoodLevel(Player player)
    {
        Main.Food.remove(player.getName());
    }
    public static int playergetFoodLevel(Player player)
    {
        return Main.Food.get(player.getName());
    }
    public static void playerClearLastLocation()
    {
        Main.Spot.clear();
    }
    public static void playerSetLastLocation(Player player)
    {
        Main.Spot.put(player.getName(), player.getLocation());
    }
    public static void playerDelLastLocation(Player player)
    {
        Main.Spot.remove(player.getName());
    }
    public static int playergetLastLocation(Player player)
    {
        return Main.Food.get(player.getName());
    }
    public static Configuration filesGetPlayers()
    {
        return Files.getPlayers();
    }
    public static Configuration filesGetArenas()
    {
        return Files.getArenas();
    }
    public static Configuration filesGetKillTypes()
    {
        return Files.getKills();
    }
    public static Configuration filesGetShop()
    {
        return Files.getShop();
    }
    public static Configuration filesGetGrenades()
    {
        return Files.getGrenades();
    }
    public static Configuration filesGetClasses()
    {
        return Files.getClasses();
    }
    public static Configuration filesGetMessages()
    {
        return Files.getMessages();
    }
    public static Configuration filesGetAbilities()
    {
        return Files.getAbilities();
    }
    public static void filesSaveAbilities()
    {
        Files.saveAbilities();
    }
    public static void filesSaveClasses()
    {
        Files.saveClasses();
    }
    public static void filesSaveMessages()
    {
        Files.saveMessages();
    }
    public static void filesSavePlayers()
    {
        Files.savePlayers();
    }
    public static void filesSaveArenas()
    {
        Files.saveArenas();
    }
    public static void filesSaveKillTypes()
    {
        Files.saveKills();
    }
    public static void filesSaveShop()
    {
        Files.saveShop();
    }
    public static void filesSaveGrenades()
    {
        Files.saveGrenades();
    }
    public static void filesReloadClasses()
    {
        Files.reloadClasses();
    }
    public static void filesReloadAbilities()
    {
        Files.reloadPlayers();
    }
    public static void filesReloadPlayers()
    {
        Files.reloadPlayers();
    }
    public static void filesReloadShop()
    {
        Files.reloadShop();
    }
    public static void filesReloadGrenades()
    {
        Files.reloadGrenades();
    }
    public static void filesReloadMessages()
    {
        Files.reloadMessages();
    }
    public static void filesReloadArenas()
    {
        Files.reloadArenas();
    }
    public static void filesReloadKillTypes()
    {
        Files.reloadKills();
    }
    public static void resetPlayer(Player player)
    {
        Methods.resetp(player);
    }
    public static void resetPlugin()
    {
        Methods.reset();
    }
    public static String playerGetGroup(Player player)
    {
        return Methods.getGroup(player);
    }
    public static String playerGetLastDamage(Player player)
    {
        return Main.Lasthit.get(player.getName());
    }
    public static String playerDelLastDamage(Player player)
    {
        return Main.Lasthit.remove(player.getName());
    }
    public static boolean playerHasLastDamage(Player player)
    {
        return Main.Lasthit.containsKey(player.getName());
    }
    public static void arenaReset()
    {
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