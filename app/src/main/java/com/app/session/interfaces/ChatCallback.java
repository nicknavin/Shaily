package com.app.session.interfaces;

import android.view.View;

import com.app.session.model.ChatMessage;

public interface ChatCallback {
    void result(ChatMessage chatMessage, int position, View view);

}
