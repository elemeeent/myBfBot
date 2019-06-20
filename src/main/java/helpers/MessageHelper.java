package helpers;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import pojo.lastMaps.DataReportsRequest;
import pojo.lastMaps.Report;
import pojo.mapReport.DataMapRequest;
import pojo.profileStats.ProfileDataRequest;

import static dataParser.JsonParseHelper.mergeAllData;
import static dataParser.JsonParseHelper.parseMapStatsToString;
import static requests.BattlefieldStatsRequest.*;

@Slf4j
public class MessageHelper {

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
            String playerFullName = event.getMessage().getMember().getNickname();
            if (playerFullName == null) {
                playerFullName = event.getMessage().getAuthor().getName();
            }
            playerName = DataHandlerHelper.concatPlayerName(playerFullName);
        }
        if (event.getMessage().getContentRaw().startsWith("//stats ")) {
            String contentRaw = event.getMessage().getContentRaw();
            String[] split = contentRaw.split("//stats ");
            playerName = split[1];
        }
        User author = event.getMessage().getAuthor();
        log.info("{} requests stats for {}", author.getName(), playerName);

        Response stats = getProfileStats(playerName);

        if (stats.then().extract().path("status").equals("NotFound")) {
            event
                    .getChannel()
                    .sendMessage("Player `" + playerName + "` not found")
                    .queue();
            return;
        }

        if (stats.then().extract().path("status").equals("Success")) {
            event
                    .getChannel()
                    .sendMessage("\nPlayer `" + playerName + "` found. Gathering data")
                    .queue();
        }

        if (stats.then().extract().path("status").equals("Success")) {
            ProfileDataRequest profileStats = stats.as(ProfileDataRequest.class);
            StringBuilder stringBuilder = mergeAllData(profileStats);
            String link = getBaseUrl() + playerName + "/overview?ref=discord";

            // disabled cuz of permissions (i suppose)
//            EmbedBuilder embedBuilder = new EmbedBuilder();
//            embedBuilder.setTitle("\nStats for `" + playerName + "`\n");
//            embedBuilder.setDescription("For more stats, visit:\n" + link);
//
            MessageBuilder builder = new MessageBuilder();
//            builder.setEmbed(embedBuilder.build());
            builder.append("\nStats for `" + playerName + "`\n");
            builder.append("\n" + stringBuilder);
            builder.append("\nFor more stats, visit:\n" + link);

            event
                    .getChannel()
                    .sendMessage(builder.build())
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
