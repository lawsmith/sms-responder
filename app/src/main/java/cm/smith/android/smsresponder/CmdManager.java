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
        if (arr.length != 2) {
            CmdManager.message.sendMessage(inputPhone, R.string.error_input_invalid);
            return false;
        }
        String commandString = arr[0];
        String argumentString = arr[1];

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

}
