package cm.smith.android.smsresponder.command;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;

import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.Alert;
import cm.smith.android.smsresponder.model.AlertResponse;
import cm.smith.android.smsresponder.model.Role;
import cm.smith.android.smsresponder.model.User;
import cm.smith.android.smsresponder.receiver.AlertReceiver;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by anthony on 2016-05-11.
 */
public class ALERTcommand extends Command {

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    public ALERTcommand(Message message) {
        super("ALERT", Role.ADMIN, message);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        // Validate that the input has at least the right number of arguments
        String arr[] = arguments.split(" ");
        if (arguments.isEmpty() || arr.length != 1) {
            getMessage().sendMessage(senderPhone, "usage: " + getCommand() + " start/stop");
            return false;
        }

        // Check if there is an alert currently in progress
        final Alert onGoingAlert = getDatabase().where(Alert.class)
                .equalTo("resolved", false)
                .findFirst();
        final RealmResults<Alert> allAlerts = getDatabase().where(Alert.class)
                .findAll();

        // Start of Stop an alert based on the passed in argument
        if (onGoingAlert == null) {
            if (arr[0].toLowerCase().equals("start")) {
                final Alert tempAlert = new Alert();
                tempAlert.setStartDate(new Date());
                tempAlert.setResolved(false);
                tempAlert.setId(allAlerts.size() + 1);

                getDatabase().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(tempAlert);
                    }
                });

                // send a sms message to everyone, every minute
                setAlarm(getMessage().getContext());

                getMessage().sendMessage(senderPhone, "(" + getCommand() + ") alert started successfully");
                return true;
            }
            else {
                getMessage().sendMessage(senderPhone, "(" + getCommand() + ") error, no alert currently in progress");
                return true;
            }
        } else {
            if (arr[0].toLowerCase().equals("start")) {
                getMessage().sendMessage(senderPhone, "(" + getCommand() + ") alert already in progress, use the [stop] argument to stop it.");
                return true;
            }
            else if (arr[0].toLowerCase().equals("stop")) {
                getDatabase().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        onGoingAlert.setResolved(true);
                        realm.copyToRealmOrUpdate(onGoingAlert);
                    }
                });

                // stop sending sms messages to everyone
                cancelAlarm(getMessage().getContext());

                getMessage().sendMessage(senderPhone, "(" + getCommand() + ") alert stopped successfully");
                return true;
            }
            else if (arr[0].toLowerCase().equals("list")) {
                RealmResults<User> allUsers = getDatabase().where(User.class)
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
                    getMessage().sendMessage(senderPhone, "(" + getCommand() + ") no users have responded");
                    return true;
                } else {
                    String output = "";
                    for (User user : nonResponses) {
                        output += "\n" + user.getName();
                    }
                    getMessage().sendMessage(senderPhone, "(" + getCommand() + ") users not responded:" + output);
                    return true;
                }
            }
            else if (arr[0].toLowerCase().equals("status")) {
                getMessage().sendMessage(senderPhone, "(" + getCommand() + ") Running since " + onGoingAlert.getStartDate());
                return true;
            }
        }

        getMessage().sendMessage(senderPhone, "usage: " + getCommand() + " start/stop/status");
        return false;
    }

    private void setAlarm(Context context) {
        Intent alarmIntent = new Intent(context, AlertReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    public void cancelAlarm(Context context) {
        Intent alarmIntent = new Intent(context, AlertReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

}
