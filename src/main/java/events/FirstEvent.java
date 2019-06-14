package events;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static helpers.MessageHelpers.*;

public class FirstEvent extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSent = event.getMessage().getContentRaw();
        if (messageSent.equalsIgnoreCase("/hi")) {
            sendHello(event);
        }
        if (messageSent.equalsIgnoreCase("/help")) {
            sendHelp(event);
        }
        if (messageSent.matches("/stats \\w+")) {
            sendPlayerStats(event);
        }
    }
}
