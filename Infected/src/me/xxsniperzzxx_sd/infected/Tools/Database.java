package me.xxsniperzzxx_sd.infected.Tools;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
public class Database
{
    private HashMap < Location, Material > Blocks = new HashMap < Location, Material > ();
    private HashMap < String, String > shopSigns = new HashMap < String, String > ();
    private HashMap < String, Integer > infoSigns = new HashMap < String, Integer > ();
    private HashMap < String, ItemStack[] > Backup = new HashMap < String, ItemStack[] > ();
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private ObjectInputStream oin;
    //Subroutines
    public Boolean saveDB(String filename)
    {
        try
        {
            fos = new FileOutputStream(filename);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            return false;
        }
        try
        {
            oos = new ObjectOutputStream(fos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        try
        {
            oos.writeObject(Backup);
            oos.writeObject(shopSigns);
            oos.writeObject(infoSigns);
            oos.writeObject(Blocks);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }@
    SuppressWarnings("unchecked")
    public Boolean loadDB(String filename)
    {
        try
        {
            fis = new FileInputStream(filename);
        }
        catch (FileNotFoundException ex)
        {
            createDB(filename);
            return true;
        }
        try
        {
            oin = new ObjectInputStream(fis);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return false;
        }
        try
        {
            Backup.putAll((Map <? extends String, ? extends ItemStack[] > ) oin.readObject());
            shopSigns.putAll((Map <? extends String, ? extends String > ) oin.readObject());
            infoSigns.putAll((Map <? extends String, ? extends Integer > ) oin.readObject());
            Blocks.putAll((Map <? extends Location, ? extends Material > ) oin.readObject());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public void setBackup(String key, ItemStack[] inventory)
    {
        Backup.put(key, inventory);
    }
    public void delBackup(String playername)
    {
        Backup.remove(playername);
    }
    public ItemStack[] getBackup(String playername)
    {
        return Backup.get(playername);
    }
    public boolean createDB(String filename)
    {
        System.out.println("Creating new database...");
        try
        {
            fos = new FileOutputStream(filename);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            return false;
        }
        try
        {
            oos = new ObjectOutputStream(fos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        try
        {
            oos.writeObject(Backup);
            oos.writeObject(Blocks);
            oos.writeObject(shopSigns);
            oos.writeObject(infoSigns);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public Boolean hasBackup(String playername)
    {
        if (Backup.get(playername) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public HashMap < String, ItemStack[] > getBackups()
    {
        return Backup;
    }
    public HashMap < Location, Material > getBlocks()
    {
        return Blocks;
    }
    public HashMap < String, String > getSigns()
    {
        return shopSigns;
    }
    public HashMap < String, Integer > getInfoSigns()
    {
        return infoSigns;
    }
    public void setSigns(Location loc, String Item)
    {
        String s = String.valueOf(loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        getSigns().put(s, Item);
    }
    public String getSignsItem(Location loc)
    {
        String s = String.valueOf(loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        return getSigns().get(s);
    }
    public void removeSign(Location loc)
    {
        String s = String.valueOf(loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        getSigns().remove(s);
    }
    public boolean isSign(Location loc)
    {
        String s = String.valueOf(loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        return getSigns().containsKey(s);
    }
    //###################################################################################################################################################
    public void setInfoSigns(Location loc)
    {
        String s = String.valueOf(loc.getWorld().getName() + "," + loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        getInfoSigns().put(s, 1);
    }
    public void removeInfoSignLoc(Location loc)
    {
        String s = String.valueOf(loc.getWorld().getName() + "," + loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        getInfoSigns().remove(s);
    }
    public void removeInfoSign(String stringLoc)
    {
        getInfoSigns().remove(stringLoc);
    }
    public Sign getInfoSign(String stringLoc)
    {
        Sign sign = null;
        String[] ss = stringLoc.split(",");
        Location loc = new Location(Bukkit.getWorld(ss[0]), Double.parseDouble(ss[1]), Double.parseDouble(ss[2]), Double.parseDouble(ss[3]));
        Block b = Bukkit.getWorld(ss[0]).getBlockAt(loc);
        if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
        {
            sign = ((Sign) b.getState());
        }
        else
        {
            removeInfoSign(stringLoc);
        }
        if (sign == null) System.out.println("Not a sign");
        return sign;
    }
    public boolean isInfoSign(Location loc)
    {
        String s = String.valueOf(loc.getWorld().getName() + "," + loc.getX()) + "," + String.valueOf(loc.getY()) + "," + String.valueOf(loc.getZ());
        return getInfoSigns().containsKey(s);
    }
}