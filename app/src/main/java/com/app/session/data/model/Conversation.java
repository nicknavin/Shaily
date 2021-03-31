package com.app.session.data.model;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<MessageChat> listMessageData;

    public Conversation()
    {
        listMessageData = new ArrayList<>();

    }

    public ArrayList<MessageChat> getListMessageData() {
        return listMessageData;
    }
}
