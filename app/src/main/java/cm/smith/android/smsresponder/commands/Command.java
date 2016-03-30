package cm.smith.android.smsresponder.commands;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.R;

/**
 * Created by anthony on 2016-03-30.
 */
public abstract class Command implements ICommand {

    public static String DEBUG_KEY = "COMMAND";
    private String command;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    @Override
    public boolean parseInput(String phoneNum, String input) {
        String arr[] = input.split(" ", 2);

        if (arr.length == 2) {
            String command = arr[0];
            String arguments = arr[1];

            if (command.equals(getCommand())) {
                return this.execute(phoneNum, arguments);
            } else {
                CmdManager.message.sendMessage(R.string.error_command_invalid);
                return false;
            }
        } else {
            CmdManager.message.sendMessage(R.string.error_command_invalid);
            return false;
        }
    }

}
