package cm.smith.android.smsresponder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import cm.smith.android.smsresponder.CmdManager;
import cm.smith.android.smsresponder.command.ADDcommand;
import cm.smith.android.smsresponder.command.ALERTcommand;
import cm.smith.android.smsresponder.command.DELETEcommand;
import cm.smith.android.smsresponder.command.LISTcommand;
import cm.smith.android.smsresponder.command.SAFEcommand;
import cm.smith.android.smsresponder.command.TESTcommand;
import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.message.SMSMessenger;

/**
 * This class is used to intercept inbound SMS messages and check if they are potential commands.
 * It only intercepts messages if the user has turned the SMS Listener setting on within the app.
 *
 * Created by anthony on 2016-04-09.
 */
public class SmsReceiver extends BroadcastReceiver {

    private SharedPreferences preferences;

    public SmsReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences(CmdManager.KEY_PREF_FILE, Context.MODE_PRIVATE);
        boolean isRunning = sharedPref.getBoolean(CmdManager.KEY_PREF_RUNNING, false);

        if(isRunning && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
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
                Message message = new SMSMessenger(context);
                CmdManager manager = new CmdManager(message, false);

                // Register the commands we want to use
                manager.registerCommand(new ADDcommand(message));
                manager.registerCommand(new DELETEcommand(message));
                manager.registerCommand(new ALERTcommand(message));
                manager.registerCommand(new SAFEcommand(message));
                manager.registerCommand(new TESTcommand(message));
                manager.registerCommand(new LISTcommand(message));

                manager.checkCommand(msg_from, msg_body);
            }
        }
    }




}