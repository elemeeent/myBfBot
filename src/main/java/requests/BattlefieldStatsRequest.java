package requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BattlefieldStatsRequest {

    private static final String BASE_URL = "https://api.tracker.gg/api/v1/bfv/standard/profile/origin/";
    private static final String LAST_GAMES = "https://api.tracker.gg/api/v1/bfv/gamereports/origin/latest/";
    private static final String GAME_REPORT = "https://api.tracker.gg/api/v1/bfv/gamereports/origin/direct/";

    public static Response getStats(String name) {
        return given().get(BASE_URL + name);
    }

    public static Response getLastGames(String name) {
        return given().get(LAST_GAMES + name);
    }

    public static Response getGameReport(String gameReportId) {
        return given().get(GAME_REPORT + gameReportId);
    }

}
