package pojo.profileStats.jsonObjects.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDataStats {

    @JsonProperty("scorePerMinute")
    private ProfileDataStatsScorePerMinute profileDataStatsScorePerMinute;
    @JsonProperty("kdRatio")
    private ProfileDataStatsKdRatio profileDataStatsKdRatio;
    @JsonProperty("kills")
    private ProfileDataStatsKills profileDataStatsKills;
    @JsonProperty("shotsAccuracy")
    private ProfileDataStatsShotsAccuracy profileDataStatsShotsAccuracy;
    @JsonProperty("killsPerMinute")
    private ProfileDataStatsKillsPerMinute profileDataStatsKillsPerMinute;
    @JsonProperty("rank")
    private ProfileDataStatsRank profileDataStatsRank;
    @JsonProperty("timePlayed")
    private ProfileDataStatsTimePlayed profileDataStatsTimePlayed;
    @JsonProperty("wlPercentage")
    private ProfileDataStatsWlPercentage profileDataStatswlPercentage;
//    @JsonProperty("scoreTanks")
//    private ProfileDataStatsScoreTanks profileDataStatsScoreTanks;


    public ProfileDataStatsScorePerMinute getProfileDataStatsScorePerMinute() {
        return profileDataStatsScorePerMinute;
    }

    public ProfileDataStatsKdRatio getProfileDataStatsKdRatio() {
        return profileDataStatsKdRatio;
    }

    public ProfileDataStatsKills getProfileDataStatsKills() {
        return profileDataStatsKills;
    }

    public ProfileDataStatsShotsAccuracy getProfileDataStatsShotsAccuracy() {
        return profileDataStatsShotsAccuracy;
    }

    public ProfileDataStatsKillsPerMinute getProfileDataStatsKillsPerMinute() {
        return profileDataStatsKillsPerMinute;
    }

    public ProfileDataStatsRank getProfileDataStatsRank() {
        return profileDataStatsRank;
    }

    public ProfileDataStatsTimePlayed getProfileDataStatsTimePlayed() {
        return profileDataStatsTimePlayed;
    }

    public ProfileDataStatsWlPercentage getProfileDataStatswlPercentage() {
        return profileDataStatswlPercentage;
    }
}
