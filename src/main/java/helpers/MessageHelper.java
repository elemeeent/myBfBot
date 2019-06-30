package helpers;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import pojo.lastMaps.DataReportsRequest;
import pojo.lastMaps.Report;
import pojo.mapReport.DataMapRequest;
import pojo.profileStats.ProfileDataRequest;

import java.util.ArrayList;
import java.util.List;

import static dataParser.JsonParseHelper.*;
import static helpers.ChatHelper.isBot;
import static helpers.DataHandlerHelper.*;
import static requests.BattlefieldStatsRequest.*;
import static requests.TeamStatsUrl.getTeamStats;

@Slf4j
public class MessageHelper {

    public static void ignorePrivate(PrivateMessageReceivedEvent event) {
        if (!isBot(event)) {
            log.warn("Someone spams bot: '{}'", event.getMessage().getAuthor().getName());
            event.getChannel()
                    .sendMessage("I'm not allowed to answer you")
                    .queue();

        }
    }

    public static void sendReady(ReadyEvent event) {
        log.info("I'm running at {} servers", event.getGuildAvailableCount());
    }

    public static void sendHello(GuildMessageReceivedEvent event) {
        if (!isBot(event)) {
            String nickname = getAuthorNickname(event);
            log.info("'{}' says hello.", nickname);
            event.getChannel()
                    .sendMessage("\nHello " + nickname +
                            "\nType `//help` for get command list...")
                    .queue();
        }
    }

    public static void sendHelp(GuildMessageReceivedEvent event) {
        if (!isBot(event)) {
            String nickname = getAuthorNickname(event);
            log.info("'{}' requests help.", nickname);
            event.getChannel()
                    .sendMessage("\nCurrent commands:" +
                            "\n1) type `//stats %playerName%` to get his statistics" +
                            "\n2) type `//me` to get your profile stats" +
                            "\n3) type `//compare %playerName% %playerName%` to compare 2 players" +
                            "\n4) type `//team %teamName%` to get statistics for team members" +
                            "\n\nExamples: " +
                            "\n`//stats i_am_prooo_tv`" +
                            "\n`//me`" +
                            "\n`//compare fu2zy farmlx`" +
                            "\n`//team ru-team`")
                    .queue();
        }
    }

    public static void sendPlayerStats(GuildMessageReceivedEvent event) {
        if (!isBot(event)) {
            String playerName = "";
            if (event.getMessage().getContentRaw().startsWith("//me")) {
                String playerFullName = getAuthorNickname(event);
                playerName = DataHandlerHelper.concatPlayerName(playerFullName);
            }
            if (event.getMessage().getContentRaw().startsWith("//stats ")) {
                String contentRaw = event.getMessage().getContentRaw();
                String[] split = contentRaw.split("//stats ");
                playerName = split[1];
            }
            User author = event.getMessage().getAuthor();
            log.info("'{}' requests stats for '{}'", author.getName(), playerName);

            Response stats = getProfileStats(playerName);
            String status = stats.then().extract().path("status").toString();

            if (checkStatus(event, playerName, status)) return;

            event.getChannel()
                    .sendMessage("\nPlayer `" + playerName + "` found. Gathering data")
                    .queue();

            ProfileDataRequest profileStats = stats.as(ProfileDataRequest.class);
            StringBuilder stringBuilder = getAndMergePlayerData(profileStats);
            String link = getBaseUrl() + playerName + "/overview?ref=discord";

            MessageBuilder builder = new MessageBuilder();
            builder.append("\nStats for `" + playerName + "`\n");
            builder.append("\n" + stringBuilder);
            builder.append("\nFor more stats, visit:\n" + link);

            event.getChannel()
                    .sendMessage(builder.build())
                    .queue();
        }
    }

    public static void sendPlayersCompareStats(GuildMessageReceivedEvent event) {
        if (!isBot(event)) {
            String firstPlayerName = "";
            String secondPlayerName = "";
            String authorNickname = getAuthorNickname(event);
            String message = event.getMessage().getContentRaw();
            if (event.getMessage().getContentRaw().startsWith("//compare ")) {
                String[] strings = playerNamesSeparator(message);
                firstPlayerName = strings[0];
                secondPlayerName = strings[1];
            }

            log.info("'{}' requests stats for '{}' and '{}'", authorNickname, firstPlayerName, secondPlayerName);

            Response fPStats = getProfileStats(firstPlayerName);
            Response sPStats = getProfileStats(secondPlayerName);

            String firstRequestStatus = fPStats.then().extract().path("status").toString();
            String secondRequestStatus = sPStats.then().extract().path("status").toString();

            if (checkStatus(event, firstPlayerName, firstRequestStatus)) return;

            if (checkStatus(event, secondPlayerName, secondRequestStatus)) return;

            event
                    .getChannel()
                    .sendMessage("\nPlayers `" + firstPlayerName + "` and `" + secondPlayerName + "` found. Gathering data")
                    .queue();

            ProfileDataRequest fPProfileStats = fPStats.as(ProfileDataRequest.class);
            ProfileDataRequest SPProfileStats = sPStats.as(ProfileDataRequest.class);

            StringBuilder stringBuilder = getAndMergePlayersData(fPProfileStats, SPProfileStats);

            String fPlink = getBaseUrl() + firstPlayerName + "/overview?ref=discord";
            String sPlink = getBaseUrl() + secondPlayerName + "/overview?ref=discord";

            // disabled cuz of permissions (i suppose)
//            EmbedBuilder embedBuilder = new EmbedBuilder();
//            embedBuilder.setTitle("\nStats for `" + playerName + "`\n");
//            embedBuilder.setDescription("For more stats, visit:\n" + link);
//
            MessageBuilder builder = new MessageBuilder();
//            builder.setEmbed(embedBuilder.build());
            builder.append("\nCompare for `" + firstPlayerName + "` and `" + secondPlayerName + "`\n");
            builder.append("\n" + stringBuilder);
            builder.append("\nFor more stats for `" + firstPlayerName + "`, visit:\n" + fPlink);
            builder.append("\nFor more stats for `" + secondPlayerName + "`, visit:\n" + sPlink);

            event
                    .getChannel()
                    .sendMessage(builder.build())
                    .queue();
        }
    }

