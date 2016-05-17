package cm.smith.android.smsresponder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.util.ArrayList;

import cm.smith.android.smsresponder.command.Command;
import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by anthony on 2016-03-30.
 */
public class CmdManager {

    public static final String KEY_PREF_FILE = "pref_file";
    public static final String KEY_PREF_RUNNING = "pref_running_key";
    public static final Integer KEY_NOTIFICATION = 343434;

    public static Message message;
    ArrayList<Command> allCommands;
    private Realm database;
    private Boolean bypassPermissions;

    public CmdManager(Message msg, Boolean bypassPermissions) {
        CmdManager.message = msg;
        allCommands = new ArrayList<>();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(CmdManager.message.getContext()).build();
        database = Realm.getInstance(realmConfig);

        this.bypassPermissions = bypassPermissions;
    }

    /**
     * Adds a command to the currently registered list. Only commands that are registered will be
     * checked.
     * @param command
     */
    public void registerCommand(Command command) {
        allCommands.add(command);
    }

    /**
     * Validates an input command and phone number to see if:
     * 1) The input phone number belongs to a user with the right privileges
     * 2) The input command is valid
     * @param inputPhone
     * @param inputCommand
     * @return true is the command was successfully executed
     */
    public boolean checkCommand(String inputPhone, String inputCommand) {
        if (!bypassPermissions) {
            // Check to if a registered user exists
            if (!validUserByPhoneNum(inputPhone)) {
                // TODO: Log an invalid attempt
                return false;
            }
        }

        // ERROR: no commands registered
        if (allCommands.size() == 0) {
            CmdManager.message.sendMessage(inputPhone, R.string.error_configuration);
            return false;
        }

        // Parse the input into Command and Arguments
        String arr[] = inputCommand.split(" ", 2);
        String commandString = "";
        String argumentString = "";

        if (arr.length >= 1) {
            commandString = arr[0];
        }
        if (arr.length >= 2) {
            argumentString = arr[1];
        }

        // We're clear to start checking input command against registered ones
        Command foundCmd = findCommand(commandString);
        if (foundCmd == null) {
            CmdManager.message.sendMessage(inputPhone, R.string.error_command_invalid);
            return false;
        }

        if (!bypassPermissions) {
            // Make sure the sender is authorized to run this command
            User sender = database.where(User.class)
                    .equalTo("userRole", foundCmd.getPermissionLevel())
                    .equalTo("phone", inputPhone)
                    .findFirst();

            if (sender == null) {
                CmdManager.message.sendMessage(inputPhone, "Sorry, you don't have sufficient permissions.");
                return false;
            }
        }

        return foundCmd.execute(inputPhone, argumentString);
    }

    /**
     * Checks to see if a phone number is registered to a user
     * @param phoneNum Phone number to search for
     * @return true if a user is found with the input phone number
     */
    private Boolean validUserByPhoneNum(String phoneNum) {
        User sender = database.where(User.class)
                .equalTo("phone", phoneNum)
                .findFirst();

        if (sender == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Searches through all registered commands to see if the input string is a match
     * @param command Command in string format to search for
     * @return the corresponding command object that matches the input string
     */
    private Command findCommand(String command) {
        for (Command cmd :
                allCommands) {
            if (cmd.getCommand().equals(command)) {
                return cmd;
            }
        }
        return null;
    }

    /**
     * Shows a persistent notification to notify the user that the background SmsListener is running
     * @param context
     */
    public static void makeNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                CmdManager.KEY_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("SMS Responder is Running")
                .setContentText("The app will intercept all SMS messages")
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.alert_dark_frame))
                ;
        Notification n;

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        // Show the notification
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(CmdManager.KEY_NOTIFICATION, n);
    }

    /**
     * Cancels the persistent notification, used when the SmsListener is turned off
     * @param context
     */
    public static void cancelNotification(Context context) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(CmdManager.KEY_NOTIFICATION);
    }

}
