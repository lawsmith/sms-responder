package cm.smith.android.smsresponder.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by anthony on 2016-03-30.
 */
public class User extends RealmObject {

    /*
        Sadly RealmDB doesn't support saving ENUM values, so I'm storing roles as
        an integer to get around that :(
     */
    public enum Role {
        MEMBER("MEMBER"),
        ADMIN("ADMIN");

        private final String value;

        Role(String role) {
            this.value = role;
        }

        public String getValue() {
            return value;
        }

        public static Role fromString(String text) throws IllegalArgumentException {
            if (text != null) {
                for (Role r : Role.values()) {
                    if (text.equalsIgnoreCase(r.value)) {
                        return r;
                    }
                }
            }
            throw new IllegalArgumentException("No role with text " + text + " found");
        }
    }

    @Required
    private String name;
    @PrimaryKey
    private String phone;
    @Required
    private String userRole;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() { return this.name; }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() { return this.phone; }

    public void setUserRole(String role) {
        this.userRole = role;
    }
    public String getUserRole() { return this.userRole; }

}
