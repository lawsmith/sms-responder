package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.Role;
import cm.smith.android.smsresponder.model.User;
import io.realm.RealmResults;

/**
 * Created by anthony on 2016-05-16.
 */
public class LISTcommand extends Command {

    public LISTcommand(Message message) {
        super("LIST", Role.ADMIN, message);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        RealmResults<User> allUsers = getDatabase().where(User.class)
                .findAll();

        String output = "";

        for (User user : allUsers) {
            output += "\n" + user.getName() + " | " + user.getPhone() + " | " + user.getUserRole();
        }

        getMessage().sendMessage(senderPhone, "(" + getCommand() + ") " + output);

        return true;
    }

}
