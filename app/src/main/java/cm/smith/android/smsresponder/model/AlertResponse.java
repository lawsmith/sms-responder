package cm.smith.android.smsresponder.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by anthony on 2016-05-14.
 */
public class AlertResponse extends RealmObject {

    @PrimaryKey
    private Integer id;
    private User user;
    @Required
    private Date date;

    public void setId(Integer newId) {
        this.id = newId;
    }
    public Integer getId() { return this.id; }

    public void setUser(User newUser) {
        this.user = newUser;
    }
    public User getUser() { return this.user; }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() { return this.date; }

}
