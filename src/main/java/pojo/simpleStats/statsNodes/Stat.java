package pojo.simpleStats.statsNodes;

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
public class Stat {

    @JsonProperty("metadata")
    private Metadata metadata;
    @JsonProperty("value")
    private Double value;
    @JsonProperty("displayValue")
    private String displayValue;

}