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
public class ProfileDataStatsShotsAccuracy {

    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("displayValue")
    private String displayValue;
}
