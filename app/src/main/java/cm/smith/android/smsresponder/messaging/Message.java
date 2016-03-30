package cm.smith.android.smsresponder.messaging;

import android.content.Context;

/**
 * Created by anthony on 2016-03-30.
 */
public abstract class Message {

    private Context ctx;

    public Message(Context ctx) {
        this.ctx = ctx;
    }

    public Context getContext() {
        return this.ctx;
    }

    public abstract void sendMessage(int id);

    public abstract void sendMessage(String message);

}
