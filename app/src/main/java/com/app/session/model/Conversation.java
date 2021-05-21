package com.app.session.model;

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
