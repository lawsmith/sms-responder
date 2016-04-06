package cm.smith.android.smsresponder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cm.smith.android.smsresponder.command.ADDcommand;
import cm.smith.android.smsresponder.command.DELETEcommand;
import cm.smith.android.smsresponder.message.ToastMessage;
import cm.smith.android.smsresponder.model.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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
        manager.registerCommand(new DELETEcommand());

        // Testing Purposes
        input = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);


        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm realm = Realm.getInstance(realmConfig);
        RealmQuery<User> query = realm.where(User.class);
        RealmResults<User> result = query.findAll();
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();

    }

    public void onClickButton(View v) {
        boolean output = manager.checkCommand("5555555555", input.getText().toString());
    }

}
