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

}
