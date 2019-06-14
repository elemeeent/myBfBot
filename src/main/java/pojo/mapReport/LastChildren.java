package pojo.mapReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pojo.profileStats.statsNodes.Metadata;
import pojo.profileStats.statsNodes.Stat;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastChildren {

    @JsonProperty("id")
    private String id;
    @JsonProperty("metadata")
    private Metadata metadata;
    @JsonProperty("stats")
    private Stat[] stats;

}
