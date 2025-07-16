package org.mesibo.messenger;

public class SampleAPI {

    public static final int MESSAGE_TYPE_TEXT = 1;
    public static final int MESSAGE_TYPE_IMAGE = 2;
    // ... (existing code)

    /**
     * Message model to include expiry
     */
    public static class Message {
        public String from;
        public String to;
        public String message;
        public int type;
        public String data;
        public long timestamp;
        public Long expiryTs; // Null = never expires
        public String status;
        // ... Add other fields as per app
        
        // Returns number of seconds left until expire, or null.
        public Long getSecondsLeft() {
            if (expiryTs == null) return null;
            long now = System.currentTimeMillis()/1000L;
            return expiryTs > now ? expiryTs - now : 0;
        }

        public boolean isExpired() {
            Long left = getSecondsLeft();
            return (left != null && left <= 0);
        }
    }

    // Modified: sendMessage to support expiry
    /**
     * PUBLIC_INTERFACE
     * Send a message, with optional expiry (TTL in seconds; if null or zero = never expires).
     */
    public static void sendMessage(Profile user, String message, Integer ttlSeconds) {
        // Construct params with TTL if provided
        // Use network/API code to send 'expiry' param to backend
        // Example: send param "expiry" with value ttlSeconds to server's send_message endpoint
        // On success, store expiryTs if returned
    }
    
    // ... rest of the class remains unchanged ...
}
