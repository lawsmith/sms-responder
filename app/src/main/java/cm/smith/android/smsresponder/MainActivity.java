package cm.smith.android.smsresponder;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cm.smith.android.smsresponder.command.ADDcommand;
import cm.smith.android.smsresponder.command.ALERTcommand;
import cm.smith.android.smsresponder.command.DELETEcommand;
import cm.smith.android.smsresponder.command.SAFEcommand;
import cm.smith.android.smsresponder.message.ToastMessenger;

public class MainActivity extends AppCompatActivity {

    CmdManager manager;

    EditText input;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Command Manager and the output stream to use
        manager = new CmdManager(new ToastMessenger(this), true);

        // Register the commands we want to use
        manager.registerCommand(new ADDcommand());
        manager.registerCommand(new DELETEcommand());
        manager.registerCommand(new ALERTcommand());
        manager.registerCommand(new SAFEcommand());

        // Testing Purposes
        input = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
    }

    public void onClickButton(View v) {
        boolean output = manager.checkCommand("5555555555", input.getText().toString());
    }

}
