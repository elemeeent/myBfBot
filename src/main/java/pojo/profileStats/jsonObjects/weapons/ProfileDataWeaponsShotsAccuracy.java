package pojo.profileStats.jsonObjects.weapons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDataWeaponsShotsAccuracy {

    @JsonProperty("value")
    private Integer value;
    @JsonProperty("displayValue")
    private String displayValue;
    @JsonProperty("displayName")
    private String displayName;

    public Integer getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getDisplayName() {
        return displayName;
    }
}
