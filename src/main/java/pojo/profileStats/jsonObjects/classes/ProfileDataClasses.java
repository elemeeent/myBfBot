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
public class ProfileDataClasses {

    @JsonProperty("class")
    private String className;
    //    @JsonProperty("kdRatio")
//    private ProfileDataClassesKdRatio profileDataClassesKdRatio;
    @JsonProperty("score")
    private ProfileDataClassesScore profileDataClassesScore;
}
