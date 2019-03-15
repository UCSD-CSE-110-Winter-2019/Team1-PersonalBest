package edu.ucsd.cse110.team1_personalbest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.R;

public class FriendsListActivity extends AppCompatActivity {
    private List<String> FriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        User cur = UserSession.getCurrentUser();
        this.FriendsList = cur.getFriends();

        Collections.sort(FriendsList);

        Button btnAddFriends = (Button) findViewById(R.id.buttonAddFriend);
        btnAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        Button btnBackMain = (Button) findViewById(R.id.buttonBackMain);
        btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, FriendsList);
        ListView listView = (ListView) findViewById(R.id.friends_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = ((TextView) view).getText().toString();
                Intent intent = new Intent(getApplicationContext(), ActivityOfFriendActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }


}
