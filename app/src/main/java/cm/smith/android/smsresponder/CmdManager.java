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
    
    public boolean checkCommand(String senderPhone, String input) {
        // ERROR: no commands registered
        if (allCommands.size() == 0) {
            CmdManager.message.sendMessage(R.string.error_configuration);
            return false;
        }

        // ELSE, run through registered commands
        for (Command cmd :
                allCommands) {
            return cmd.parseInput(senderPhone, input);
        }

        CmdManager.message.sendMessage(R.string.error_other);
        return true;
    }

}
