package cm.smith.android.smsresponder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import cm.smith.android.smsresponder.command.ADDcommand;
import cm.smith.android.smsresponder.command.DELETEcommand;
import cm.smith.android.smsresponder.message.SMSMessenger;

/**
 * Created by anthony on 2016-04-09.
 */
public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    public SmsListener() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = null;
            String msg_body = null;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        if (msg_body == null) {
                            msg_body = msgs[i].getMessageBody();
                        } else {
                            msg_body += msgs[i].getMessageBody();
                        }
                    }
                }catch(Exception e){
                    // TODO: Add error message to custom logging system
                    Log.d("Exception caught",e.getMessage());
                }

                // Setup Command Manager and the output stream to use
                CmdManager manager = new CmdManager(new SMSMessenger(context), false);

                // Register the commands we want to use
                manager.registerCommand(new ADDcommand());
                manager.registerCommand(new DELETEcommand());

                manager.checkCommand(msg_from, msg_body);
            }
        }
    }
}