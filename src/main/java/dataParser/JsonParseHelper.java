package dataParser;

import lombok.extern.slf4j.Slf4j;
import pojo.mapReport.DataMapRequest;
import pojo.mapReport.LastChildren;
import pojo.mapReport.MainChildren;
import pojo.profileStats.ProfileDataRequest;
import pojo.profileStats.jsonObjects.stats.ProfileDataStats;
import pojo.simpleStats.statsNodes.Stat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonParseHelper {

    private static String[] playerMapStats = {
            "kills",
            "deaths",
            "killsPerMinute"
    };

    public static StringBuilder parsePlayerProfileStatsToString(ProfileDataRequest data) {
        StringBuilder sb = new StringBuilder();

        if (data == null || data.getData() == null) {
            return null;
        }

        return getDataFromStats(data, sb);
    }

    public static StringBuilder getDataFromStats(ProfileDataRequest dataRequest, StringBuilder stringBuilder) {
        ProfileDataStats stats = dataRequest.getData().getStats();
        List<Object> statsObjects = new ArrayList<>();
        stringBuilder.append("```");
        stringBuilder.append(String.format("| %-16s | %-13s\n", "Stats name", "Stats value"));

        Method[] statsMethods = stats.getClass().getMethods();
        for (Method statMethod : statsMethods) {
            if (statMethod.getName().matches("getProfileDataStats\\w+")) {
                try {
                    statsObjects.add(statMethod.invoke(stats));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Object statsObject : statsObjects) {
            ArrayList<String> nameList = new ArrayList<>();
            ArrayList<String> valueList = new ArrayList<>();
            for (Method method : statsObject.getClass().getMethods()) {
                if (method.getName().matches("getDisplayName") || method.getName().matches("getDisplayValue")) {
                    if (method.getName().matches("getDisplayName")) {
                        try {
                            nameList.add(method.invoke(statsObject).toString());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if (method.getName().matches("getDisplayValue")) {
                        try {
                            valueList.add(method.invoke(statsObject).toString());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (int i = 0; i < nameList.size(); i++) {
                stringBuilder.append(String.format("| %-16s | %-13s\n", nameList.get(i), valueList.get(i)));
            }
        }
        stringBuilder.append("```");
        return stringBuilder;
    }

    public static StringBuilder parseMapStatsToString(DataMapRequest data, String playerName) {
        StringBuilder stringBuilder = new StringBuilder();

        if (data == null || data.getData() == null) {
            return null;
        }
        MainChildren[] mainChildrens = data.getData().getChildren();
        for (MainChildren mainChild : mainChildrens) {
            LastChildren[] lastChildrens = mainChild.getChildren();
            for (LastChildren lastChildren : lastChildrens) {
                if (lastChildren.getMetadata().getName().equalsIgnoreCase(playerName)) {
                    Stat[] playerStats = lastChildren.getStats();
                    for (Stat playerStat : playerStats) {
                        for (int i = 0; i < playerMapStats.length; i++) {
                            if (playerStat.getMetadata().getKey().equalsIgnoreCase(playerMapStats[i])) {
                                try {
                                    stringBuilder
                                            .append(playerStat.getMetadata().getName())
                                            .append(": ")
                                            .append(playerStat.getDisplayValue())
                                            .append("\n");
                                } catch (Exception e) {
                                    log.error("Error parse player stats. {}", e);
                                    stringBuilder = new StringBuilder("Error at gathering player stats");
                                }
                            }
                        }
                    }
                }
            }
        }
        return stringBuilder;
    }
}
