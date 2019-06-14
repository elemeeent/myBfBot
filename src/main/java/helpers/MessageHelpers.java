package helpers;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import pojo.lastMaps.DataReportsRequest;
import pojo.lastMaps.Report;
import pojo.mapReport.DataMapRequest;
import pojo.profileStats.DataStatsRequest;

import static dataParser.JsonParseHelper.parseMapStatsToString;
import static dataParser.JsonParseHelper.parsePlayerStatsToString;
import static requests.BattlefieldStatsRequest.*;

@Slf4j
public class MessageHelpers {

    public static void sendHello(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            User author = event.getMessage().getAuthor();
            log.info("{} says hello.", author.getName());
            event
                    .getChannel()
                    .sendMessage(
                            "\nHello " + author.getName() +
                                    "\nType '/help' for get command list..."
                    )
                    .queue();
        }
    }

    public static void sendHelp(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            User author = event.getMessage().getAuthor();
            log.info("{} requests help.", author.getName());
            event.
                    getChannel()
                    .sendMessage(
                            "\nCurrent commands:" +
                                    "\n1) type '/stats %playerName%' to get his statistics" +
                                    "\n2) type '/compare %playerName% %playerName' to compare 2 players (in develop)"
                    )
                    .queue();
        }
    }

    public static void sendPlayerStats(GuildMessageReceivedEvent event) {
        User author = event.getMessage().getAuthor();
        String playerName = event.getMessage().getContentRaw().substring(7);
        log.info("{} requests stats for {}", author.getName(), playerName);

        Response stats = getStats(playerName);

        if (stats.getStatusCode() != HttpStatus.SC_OK) {
            Object path = stats.then().extract().path("errors.message");
            event.getChannel().sendMessage("\nRequest error: " + path.toString()).queue();
            return;
        }

        if (stats.getStatusCode() == HttpStatus.SC_OK) {
            event.getChannel().sendMessage("\nPlayer '" + playerName + "' found. Gathering data").queue();
        }

        if (stats.getStatusCode() == HttpStatus.SC_OK) {
            DataStatsRequest playerDataStats = stats.as(DataStatsRequest.class);
            StringBuilder stringBuilder = parsePlayerStatsToString(playerDataStats);
            event.getChannel().sendMessage("\nStats for " + playerName + "\n" + stringBuilder).queue();
        }
    }

    public static void sendPlayerLastMaps(GuildMessageReceivedEvent event) {
        User author = event.getMessage().getAuthor();
        String playerName = event.getMessage().getContentRaw().substring(7);
        log.info("{} requests map stats for {}", author.getName(), playerName);

        Response lastGames = getLastGames(playerName);

        if (lastGames.getStatusCode() != HttpStatus.SC_OK) {
            event.getChannel().sendMessage("\nError while request maps stats for player: " + playerName).queue();
            return;
        }

        if (lastGames.getStatusCode() == HttpStatus.SC_OK) {
            event.getChannel().sendMessage("\nPlayer '" + playerName + "' found. Gathering data").queue();
        }

        if (lastGames.getStatusCode() == HttpStatus.SC_OK) {
            StringBuilder stringBuilder = null;
            DataReportsRequest lastGamesData = lastGames.as(DataReportsRequest.class);
            Report[] reports = lastGamesData.getData().getReports();

            if (reports.length >= 3) {
                for (int i = 0; i < 3; i++) {
                    stringBuilder = playerLastMapsHandler(playerName, stringBuilder, reports[i]);
                }
            } else {
                for (int i = 0; i < reports.length; i++) {
                    stringBuilder = playerLastMapsHandler(playerName, stringBuilder, reports[i]);
                }
            }

            event.getChannel().sendMessage("\nStats for " + playerName + "\n" + stringBuilder).queue();
        }
    }

    public static void sendError(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            User author = event.getMessage().getAuthor();
            log.info("{} says hello.", author.getName());
            event
                    .getChannel()
                    .sendMessage(
                            "\nHello " + author.getName() +
                                    "\nDon't know command you send." +
                                    "\nPlease send '//help' for get command list"
                    )
                    .queue();
        }
    }

    @NotNull
    private static StringBuilder playerLastMapsHandler(String playerName, StringBuilder stringBuilder, Report report) {
        String gameReportId = report.getGameReportId();
        Response gameReport = getGameReport(gameReportId);
        DataMapRequest dataMapRequest = gameReport.as(DataMapRequest.class);
        System.out.println(report.toString());
//        try {
//            if (report.getMode().getName() != null && report.getMap().getName() != null) {
//                stringBuilder
//                        .append("\n")
//                        .append(report.getMode().getName())
//                        .append(": ")
//                        .append(report.getMap().getName())
//                        .append("\n");
//            }
        stringBuilder.append(parseMapStatsToString(dataMapRequest, playerName));
//        } catch (NullPointerException e) {
//            log.error("Error parse player last maps stats.", e);
//            stringBuilder = new StringBuilder("Error at gathering player last maps stats");
//        }
        return stringBuilder;
    }

    public static void main(String[] args) {
        StringBuilder stringBuilder = null;
        String playerName = "ne_variik";
        Response lastGames = getLastGames(playerName);

        if (lastGames.getStatusCode() == HttpStatus.SC_OK) {
            DataReportsRequest lastGamesData = lastGames.as(DataReportsRequest.class);
            Report[] reports = lastGamesData.getData().getReports();

            if (reports.length >= 2) {
                for (int i = 0; i < 2; i++) {
                    stringBuilder = playerLastMapsHandler(playerName, stringBuilder, reports[i]);
                }
            } else {
                for (int i = 0; i < reports.length; i++) {
                    stringBuilder = playerLastMapsHandler(playerName, stringBuilder, reports[i]);
                }
            }
        }
        System.out.println(stringBuilder);
    }

}
