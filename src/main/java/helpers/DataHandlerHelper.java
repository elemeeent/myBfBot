package helpers;

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

}
