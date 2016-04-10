package cm.smith.android.smsresponder;

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

    public static Message message;
    ArrayList<Command> allCommands;
    private Realm database;

    public CmdManager(Message msg) {
        CmdManager.message = msg;
        allCommands = new ArrayList<>();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(CmdManager.message.getContext()).build();
        database = Realm.getInstance(realmConfig);
    }

    public void registerCommand(Command cmd) {
        allCommands.add(cmd);
    }
    
    public boolean checkCommand(String senderPhone, String input) {
        // Check to if a registered user exists
        User sender = database.where(User.class)
                .equalTo("phone", senderPhone)
                .findFirst();

        if (sender == null) {
            // TODO: Log an invalid attempt
            return false;
        }

        // ERROR: no commands registered
        if (allCommands.size() == 0) {
            CmdManager.message.sendMessage(senderPhone, R.string.error_configuration);
            return false;
        }

        // Parse the input into Command and Arguments
        String arr[] = input.split(" ", 2);
        if (arr.length != 2) {
            CmdManager.message.sendMessage(senderPhone, R.string.error_input_invalid);
            return false;
        }

        String command = arr[0];
        String arguments = arr[1];

        // We're clear to start checking input command against registered ones
        Command foundCmd = null;
        for (Command cmd :
                allCommands) {
            if (cmd.getCommand().equals(command)) {
                foundCmd = cmd;
                break;
            }
        }

        // Oh darn, no command was found
        if (foundCmd == null) {
            CmdManager.message.sendMessage(senderPhone, R.string.error_command_invalid);
            return false;
        }

        // Finally got to the promised land, execute the command
        return foundCmd.execute(senderPhone, arguments);
    }

}
