package helpers;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Slf4j
public class DataHandlerHelper {

    static String concatPlayerName(String name) {
        log.info("Concat player: {}", name);
        name = name.replaceAll("\\)", "");
        name = name.replaceAll("\\(.*", "");
        if (name.contains(" ")) {
            name = name.replaceAll("\\s+", "");
        }
        log.info("Final name: {}", name);
        return name;
    }

    public static String getAuthorNickname(GuildMessageReceivedEvent event) {
        String playerFullName = event.getMessage().getMember().getNickname();
        if (playerFullName == null) {
            playerFullName = event.getMessage().getAuthor().getName();
        }
        return playerFullName;
    }

    public static String[] playerNamesSeparator(String s) {
        String[] playerNames = {};

        if (!s.matches("\\/\\/compare .*[^ ]")) {
            return null;
        } else {
            String[] split = s.split("//compare ");
            if (split.length < 2) {
                return null;
            } else {
                String[] finalSplit = split[1].split("\\s+");
                if (finalSplit.length < 2) {
                    return null;
                }
                playerNames = finalSplit;
            }
        }
        return playerNames;
    }

    public static String[] getTeamMembers(Response teamResponse) {
        log.info("Parse team members");
        String content = teamResponse.asString();

        if (!content.contains("<meta name=\"twitter:description\" content=\"BFV Pre-Season Roster 8v8 ")) {
            log.info("Error while parsing html page for team");
            return null;
        }

        if (!content.contains("\" />\n<meta name=\"twitter:title\"")) {
            log.info("Error while parsing html page for members");
            return null;
        }

        String[] playersArray = null;
        String[] firstSplit = content.split("<meta name=\"twitter:description\" content=\"BFV Pre-Season Roster 8v8 ");

        for (String s : firstSplit) {
            if (s.contains("\" />\n<meta name=\"twitter:title\"")) {
                String[] secondSplit = s.split("\" />\n<meta name=\"twitter:title\"");
                if (secondSplit.length >= 2) {
                    playersArray = secondSplit[0].split(" ");
                }
            }
        }
        return playersArray;

    }

}
