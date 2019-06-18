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
import pojo.profileStats.ProfileDataRequest;

import static dataParser.JsonParseHelper.parseMapStatsToString;
import static dataParser.JsonParseHelper.parsePlayerProfileStatsToString;
import static requests.BattlefieldStatsRequest.*;

@Slf4j
public class MessageHelpers {

    public static void sendHello(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            User author = event.getMessage().getAuthor();
            log.info("{} says hello.", author.getName());
            event
                    .getChannel()
                    .sendMessage("\nHello " + author.getName() +
                            "\nType `//help` for get command list...")
                    .queue();
        }
    }

    public static void sendHelp(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            User author = event.getMessage().getAuthor();
            log.info("{} requests help.", author.getName());
            event.
                    getChannel()
                    .sendMessage("\nCurrent commands:" +
                            "\n1) type `//stats %playerName%` to get his statistics" +
                            "\n2) type `//me` to get your profile stats" +
                            "\n\nExamples: " +
                            "\n`//stats i_am_prooo_tv`" +
                            "\n`//me`")
                    .queue();
        }
    }

    public static void sendPlayerStats(GuildMessageReceivedEvent event) {
        String playerName = "";
        if (event.getMessage().getContentRaw().startsWith("//me")) {
            playerName = event.getMessage().getMember().getNickname();
        }
        if (event.getMessage().getContentRaw().startsWith("//stats ")) {
            String contentRaw = event.getMessage().getContentRaw();
            String[] split = contentRaw.split("//stats ");
            playerName = split[1];
        }
        User author = event.getMessage().getAuthor();
        log.info("{} requests stats for {}", author.getName(), playerName);

        Response stats = getProfileStats(playerName);

        if (stats.getStatusCode() != HttpStatus.SC_OK) {
            Object path = stats.then().extract().path("errors.message");
            event
                    .getChannel()
                    .sendMessage("\nRequest error: " + path.toString())
                    .queue();
            return;
        }

        if (stats.getStatusCode() == HttpStatus.SC_OK) {
            event
                    .getChannel()
                    .sendMessage("\nPlayer `" + playerName + "` found. Gathering data")
                    .queue();
        }

        if (stats.getStatusCode() == HttpStatus.SC_OK) {
            ProfileDataRequest profileStats = stats.as(ProfileDataRequest.class);
            StringBuilder stringBuilder = parsePlayerProfileStatsToString(profileStats);
            String link = getBaseUrl() + playerName + "/overview?ref=discord";
            event
                    .getChannel()
                    .sendMessage("\nStats for `" + playerName + "`\nFor more stats, visit:\n" + link + "\n" + stringBuilder)
                    .queue();
        }
    }

    public static void sendPlayerLastMaps(GuildMessageReceivedEvent event) {
        User author = event.getMessage().getAuthor();
        String contentRaw = event.getMessage().getContentRaw();
        String[] split = contentRaw.split("//map ");
        String playerName = split[1];
        log.info("{} requests map stats for {}", author.getName(), playerName);

        Response lastGames = getLastGames(playerName);

        if (lastGames.getStatusCode() != HttpStatus.SC_OK) {
            event.getChannel().sendMessage("\nError while request maps stats for player: `" + playerName + "`").queue();
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

            event.getChannel().sendMessage("\nStats for `" + playerName + "`\n" + stringBuilder).queue();
        }
    }

    public static void sendError(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            User author = event.getMessage().getAuthor();
            log.info("{} says something wrong.", author.getName());
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
        stringBuilder.append(parseMapStatsToString(dataMapRequest, playerName));
        return stringBuilder;
    }

}
