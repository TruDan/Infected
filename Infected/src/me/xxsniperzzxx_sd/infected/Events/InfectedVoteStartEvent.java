package me.xxsniperzzxx_sd.infected.Events;
import java.util.ArrayList;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
public class InfectedVoteStartEvent extends Event
{
    ArrayList < String > voting;
    int voteTimeLeft;
    public InfectedVoteStartEvent(ArrayList < String > voting, int time)
    {
        this.voting = voting;
        this.voteTimeLeft = time;
    }
    public ArrayList < String > getVotingPlayers()
    {
        return voting;
    }
    public int getVoteTimeLeft()
    {
        return voteTimeLeft;
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