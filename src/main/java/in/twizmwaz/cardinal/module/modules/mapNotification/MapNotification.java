package in.twizmwaz.cardinal.module.modules.mapNotification;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.List;

public class MapNotification implements TaskedModule {

    private long startTime;
    private int nextMessage;

    protected MapNotification() {
        this.nextMessage = 600;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * @return The current time stored in the module.
     */

    public double getTimeInSeconds() {
        return ((double) System.currentTimeMillis() - startTime) / 1000.0;
    }

    @Override
    public void run() {
        if (getTimeInSeconds() >= this.nextMessage) {
            LoadedMap map = GameHandler.getGameHandler().getMatch().getLoadedMap();
            String result = "";
            List<Pair<String, String>> authors =map.getAuthors();
            for (Pair<String, String> author : authors) {
                if (authors.indexOf(author) < authors.size() - 2) {
                    result = result + org.bukkit.ChatColor.RED + author.getLeft() + org.bukkit.ChatColor.DARK_PURPLE + ", ";
                } else if (authors.indexOf(author) == authors.size() - 2) {
                    result = result + org.bukkit.ChatColor.RED + author.getLeft() + org.bukkit.ChatColor.DARK_PURPLE + " and ";
                } else if (authors.indexOf(author) == authors.size() - 1) {
                    result = result + org.bukkit.ChatColor.RED + author.getLeft();
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Currently playing " + ChatColor.GOLD + map.getName() + ChatColor.DARK_PURPLE + " by " + result);
            this.nextMessage += 600;
        }
    }
}
