package cm.smith.android.smsresponder.command;

import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.model.Role;

/**
 * Created by anthony on 2016-05-16.
 */
public class TESTcommand extends Command {

    public TESTcommand(Message message) {
        super("TEST", Role.GUEST, message);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        getMessage().sendMessage(senderPhone, "(" + getCommand() + ") " + senderPhone);
        return true;
    }

}
