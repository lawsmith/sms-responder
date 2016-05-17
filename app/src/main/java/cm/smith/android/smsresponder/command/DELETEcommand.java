package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.Role;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;

/**
 * Created by anthony on 2016-03-30.
 */
public class DELETEcommand extends Command {

    public DELETEcommand(Message message) {
        super("DELETE", Role.ADMIN, message);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        // Validate that the input has at least the right number of arguments
        String arr[] = arguments.split(" ");
        if (arguments.isEmpty() || arr.length != 1) {
            getMessage().sendMessage(senderPhone, "usage: " + getCommand() + " 1234567890");
            return false;
        }

        final String phoneNum = arr[0];
        final User user = getDatabase().where(User.class)
                .equalTo("phone", phoneNum)
                .findFirst();

        // If user doesn't exist display error
        if (user == null) {
            getMessage().sendMessage(senderPhone, "(" + getCommand() + ") User: " + phoneNum + " not found.");
            return false;
        }

        getDatabase().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.removeFromRealm();
            }
        });

        getMessage().sendMessage(senderPhone, "(" + getCommand() + ") " + phoneNum + " removed successfully");
        return true;
    }

}
