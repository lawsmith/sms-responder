package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.Role;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by anthony on 2016-03-30.
 */
public abstract class Command implements ICommand {

    public static String DEBUG_KEY = "COMMAND";

    private Message message;
    private String command;
    private Role permissionLevel;

    private Realm database;

    public Command(String command, Role permissionLevel, Message message) {
        this.command = command;
        this.permissionLevel = permissionLevel;
        setMessage(message);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getMessage().getContext()).build();
        database = Realm.getInstance(realmConfig);
    }

    /**
     * Returns the command as a string
     * @return command keyword as a string
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Returns the minimum permission role required for the command
     * @return minimum permission role as a string
     */
    public Role getPermissionLevel() {
        return this.permissionLevel;
    }

    /**
     * Returns the mobile database specified
     * @return the mobile database specified
     */
    public Realm getDatabase() {
        return this.database;
    }

    /**
     * Sets the message handler to be used
     * @param message message handler to be used
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * Returns the message handler to send text to
     * @return the message handler to send text to
     */
    public Message getMessage() {
        return this.message;
    }

}
