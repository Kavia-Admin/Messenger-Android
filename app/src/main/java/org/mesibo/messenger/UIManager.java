package org.mesibo.messenger;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UIManager: Handles core UI behavior including chat message list and reactions.
 */
public class UIManager {
    private Context context;
    private SampleAPI api;
    private int myUserId; // Current logged-in user

    public UIManager(Context context, SampleAPI api, int myUserId) {
        this.context = context;
        this.api = api;
        this.myUserId = myUserId;
    }

    // PUBLIC_INTERFACE
    // Called when user long-presses on a message and selects a reaction
    public void onAddReactionToMessage(int messageId, String reaction) {
        api.addReaction(myUserId, messageId, reaction, response -> {
            // Show feedback
            Toast.makeText(context, "Reacted!", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(context, "Error adding reaction", Toast.LENGTH_SHORT).show();
        });
        // UI update is through realtime after API; optionally update locally
    }

    // PUBLIC_INTERFACE
    // Called when user wishes to remove a reaction
    public void onRemoveReactionFromMessage(int messageId, String reaction) {
        api.removeReaction(myUserId, messageId, reaction, response -> {
            Toast.makeText(context, "Reaction removed!", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(context, "Error removing reaction", Toast.LENGTH_SHORT).show();
        });
    }

    // PUBLIC_INTERFACE
    // To fetch and display reactions for messages (call when displaying message, or on update)
    public void fetchAndApplyReactions(List<Integer> messageIds, MessageReactionListener listener) {
        api.getReactions(messageIds, response -> {
            try {
                JSONObject resp = new JSONObject(response);
                if ("success".equals(resp.optString("result"))) {
                    JSONObject reactions = resp.optJSONObject("reactions");
                    // Each key: message_id, value: array of reactions
                    listener.onReactionsFetched(reactions);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }, error -> {}); // Optionally handle error
    }

    // PUBLIC_INTERFACE
    // Real-time update handler - call this when reaction was broadcast by server
    public void onRealtimeReactionUpdate(JSONObject event) {
        // event: {event: 'reaction_added'/removed, message_id, user_id, reaction}
        // Find message UI, update aggregated display, etc.
    }

    // ... Other UIManager code ...

    /**
     * Listener for applying reactions in message adapter/UI.
     */
    public interface MessageReactionListener {
        void onReactionsFetched(JSONObject reactions);
    }
}
