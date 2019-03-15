package edu.ucsd.cse110.team1_personalbest.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.R;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Button btnSendFriends = (Button) findViewById(R.id.buttonSendFriends);
        EditText email = findViewById(R.id.enterEmail);
        final Activity a = this;
        btnSendFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Send a friend request";
                int duration = Toast.LENGTH_SHORT;
                UserSession.addFriend(email.getText().toString(), a);
                email.setText("");
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Button btnCancelAdd = (Button) findViewById(R.id.buttonCancelAddFriend);
        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsListActivity.class);
                startActivity(intent);
            }
        });
    }
}
