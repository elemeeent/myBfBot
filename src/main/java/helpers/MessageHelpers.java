package helpers;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.http.HttpStatus;
import pojo.profileStats.DataStatsRequest;

import static dataParser.JsonParseHelper.getLastMapsStats;
import static dataParser.JsonParseHelper.parsePlayerStatsToString;
import static requests.BattlefieldStatsRequest.getStats;

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
        }

        if (stats.getStatusCode() == HttpStatus.SC_OK) {
            event.getChannel().sendMessage("\nPlayer '" + playerName + "' found. Gathering data").queue();
        }

        if (stats.getStatusCode() == HttpStatus.SC_OK) {
            DataStatsRequest playerDataStats = stats.as(DataStatsRequest.class);
            StringBuilder stringBuilder = parsePlayerStatsToString(playerDataStats);
            getLastMapsStats(playerName, stringBuilder);
            event.getChannel().sendMessage("\nStats for " + playerName + "\n" + stringBuilder).queue();

        }

    }

}
