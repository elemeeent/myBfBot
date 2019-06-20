package pojo.profileStats.jsonObjects.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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


}
