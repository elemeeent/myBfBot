package dataParser;

import io.restassured.response.Response;
import pojo.lastMaps.DataReportsRequest;
import pojo.lastMaps.Report;
import pojo.mapReport.DataMapRequest;
import pojo.mapReport.LastChildren;
import pojo.mapReport.MainChildren;
import pojo.profileStats.DataStatsRequest;
import pojo.profileStats.statsNodes.Stat;

import static requests.BattlefieldStatsRequest.getGameReport;
import static requests.BattlefieldStatsRequest.getLastGames;

public class JsonParseHelper {

    private static String[] playerStats = {
            "rank",
            "timePlayed",
            "scorePerMinute",
            "kdRatio",
            "kills",
            "shotsAccuracy",
            "killsPerMinute",
            "scoreTanks"
    };
    private static String[] playerMapStats = {
            "kills",
            "deaths",
            "killsPerMinute"
    };

    public static StringBuilder parsePlayerStatsToString(DataStatsRequest data) {
        StringBuilder sb = new StringBuilder();

        if (data == null || data.getDataStats() == null) {
            return null;
        }

        for (Stat stat : data.getDataStats().getStats()) {
            for (int i = 0; i < playerStats.length; i++) {
                getDataFromStats(stat, playerStats[i], sb);
            }
        }

        return sb;
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
                                stringBuilder
                                        .append(playerStat.getMetadata().getName())
                                        .append(": ")
                                        .append(playerStat.getDisplayValue())
                                        .append("\n");

                            }
                        }
                    }
                }
            }
        }
        return stringBuilder;
    }

    public static StringBuilder getLastMapsStats(String playerName, StringBuilder stringBuilder) {
        DataReportsRequest playerLastGames = getLastGames(playerName).as(DataReportsRequest.class);

        Report[] reports = playerLastGames.getData().getReports();


        for (int i = 0; i < 3; i++) {
            String gameReportId = reports[i].getGameReportId();
            Response gameReport = getGameReport(gameReportId);
            DataMapRequest dataMapRequest = gameReport.as(DataMapRequest.class);
            stringBuilder
                    .append("\n")
                    .append(reports[i].getMode().getName())
                    .append(": ")
                    .append(reports[i].getMap().getName())
                    .append("\n")
                    .append(parseMapStatsToString(dataMapRequest, playerName));
        }
        return stringBuilder;
    }

    private static StringBuilder getDataFromStats(Stat stat, String key, StringBuilder stringBuilder) {
        if (stat.getMetadata().getKey().equalsIgnoreCase(key)) {
            return stringBuilder
                    .append(stat.getMetadata().getName())
                    .append(": ")
                    .append(stat.getDisplayValue())
                    .append("\n");
        }
        return null;
    }

}
