package me.xxsniperzzxx_sd.infected;

import java.util.ArrayList;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@
SuppressWarnings(
{
    "static-access"
})

public class PlayerListener implements Listener
{

    public Main Main = new Main();
    public ArrayList < String > item = new ArrayList < String > ();

    public Main plugin;
    public PlayerListener(Main instance)
    {
        this.plugin = instance;
    }

    //Create global players (Bows and melee)
    Player playeruser = null;
    Player useruser = null;

    //Settings for effects
    int effect = 0;
    boolean effectb = false;

    //Check for updates when a player joins, making sure they are OP
    @
    EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("Check For Updates"))
            if (plugin.update && player.isOp())
                if (plugin.getConfig().getBoolean("Update Notification"))
                {
                    player.sendMessage(Main.I + ChatColor.RED + "An update is available: " + Main.name);
                    player.sendMessage(Main.I + ChatColor.RED + "Download it at: http://dev.bukkit.org/server-mods/infected-core/");
                }
    }

    //Disable dropping items if the player is in game
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent e)
    {
        if (Main.inGame.contains(e.getPlayer().getName()))
            e.setCancelled(true);
    }

    //If the game hasn't started yet
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(InventoryClickEvent e)
    {
        if (Main.inGame.contains(e.getWhoClicked().getName()))
            if (!Infected.booleanIsStarted())
                e.setCancelled(true);
    }

    //Check if the player is in game and the situation around them breaking a block 
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBreakBlock(BlockBreakEvent e)
    {
        if (!e.isCancelled())
        {
            if (Main.db.isInfoSign(e.getBlock().getLocation()))
            {
                Main.db.removeInfoSignLoc(e.getBlock().getLocation());
            }
            if (Main.db.isSign(e.getBlock().getLocation()))
            {
                Main.db.removeSign(e.getBlock().getLocation());
            }
            if (Main.inLobby.contains(e.getPlayer().getName()))
            {
                e.setCancelled(true);
            }
            else if (Main.inGame.contains(e.getPlayer().getName()))
            {
                e.getBlock().getDrops().clear();
                if (Files.getArenas().getIntegerList("Arenas." + Main.playingin + ".Allow Breaking Of.Global").contains(e.getBlock().getTypeId()) || Files.getArenas().getIntegerList("Arenas." + Main.playingin + ".Allow Breaking Of." + Methods.getGroup(e.getPlayer())).contains(e.getBlock().getTypeId()))
                {
                    Location loc = e.getBlock().getLocation();
                    plugin.db.getBlocks().put(loc, e.getBlock().getType());
                    e.getBlock().getDrops().clear();
                }
                else
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    //Check to make sure they arn't trying to place a block in game
    @
    EventHandler(priority = EventPriority.LOW)
    public void onPlayerBlockPlace(BlockPlaceEvent e)
    {
        if (!e.isCancelled())
        {
            if (Main.inLobby.contains(e.getPlayer().getName()))
            {
                e.setCancelled(true);
            }
        }
        else if (Main.inGame.contains(e.getPlayer().getName()))
        {
            Location loc = e.getBlock().getLocation();
            plugin.db.getBlocks().put(loc, Material.AIR);
        }
    }

    //See if a player got kicked and if it effected the game
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGetKicked(final PlayerKickEvent e)
    {
        boolean Started = plugin.Booleans.get("Started");
        boolean BeforeGame = plugin.Booleans.get("BeforeGame");
        boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
        plugin.Creating.remove(e.getPlayer().getName());

        //If a player logs out well playing, make sure it doesn't effect the game, end the game if it does
        if (plugin.inGame.contains(e.getPlayer().getName()))
        {
            plugin.inGame.remove(e.getPlayer().getName());

            //Leave well everyones in the lobby
            if (!Started && !BeforeGame && !BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: Leaving, wellin lobby, no timers active");
                }
                e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                Methods.resetp(e.getPlayer());
                for (Player players: Bukkit.getOnlinePlayers())
                {
                    if (Methods.isPlayer(players)) players.sendMessage(Methods.sendMessage("Leave_InLobby", e.getPlayer(), null, null));
                }
            }

            //Voting has started, less then 2 people left
            else if (!Started && BeforeGame && !BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: Before Voting");
                }
                if (Main.inGame.size() == 1)
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: Before Voting(Triggered)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }
            }

            //In Arena, before first Infected
            else if (!Started && !BeforeGame && BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: In Arena Before Infected");
                }
                if (Main.inGame.size() == 1)
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena Before Infected(Triggered)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }
            }

            //In Arena, Game has started
            else if (Started && !BeforeGame && !BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: In Arena, Game Has Started");
                }
                if (Main.inGame.size() == 1)
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena, Game Has Started (Not Enough Players)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }

                // If Not Enough zombies remain 
                else if (Main.zombies.size() == 1 && Infected.isPlayerZombie(e.getPlayer()))
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    Random r = new Random();
                    int alpha = r.nextInt(Main.inGame.size());
                    String name = Main.inGame.get(alpha);
                    Player zombie = Bukkit.getServer().getPlayer(name);
                    zombie.sendMessage(Main.I + ChatColor.RED + "You are the new Infected! Good luck!");
                    Main.zombies.clear();
                    Main.zombies.add(zombie.getName());
                    Main.Winners.remove(zombie.getName());
                    if (plugin.getConfig().getBoolean("New Zombie Tp"))
                    {
                        Methods.respawn(zombie);
                    }
                    zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                    if (plugin.getConfig().getBoolean("Zombie Abilties") == true)
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
                            if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
                            {

                                //if(Main.humans.contains(online)) {
                                Main.humans.add(online.getName());
                                online.sendMessage(Main.I + ChatColor.RED + zombie.getName() + " has became the new Infected!");
                                online.setHealth(20);
                                online.playEffect(online.getLocation(), Effect.SMOKE, 1);
                            }
                        }
                    }
                }

                // Not enough humans left
                else if (Main.humans.size() == 1 && plugin.humans.contains(e.getPlayer().getName()))
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena, Game Has Started (Not Enough Humans)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }
                else
                {
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
                        }
                    }
                }
            }
        }
    }

    //When players leave the server willingly, make sure it doesn't effect the game
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent e)
    {
        boolean Started = plugin.Booleans.get("Started");
        boolean BeforeGame = plugin.Booleans.get("BeforeGame");
        boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
        plugin.Creating.remove(e.getPlayer().getName());

        //If a player logs out well playing, make sure it doesn't effect the game, end the game if it does
        if (plugin.inGame.contains(e.getPlayer().getName()))
        {
            plugin.inGame.remove(e.getPlayer().getName());

            //Leave well everyones in the lobby
            if (!Started && !BeforeGame && !BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: Leaving, wellin lobby, no timers active");
                }
                e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                Methods.resetp(e.getPlayer());
                for (Player players: Bukkit.getOnlinePlayers())
                {
                    if (Methods.isPlayer(players)) players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
                }
            }

            //Voting has started, less then 2 people left
            else if (!Started && BeforeGame && !BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: Before Voting");
                }
                if (Main.inGame.size() == 1)
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: Before Voting(Triggered)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }
                else
                {
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
                        }
                    }
                }
            }

            //In Arena, before first Infected
            else if (!Started && !BeforeGame && BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: In Arena Before Infected");
                }
                if (Main.inGame.size() == 1)
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena Before Infected(Triggered)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }
                else
                {
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
                        }
                    }
                }
            }

            //In Arena, Game has started
            else if (Started && !BeforeGame && !BeforeFirstInf)
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("Leave: In Arena, Game Has Started");
                }
                if (Main.inGame.size() == 1)
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena, Game Has Started (Not Enough Players)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }

                // If Not Enough zombies remain 
                else if (Main.zombies.size() == 1 && Infected.isPlayerZombie(e.getPlayer()))
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    Methods.newZombieSetUpEveryOne();
                }

                // Not enough humans left
                else if (Main.humans.size() == 1 && plugin.humans.contains(e.getPlayer().getName()))
                {
                    if (plugin.getConfig().getBoolean("Debug"))
                    {
                        System.out.println("Leave: In Arena, Game Has Started (Not Enough Humans)");
                    }
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
                            Methods.tp2LobbyAfter(players);
                        }
                    }
                    Methods.resetInf();
                }
                else
                {
                    e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
                    Methods.resetp(e.getPlayer());
                    for (Player players: Bukkit.getOnlinePlayers())
                    {
                        if (Methods.isPlayer(players))
                        {
                            players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
                        }
                    }
                }
            }
        }
    }

    //When a player uses a command make sure it does, what its supposed to 
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent e)
    {
        String msg = null;
        if (e.getMessage().contains(" "))
        {
            String[] ss = e.getMessage().split(" ");
            msg = ss[0];
        }
        else
        {
            msg = e.getMessage();
        }
        if (Main.inGame.contains(e.getPlayer().getName()) || Main.inLobby.contains(e.getPlayer().getName()))

            if (!e.getPlayer().isOp())
            {

                //If a player tries a command but is in infected, block all that aren't /inf
                if (plugin.getConfig().getStringList("Blocked Commands").contains(msg.toLowerCase()))
                {
                    e.getPlayer().sendMessage(Methods.sendMessage("Error_CantUseCommand", null, null, null));
                    e.setCancelled(true);
                }
                else if (!(plugin.getConfig().getStringList("Allowed Commands").contains(msg.toLowerCase()) || e.getMessage().toLowerCase().contains("inf")))
                {
                    e.getPlayer().sendMessage(Methods.sendMessage("Error_CantUseCommand", null, null, null));
                    e.setCancelled(true);
                }
            }
    }

    //When a Living Entitie targets another block it if its in a game
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onEntityAtkPlayerLivingEntity(EntityTargetLivingEntityEvent e)
    {
        if (e.getTarget() instanceof Player)
        {
            Player player = (Player) e.getTarget();
            if (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName()))
                e.setCancelled(true);
        }
    }


    //When a Entity targets another, block it if it's in a game
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onEntityAtk(EntityTargetEvent e)
    {
        if (e.getTarget() instanceof Player)
        {
            Player player = (Player) e.getTarget();
            if (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName()))
                e.setCancelled(true);
        }
    }

    //If theres no other plugin that interfers with Food level, do "stuff"
    @
    EventHandler(priority = EventPriority.LOW)
    public void onPlayerHunger(FoodLevelChangeEvent e)
    {
        if (!e.isCancelled())
        {
            Player player = (Player) e.getEntity();
            if (plugin.getConfig().getBoolean("Disable Hunger"))
                if (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName()))
                    e.setCancelled(true);
        }
    }

    //Block enchantment tables if they're just for show
    @
    EventHandler(priority = EventPriority.LOW)
    public void PlayerTryEnchant(PrepareItemEnchantEvent e)
    {
        Player player = e.getEnchanter();
        if ((Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName())) && plugin.getConfig().getBoolean("Disable Enchanting"))
            e.setCancelled(true);
    }

    //If an entity shoots a bow do "Stuff"
    @
    SuppressWarnings("deprecation")@ EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerShootBow(EntityShootBowEvent e)
    {
        if (!e.isCancelled() && !Infected.booleanIsStarted())
            if (e.getEntity() instanceof Player)
            {
                Player player = (Player) e.getEntity();
                if (Main.inGame.contains(player.getName()))
                {
                    e.getProjectile().remove();
                    e.setCancelled(true);
                    player.updateInventory();
                }
                else
                {
                    e.setCancelled(false);
                }
            }
    }

    //When a player moves trigger effects
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(final PlayerMoveEvent e)
    {
        if (plugin.getConfig().getBoolean("Use Zombie Movement Effects") && Methods.isZombie(e.getPlayer()))
        {
            if (!Bukkit.getScheduler().isCurrentlyRunning(effect))
            {
                if (!effectb)
                {
                    effectb = true;
                    effect = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
                    {@
                        Override
                        public void run()
                        {
                            e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                            effectb = false;
                        }
                    }, 200L);
                }
            }
        }
    }

    //Player is Damaged, User is Damager
    //When entity is damaged
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent e)
    {
        boolean Started = plugin.Booleans.get("Started");
        boolean BeforeGame = plugin.Booleans.get("BeforeGame");
        boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
        if (e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            if (plugin.inLobby.contains(p.getName()))
            {
                if (plugin.getConfig().getBoolean("Debug"))
                {
                    System.out.println("PVEhumans: " + plugin.humans.toString());
                    System.out.println("PVEzombies: " + plugin.zombies.toString());
                    System.out.println("PVEInGame " + plugin.inGame.toString());
                    System.out.println("PVE In Lobby");
                }
                e.setCancelled(true);
            }
            else if (plugin.inGame.contains(p.getName()))
                if (!(p.getLastDamageCause() instanceof Player))
                    if (plugin.inGame.contains(p.getName()) && Started == true)
                        if (e.getCause() == DamageCause.PROJECTILE)
                        {
                            EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
                            if (ee.getDamager() instanceof Arrow)
                            {
                                if (e.getEntity() instanceof Player)
                                {
                                    playeruser = (Player) e.getEntity();
                                    Arrow arrow = (Arrow) ee.getDamager();
                                    useruser = (Player) arrow.getShooter();
                                    Main.Lasthit.put(playeruser.getName(), useruser.getName());
                                }
                            }
                        }
            if (p.getHealth() - e.getDamage() <= 0)
                if (plugin.inGame.contains(p.getName()) && BeforeGame == true)
                {
                    e.setCancelled(true);
                }
                else if (plugin.inGame.contains(p.getName()) && BeforeFirstInf == true)
            {
                p.sendMessage(plugin.I + "You almost died before the game even started!");
                p.setHealth(20);
                p.setFoodLevel(20);
                plugin.KillStreaks.put(p.getName(), 0);
                p.setFallDistance(0F);
                p.setFoodLevel(20);
                Methods.respawn(p);
                p.setFallDistance(0F);
                e.setCancelled(true);
            }
            else if (!(e.getCause() == DamageCause.ENTITY_ATTACK) && plugin.inGame.contains(p.getName()) && Started == true)
            {
                e.setDamage(0);
                if (plugin.getConfig().getBoolean("Debug")) System.out.println("3");
                Methods.equipZombies(p);
                if (Main.Lasthit.containsKey(p.getName()))
                {
                    Player human = Bukkit.getPlayer(Main.Lasthit.get(p.getName()));
                    Player Killed = p;
                    Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(human, Killed, Infected.playerGetGroup(Killed), Infected.isPlayerHuman(Killed) ? true : false));
                    Methods.stats(human, 1, 0);
                    Methods.rewardPoints(human, "Kill");
                    String kill = Methods.getKillType(Methods.getGroup(human) + "s", human.getName(), Killed.getName());
                    for (Player playing: Bukkit.getServer().getOnlinePlayers())
                        if (plugin.inGame.contains(playing.getName()))
                        {
                            playing.sendMessage(kill);
                        }
                    if (!plugin.KillStreaks.containsKey(human.getName())) plugin.KillStreaks.put(human.getName(), 1);
                    else plugin.KillStreaks.put(human.getName(), plugin.KillStreaks.get(human.getName()) + 1);
                    Files.getPlayers().set("Players." + human.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(human.getName()));
                    if (plugin.KillStreaks.get(human.getName()) > 2)
                        for (Player playing: Bukkit.getServer().getOnlinePlayers())
                            if (plugin.inGame.contains(playing.getName()))
                            {
                                playing.sendMessage(plugin.I + ChatColor.GREEN + human.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + plugin.KillStreaks.get(human.getName()));
                            }
                    if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(plugin.KillStreaks.get(human.getName())))))
                    {
                        String command = null;
                        command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + plugin.KillStreaks.get(human.getName()))).replaceAll("<player>", human.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                    Methods.stats(Killed, 0, 1);
                    if (plugin.KillStreaks.containsKey(Killed.getName()))
                    {
                        if (plugin.KillStreaks.get(Killed.getName()) > Files.getPlayers().getInt("Players." + Killed.getName().toLowerCase() + ".KillStreak"))
                        {
                            Files.getPlayers().set("Players." + Killed.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(Killed.getName()));
                            Files.savePlayers();
                        }
                        plugin.KillStreaks.put(Killed.getName(), 0);
                    }
                    Main.Lasthit.remove(Killed.getName());
                }
                else
                {
                    for (Player playing: Bukkit.getServer().getOnlinePlayers())
                    {
                        if (Infected.isPlayerInGame(playing))
                        {
                            playing.sendMessage(Methods.sendMessage("Game_GotInfected", p, null, null));
                        }
                    }
                }
                p.setHealth(20);
                p.setFallDistance(0F);
                p.setFoodLevel(20);
                Methods.respawn(p);
                p.setFallDistance(0F);
                e.setDamage(0);
                Main.humans.remove(p.getName());
                Main.Lasthit.remove(p.getName());
                if (plugin.humans.size() == 0)
                {
                    Methods.endGame(false);
                }
                else
                {
                    Methods.equipZombies(p);
                    Methods.zombifyPlayer(p);
                }

                if (plugin.Winners.contains(p.getName())) plugin.Winners.remove(p.getName());
                if (plugin.humans.contains(p.getName())) plugin.humans.remove(p.getName());
                if (!plugin.zombies.contains(p.getName())) plugin.zombies.add(p.getName());
            }
        }
    }

    //Player is Damaged, User is Damager
    //When entity is damaged by another entity(As some times the first one doesn't work...
    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageByEntityEvent e)
    {
        if (plugin.getConfig().getBoolean("Debug"))
        {
            System.out.println("PVPhumans: " + plugin.humans.toString());
            System.out.println("PVPzombies: " + plugin.zombies.toString());
            System.out.println("PVPInGame " + plugin.inGame.toString());
        }
        boolean Started = plugin.Booleans.get("Started");
        boolean BeforeGame = plugin.Booleans.get("BeforeGame");
        boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
        if (e.getEntity() instanceof Player)
        {
            if (e.getDamager() instanceof Arrow)
            {
                playeruser = (Player) e.getEntity();
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player)
                {
                    useruser = (Player) arrow.getShooter();
                }
            }
            if (e.getDamager() instanceof Snowball)
            {
                playeruser = (Player) e.getEntity();
                Snowball ball = (Snowball) e.getDamager();
                if (ball.getShooter() instanceof Player)
                {
                    useruser = (Player) ball.getShooter();
                }
            }
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
            {
                playeruser = (Player) e.getEntity();
                useruser = (Player) e.getDamager();
            }
            if (e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow))
                if (plugin.inGame.contains(playeruser.getName()) || plugin.inLobby.contains(playeruser.getName()))
                {
                    if (!plugin.inGame.contains(useruser.getName()))
                        e.setCancelled(true);
                    if (BeforeGame)
                        e.setCancelled(true);
                    else if (BeforeFirstInf)
                        e.setCancelled(true);
                    else if (Started)
                    {
                        if (plugin.humans.contains(playeruser.getName()) && plugin.humans.contains(useruser.getName()))
                            e.setCancelled(true);
                        if (plugin.zombies.contains(playeruser.getName()) && plugin.zombies.contains(useruser.getName()))
                            e.setCancelled(true);
                        ///////////////////////////////////////////////////////////////////////////////////     HUMAN KILLING ZOMBIE
                        //If the attacker is a human, and the attacky is a zombie
                        if (plugin.humans.contains(useruser.getName()) && plugin.zombies.contains(playeruser.getName()))
                        {
                            if (plugin.getConfig().getBoolean("Debug")) System.out.println("4");
                            Player human = useruser;
                            Player zombie = playeruser;
                            if (!Files.getArenas().getBoolean("Arenas." + Main.playingin + "Plain Zombie Survival"))
                            {
                                Main.Lasthit.put(zombie.getName(), human.getName());

                                //If the damage done will kill the zombie
                                if (zombie.getHealth() - e.getDamage() <= 0)
                                {
                                    Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(human, zombie, Infected.playerGetGroup(zombie), false));
                                    if (plugin.getConfig().getBoolean("Debug")) System.out.println("4.1");
                                    e.setDamage(0);

                                    //Get a random killtype then set as string and send it out
                                    String kill = Methods.getKillType("Humans", human.getName(), zombie.getName());
                                    for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                    {
                                        if (plugin.inGame.contains(playing.getName()))
                                            playing.sendMessage(kill);
                                    }
                                    Methods.stats(human, 1, 0);
                                    plugin.KillStreaks.put(human.getName(), plugin.KillStreaks.get(human.getName()) + 1);
                                    Files.getPlayers().set("Players." + human.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(human.getName()));
                                    if (plugin.KillStreaks.get(human.getName()) > 2)
                                        for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                            if (plugin.inGame.contains(playing.getName()))
                                                playing.sendMessage(plugin.I + ChatColor.GREEN + human.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + plugin.KillStreaks.get(human.getName()));
                                    if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(plugin.KillStreaks.get(human.getName())))))
                                    {
                                        String command = null;
                                        command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + plugin.KillStreaks.get(human.getName()))).replaceAll("<player>", human.getName());
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                    }
                                    Methods.stats(zombie, 0, 1);
                                    if (plugin.KillStreaks.containsKey(zombie.getName()))
                                    {
                                        if (plugin.KillStreaks.get(zombie.getName()) > Files.getPlayers().getInt("Players." + zombie.getName().toLowerCase() + ".KillStreak"))
                                        {
                                            Files.getPlayers().set("Players." + zombie.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(zombie.getName()));
                                            Files.savePlayers();
                                        }

                                        plugin.KillStreaks.put(zombie.getName(), 0);
                                    }
                                    Methods.rewardPoints(human, "Kill");
                                    Main.Lasthit.remove(zombie.getName());
                                    //Reset the zombie
                                    Methods.equipZombies(zombie);
                                    zombie.setFoodLevel(20);
                                    zombie.setHealth(20);
                                    zombie.setFallDistance(0F);
                                    if (plugin.getConfig().getBoolean("Debug")) System.out.println("4.3");
                                    Methods.respawn(zombie);
                                    zombie.setFallDistance(0F);
                                    Methods.zombifyPlayer(zombie);
                                }
                            }
                            //////////////////////////////////////////////////////////////////////////           ZOMBIE KILLING HUMAN
                        }
                        else if (plugin.humans.contains(playeruser.getName()) && plugin.zombies.contains(useruser.getName()))
                        {

                            if (plugin.getConfig().getBoolean("Debug")) System.out.println("5");
                            //If the attacker is a zombie and the attacky is a human
                            Player newzombie = playeruser;
                            Player zombie = useruser;
                            Main.Lasthit.put(newzombie.getName(), zombie.getName());
                            if (newzombie.getHealth() - e.getDamage() <= 0)
                            {

                                Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(zombie, newzombie, Infected.playerGetGroup(newzombie), true));
                                e.setDamage(0);
                                plugin.humans.remove(newzombie.getName());
                                plugin.zombies.add(newzombie.getName());
                                Methods.rewardPoints(zombie, "Kill");
                                plugin.Winners.remove(newzombie.getName());
                                Main.Lasthit.remove(newzombie.getName());
                                newzombie.sendMessage(plugin.I + "You have become infected!");
                                newzombie.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 2));
                                Methods.equipZombies(newzombie);
                                newzombie.setHealth(20);
                                newzombie.setFoodLevel(20);
                                Methods.stats(newzombie, 0, 1);
                                Methods.stats(zombie, 1, 0);
                                if (plugin.KillStreaks.get(newzombie.getName()) > Files.getPlayers().getInt("Players." + newzombie.getName().toLowerCase() + ".KillStreak"))
                                {
                                    Files.getPlayers().set("Players." + newzombie.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(newzombie.getName()));
                                    Files.savePlayers();
                                }

                                if (!plugin.KillStreaks.containsKey(zombie.getName()))
                                    plugin.KillStreaks.put(zombie.getName(), Integer.valueOf("0"));
                                plugin.KillStreaks.put(newzombie.getName(), 0);
                                plugin.KillStreaks.put(zombie.getName(), plugin.KillStreaks.get(zombie.getName()) + 1);

                                if (plugin.KillStreaks.get(zombie.getName()) > 2)
                                    for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                        if (plugin.inGame.contains(playing.getName()))
                                            playing.sendMessage(plugin.I + ChatColor.RED + zombie.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + plugin.KillStreaks.get(zombie.getName()));
                                if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(plugin.KillStreaks.get(zombie.getName())))))
                                {
                                    String command = null;
                                    command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + plugin.KillStreaks.get(zombie.getName()))).replaceAll("<player>", zombie.getName());
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                }
                                String kill = Methods.getKillType("Zombies", newzombie.getName(), zombie.getName());
                                for (Player playing: Bukkit.getServer().getOnlinePlayers())
                                {
                                    if (plugin.inGame.contains(playing.getName()))
                                    {
                                        playing.sendMessage(kill);
                                    }
                                    if (plugin.humans.contains(playing.getName())) Methods.rewardPoints(playing, "Survive");

                                }
                                if (plugin.humans.size() == 0)
                                {
                                    Methods.endGame(false);
                                }
                                else
                                {
                                    for (Player zombies: Bukkit.getOnlinePlayers())
                                    {
                                        if (Infected.isPlayerZombie(zombies) && !(zombies == newzombie)) Methods.rewardPoints(zombies, "Zombies Infected");
                                    }
                                    //If New Zombies Tp is true, tp the zombie on infection
                                    if (plugin.getConfig().getBoolean("New Zombies Tp") == true)
                                    {

                                        newzombie.setFallDistance(0F);
                                        Methods.respawn(newzombie);
                                        newzombie.setFallDistance(0F);
                                    }
                                    Methods.zombifyPlayer(newzombie);
                                }
                            }
                        }
                        else
                        {

                            if (plugin.getConfig().getBoolean("Debug")) System.out.println("6");

                            if (plugin.getConfig().getBoolean("Debug"))
                            {
                                System.out.println("humans: " + plugin.humans.toString());
                                System.out.println("zombies: " + plugin.zombies.toString());
                                System.out.println("InGame " + plugin.inGame.toString());
                            }

                            //If the attackers arn't from different groups
                            //act like nothing happened
                            e.setDamage(0);
                            e.setCancelled(true);
                        }
                    }
                }
        }
    }@
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerReThrowEvent(PlayerPickupItemEvent event)
    {
        if (!event.isCancelled())
        {
            if (Files.getGrenades().getBoolean("Use"))
                if (event.getPlayer().hasPermission("Infected.Grenades"))
                    if (item.contains(event.getItem().getUniqueId().toString()))
                    {
                        if (event.getPlayer().getItemInHand().getType() == Material.AIR)
                            event.getItem().setVelocity(event.getPlayer().getEyeLocation().getDirection());
                        event.setCancelled(true);
                    }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   GRENADES
    @
    SuppressWarnings("deprecation")@ EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerThrowEvent(PlayerInteractEvent event)
    {
        if (!event.isCancelled())
        {
            final Player player = event.getPlayer();
            if (Main.inGame.contains(player.getName()) && Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())))
            {
                if (Infected.booleanIsStarted() && Files.getGrenades().getBoolean("Use"))
                {
                    if (!Main.inLobby.contains(player.getName()) && player.hasPermission("Infected.Grenades"))
                    {
                        final int delay = Methods.grenadeGetDelay(player.getItemInHand().getTypeId());
                        final String ItemId = String.valueOf(player.getItemInHand().getTypeId());
                        final Item grenade = event.getPlayer().getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.getMaterial(player.getItemInHand().getTypeId())));
                        grenade.setVelocity(event.getPlayer().getEyeLocation().getDirection());
                        if (Methods.grenadeTakeAfter(player.getItemInHand().getTypeId()))
                        {
                            if (player.getInventory().getItemInHand().getAmount() >= 2) player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                            else player.getInventory().remove(player.getItemInHand());
                        }
                        player.updateInventory();
                        item.add(grenade.getUniqueId().toString());
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                        {@
                            Override
                            public void run()
                            {
                                grenade.getWorld().playEffect(grenade.getLocation(), Effect.SMOKE, 5);
                                for (Player ppl: Bukkit.getServer().getOnlinePlayers())
                                {
                                    if (Methods.isPlayer(ppl) && ((!(Methods.getGroup(ppl) == Methods.getGroup(player))) || (Main.grenades.getBoolean("Damage Self") && ppl == player)) && ppl.getLocation().distance(grenade.getLocation()) < Methods.grenadeGetRange(Integer.valueOf(ItemId)))
                                    {
                                        Methods.grenadeAddPotion(ppl, Integer.valueOf(ItemId));
                                        ppl.playEffect(EntityEffect.HURT);
                                        if (ppl.getHealth() - Methods.grenadeGetDamage(Integer.valueOf(ItemId)) <= 0) Methods.grenadeKill(player, ppl);
                                        else ppl.setHealth(ppl.getHealth() - Methods.grenadeGetDamage(Integer.valueOf(ItemId)));
                                    }
                                }
                                grenade.remove();
                                Location loc = grenade.getLocation();
                                player.getWorld().createExplosion(loc, 0.0F, false);
                                item.remove(grenade.getUniqueId().toString());
                            }
                        }, delay);
                    }
                }
                event.setCancelled(true);
                event.setUseInteractedBlock(Result.DENY);
                event.setUseItemInHand(Result.DENY);
            }
        }
    }@
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        final Player player = event.getPlayer();
        if (Main.inGame.contains(player.getName()))
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {@
                Override
                public void run()
                {
                    player.sendMessage(Main.I + "Apperently i missed a way to die...");
                    player.sendMessage(Main.I + "Inform the author on how you died!");
                    player.sendMessage(Main.I + "http://www.dev.Bukkit.org/Server_Mods/Infected-Core");
                    player.performCommand("Infected Leave");
                    player.performCommand("Infected Join");
                }

            }, 20L);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   SHOP


    @
    SuppressWarnings(
    {
        "deprecation"
    })@ EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBuyEvent(PlayerInteractEvent event)
    {
        if (!event.isCancelled())
        {
            Player player = event.getPlayer();
            if (player.getItemInHand().getType() == Material.BOW)
            {
                event.setUseItemInHand(Result.DEFAULT);
                event.setCancelled(false);
            }
            else
            {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                {
                    Block b = event.getClickedBlock();
                    if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
                    {
                        Sign sign = ((Sign) b.getState());
                        if (sign.getLine(0).contains("[Inf]") && sign.getLine(1).toLowerCase().contains("click to use"))
                        {
                            String command = sign.getLine(2).replaceAll("§6", "");
                            String commandarg = sign.getLine(3).replaceAll("§6", "");
                            event.getPlayer().performCommand("Infected " + command + " " + commandarg);
                        }
                    }
                }
                if (Methods.isInLobby(player) || Methods.isPlayer(player))
                {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    {
                        Block b = event.getClickedBlock();
                        if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
                        {
                            if (Files.getShop().getBoolean("Use"))
                            {
                                int points = Methods.getPoints(player);
                                Sign sign = ((Sign) b.getState());
                                if (sign.getLine(0).contains("[Infected]"))
                                {
                                    String prices = sign.getLine(3).replaceAll("§4Cost: ", "");
                                    int price = Integer.valueOf(prices);
                                    String itemstring = sign.getLine(1).replaceAll("§f", "");

                                    String itemname = null;
                                    short itemdata = 0;
                                    String s = itemstring;
                                    if (s.contains(":"))
                                    {
                                        String[] s1 = s.split(":");
                                        itemname = s1[0];
                                        itemdata = Short.valueOf(s1[1]);
                                    }
                                    else
                                    {
                                        itemname = s;
                                        itemdata = 0;
                                    }
                                    Material im = null;
                                    for (Material item: Material.values())
                                        if (item.toString().equalsIgnoreCase(itemname))
                                        {
                                            im = item;
                                            break;
                                        }
                                    if (im != null)
                                    {
                                        Material item = Material.getMaterial(itemname);
                                        if (player.hasPermission("Infected.Shop") || player.hasPermission("Infected.Shop." + item.getId()))
                                        {
                                            if (price < points + 1)
                                            {
                                                Methods.setPoints(player, points - price);
                                                ItemStack stack = new ItemStack(item);
                                                stack.setDurability(itemdata);
                                                if (!player.getInventory().contains(stack))
                                                {
                                                    player.getInventory().addItem(stack);
                                                    if (Files.getShop().getBoolean("Save Items")) Infected.playerAddToShopInventory(player, stack);
                                                }
                                                else
                                                    player.getInventory().addItem(new ItemStack(item, +1));
                                                player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You have bought a " + item);
                                                if (Files.getShop().getBoolean("Save Items") || Files.getShop().getIntegerList("Save These Items No Matter What").contains(item.getId())) Infected.playerSaveShopInventory(player);
                                            }
                                            else
                                                player.sendMessage(Main.I + "Not enough points!");
                                            Files.savePlayers();
                                            player.updateInventory();
                                        }
                                        else player.sendMessage(Main.I + ChatColor.RED + "You don't have permission to buy this item!");
                                    }
                                    else
                                    {
                                        if (Files.getShop().contains(itemname))
                                        {
                                            ItemStack is = Methods.getItemStack(Files.getShop().getString(itemname));
                                            for (Material items: Material.values())
                                            {
                                                if (items == is.getType())
                                                {
                                                    im = items;
                                                    break;
                                                }
                                            }
                                            if (price < points + 1)
                                            {
                                                if (player.hasPermission("Infected.Shop") || player.hasPermission("Infected.Shop." + is.getTypeId()))
                                                {
                                                    Methods.setPoints(player, points - price);
                                                    ItemMeta i = is.getItemMeta();
                                                    if (!player.getInventory().contains(is))
                                                    {
                                                        i.setDisplayName("§e" + itemname);
                                                        is.setItemMeta(i);
                                                        player.getInventory().addItem(is);
                                                        if ((Files.getShop().getBoolean("Save Items") || Files.getShop().getIntegerList("Save These Items No Matter What").contains(is.getTypeId())) && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId())))) Infected.playerSaveShopInventory(player);
                                                    }
                                                    else
                                                    {
                                                        i.setDisplayName("§e" + itemname);
                                                        is.setItemMeta(i);
                                                        player.getInventory().addItem(is);
                                                    }
                                                    player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You have bought a " + itemname);
                                                    if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId())))) Infected.playerSaveShopInventory(player);
                                                }
                                            }
                                            else
                                            {
                                                player.sendMessage(Main.I + "Not enough points!");
                                            }
                                            Files.savePlayers();
                                            player.updateInventory();
                                            event.setCancelled(true);
                                        }
                                        Location loc = event.getClickedBlock().getLocation();
                                        if (Main.db.isSign(loc))
                                        {
                                            String i = Main.db.getSignsItem(loc);
                                            String itemi = null;
                                            short itemd = 0;
                                            if (i.contains(":"))
                                            {
                                                String[] i1 = i.split(":");
                                                itemi = i1[0];
                                                itemdata = Short.valueOf(i1[1]);
                                            }
                                            else
                                            {
                                                itemi = i;
                                                itemd = 0;
                                            }
                                            Material item = Material.getMaterial(Integer.valueOf(itemi));
                                            if (price < points + 1)
                                            {
                                                Methods.setPoints(player, points - price);
                                                ItemStack stack = new ItemStack(Material.getMaterial(Integer.valueOf(itemi)));
                                                stack.setDurability(itemd);
                                                if (!player.getInventory().contains(stack))
                                                {
                                                    player.getInventory().addItem(stack);
                                                    if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(item.getId())))) Infected.playerSaveShopInventory(player);
                                                }
                                                else
                                                    player.getInventory().addItem(new ItemStack(item, +1));
                                                player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You have bought a " + item);
                                                if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(item.getId())))) Infected.playerSaveShopInventory(player);

                                            }
                                            else
                                                player.sendMessage(Main.I + "Not enough points!");
                                            Files.savePlayers();
                                            player.updateInventory();
                                        }
                                    }
                                }
                            }
                            else if ((event.getClickedBlock().getTypeId() == 330 || event.getClickedBlock().getTypeId() == 96 || event.getClickedBlock().getTypeId() == 324 || event.getClickedBlock().getTypeId() == 69 || event.getClickedBlock().getTypeId() == 77 || event.getClickedBlock().getTypeId() == 143 || event.getClickedBlock().getTypeId() == 147 || event.getClickedBlock().getTypeId() == 148 || event.getClickedBlock().getTypeId() == 70 || event.getClickedBlock().getTypeId() == 72) && !Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())) && !plugin.getConfig().getBoolean("Allow Interacting"))
                            {
                                event.setCancelled(true);
                            }
                        }
                        else if ((event.getClickedBlock().getTypeId() == 330 || event.getClickedBlock().getTypeId() == 96 || event.getClickedBlock().getTypeId() == 324 || event.getClickedBlock().getTypeId() == 69 || event.getClickedBlock().getTypeId() == 77 || event.getClickedBlock().getTypeId() == 143 || event.getClickedBlock().getTypeId() == 147 || event.getClickedBlock().getTypeId() == 148 || event.getClickedBlock().getTypeId() == 70 || event.getClickedBlock().getTypeId() == 72) && !Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())) && !plugin.getConfig().getBoolean("Allow Interacting"))
                        {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

    @
    EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCreateShop(SignChangeEvent event)
    {
        if (!event.isCancelled())
        {
            if (Files.getShop().getBoolean("Use"))
            {
                Player player = event.getPlayer();
                if (event.getLine(0).equalsIgnoreCase("[Inf]"))
                {
                    if (!player.hasPermission("Infected.Setup"))
                    {
                        player.sendMessage(Main.I + "Invalid Permissions.");
                        event.setCancelled(true);
                    }
                    if (event.getLine(1).isEmpty())
                    {
                        event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 2 is empty");
                        event.getBlock().breakNaturally();
                        event.setCancelled(true);
                    }
                    else
                    {
                        if (event.getLine(1).equalsIgnoreCase("Info"))
                        {
                            if (!player.hasPermission("Infected.Setup"))
                            {
                                player.sendMessage(Main.I + "Invalid Permissions.");
                                event.setCancelled(true);
                            }
                            event.setLine(0, ChatColor.DARK_RED + "" + "[Inf]");
                            String status;
                            if (Infected.booleanIsBeforeGame()) status = "Voting";
                            if (Infected.booleanIsBeforeInfected()) status = "B4 Infected";
                            if (Infected.booleanIsStarted()) status = "Started";
                            else status = "In Lobby";
                            int time = Main.currentTime;
                            event.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Infected.listInGame().size()));
                            event.setLine(2, ChatColor.GOLD + status);
                            event.setLine(3, ChatColor.WHITE + "Time: " + ChatColor.YELLOW + String.valueOf(time));
                            Main.db.setInfoSigns(event.getBlock().getLocation());
                        }
                        else if (event.getLine(1).equalsIgnoreCase("Cmd"))
                        {
                            event.setLine(0, ChatColor.DARK_RED + "" + "[Inf]");
                            event.setLine(1, ChatColor.GREEN + "Click to use CMD");
                            event.setLine(2, ChatColor.GOLD + event.getLine(2).toUpperCase());
                            event.setLine(3, ChatColor.GOLD + event.getLine(3).toUpperCase());
                        }
                    }
                }
                else if (event.getLine(0).equalsIgnoreCase("[Infected]"))
                {
                    if (!player.hasPermission("Infected.Setup"))
                    {
                        player.sendMessage(Main.I + "Invalid Permissions.");
                        event.setCancelled(true);
                    }
                    if (event.getLine(1).isEmpty())
                    {
                        event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 2 is empty");
                        event.getBlock().breakNaturally();
                        event.setCancelled(true);
                    }
                    else if (event.getLine(2).isEmpty())
                    {
                        event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 3 is empty!");
                        event.getBlock().breakNaturally();
                        event.setCancelled(true);
                    }
                    else
                    {
                        if (Files.getShop().contains(event.getLine(1)))
                        {
                            String s = Files.getShop().getString(event.getLine(1));
                            Material im = null;
                            ItemStack is = new ItemStack(Methods.getItem(s));
                            for (Material items: Material.values())
                            {
                                if (items == is.getType())
                                {
                                    im = items;
                                    break;
                                }
                            }
                            if (im != null)
                            {
                                try
                                {@
                                    SuppressWarnings("unused")
                                    int amount = Integer.valueOf(event.getLine(2));
                                }
                                catch (NumberFormatException nfe)
                                {
                                    event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Cost must be a number!");
                                    event.getBlock().breakNaturally();
                                    event.setCancelled(true);
                                }


                            }
                            int amount = Integer.valueOf(event.getLine(2));
                            event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
                            event.setLine(1, ChatColor.WHITE + event.getLine(1));
                            event.setLine(2, ChatColor.GREEN + "Click To Buy");
                            event.setLine(3, ChatColor.DARK_RED + "Cost: " + String.valueOf(amount));
                        }
                        else
                        {
                            try
                            {
                                String s = event.getLine(1);
                                String[] s1 = s.split(":");@
                                SuppressWarnings("unused")
                                int itemid = Integer.valueOf(s1[0]);
                            }
                            catch (NumberFormatException nfe)
                            {
                                event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Invalid Item");
                                event.getBlock().breakNaturally();
                                event.setCancelled(true);
                                return;
                            }
                            Material im = null;
                            String itemid = null;
                            String itemdata = null;
                            String s = event.getLine(1);

                            if (s.contains(":"))
                            {
                                String[] s1 = s.split(":");
                                itemid = s1[0];
                                itemdata = s1[1];
                            }
                            else
                            {
                                itemid = s;
                                itemdata = "";
                            }
                            for (Material item: Material.values())
                                if (item.getId() == Integer.valueOf(itemid))
                                {
                                    im = item;
                                    break;
                                }
                            if (im != null)
                            {

                                int amount = Integer.valueOf(event.getLine(2));
                                Material item = Material.getMaterial(Integer.valueOf(itemid));
                                event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
                                event.setLine(1, ChatColor.WHITE + item.name().toUpperCase() + ":" + itemdata);
                                if (itemdata == "")
                                    event.setLine(1, ChatColor.WHITE + item.name().toUpperCase());
                                event.setLine(2, ChatColor.GREEN + "Click To Buy");
                                event.setLine(3, ChatColor.DARK_RED + "Cost: " + String.valueOf(amount));
                                if (itemdata == "")
                                {
                                    itemdata = "0";
                                }
                                Main.db.setSigns(event.getBlock().getLocation(), itemid + ":" + Integer.valueOf(itemdata));
                            }
                        }
                    }
                }

            }
        }
    }
}