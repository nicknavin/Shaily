package com.app.session.room;

import java.util.LinkedList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TaskDAO
{
    @Insert
    public void chatInsertion(ChatMessage chatMessage);

    @Query("Select * from ChatMessage")
    List<ChatMessage> getChatMessages();

    @Query("Update ChatMessage set upload = :flag where id = :msgid")
    public void updateChat(boolean flag,String msgid);


    @Query("Delete from ChatMessage")
    public void delete();




}
