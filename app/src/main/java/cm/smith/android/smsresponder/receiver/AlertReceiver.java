package cm.smith.android.smsresponder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * This class is used to send out SMS "Alert" messages every interval to users who haven't
 * responded back with the SAFE command.
 *
 * Created by anthony on 2016-05-16.
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
    }

}
