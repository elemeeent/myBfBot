package pojo.profileStats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pojo.profileStats.jsonObjects.ProfileData;

@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDataRequest {

    @JsonProperty("data")
    private ProfileData data;
    @JsonProperty("platformUserIdentifier")
    private String platformUserIdentifier;

    public ProfileData getData() {
        return data;
    }

    public String getPlatformUserIdentifier() {
        return platformUserIdentifier;
    }
}
