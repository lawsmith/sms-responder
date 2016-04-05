package cm.smith.android.smsresponder.command;

/**
 * Created by anthony on 2016-03-30.
 */
public interface ICommand {

    boolean execute(String senderPhone, String args);

}
