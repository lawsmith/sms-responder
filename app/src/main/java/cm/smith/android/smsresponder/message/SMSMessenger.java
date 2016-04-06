package cm.smith.android.smsresponder.message;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Created by anthony on 2016-03-30.
 */
public class SMSMessenger extends Message {

    public SMSMessenger(Context ctx) {
        super(ctx);
    }

    @Override
    public void sendMessage(String phoneNum, int id) {
        String message = getContext().getString(id);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, message, null, null);
    }

    @Override
    public void sendMessage(String phoneNum, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, message, null, null);
    }

}
