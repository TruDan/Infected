package me.xxsniperzzxx_sd.infected.Events;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
public class InfectedPlayerJoinEvent extends Event
{
    Player player;
    ArrayList < String > playersInLobby;
    int requiredPlayers;
    public InfectedPlayerJoinEvent(Player player, ArrayList < String > inLobby, int requiredPlayers)
    {
        this.player = player;
        this.playersInLobby = inLobby;
        this.requiredPlayers = requiredPlayers;
    }
    public Player getPlayer()
    {
        return player;
    }
    public int getPRequiredPlayersForAutoStart()
    {
        return requiredPlayers;
    }
    public ArrayList < String > getPlayersInLobby()
    {
        return playersInLobby;
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