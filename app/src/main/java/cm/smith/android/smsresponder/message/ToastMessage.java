package cm.smith.android.smsresponder.message;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by anthony on 2016-03-30.
 */
public class ToastMessage extends Message {

    public ToastMessage(Context ctx) {
        super(ctx);
    }

    @Override
    public void sendMessage(String phoneNum, int id) {
        String message = getContext().getString(id);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendMessage(String phoneNum, String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
