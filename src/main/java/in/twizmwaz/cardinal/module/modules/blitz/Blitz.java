package in.twizmwaz.cardinal.module.modules.blitz;


import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.ScoreboardUpdateEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unchecked")
public class Blitz implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    private final JavaPlugin plugin;

    private String title = null;
    private boolean broadcastLives;
    private int lives;
    private int time;

    protected Blitz(final String title, final boolean broadcastLives, final int lives, final int time) {
        this.title = title;
        this.broadcastLives = broadcastLives;
        this.lives = lives;
        this.time = time;

        this.plugin = GameHandler.getGameHandler().getPlugin();
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        if (team != null && !team.isObserver()) {
            int oldMeta = this.getLives(player);
            player.removeMetadata("lives", plugin);
            player.setMetadata("lives", new LazyMetadataValue(plugin, LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(oldMeta - 1)));
            if (this.getLives(player) == 0) {
                TeamUtils.getTeamById("observers").add(player, true);
                player.removeMetadata("lives", plugin);
            }
        }
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            Player player = event.getPlayer();
            if (TeamUtils.getTeamByPlayer(player) != null) {
                if (!TeamUtils.getTeamByPlayer(player).isObserver()) {
                    if (!player.hasMetadata("lives")) {
                        player.setMetadata("lives", new LazyMetadataValue(plugin, LazyMetadataValue.CacheStrategy.NEVER_CACHE, new BlitzLives(this.lives)));
                    }
                    if (this.broadcastLives) {
                        int lives = this.getLives(player);
                        player.sendMessage(ChatColor.RED + "You have " + ChatColor.AQUA + "" + ChatColor.BOLD + lives + " " + (lives == 1 ? "life" : "lives") + ChatColor.RED + " remaining.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent());
    }

    private int getLives(Player player){
        return player.getMetadata("lives").get(0).asInt();
    }

    public String getTitle(){
        return this.title;
    }

    public int getTime() {
        return time;
    }

    public static boolean matchIsBlitz() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class) != null;
    }

    public static int getTimeLimit() {
        int time = 0;
        for (Blitz blitz : GameHandler.getGameHandler().getMatch().getModules().getModules(Blitz.class)) {
            time = blitz.getTime();
        }
        return time;
    }

}
