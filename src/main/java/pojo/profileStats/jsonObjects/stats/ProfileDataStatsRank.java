package pojo.profileStats.jsonObjects.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDataStatsRank {

    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("displayValue")
    private String displayValue;
    @JsonProperty("value")
    private Double value;

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public Double getValue() {
        return value;
    }
}
