package requests;

import io.restassured.response.Response;

import static helpers.DataHandlerHelper.getTeamMembers;
import static io.restassured.RestAssured.given;

public class TeamStatsUrl {

    private static final String TEAMS_URL = "https://league.esport-battlefield.com/team/";

    public static Response getTeamStats(String name) {
        return given().get(TEAMS_URL + name + "/");
    }

    public static void main(String[] args) {
        String[] teamMembers = getTeamMembers(getTeamStats("ru-team"));
        for (String teamMember : teamMembers) {
            System.out.println(teamMember);
        }
    }
}
