package com.android.mycourse.notepad;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteBean implements Parcelable {

    private long id;        // 备忘Id
    private String time;    // 备忘创建时间
    private String title;   // 备忘标题
    private String content; // 备忘内容

    NoteBean() {
    }

    private NoteBean(Parcel in) {
        id = in.readLong();
        time = in.readString();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<NoteBean> CREATOR = new Creator<NoteBean>() {
        @Override
        public NoteBean createFromParcel(Parcel in) {
            return new NoteBean(in);
        }

        @Override
        public NoteBean[] newArray(int size) {
            return new NoteBean[size];
        }
    };

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    String getTime() {
        return time;
    }

    void setTime(String time) {
        this.time = time;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(time);
        parcel.writeString(title);
        parcel.writeString(content);
    }
}
