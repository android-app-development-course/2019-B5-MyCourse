package com.android.mycourse.experiment;

import android.os.Parcel;
import android.os.Parcelable;

public class ExperimentBean implements Parcelable {

    private long id;
    private String semester;
    private String course;
    private String name;
    private String time;
    private String content;

    ExperimentBean() {
    }

    private ExperimentBean(Parcel in) {
        id = in.readLong();
        semester = in.readString();
        course = in.readString();
        name = in.readString();
        time = in.readString();
        content = in.readString();
    }

    public static final Creator<ExperimentBean> CREATOR = new Creator<ExperimentBean>() {
        @Override
        public ExperimentBean createFromParcel(Parcel in) {
            return new ExperimentBean(in);
        }

        @Override
        public ExperimentBean[] newArray(int size) {
            return new ExperimentBean[size];
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeString(content);
    }
}
