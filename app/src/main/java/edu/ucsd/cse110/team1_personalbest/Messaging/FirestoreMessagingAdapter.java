package edu.ucsd.cse110.team1_personalbest.Messaging;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Map;

public class FirestoreMessagingAdapter implements IMessagingService {
    private final String COLLECTION_KEY = "chats";
    private final String MESSAGES_KEY = "messages";
    public static final String FROM_KEY = "from";
    public static final String TEXT_KEY = "text";
    private final String TIMESTAMP_KEY = "timestamp";
    private CollectionReference chat;
    private static final String TAG = "[FSMessaging]";
    private ListenerRegistration reg;

    private void setChat(final String DOCUMENT_KEY) {
        this.chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);
    }
    @Override
    public void sendMessage(final Map<String,String> newMessage, final EditText messageView,
                            final String DOCUMENT_KEY) {
        setChat(DOCUMENT_KEY);
        this.chat.add(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void init(final TextView textView, final Query.Direction messageOrder,
                     final String DOCUMENT_KEY) {
        setChat(DOCUMENT_KEY);
        this.reg = chat.orderBy(TIMESTAMP_KEY, messageOrder)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                        documentChanges.forEach(change -> {
                            QueryDocumentSnapshot document = change.getDocument();
                            sb.append(document.get(FROM_KEY));
                            sb.append(":\n");
                            sb.append(document.get(TEXT_KEY));
                            sb.append("\n");
                            sb.append("---\n");
                        });
                        textView.append(sb.toString());
                    }
                });
    }

    @Override
    public void removeListener() {
        reg.remove();
    }

    public static void subscribe(final Activity activity, final String DOCUMENT_KEY) {
        FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                        }
                );
    }
}

