package pojo.profileStats.jsonObjects.weapons;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDataWeapons {

    @JsonProperty("code")
    private String code;
    @JsonProperty("kills")
    private ProfileDataWeaponsKills profileDataWeaponsKills;
    @JsonProperty("shotsAccuracy")
    private ProfileDataWeaponsShotsAccuracy profileDataWeaponsShotsAccuracy;

    public String getCode() {
        return code;
    }

    public ProfileDataWeaponsKills getProfileDataWeaponsKills() {
        return profileDataWeaponsKills;
    }

    public ProfileDataWeaponsShotsAccuracy getProfileDataWeaponsShotsAccuracy() {
        return profileDataWeaponsShotsAccuracy;
    }
}
