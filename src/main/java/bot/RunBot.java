package bot;

import events.FirstEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.io.File;

import static helpers.FileHelper.readFile;

@Slf4j
public class RunBot {

    public static void main(String[] args) {

        String keyFile = System.getProperty("keyFile");
        if (keyFile == null) {
            log.error("No token file found");
            return;
        }

        try {
            new JDABuilder(readFile(System.getProperty("user.dir") + File.separator + keyFile))
                    .setStatus(OnlineStatus.ONLINE)
                    .setGame(Game.playing("https://battlefieldtracker.com"))
                    .addEventListener(new FirstEvent())
                    .build();
        } catch (LoginException ex) {
            log.error("Error while trying to login: {}", ex);
        }
    }
}
