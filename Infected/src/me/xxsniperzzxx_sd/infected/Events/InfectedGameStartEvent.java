package me.xxsniperzzxx_sd.infected.Events;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
public class InfectedGameStartEvent extends Event
{
    ArrayList < String > players;
    Player alpha;
    int Time;
    public InfectedGameStartEvent(ArrayList < String > inGame, int time, Player alpha)
    {
        this.players = inGame;
        this.Time = time;
        this.alpha = alpha;
    }
    public ArrayList < String > getPlayers()
    {
        return players;
    }
    public Player getAlpha()
    {
        return alpha;
    }
    public int getTimeLeft()
    {
        return Time;
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