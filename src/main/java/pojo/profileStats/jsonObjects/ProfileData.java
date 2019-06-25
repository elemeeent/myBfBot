package pojo.profileStats.jsonObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pojo.profileStats.jsonObjects.classes.ProfileDataClasses;
import pojo.profileStats.jsonObjects.stats.ProfileDataStats;
import pojo.profileStats.jsonObjects.weapons.ProfileDataWeapons;

import java.util.*;

@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileData {

    @JsonProperty("stats")
    private ProfileDataStats stats;
    @JsonProperty("classes")
    private ProfileDataClasses[] classes;
    @JsonProperty("weapons")
    private ProfileDataWeapons[] weapons;

    public ProfileDataStats getStats() {
        return stats;
    }

    public ProfileDataClasses[] getClasses() {
        return classes;
    }

    public static Map<String, List<String>> getTopWeapons(ProfileDataWeapons[] profileDataWeapons) {

        if (profileDataWeapons == null) {
            return null;
        }

        List<Integer> weaponKills = new ArrayList<>();

        for (ProfileDataWeapons profileDataWeapon : profileDataWeapons) {
            weaponKills.add(profileDataWeapon.getProfileDataWeaponsKills().getValue());
        }

        Collections.sort(weaponKills);
        Collections.reverse(weaponKills);

        Map<String, List<String>> topWeapons = new HashMap<>();

        for (int i = 0; i <= 3; i++) {
            for (ProfileDataWeapons profileDataWeapon : profileDataWeapons) {
                if (profileDataWeapon.getProfileDataWeaponsKills().getValue() == weaponKills.get(i)) {
                    List<String> weaponStats = new ArrayList<>();
                    weaponStats.add(profileDataWeapon.getProfileDataWeaponsKills().getDisplayValue());
                    weaponStats.add(profileDataWeapon.getProfileDataWeaponsShotsAccuracy().getDisplayValue());
                    topWeapons.put(profileDataWeapon.getCode(), weaponStats);
                }
            }
        }
        return topWeapons;
    }

    public static StringBuilder handleAndGetTopWeapons(Map<String, List<String>> weaponsMap, StringBuilder stringBuilder) {
        Iterator<Map.Entry<String, List<String>>> iterator = weaponsMap.entrySet().iterator();
        stringBuilder.append(String.format("| %-16s | %-13s |\n", "Weapon name", "Weapon stats"));
        stringBuilder.append("|==================================|\n");
        while (iterator.hasNext()) {
            Map.Entry pairs = iterator.next();
            String s = pairs.getKey().toString();
            String[] wtypes;
            if (s.matches("wtype\\w+")) {
                wtypes = s.split("wtype");
                String value = pairs.getValue().toString();
                if (value.matches(".*, .*")) {
                    String substring = value.substring(1, value.length() - 1);
                    String[] split = substring.split(", ");
                    stringBuilder.append(String.format("| %-16s | %-13s |\n", wtypes[1], "kills: " + split[0]));
                    stringBuilder.append(String.format("| %-16s | %-13s |\n", "________________", "acc.: " + split[1] + "%"));
                    continue;
                }
                stringBuilder.append(wtypes[1] + ": " + value + "\n");
            } else {
                stringBuilder.append(s + ": " + pairs.getValue() + "\n");
            }
        }
        return stringBuilder;
    }

    public ProfileDataWeapons[] getWeapons() {
        return weapons;
    }
}
