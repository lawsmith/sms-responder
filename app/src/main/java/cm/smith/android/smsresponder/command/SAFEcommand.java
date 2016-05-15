package cm.smith.android.smsresponder.command;

import java.util.Date;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.model.Alert;
import cm.smith.android.smsresponder.model.AlertResponse;
import cm.smith.android.smsresponder.model.Role;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anthony on 2016-05-14.
 */
public class SAFEcommand extends Command {

    public SAFEcommand() {
        super("SAFE", Role.MEMBER);
    }

    @Override
    public boolean execute(final String senderPhone, final String arguments) {
        // Check if there is an alert currently in progress
        final Alert onGoingAlert = getDatabase().where(Alert.class)
                .equalTo("resolved", false)
                .findFirst();

        if (onGoingAlert == null) {
            CmdManager.message.sendMessage(senderPhone, getCommand() + ": there is no alert currently in progress");
            return true;
        } else {
            final User user = getDatabase().where(User.class)
                    .equalTo("phone", senderPhone)
                    .findFirst();

            if (user == null) {
                CmdManager.message.sendMessage(senderPhone, getCommand() + ": the phone can't call itself safe");
                return false;
            }

            AlertResponse existingResponse = null;
            for (AlertResponse response : onGoingAlert.getResponses()) {
                if (response.getUser().equals(user)) {
                    existingResponse = response;
                    break;
                }
            }
            if (existingResponse != null) {
                CmdManager.message.sendMessage(senderPhone, getCommand() + ": you have already responded to the current alert");
                return true;
            }

            final RealmResults<AlertResponse> allAlertResponses = getDatabase().where(AlertResponse.class)
                    .findAll();
            final AlertResponse tempAlert = new AlertResponse();
            tempAlert.setDate(new Date());
            tempAlert.setUser(user);
            tempAlert.setId(allAlertResponses.size() + 1);

            getDatabase().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    onGoingAlert.addResponse(tempAlert);
                    realm.copyToRealmOrUpdate(tempAlert);
                }
            });

            CmdManager.message.sendMessage(senderPhone, getCommand() + ": response successfully saved");
            return true;
        }
    }

}
