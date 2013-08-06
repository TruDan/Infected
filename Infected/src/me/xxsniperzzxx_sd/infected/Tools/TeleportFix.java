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
        // Fix the visibility issue one tick later
        server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                // Refresh nearby clients
                final List<Player> nearby = getPlayersInInfected();
                
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
    
    
    public void updateEntities(Player tpedPlayer, List<Player> players, boolean visible) {
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
    
    public List<Player> getPlayersInInfected() {
        List<Player> res = new ArrayList<Player>();
        for (Player p : server.getOnlinePlayers()) 
        	if(Infected.isPlayerInGame(p) || Infected.isPlayerInLobby(p))
	              res.add(p);
        return res;
    }
}