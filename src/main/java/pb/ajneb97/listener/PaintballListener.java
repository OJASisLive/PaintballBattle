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
import pb.ajneb97.lib.fastboard.FastBoard;

import java.util.*;

public class PaintballListener implements Listener {

    // Map zum Speichern der Boards pro Spieler
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    // ----------- TEAM-Zuweisung (Nametag/Collision) ----------
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
        // Board wird NICHT beim Join erstellt!
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Team verlassen
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("PaintballBattle");
        if (team != null) {
            team.removeEntry(player.getName());
            if (team.getEntries().isEmpty()) {
                team.unregister();
            }
        }

        // Board löschen (falls vorhanden)
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    // ----------- BOARD beim Spielstart anzeigen -----------

    /**
     * Diese Methode beim Spielstart für den Spieler aufrufen!
     * Füge überall die richtigen Werte für die Platzhalter ein.
     */
    public void createGameBoard(
            Player player,
            String status,
            String team1Name, int team1Lives,
            String team2Name, int team2Lives,
            int kills, String arena,
            int currentPlayers, int maxPlayers
    ) {
        List<String> lines = Arrays.asList(
                "&1",
                "&fStatus:",
                "&7" + status,
                "&2",
                "&7&l" + team1Name + " &fLives: &a" + team1Lives,
                "&7&l" + team2Name + " &fLives: &a" + team2Lives,
                "&3",
                "&fYour Kills: &a" + kills,
                "&fMap: &a" + arena,
                "&4",
                "&fPlayers:",
                "&a" + currentPlayers + "&7/&a" + maxPlayers
        );
        // Farben ersetzen
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, ChatColor.translateAlternateColorCodes('&', lines.get(i)));
        }

        FastBoard board = new FastBoard(player);
        board.updateTitle("§aPaintballBattle");
        board.updateLines(lines);
        boards.put(player.getUniqueId(), board);
    }

    /**
     * Diese Methode beim Spielende für den Spieler aufrufen!
     */
    public void removeGameBoard(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    /**
     * Diese Methode immer aufrufen, wenn du das laufende Board aktualisieren willst (z.B. bei Kill, Teamwechsel etc.)!
     */
    public void updateGameBoard(
            Player player,
            String status,
            String team1Name, int team1Lives,
            String team2Name, int team2Lives,
            int kills, String arena,
            int currentPlayers, int maxPlayers
    ) {
        FastBoard board = boards.get(player.getUniqueId());
        if (board != null) {
            List<String> lines = Arrays.asList(
                    "&1",
                    "&fStatus:",
                    "&7" + status,
                    "&2",
                    "&7&l" + team1Name + " &fLives: &a" + team1Lives,
                    "&7&l" + team2Name + " &fLives: &a" + team2Lives,
                    "&3",
                    "&fYour Kills: &a" + kills,
                    "&fMap: &a" + arena,
                    "&4",
                    "&fPlayers:",
                    "&a" + currentPlayers + "&7/&a" + maxPlayers
            );
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, ChatColor.translateAlternateColorCodes('&', lines.get(i)));
            }
            board.updateLines(lines);
        }
    }
}