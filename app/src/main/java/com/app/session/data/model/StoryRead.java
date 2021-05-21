package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class StoryRead implements Parcelable
{
    @SerializedName("count")
    @Nullable
   private int count;

   public StoryRead()
    {

    }

    protected StoryRead(Parcel in) {
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StoryRead> CREATOR = new Creator<StoryRead>() {
        @Override
        public StoryRead createFromParcel(Parcel in) {
            return new StoryRead(in);
        }

        @Override
        public StoryRead[] newArray(int size) {
            return new StoryRead[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
