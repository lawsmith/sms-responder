package cm.smith.android.smsresponder;

import java.util.ArrayList;

import cm.smith.android.smsresponder.command.Command;
import cm.smith.android.smsresponder.message.Message;

/**
 * Created by anthony on 2016-03-30.
 */
public class CmdManager {

    public static Message message;
    ArrayList<Command> allCommands;

    public CmdManager(Message msg) {
        CmdManager.message = msg;
        allCommands = new ArrayList<>();
    }

    public void registerCommand(Command cmd) {
        allCommands.add(cmd);
    }
    
    public void checkCommand(String senderPhone, String input) {
        // ERROR: no commands registered
        if (allCommands.size() == 0) {
            CmdManager.message.sendMessage(R.string.error_configuration);
            return;
        }

        // ELSE, run through registered commands
        boolean foundCmd = false;
        for (Command cmd :
                allCommands) {
            if (!foundCmd) {
                foundCmd = cmd.parseInput(senderPhone, input);
            } else {
                break;
            }
        }

        if (!foundCmd) {
            CmdManager.message.sendMessage(R.string.error_command_invalid);
        }
    }

}
