package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by anthony on 2016-03-30.
 */
public abstract class Command implements ICommand {

    public static String DEBUG_KEY = "COMMAND";
    private String command;
    private Realm database;

    public Command(String command) {
        this.command = command;

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(CmdManager.message.getContext()).build();
        database = Realm.getInstance(realmConfig);
    }

    public String getCommand() {
        return this.command;
    }

    public Realm getDatabase() {
        return this.database;
    }

    @Override
    public boolean parseInput(String senderPhone, String input) {
        String arr[] = input.split(" ", 2);

        if (arr.length == 2) {
            String command = arr[0];
            String arguments = arr[1];

            if (command.equals(getCommand())) {
                this.execute(senderPhone, arguments);
                return true;
            } else {
                return false;
            }
        } else {
            CmdManager.message.sendMessage(R.string.error_command_invalid);
            return false;
        }
    }

}
