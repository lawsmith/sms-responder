package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.model.Role;

/**
 * Created by anthony on 2016-05-16.
 */
public class TESTcommand extends Command {

    public TESTcommand() {
        super("TEST", Role.GUEST);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        CmdManager.message.sendMessage(senderPhone, "(" + getCommand() + ") " + senderPhone);
        return true;
    }

}
