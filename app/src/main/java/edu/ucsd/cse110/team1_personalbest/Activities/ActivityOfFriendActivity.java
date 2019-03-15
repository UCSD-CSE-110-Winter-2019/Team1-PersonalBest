package edu.ucsd.cse110.team1_personalbest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsd.cse110.team1_personalbest.R;

public class ActivityOfFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_of_friend);
        TextView txtProduct = (TextView) findViewById(R.id.FriendName);

        Intent i = getIntent();
        String name = i.getStringExtra("name" );
        String act = "'s Activity";
        String nameAct = name + act;
        txtProduct.setText(nameAct);

        Button btnToMessage = (Button) findViewById(R.id.buttonToMessage);
        btnToMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        Button historyButton = (Button) findViewById(R.id.friendHistoryButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivityGraph.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        Button btnBackFriendList = (Button) findViewById(R.id.buttonBackFriendList);
        btnBackFriendList .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsListActivity.class);
                startActivity(intent);
            }
        });
    }
}
