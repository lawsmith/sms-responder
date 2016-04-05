package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;

/**
 * Created by anthony on 2016-03-30.
 */
public class DELETEcommand extends Command {

    public DELETEcommand() {
        super("DELETE");
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        // Make sure the sender is authorized to run this command
        User sender = getDatabase().where(User.class)
                .equalTo("userRole", User.Role.ADMIN.getValue())
                .equalTo("phone", senderPhone)
                .findFirst();
        if (sender == null) {
            CmdManager.message.sendMessage("Sorry, you don't have sufficient permissions.");
            return false;
        }

        // Validate that the input has at least the right number of arguments
        String arr[] = arguments.split(" ", 2);
        if (arr.length != 1) {
            CmdManager.message.sendMessage("Invalid Command: '" + getCommand() + "' with Args: '" + arguments + "'");
            return false;
        }

        final String phoneNum = arr[0];
        final User user = getDatabase().where(User.class)
                .equalTo("phone", phoneNum)
                .findFirst();

        getDatabase().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.removeFromRealm();
            }
        });

        CmdManager.message.sendMessage("(" + getCommand() + ") " + phoneNum + " removed successfully");
        return true;
    }

}
