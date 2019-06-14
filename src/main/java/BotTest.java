import events.FirstEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class BotTest {

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder("NTg4Mzg0ODQwMDA5OTA4MjM0.XQEWOA.1w11HJNmbaUBXHQ0G96H2WsuQJI").build();

        jda.addEventListener(new FirstEvent());
    }
}
