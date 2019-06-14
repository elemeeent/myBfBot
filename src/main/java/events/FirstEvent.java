package events;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static helpers.MessageHelpers.*;

public class FirstEvent extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSent = event.getMessage().getContentRaw();

        if (messageSent.equalsIgnoreCase("//hi")) {
            sendHello(event);
            return;
        }
        if (messageSent.equalsIgnoreCase("//help")) {
            sendHelp(event);
            return;
        }
        if (messageSent.matches("//stats \\w+")) {
            sendPlayerStats(event);
            return;
        }
        if (messageSent.matches("//map \\w+")) {
            sendPlayerLastMaps(event);
            return;
        }
        if (messageSent.matches("//\\w+")) {
            sendError(event);
        }
    }
}
