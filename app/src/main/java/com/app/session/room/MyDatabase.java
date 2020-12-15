package com.app.session.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase
{

    public abstract TaskDAO taskDAO();

}
