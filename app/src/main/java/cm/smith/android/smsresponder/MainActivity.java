package cm.smith.android.smsresponder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cm.smith.android.smsresponder.commands.ADDcommand;
import cm.smith.android.smsresponder.messaging.ToastMessage;

public class MainActivity extends AppCompatActivity {

    CmdManager manager;

    EditText input;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Command Manager and the output stream to use
        manager = new CmdManager(new ToastMessage(this));

        // Register the commands we want to use
        manager.registerCommand(new ADDcommand());

        // Testing Purposes
        input = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
    }

    public void onClickButton(View v) {
        boolean output = manager.checkCommand("6045555555", input.getText().toString());
    }
}
