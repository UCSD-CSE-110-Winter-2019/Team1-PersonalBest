package edu.ucsd.cse110.team1_personalbest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Messaging.FirestoreMessagingAdapter;
import edu.ucsd.cse110.team1_personalbest.Messaging.IMessagingService;
import edu.ucsd.cse110.team1_personalbest.Messaging.MessagingServiceFactory;
import edu.ucsd.cse110.team1_personalbest.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent i = getIntent();
        String name = i.getStringExtra("name" );
        final IMessagingService messagingService = MessagingServiceFactory.createMessagingService();
        User cur = UserSession.getCurrentUser();
        final String doc_key = MessagingServiceFactory.getConversationKey(name, cur.getEmail());
        TextView chat = findViewById(R.id.chat);
        EditText message = findViewById(R.id.text_message);
        messagingService.init(chat, Query.Direction.ASCENDING, doc_key);
        Button btnSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        Button btnCancelMessage = (Button) findViewById(R.id.buttonCancelMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map.put(FirestoreMessagingAdapter.FROM_KEY, cur.getEmail());
                map.put(FirestoreMessagingAdapter.TEXT_KEY, message.getText().toString());
                messagingService.sendMessage(map, message, doc_key);
            }
        });

        btnCancelMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityOfFriendActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }
}
