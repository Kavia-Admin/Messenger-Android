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

package org.mesibo.messenger.Utils;

import android.content.Context;
import android.view.View;

import org.mesibo.messenger.UIManager;

// Example skeleton for wiring edit/delete in the adapter
public class AppUtils {
    // This is a utility method for binding message view
    // Integrate this logic for edit/delete support in your message UI (e.g. adapter)

    public static void bindMessageView(View messageView, Message msg, Context ctx) {
        View optionsBtn = messageView.findViewById(R.id.msg_options_btn); // btn for msg actions
        if (msg.isOwnMessage()) {
            optionsBtn.setVisibility(View.VISIBLE);
            optionsBtn.setOnClickListener(v -> {
                UIManager.showMessageOptions(ctx, v, msg,
                    () -> promptEditMessage(ctx, msg),
                    () -> confirmAndDeleteMessage(ctx, msg)
                );
            });
        } else {
            optionsBtn.setVisibility(View.GONE);
        }
    }

    private static void promptEditMessage(Context ctx, Message msg) {
        // Show your preferred dialog to edit text, then call:
        UIManager.editMessageApi(ctx, msg.getId(), "edited text", () -> {
            // Update message in UI (e.g. set as edited)
        }, () -> {
            // Show error
        });
    }

    private static void confirmAndDeleteMessage(Context ctx, Message msg) {
        // Show confirmation dialog, then:
        UIManager.deleteMessageApi(ctx, msg.getId(), () -> {
            // UI: mark message as deleted ("Message deleted")
        }, () -> {
            // Show error
        });
    }
}
