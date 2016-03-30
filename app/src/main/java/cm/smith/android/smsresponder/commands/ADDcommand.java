package cm.smith.android.smsresponder.commands;

import cm.smith.android.smsresponder.CmdManager;

/**
 * Created by anthony on 2016-03-30.
 */
public class ADDcommand extends Command {

    public ADDcommand() {
        super("ADD");
    }

    @Override
    public boolean execute(String phoneNum, String arguments) {
        CmdManager.message.sendMessage("ADD: " + arguments + " AUTHORIZE: " + phoneNum);
        return true;
    }

}
