package helpers;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class ChatHelper {

    public static boolean isBot(GuildMessageReceivedEvent event) {
        return event.getMessage().getAuthor().isBot() || event.getMessage().getMember().getUser().isBot();
    }

    public static boolean isBot(PrivateMessageReceivedEvent event) {
        return event.getMessage().getAuthor().isBot();
    }

}
