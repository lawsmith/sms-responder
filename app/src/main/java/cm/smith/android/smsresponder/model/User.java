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
        MEMBER(100),
        ADMIN(500);

        private final int value;

        Role(int role) {
            this.value = role;
        }

        public int getValue() {
            return value;
        }
    }

    @Required
    private String name;
    @PrimaryKey
    private String phone;
    @Required
    private Integer userRole;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() { return this.name; }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() { return this.phone; }

    public void setUserRole(Integer role) {
        this.userRole = role;
    }
    public Integer getUserRole() { return this.userRole; }

}
