package dataParser;

import lombok.extern.slf4j.Slf4j;
import pojo.mapReport.DataMapRequest;
import pojo.mapReport.LastChildren;
import pojo.mapReport.MainChildren;
import pojo.profileStats.ProfileDataRequest;
import pojo.profileStats.jsonObjects.stats.ProfileDataStats;
import pojo.simpleStats.statsNodes.Stat;

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

    private static StringBuilder getDataFromStats(ProfileDataRequest dataRequest, StringBuilder stringBuilder) {

        ProfileDataStats stats = dataRequest.getData().getStats();
        String tab = "\t";
        return stringBuilder
                .append(stats.getProfileDataStatsScorePerMinute().getDisplayName())
                .append(": " + tab + tab + tab)
                .append(stats.getProfileDataStatsScorePerMinute().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsKdRatio().getDisplayName())
                .append(": " + tab + tab + tab + tab + tab + tab)
                .append(stats.getProfileDataStatsKdRatio().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsKills().getDisplayName())
                .append(": " + tab + tab + tab + tab + tab + tab)
                .append(stats.getProfileDataStatsKills().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsShotsAccuracy().getDisplayName())
                .append(":" + tab)
                .append(stats.getProfileDataStatsShotsAccuracy().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsKillsPerMinute().getDisplayName())
                .append(":" + tab + tab + tab + tab)
                .append(stats.getProfileDataStatsKillsPerMinute().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsRank().getDisplayName())
                .append(":" + tab + tab + tab + tab + tab + tab)
                .append(stats.getProfileDataStatsRank().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsTimePlayed().getDisplayName())
                .append(":  " + tab + tab)
                .append(stats.getProfileDataStatsTimePlayed().getDisplayValue())
                .append("\n")
                .append(stats.getProfileDataStatsScoreTanks().getDisplayName())
                .append(":  " + tab + tab)
                .append(stats.getProfileDataStatsScoreTanks().getDisplayValue())
                .append("\n");

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
