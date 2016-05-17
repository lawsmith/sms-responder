package cm.smith.android.smsresponder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

import cm.smith.android.smsresponder.model.Alert;
import cm.smith.android.smsresponder.model.AlertResponse;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * This class is used to send out SMS "Alert" messages every interval to users who haven't
 * responded back with the SAFE command.
 *
 * Created by anthony on 2016-05-16.
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm database = Realm.getInstance(realmConfig);

        // Get the alert that's currently running
        final Alert onGoingAlert = database.where(Alert.class)
                .equalTo("resolved", false)
                .findFirst();

        // A complex inefficent way to find all users who HAVE NOT responded
        RealmResults<User> allUsers = database.where(User.class)
                .findAll();
        RealmList<AlertResponse> allResponses = onGoingAlert.getResponses();
        ArrayList<User> userResponses = new ArrayList<User>();
        ArrayList<User> nonResponses = new ArrayList<User>();

        for (AlertResponse alertResponse : allResponses) {
            userResponses.add(alertResponse.getUser());
        }

        for (User user : allUsers) {
            if (!userResponses.contains(user)) {
                nonResponses.add(user);
            }
        }

        if (nonResponses.size() == 0) {
            // TODO: turn off the alarm cause all users have responded with safe! woo hoo!
        } else {
            for (User user : nonResponses) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(user.getPhone(), null, "An alert has been started, " +
                        "please respond with 'SAFE' if you're okay", null, null);
            }
        }
    }

}
