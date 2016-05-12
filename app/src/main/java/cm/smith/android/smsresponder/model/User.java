package cm.smith.android.smsresponder.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by anthony on 2016-03-30.
 */
public class User extends RealmObject {

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
