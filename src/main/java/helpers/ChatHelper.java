package helpers;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ChatHelper {

    public static boolean isBot(GuildMessageReceivedEvent event) {
        return event.getMessage().getAuthor().isBot() || event.getMessage().getMember().getUser().isBot();
    }

}
