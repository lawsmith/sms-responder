package cm.smith.android.smsresponder.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by anthony on 2016-03-30.
 */
public class User extends RealmObject {

    @PrimaryKey
    private Integer id;
    @Required
    private String name;
    @Required
    private String phone;
    @Required
    private String userRole;

    public void setId(Integer newId) {
        this.id = newId;
    }
    public Integer getId() { return this.id; }

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

    /**
     * Checks to see if a user already exists with the same phone number
     * @param phone
     * @param database
     * @return
     */
    public static boolean checkIfExists(Realm database, String phone){
        RealmQuery<User> query = database.where(User.class)
                .equalTo("phone", phone);

        return query.count() == 0 ? false : true;
    }

    /**
     * Checks to see if a user already exists with the same id
     * @param id
     * @param database
     * @return
     */
    public static boolean checkIfExists(Realm database, Integer id){
        RealmQuery<User> query = database.where(User.class)
                .equalTo("id", id);

        return query.count() == 0 ? false : true;
    }

}
