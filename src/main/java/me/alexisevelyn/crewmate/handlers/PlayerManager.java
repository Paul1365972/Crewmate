package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.api.Player;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    public static final HashMap<Integer, Player> playersByID = new HashMap<>();
    public static final HashMap<String, Player> playersByAddress = new HashMap<>();

    public static void addPlayer(Player player) {
        LogHelper.printLine(String.format(Main.getTranslation("adding_player"), player.getName()));
        removePlayer(player);

        playersByID.put(player.getID(), player);
        playersByAddress.put(player.getAddress() + ":" + player.getPort(), player);
        LogHelper.printLine(String.format(Main.getTranslation("added_player"), player.getName()));
    }

    public static void removePlayer(Player player) {
        removePlayer(player.getAddress(), player.getPort());
    }

    public static void removePlayer(InetAddress address, int port) {
        if (existsWithAddress(address, port)) {
            for (Map.Entry<Integer, Player> playerEntry : playersByID.entrySet()) {
                Player foundPlayer = playerEntry.getValue();

                if (foundPlayer.getAddress().equals(address) && foundPlayer.getPort() == port) {
                    playersByID.remove(playerEntry.getKey(), foundPlayer);
                    playersByAddress.remove(address + ":" + port, foundPlayer);
                    removeFromGames(foundPlayer);

                    LogHelper.printLine("Removed player: " + foundPlayer.getName());
                    break;
                }
            }
        }
    }

    /**
     * @see PlayerManager#removePlayer(InetAddress, int)
     * @see PlayerManager#removePlayer(Player)
     * @param id
     */
    @Deprecated
    public static void removePlayer(int id) {
        if (existsWithID(id)) {
            for (Map.Entry<String, Player> playerEntry : playersByAddress.entrySet()) {
                Player foundPlayer = playerEntry.getValue();

                if (foundPlayer.getID() == id) {
                    playersByAddress.remove(playerEntry.getKey(), foundPlayer);
                    playersByID.remove(id, foundPlayer);
                    removeFromGames(foundPlayer);

                    LogHelper.printLine(String.format(Main.getTranslation("removed_player"), foundPlayer.getName()));
                    break;
                }
            }
        }
    }

    public static void removeFromGames(Player player) {
        GameManager.removePlayer(player);
    }

    public static boolean existsWithID(int id) {
        return playersByID.containsKey(id);
    }

    private static boolean existsWithAddress(String address) {
        return playersByAddress.containsKey(address);
    }

    public static boolean existsWithAddress(InetAddress address, int port) {
        return existsWithAddress(address.getHostAddress() + ":" + port);
    }

    public static Player getPlayerByAddress(InetAddress address, int port) {
        return playersByAddress.get(address + ":" + port);
    }

    /**
     * @see PlayerManager#getPlayerByAddress(InetAddress, int)
     * @param id
     * @return Player
     */
    @Deprecated
    public static Player getPlayerById(int id) {
        return playersByID.get(id);
    }

    public static Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(playersByAddress.values());
    }
}
