package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;

/**
 * Created by anthony on 2016-03-30.
 */
public class ADDcommand extends Command {

    public ADDcommand() {
        super("ADD");
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
        String arr[] = arguments.split(" ");
        if (arr.length != 2 && arr.length != 3) {
            CmdManager.message.sendMessage("usage: " + getCommand() + " 1234567890 name [role]");
            return false;
        }

        String phoneNum = arr[0];
        String name = arr[1];
        User.Role role = User.Role.MEMBER;

        // Check if the input wants to set the role of the user
        if (arr.length == 3) {
            try {
                role = User.Role.fromString(arr[2]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        final User tempUser = new User();
        tempUser.setName(name);
        tempUser.setPhone(phoneNum);
        tempUser.setUserRole(role.getValue());

        getDatabase().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tempUser);
            }
        });

        CmdManager.message.sendMessage(getCommand() + ": " + name + " added successfully");
        return true;
    }

}
