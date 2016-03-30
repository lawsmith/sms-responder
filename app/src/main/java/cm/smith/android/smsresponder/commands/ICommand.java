package cm.smith.android.smsresponder.commands;

/**
 * Created by anthony on 2016-03-30.
 */
public interface ICommand {

    boolean parseInput(String phoneNum, String input);
    boolean execute(String phoneNum, String args);

}
