package bot;

import events.FirstEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

@Slf4j
public class RunBot {

    private static JDA jda;

    public static void main(String[] args) {

        try {
            jda = new JDABuilder("NTg4Mzg0ODQwMDA5OTA4MjM0.XQEWOA.1w11HJNmbaUBXHQ0G96H2WsuQJI")
                    .setStatus(OnlineStatus.ONLINE)
                    .setGame(Game.playing("https://battlefieldtracker.com"))
                    .build();
        } catch (LoginException ex) {
            log.error("Error while trying to login: {}", ex);
        }

        jda.addEventListener(new FirstEvent());
    }
}
