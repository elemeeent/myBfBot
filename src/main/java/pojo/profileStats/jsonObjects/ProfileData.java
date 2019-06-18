package pojo.profileStats.jsonObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pojo.profileStats.jsonObjects.classes.ProfileDataClasses;
import pojo.profileStats.jsonObjects.stats.ProfileDataStats;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileData {

    @JsonProperty("stats")
    private ProfileDataStats stats;
    @JsonProperty("classes")
    private ProfileDataClasses[] classes;
}
