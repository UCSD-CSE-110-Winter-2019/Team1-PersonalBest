package edu.ucsd.cse110.team1_personalbest.Messaging;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.Query;

import java.util.Map;

public interface IMessagingService {
    void sendMessage(final Map<String,String> newMessage, final EditText messageView, final String DOCUMENT_KEY);
    void init(final TextView textView, final Query.Direction messageOrder, final String DOCUMENT_KEY);
    void removeListener();
}

