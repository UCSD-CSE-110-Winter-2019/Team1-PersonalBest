package edu.ucsd.cse110.team1_personalbest.Messaging;

public class MessagingServiceFactory {
    private static boolean TEST_MODE = false;
    private static IMessagingService test_service = null;
    public static final String CHAT_SEPARATOR = "#";

    public static void setTestService(IMessagingService service) {
        test_service = service;
    }
    public static void toggleTestMode() {
        TEST_MODE = true;
    }
    public static IMessagingService createMessagingService(final String u1, final String u2) {
        if (TEST_MODE) {
            return test_service;
        } else {
            return new FirestoreMessagingAdapter();
        }
    }

    public static String getConversationKey(String u1, String u2) {
        // just put the username that comes alphabetically first, first.
        if(u1.compareTo(u2) < 0) {
            return u1 + CHAT_SEPARATOR + u2;
        }
        return u2 + CHAT_SEPARATOR + u1;
    }
}

