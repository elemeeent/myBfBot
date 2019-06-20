package helpers;

import lombok.extern.slf4j.Slf4j;

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

}
