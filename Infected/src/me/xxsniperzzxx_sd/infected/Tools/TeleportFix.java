package me.xxsniperzzxx_sd.infected.Tools;
 
import java.util.ArrayList;
import java.util.List;
 
import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
 
//###########################################################################

//TODO: Make it so when a player is disguised it won't hide()/show() them 
//TODO: Make Main.java refer to this instead of old teleport fix

//###########################################################################


public class TeleportFix implements Listener {
    private Server server;
    private Plugin plugin;
    
    private final int TELEPORT_FIX_DELAY = 15; // ticks
    
    public TeleportFix(Plugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
 
        final Player player = event.getPlayer();
        final int visibleDistance = server.getViewDistance() * 16;
        
        // Fix the visibility issue one tick later
        server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                // Refresh nearby clients
                final List<Player> nearby = getPlayersWithin(player, visibleDistance);
                
                // Hide every player
                updateEntities(player, nearby, false);
                
                // Then show them again
                server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        updateEntities(player, nearby, true);
                    }
                }, 1);
            }
        }, TELEPORT_FIX_DELAY);
    }
    
    private void updateEntities(Player tpedPlayer, List<Player> players, boolean visible) {
        // Hide or show every player to tpedPlayer
        // and hide or show tpedPlayer to every player.
        for (Player player : players) {
        	if(Infected.isPlayerInGame(player) || Infected.isPlayerInLobby(player)){
        		if(Main.config.getBoolean("DisguiseCraft Support")){
        			if (visible){
        				if(!Main.dcAPI.isDisguised(player))	tpedPlayer.showPlayer(player);
        				if(!Main.dcAPI.isDisguised(tpedPlayer))	player.showPlayer(tpedPlayer);
        			}else{
        				if(!Main.dcAPI.isDisguised(player))	tpedPlayer.hidePlayer(player);
        				if(!Main.dcAPI.isDisguised(player)) player.hidePlayer(tpedPlayer);
        			}
        		}else{
        			if (visible){
        				tpedPlayer.showPlayer(player);
        				player.showPlayer(tpedPlayer);
        			}else{
        				tpedPlayer.hidePlayer(player);
        				player.hidePlayer(tpedPlayer);
        			}
        		}
        	}
        }
    }
    
    public List<Player> getPlayersWithin(Player player, int distance) {
        List<Player> res = new ArrayList<Player>();
        int d2 = distance * distance;
        for (Player p : server.getOnlinePlayers()) {
        	if(Infected.isPlayerInGame(p) || Infected.isPlayerInLobby(p))
	            if (p != player && p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
	                res.add(p);
	            }
        }
        return res;
    }
}