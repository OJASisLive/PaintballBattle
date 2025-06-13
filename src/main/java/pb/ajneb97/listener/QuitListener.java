package pb.ajneb97.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import pb.ajneb97.lib.fastboard.FastBoard;

import java.util.Map;
import java.util.UUID;

public class QuitListener implements Listener {

    // Referenz auf die Boards-Map aus deiner Hauptklasse (über Konstruktor reinreichen!)
    private final Map<UUID, FastBoard> boards;

    public QuitListener(Map<UUID, FastBoard> boards) {
        this.boards = boards;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Paintball-Team verlassen
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("PaintballBattle");
        if (team != null) {
            team.removeEntry(player.getName());
            // Optional: Team aufräumen, falls leer
            if (team.getEntries().isEmpty()) {
                team.unregister();
            }
        }

        // FastBoard Scoreboard entfernen, falls vorhanden
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }
}