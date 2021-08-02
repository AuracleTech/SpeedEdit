package auracle;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Functions {
    static CoreProtectAPI CPapi = SpeedEdit.CPapi;
    static boolean isCPapi = CPapi != null;

    public static void changeBlock(Player player, Location location, BlockData blockData) {
        if (isCPapi)
            CPapi.logRemoval("#SE@" + player.getName(), location, location.getBlock().getType(), location.getBlock().getBlockData());
        location.getBlock().setBlockData(blockData);
        if (isCPapi)
            CPapi.logPlacement("#SE@" + player.getName(), location, location.getBlock().getType(), location.getBlock().getBlockData());
    }

    public static int manipulateBlocks(Player player, Material fromMat, Material toMat, ErrorManagement EM) {
        UserData userData = SpeedEdit.getUser(player);
        List<Location> newList = new ArrayList<>();
        if (toMat == null) {
            for (Location location : userData.SelectedZone)
                if (location.getBlock().getType() != Material.AIR) newList.add(location);
            toMat = fromMat;
        } else
            for (Location location : userData.SelectedZone)
                if (location.getBlock().getType() == fromMat) newList.add(location);
        manipulateBlocks(player, newList, toMat.createBlockData(), EM);
        return newList.size();
    }

    public static void manipulateBlocks(Player player, List<Location> locations, BlockData blockData, ErrorManagement EM) {
        UserData userData = SpeedEdit.getUser(player);
        HashMap<Location, BlockData> memory = new HashMap<>();
        for (Location location : locations) {
            memory.put(location, location.getBlock().getBlockData());
            changeBlock(player, location, blockData);
        }
        userData.clearRedo = true;
        userData.addUndo(memory);
    }

    public static void pasteBlocks(Player player, UserData userData) {
        HashMap<Location, BlockData> memory = new HashMap<>();
        for (Entry<Vector, BlockData> entry : userData.clipboard.entrySet()) {
            Location location = player.getLocation().add(entry.getKey());
            memory.put(location, location.getBlock().getBlockData());
            changeBlock(player, location, entry.getValue());
        }
        userData.clearRedo = true;
        userData.addUndo(memory);
    }

    public static void moveBlocks(Player player, Vector vector, String directionTexte, int Distance) {
        UserData userData = SpeedEdit.getUser(player);
        HashMap<Location, BlockData> memory = new HashMap<>();
        for (Location location : userData.SelectedZone) {
            memory.put(location, location.getBlock().getBlockData());
            changeBlock(player, location, Material.AIR.createBlockData());
        }
        HashMap<Location, BlockData> memoryClone = new HashMap<>(memory);
        for (Entry<Location, BlockData> entry : memoryClone.entrySet()) {
            if (!memory.containsKey(entry.getKey().clone().add(vector).getBlock().getLocation()))
                memory.put(entry.getKey().clone().add(vector).getBlock().getLocation(), entry.getKey().clone().add(vector).getBlock().getBlockData());
            changeBlock(player, entry.getKey().clone().add(vector), entry.getValue());
        }
        userData.clearRedo = true;
        userData.addUndo(memory);
    }

    public static void undo(Player player, int undoQtt) {
        UserData userData = SpeedEdit.getUser(player);
        for (int i = 1; i <= undoQtt; i++) {
            HashMap<Location, BlockData> memory = new HashMap<>();
            HashMap<Location, BlockData> locAndBlockDatas = userData.undo.get(userData.undo.size() - 1);
            for (Entry<Location, BlockData> locMat : locAndBlockDatas.entrySet()) {
                memory.put(locMat.getKey(), locMat.getKey().getBlock().getBlockData());
                changeBlock(player, locMat.getKey(), locMat.getValue());
            }
            userData.addRedo(memory);
            userData.undo.remove(userData.undo.size() - 1);
        }
    }

    public static void redo(Player player, int redoQtt) {
        UserData userData = SpeedEdit.getUser(player);
        for (int i = 1; i <= redoQtt; i++) {
            HashMap<Location, BlockData> memory = new HashMap<>();
            HashMap<Location, BlockData> locAndBlockDatas = userData.redo.get(userData.redo.size() - 1);
            for (Entry<Location, BlockData> locMat : locAndBlockDatas.entrySet()) {
                memory.put(locMat.getKey(), locMat.getKey().getBlock().getBlockData());
                changeBlock(player, locMat.getKey(), locMat.getValue());
            }
            userData.addUndo(memory);
            userData.redo.remove(userData.redo.size() - 1);
        }
    }

    static List<Block> getBlocksInZone(List<Location> locations) {
        List<Block> blocks = new ArrayList<>();
        for (Location location : locations) blocks.add(location.getBlock());
        return blocks;
    }

    static List<Location> getLocationsInSphere(String pattern, Location origin, int radius) {
        World world = origin.getWorld();
        List<Location> locations = new ArrayList<>();
        switch (pattern) {
            case "sphere":
                for (double Y = -radius; Y <= radius; Y++)
                    for (double X = -radius; X <= radius; X++)
                        for (double Z = -radius; Z <= radius; Z++)
                            if (Math.round(Math.sqrt((X * X) + (Y * Y) + (Z * Z))) <= radius)
                                locations.add(world.getBlockAt((int) X + origin.getBlockX(), (int) Y + origin.getBlockY(), (int) Z + origin.getBlockZ()).getLocation());
                break;
            case "hsphere":
                for (double Y = -radius; Y <= radius; Y++)
                    for (double X = -radius; X <= radius; X++)
                        for (double Z = -radius; Z <= radius; Z++)
                            if (Math.round(Math.sqrt((X * X) + (Y * Y) + (Z * Z))) == radius)
                                locations.add(world.getBlockAt((int) X + origin.getBlockX(), (int) Y + origin.getBlockY(), (int) Z + origin.getBlockZ()).getLocation());
                break;
        }
        return locations;
    }

    static List<Location> getLocationsInZone(String pattern, Location pos1, Location pos2, Object... params) {
        World world = pos1.getWorld();
        List<Location> locations = new ArrayList<>();
        int topBlockX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int bottomBlockX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int topBlockY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int bottomBlockY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int topBlockZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        int bottomBlockZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        switch (pattern) {
            case "walls":
                for (int x = bottomBlockX; x <= topBlockX; x++)
                    for (int z = bottomBlockZ; z <= topBlockZ; z++)
                        for (int y = bottomBlockY; y <= topBlockY; y++)
                            if (x == pos1.getX() || z == pos1.getZ() || x == pos2.getX() || z == pos2.getZ())
                                locations.add(new Location(world, x, y, z));
                break;
            case "outline":
                for (int x = bottomBlockX; x <= topBlockX; x++)
                    for (int z = bottomBlockZ; z <= topBlockZ; z++)
                        for (int y = bottomBlockY; y <= topBlockY; y++)
                            if (x == pos1.getX() || z == pos1.getZ() || y == pos1.getY() || x == pos2.getX() || z == pos2.getZ() || y == pos2.getY())
                                locations.add(new Location(world, x, y, z));
                break;
            case "skeleton":
                for (int x = bottomBlockX; x <= topBlockX; x++)
                    for (int z = bottomBlockZ; z <= topBlockZ; z++)
                        for (int y = bottomBlockY; y <= topBlockY; y++)
                            if ((x == pos1.getX() && y == pos1.getY()) || (y == pos1.getY() && z == pos1.getZ()) || (x == pos1.getX() && z == pos1.getZ()) || (x == pos2.getX() && y == pos2.getY()) || (y == pos2.getY() && z == pos2.getZ()) || (x == pos2.getX() && z == pos2.getZ()) || (x == pos2.getX() && y == pos1.getY()) || (y == pos2.getY() && z == pos1.getZ()) || (x == pos2.getX() && z == pos1.getZ()) || (x == pos1.getX() && y == pos2.getY()) || (y == pos1.getY() && z == pos2.getZ()) || (x == pos1.getX() && z == pos2.getZ()))
                                locations.add(new Location(world, x, y, z));
                break;
            default:
                for (int x = bottomBlockX; x <= topBlockX; x++)
                    for (int z = bottomBlockZ; z <= topBlockZ; z++)
                        for (int y = bottomBlockY; y <= topBlockY; y++)
                            locations.add(new Location(world, x, y, z));
                break;
        }
        return locations;
    }

    public static String getCardinalDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) yaw += 360;
        if (yaw >= 315 || yaw < 45)
            return "SOUTH";
        else if (yaw < 135)
            return "WEST";
        else if (yaw < 225)
            return "NORTH";
        else if (yaw < 315)
            return "EAST";
        return "NORTH";
    }

    public static String getPitch(Player player) {
        float pitch = player.getLocation().getPitch();
        if (pitch >= 45)
            return "DOWN";
        else if (pitch <= -45)
            return "UP";
        return "";
    }

    public static int zoneCount(Location pos1, Location pos2) {
        int topBlockX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int bottomBlockX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int topBlockY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int bottomBlockY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int topBlockZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        int bottomBlockZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        return ((1 + topBlockX - bottomBlockX) * (1 + topBlockY - bottomBlockY) * (1 + topBlockZ - bottomBlockZ));
    }

    public static void logBlocks(Player player, List<Location> selectedZone) {
        for (Location location : selectedZone)
            if (isCPapi)
                CPapi.logPlacement("#SE@" + player.getName(), location, location.getBlock().getType(), location.getBlock().getBlockData());
    }
}