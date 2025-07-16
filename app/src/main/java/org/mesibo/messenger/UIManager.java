/******************************************************************************
* By accessing or copying this work, you agree to comply with the following   *
* terms:                                                                      *
*                                                                             *
* Copyright (c) 2019-2024 mesibo                                              *
* https://mesibo.com                                                          *
* All rights reserved.                                                        *
*                                                                             *
* Redistribution is not permitted. Use of this software is subject to the     *
* conditions specified at https://mesibo.com . When using the source code,    *
* maintain the copyright notice, conditions, disclaimer, and  links to mesibo * 
* website, documentation and the source code repository.                      *
*                                                                             *
* Do not use the name of mesibo or its contributors to endorse products from  *
* this software without prior written permission.                             *
*                                                                             *
* This software is provided "as is" without warranties. mesibo and its        *
* contributors are not liable for any damages arising from its use.           *
*                                                                             *
* Documentation: https://docs.mesibo.com/                                     *
*                                                                             *
* Source Code Repository: https://github.com/mesibo/                          *
*******************************************************************************/

package org.mesibo.messenger;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class UIManager {

    /**
     * Show options (Edit/Delete) for own messages.
     * @param context
     * @param anchorView
     * @param messageObj - message data object (should have isOwnMessage() and getId())
     * @param onEditCallback
     * @param onDeleteCallback
     */
    // PUBLIC_INTERFACE
    public static void showMessageOptions(Context context, View anchorView, final Message messageObj, Runnable onEditCallback, Runnable onDeleteCallback) {
        if (messageObj == null || !messageObj.isOwnMessage()) return;
        PopupMenu popup = new PopupMenu(context, anchorView);
        popup.getMenu().add("Edit");
        popup.getMenu().add("Delete");
        popup.setOnMenuItemClickListener(item -> {
            if ("Edit".equals(item.getTitle())) {
                onEditCallback.run();
                return true;
            }
            if ("Delete".equals(item.getTitle())) {
                onDeleteCallback.run();
                return true;
            }
            return false;
        });
        popup.show();
    }

    /**
     * Sends API call to edit a message.
     * This should use async Retrofit, OkHttp, or preferred client.
     * @param context
     * @param messageId
     * @param newMessage
     * @param onSuccess
     * @param onError
     */
    // PUBLIC_INTERFACE
    public static void editMessageApi(Context context, long messageId, String newMessage, Runnable onSuccess, Runnable onError) {
        // TODO: Use HTTP library to POST JSON to backend (/edit_message)
        Toast.makeText(context, "Message edited (demo)", Toast.LENGTH_SHORT).show();
        if (onSuccess != null) onSuccess.run();
    }

    /**
     * Sends API call to delete a message.
     * @param context
     * @param messageId
     * @param onSuccess
     * @param onError
     */
    // PUBLIC_INTERFACE
    public static void deleteMessageApi(Context context, long messageId, Runnable onSuccess, Runnable onError) {
        // TODO: Use HTTP library to POST JSON to backend (/delete_message)
        Toast.makeText(context, "Message deleted (demo)", Toast.LENGTH_SHORT).show();
        if (onSuccess != null) onSuccess.run();
    }
}
