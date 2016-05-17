package cm.smith.android.smsresponder;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import cm.smith.android.smsresponder.command.ADDcommand;
import cm.smith.android.smsresponder.command.ALERTcommand;
import cm.smith.android.smsresponder.command.DELETEcommand;
import cm.smith.android.smsresponder.command.LISTcommand;
import cm.smith.android.smsresponder.command.SAFEcommand;
import cm.smith.android.smsresponder.command.TESTcommand;
import cm.smith.android.smsresponder.message.Message;
import cm.smith.android.smsresponder.message.ToastMessenger;

public class MainActivity extends AppCompatActivity {

    CmdManager manager;

    EditText input;
    Button button;
    Switch listenerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Command Manager and the output stream to use
        Message message = new ToastMessenger(this);
        manager = new CmdManager(message, true);

        // Register the commands we want to use
        manager.registerCommand(new ADDcommand(message));
        manager.registerCommand(new DELETEcommand(message));
        manager.registerCommand(new ALERTcommand(message));
        manager.registerCommand(new SAFEcommand(message));
        manager.registerCommand(new TESTcommand(message));
        manager.registerCommand(new LISTcommand(message));


        // Testing Purposes
        input = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        listenerSwitch = (Switch)findViewById(R.id.smslistener_switch);

        listenerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.this.setListeningStatus(isChecked);
            }
        });
        setListeningStatus(getListeningStatus());

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
    }

    public void onClickButton(View v) {
        boolean output = manager.checkCommand("5555555555", input.getText().toString());
    }

    public void setListeningStatus(boolean isChecked) {
        if (isChecked) {
            listenerSwitch.setText("SMS Listener is running");
            CmdManager.makeNotification(getApplicationContext());
        } else {
            listenerSwitch.setText("SMS Listener is not running");
            CmdManager.cancelNotification(getApplicationContext());
        }
        listenerSwitch.setChecked(isChecked);
        saveListeningStatus(isChecked);
    }

    private boolean getListeningStatus() {
        SharedPreferences sharedPref = this.getSharedPreferences(CmdManager.KEY_PREF_FILE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CmdManager.KEY_PREF_RUNNING, false);
    }

    private void saveListeningStatus(boolean status) {
        SharedPreferences sharedPref = this.getSharedPreferences(CmdManager.KEY_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CmdManager.KEY_PREF_RUNNING, status);
        editor.apply();
    }

}
