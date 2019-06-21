package dataParser;

import lombok.extern.slf4j.Slf4j;
import pojo.mapReport.DataMapRequest;
import pojo.mapReport.LastChildren;
import pojo.mapReport.MainChildren;
import pojo.profileStats.ProfileDataRequest;
import pojo.profileStats.jsonObjects.classes.ProfileDataClasses;
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

    public static StringBuilder getAndMergePlayerData(ProfileDataRequest dataRequest) {
        StringBuilder playerFullStats = new StringBuilder();
        playerFullStats.append("```");
        getDataFromStats(dataRequest, playerFullStats);
        playerFullStats.append("|==================================|\n");
        getDataFromClasses(dataRequest, playerFullStats);
        playerFullStats.append("|==================================|\n");
        playerFullStats.append("```");
        return playerFullStats;
    }

    private static StringBuilder getDataFromClasses(ProfileDataRequest dataRequest, StringBuilder stringBuilder) {
        ProfileDataClasses[] classes = dataRequest.getData().getClasses();
        stringBuilder.append(String.format("| %-16s | %-13s |\n", "Class name", "Class score"));
        stringBuilder.append("|==================================|\n");

        for (ProfileDataClasses aClass : classes) {
            String className = aClass.getClassName();
            if (className.equals("pilot")) {
                continue;
            }
            String displayName = aClass.getProfileDataClassesScore().getDisplayValue();
            stringBuilder.append(String.format("| %-16s | %-13s |\n", className, displayName));
        }

        return stringBuilder;

    }

    private static StringBuilder getDataFromStats(ProfileDataRequest dataRequest, StringBuilder stringBuilder) {
        ProfileDataStats stats = dataRequest.getData().getStats();
        List<Object> statsObjects = new ArrayList<>();
        stringBuilder.append(String.format("| %-16s | %-13s |\n", "Stats name", "Stats value"));
        stringBuilder.append("|==================================|\n");

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
                stringBuilder.append(String.format("| %-16s | %-13s |\n", nameList.get(i), valueList.get(i)));
            }
        }
        return stringBuilder;
    }


    public static StringBuilder getAndMergePlayersData(ProfileDataRequest fPDataRequest, ProfileDataRequest sPDataRequest) {
        StringBuilder playerFullStats = new StringBuilder();
        playerFullStats.append("```");
        getPlayersDataFromStats(fPDataRequest, sPDataRequest, playerFullStats);
        playerFullStats.append("|================================================|\n");
        getPlayersDataFromClasses(fPDataRequest, sPDataRequest, playerFullStats);
        playerFullStats.append("|================================================|\n");
        playerFullStats.append("```");
        return playerFullStats;
    }

    public static StringBuilder getPlayersDataFromClasses(ProfileDataRequest firstPlayer, ProfileDataRequest secondPlayer, StringBuilder stringBuilder) {
        ProfileDataClasses[] fpClasses = firstPlayer.getData().getClasses();
        ProfileDataClasses[] spClasses = secondPlayer.getData().getClasses();

        stringBuilder.append(String.format("| %-14s | %-11s | %-1s | %-11s |\n", "Class name", "Class score", "", "Class score"));
        stringBuilder.append("|================================================|\n");

        if (fpClasses[0].getProfileDataClassesScore() == null && spClasses[0].getProfileDataClassesScore() == null) {
            return stringBuilder.append("No stats found");
        }

        for (int i = 0; i <= fpClasses.length - 1; i++) {
            String className = fpClasses[i].getClassName();
            if (className.equals("pilot")) {
                continue;
            }

            String fpDisplayValue = fpClasses[i].getProfileDataClassesScore().getDisplayValue();
            String spDisplayValue = spClasses[i].getProfileDataClassesScore().getDisplayValue();

            int fpValue = fpClasses[i].getProfileDataClassesScore().getValue();
            int spValue = spClasses[i].getProfileDataClassesScore().getValue();

            String compare = null;

            if (fpValue > spValue) {
                compare = ">";
            }
            if (fpValue < spValue) {
                compare = "<";
            }
            if (fpValue == spValue) {
                compare = "=";
            }

            stringBuilder.append(String.format("| %-14s | %-11s | %-1s | %-11s |\n", className, fpDisplayValue, compare, spDisplayValue));
        }

        return stringBuilder;

    }

    public static StringBuilder getPlayersDataFromStats(ProfileDataRequest firstPlayer, ProfileDataRequest secondPlayer, StringBuilder stringBuilder) {
        ProfileDataStats fPstats = firstPlayer.getData().getStats();
        ProfileDataStats sPstats = secondPlayer.getData().getStats();
        List<Object> fPStatsObjects = new ArrayList<>();
        List<Object> sPStatsObjects = new ArrayList<>();

        int nameWidth = firstPlayer.getPlatformUserIdentifier().length() + secondPlayer.getPlatformUserIdentifier().length();
        int fullWidth = 50 - nameWidth;

        stringBuilder.append(String.format(" %-" + fullWidth + "s\n", "Compare " + firstPlayer.getPlatformUserIdentifier() + " to " + secondPlayer.getPlatformUserIdentifier()));
        stringBuilder.append(String.format("| %-14s | %-11s | %-1s | %-11s |\n", "Stats name", "Stats value", "", "Stats value"));
        stringBuilder.append("|================================================|\n");

        Method[] fPStatsMethods = fPstats.getClass().getMethods();
        Method[] sPStatsMethods = fPstats.getClass().getMethods();

        if (fPStatsMethods.length == sPStatsMethods.length) {
            for (Method statMethod : fPStatsMethods) {
                if (statMethod.getName().matches("getProfileDataStats\\w+")) {
                    try {
                        fPStatsObjects.add(statMethod.invoke(fPstats));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (Method statMethod : sPStatsMethods) {
                if (statMethod.getName().matches("getProfileDataStats\\w+")) {
                    try {
                        sPStatsObjects.add(statMethod.invoke(sPstats));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return new StringBuilder().append("Something went wrong");
        }

        for (int i = 0; i <= fPStatsObjects.size() - 1; i++) {
            ArrayList<String> nameList = new ArrayList<>();
            ArrayList<String> fPValueList = new ArrayList<>();
            ArrayList<String> sPValueList = new ArrayList<>();
            ArrayList<String> compareAttr = new ArrayList<>();
            Method[] methods = fPStatsObjects.get(i).getClass().getMethods();
            for (int j = 0; j <= methods.length - 1; j++) {
                if (methods[j].getName().matches("getDisplayName")
                        || methods[j].getName().matches("getDisplayValue")
                        || methods[j].getName().matches("getValue")) {
                    if (methods[j].getName().matches("getDisplayName")) {
                        try {
                            nameList.add(methods[j].invoke(fPStatsObjects.get(i)).toString());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if (methods[j].getName().matches("getDisplayValue")) {
                        try {
                            fPValueList.add(methods[j].invoke(fPStatsObjects.get(i)).toString());
                            sPValueList.add(methods[j].invoke(sPStatsObjects.get(i)).toString());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if (methods[j].getName().matches("getValue")) {

                        String compare = null;
                        Object fpValue = null;
                        Object spValue = null;
                        try {
                            fpValue = methods[j].invoke(fPStatsObjects.get(i));
                            spValue = methods[j].invoke(sPStatsObjects.get(i));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        double i1 = Double.parseDouble(fpValue.toString());

                        double i2 = Double.parseDouble(spValue.toString());

                        if (i1 > i2) {
                            compare = ">";
                        }
                        if (i1 < i2) {
                            compare = "<";
                        }
                        if (i1 == i2) {
                            compare = "=";
                        }

                        compareAttr.add(compare);
                    }
                }
            }
            for (int z = 0; z < nameList.size(); z++) {
                stringBuilder.append(String.format("| %-14s | %-11s | %-1s | %-11s |\n", nameList.get(z), fPValueList.get(z), compareAttr.get(z), sPValueList.get(z)));
            }

        }
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
