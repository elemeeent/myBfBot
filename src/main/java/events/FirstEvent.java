package events;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static helpers.MessageHelper.*;

public class FirstEvent extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        sendReady(event);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        ignorePrivate(event);
        return;
    }

    @Override
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
        if (messageSent.equalsIgnoreCase("//me")) {
            sendPlayerStats(event);
            return;
        }
        if (messageSent.matches("//stats .*")) {
            sendPlayerStats(event);
            return;
        }
        if (messageSent.matches("//map .*")) {
            sendPlayerLastMaps(event);
            return;
        }
        if (messageSent.matches("//compare .*")) {
            sendPlayersCompareStats(event);
            return;
        }
        if (messageSent.matches("//team .*")) {
            sendTeamMembersStats(event);
            return;
        }
        if (messageSent.matches("//.*")) {
            sendError(event);
        }
    }
}
