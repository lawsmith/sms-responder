package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.CmdManager;
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

}
