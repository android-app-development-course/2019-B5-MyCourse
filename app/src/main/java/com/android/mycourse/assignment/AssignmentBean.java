package com.android.mycourse.assignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.TimeUnit;

public class AssignmentBean implements Parcelable {

    private long id;
    private String semester;
    private String course;
    private String title;
    private String time;
    private String content;

    public AssignmentBean() {

    }

    private AssignmentBean(Parcel in) {
        id = in.readLong();
        semester = in.readString();
        course = in.readString();
        title = in.readString();
        time = in.readString();
        content = in.readString();
    }

    public static final Creator<AssignmentBean> CREATOR = new Creator<AssignmentBean>() {
        @Override
        public AssignmentBean createFromParcel(Parcel in) {
            return new AssignmentBean(in);
        }

        @Override
        public AssignmentBean[] newArray(int size) {
            return new AssignmentBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(semester);
        parcel.writeString(course);
        parcel.writeString(title);
        parcel.writeString(time);
        parcel.writeString(content);
    }
}