    private static boolean checkStatus(GuildMessageReceivedEvent event, String firstPlayerName, String firstRequestStatus) {
        if (!firstRequestStatus.equals("Success")) {
            event.getChannel()
                    .sendMessage("Player `" + firstPlayerName + "` not found or something went wrong. " +
                            "Error: " + firstRequestStatus)
                    .queue();
            log.warn("Player '{}' stats wasn't showed. Error: '{}'", firstPlayerName, firstRequestStatus);
            return true;
        }
        return false;
    }

    public static void sendError(GuildMessageReceivedEvent event) {
        if (!isBot(event)) {
            User author = event.getMessage().getAuthor();
            log.info("'{}' says something wrong.", author.getName());
            event
                    .getChannel()
                    .sendMessage(
                            "\nHello " + author.getName() +
                                    "\nDon't know command you send." +
                                    "\nPlease send `//help` for get command list"
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

    public static void sendPlayerLastMaps(GuildMessageReceivedEvent event) {
        String nickname = getAuthorNickname(event);
        String contentRaw = event.getMessage().getContentRaw();
        String[] split = contentRaw.split("//map ");
        String playerName = split[1];
        log.info("{} requests map stats for {}", nickname, playerName);

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

    public static void sendTeamMembersStats(GuildMessageReceivedEvent event) {
        if (!isBot(event)) {
            String teamName = "";
            if (event.getMessage().getContentRaw().startsWith("//team ")) {
                String contentRaw = event.getMessage().getContentRaw();
                String[] split = contentRaw.split("//team ");
                teamName = split[1];
            }

            User author = event.getMessage().getAuthor();
            log.info("'{}' requests stats for team '{}'", author.getName(), teamName);

            Response teamStats = getTeamStats(teamName);

            int statusCode = teamStats.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.warn("Error while getting team members for team {}. Request code is {}.", teamName, statusCode);
                event.getChannel()
                        .sendMessage("\nTeam `" + teamName + "` not found or page unavailable. Sorry :(")
                        .queue();
                return;
            }

            String[] teamMembers = getTeamMembers(teamStats);
            List<ProfileDataRequest> profilesList = new ArrayList<>();
            List<String> errorPlayers = new ArrayList<>();

            assert teamMembers != null;
            for (String teamMember : teamMembers) {
                Response stats = getProfileStats(teamMember);
                String status = stats.then().extract().path("status").toString();

                if (checkStatus(event, teamMember, status)) {
                    errorPlayers.add(teamMember);
                    continue;
                } else {
                    profilesList.add(stats.as(ProfileDataRequest.class));
                }
            }

            if (!CollectionUtils.isEmpty(errorPlayers)) {
                event.getChannel()
                        .sendMessage("Couldn't get data for this players: " + errorPlayers)
                        .queue();
            }

            if (CollectionUtils.isEmpty(profilesList)) {
                event.getChannel()
                        .sendMessage("Couldn't get data for this team: " + teamName)
                        .queue();
            }

            for (ProfileDataRequest profileDataRequest : profilesList) {
                String playerName = profileDataRequest.getPlatformUserIdentifier();
                StringBuilder stringBuilder = getAndMergePlayerData(profileDataRequest);
                String link = getBaseUrl() + playerName + "/overview?ref=discord";

                MessageBuilder builder = new MessageBuilder();
                builder.append("\nStats for `" + playerName + "`\n");
                builder.append("\n" + stringBuilder);
                builder.append("\nFor more stats, visit:\n" + link);

                event.getChannel()
                        .sendMessage(builder.build())
                        .queue();
            }

            event.getChannel()
                    .sendMessage("Team: " + teamName + " roster:\n" + "https://league.esport-battlefield.com/team/" + teamName)
                    .queue();
        }
    }

}
