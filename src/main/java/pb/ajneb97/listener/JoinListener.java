package pb.ajneb97.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("PaintballBattle");
        if (team == null) team = scoreboard.registerNewTeam("PaintballBattle");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        team.setPrefix(ChatColor.GREEN + "[Paintball] ");
        team.addEntry(player.getName());
        // KEIN FastBoard hier!
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("PaintballBattle");
        if (team != null) {
            team.removeEntry(player.getName());
            if (team.getEntries().isEmpty()) {
                team.unregister();
            }
        }
        // FastBoard-Entfernung NICHT hier, sondern im Game/Arena-Handler!
    }
}