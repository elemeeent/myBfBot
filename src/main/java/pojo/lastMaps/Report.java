package pojo.lastMaps;

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
public class Report {

    @JsonProperty("gameReportId")
    private String gameReportId;
    @JsonProperty("map")
    private Map map;
    @JsonProperty("mode")
    private Mode mode;
}
