package requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BattlefieldStatsRequest {

    private static final String BASE_URL = "https://api.tracker.gg/api/v1/bfv/";
    private static final String STATS_URL = BASE_URL + "standard/profile/origin/";
    private static final String LAST_GAMES = BASE_URL + "gamereports/origin/latest/";
    private static final String GAME_REPORT = BASE_URL + "gamereports/origin/direct/";

    public static Response getStats(String name) {
        return given().get(STATS_URL + name);
    }

    public static Response getLastGames(String name) {
        return given().get(LAST_GAMES + name);
    }

    public static Response getGameReport(String gameReportId) {
        return given().get(GAME_REPORT + gameReportId);
    }

}
