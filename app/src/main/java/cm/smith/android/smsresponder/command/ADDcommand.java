package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.Role;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anthony on 2016-03-30.
 */
public class ADDcommand extends Command {

    public ADDcommand(Message message) {
        super("ADD", Role.ADMIN, message);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        // Validate that the input has at least the right number of arguments
        String arr[] = arguments.split(" ");
        if (arguments.isEmpty() || (arr.length != 2 && arr.length != 3)) {
            getMessage().sendMessage(senderPhone, "usage: " + getCommand() + " 1234567890 name [role]");
            return false;
        }

        String phoneNum = arr[0];
        String name = arr[1];
        Role role = Role.MEMBER;

        // Make sure that the phone number isn't already in use
        if (User.checkIfExists(getDatabase(), phoneNum)) {
            getMessage().sendMessage(senderPhone, "(" + getCommand() + ") '" + phoneNum + "' is already in use");
            return false;
        }

        // Make sure that the username isn't too long
        if (name.length() > 15) {
            getMessage().sendMessage(senderPhone, "(" + getCommand() + ") Sorry, that name is too long :(");
            return false;
        }

        // Check if the input wants to set the role of the user
        if (arr.length == 3) {
            try {
                role = Role.fromString(arr[2]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        final RealmResults<User> allUsers = getDatabase().where(User.class)
                .findAll();
        final User tempUser = new User();
        tempUser.setId(allUsers.size() + 1);
        tempUser.setName(name);
        tempUser.setPhone(phoneNum);
        tempUser.setUserRole(role.getValue());

        getDatabase().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tempUser);
            }
        });

        getMessage().sendMessage(senderPhone, "(" + getCommand() + ") " + name + " added successfully as a " + tempUser.getUserRole());
        return true;
    }

}
