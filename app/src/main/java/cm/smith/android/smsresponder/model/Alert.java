package cm.smith.android.smsresponder.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anthony on 2016-05-11.
 */
public class Alert extends RealmObject {

    @PrimaryKey
    private Integer id;
    private Date startDate;
    private Boolean resolved;

    public void setId(Integer newId) {
        this.id = newId;
    }
    public Integer getId() { return this.id; }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getStartDate() { return this.startDate; }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }
    public Boolean getResolved() { return this.resolved; }

}
