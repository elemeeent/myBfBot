package pojo.profileStats.jsonObjects.classes;

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
public class ProfileDataClassesKdRatio {

    @JsonProperty("value")
    private Double value;
    @JsonProperty("displayValue")
    private String displayValue;
    @JsonProperty("displayName")
    private String displayName;

}
