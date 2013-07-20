package me.xxsniperzzxx_sd.infected.Events;
import me.xxsniperzzxx_sd.infected.Infected;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
public class InfectedPlayerDieEvent extends Event
{
    Player killer;
    Player killed;
    String killedTeam;
    boolean becomeInfected;
    public InfectedPlayerDieEvent(Player killer, Player killed, String killedTeam, boolean becameInfected)
    {
        this.killer = killer;
        this.killed = killed;
        this.killedTeam = Infected.playerGetGroup(killed);
        this.becomeInfected = becameInfected;
    }
    public Player getKilled()
    {
        return this.killed;
    }
    public Player getKiller()
    {
        return this.killer;
    }
    public String getKilledsTeam()
    {
        return this.killedTeam;
    }
    public boolean didKilledBecameInfected()
    {
        return becomeInfected;
    }
    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers()
    {
        return handlers;
    }
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}