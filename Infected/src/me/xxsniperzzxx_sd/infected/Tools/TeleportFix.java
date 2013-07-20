package me.xxsniperzzxx_sd.infected.Tools;
import java.util.ArrayList;
import java.util.List;
import me.xxsniperzzxx_sd.infected.Main;
import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.EntityTracker;
import net.minecraft.server.v1_6_R2.EntityTrackerEntry;
import net.minecraft.server.v1_6_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
public class TeleportFix implements Listener
{
    public Main plugin;
    private Server server;
    private final int TELEPORT_FIX_DELAY = 20; // ticks
    public TeleportFix(Main plugin, Server server)
    {
        this.plugin = plugin;
        this.server = server;
    }@
    EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        final Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("Teleport Bug Fix"))
        {
            if (player.isOnline() && (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName())))
            {
                final int visibleDistance = server.getViewDistance() * 16;
                // Fix the visibility issue one tick later
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                {@
                    Override
                    public void run()
                    {
                        // Refresh nearby clients
                        updateEntities(getPlayersWithin(player, visibleDistance));
                    }
                }, TELEPORT_FIX_DELAY);
            }
        }
    }
    public void updateEntities(List < Player > observers)
    {
        // Refresh every single player
        for (Player player: observers)
        {
            updateEntity(player, observers);
        }
    }@
    SuppressWarnings("unchecked")
    public void updateEntity(Entity entity, List < Player > observers)
    {
        World world = entity.getWorld();
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        EntityTracker tracker = worldServer.tracker;
        EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntities
            .get(entity.getEntityId());
        List < EntityPlayer > nmsPlayers = getNmsPlayers(observers);
        // Force Minecraft to resend packets to the affected clients
        if (!entry.trackedPlayers.isEmpty())
        {
            entry.trackedPlayers.removeAll(nmsPlayers);
            entry.scanPlayers(nmsPlayers);
        }
    }
    private List < EntityPlayer > getNmsPlayers(List < Player > players)
    {
        List < EntityPlayer > nsmPlayers = new ArrayList < EntityPlayer > ();
        for (Player bukkitPlayer: players)
        {
            CraftPlayer craftPlayer = (CraftPlayer) bukkitPlayer;
            nsmPlayers.add(craftPlayer.getHandle());
        }
        return nsmPlayers;
    }
    private List < Player > getPlayersWithin(Player player, int distance)
    {
        List < Player > res = new ArrayList < Player > ();
        int d2 = distance * distance;
        for (Player p: server.getOnlinePlayers())
        {
            if ((!Main.dcAPI.isDisguised(p)) && p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2)
            {
                res.add(p);
            }
        }
        return res;
    }
}