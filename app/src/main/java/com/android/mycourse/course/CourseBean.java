package com.android.mycourse.course;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseBean implements Parcelable {

    private long id;
    private String name;
    private String type;
    private String semester;
    private String classWeek;
    private String classTime;
    private String classPlace;
    private String classTeacher;
    private String courseRemarks;
    private String ExperimentWeek;
    private String ExperimentTime;
    private String ExperimentPlace;
    private String ExperimentTeacher;
    private String ExperimentRemarks;
    private String AssignmentRemarks;

    CourseBean() {
    }

    private CourseBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        type = in.readString();
        semester = in.readString();
        classWeek = in.readString();
        classTime = in.readString();
        classPlace = in.readString();
        classTeacher = in.readString();
        courseRemarks = in.readString();
        ExperimentWeek = in.readString();
        ExperimentTime = in.readString();
        ExperimentPlace = in.readString();
        ExperimentTeacher = in.readString();
        ExperimentRemarks = in.readString();
        AssignmentRemarks = in.readString();
    }

    public static final Creator<CourseBean> CREATOR = new Creator<CourseBean>() {
        @Override
        public CourseBean createFromParcel(Parcel in) {
            return new CourseBean(in);
        }

        @Override
        public CourseBean[] newArray(int size) {
            return new CourseBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getClassWeek() {
        return classWeek;
    }

    public void setClassWeek(String classWeek) {
        this.classWeek = classWeek;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public String getCourseRemarks() {
        return courseRemarks;
    }

    public void setCourseRemarks(String courseRemarks) {
        this.courseRemarks = courseRemarks;
    }

    public String getExperimentWeek() {
        return ExperimentWeek;
    }

    public void setExperimentWeek(String experimentWeek) {
        ExperimentWeek = experimentWeek;
    }

    public String getExperimentTime() {
        return ExperimentTime;
    }

    public void setExperimentTime(String experimentTime) {
        ExperimentTime = experimentTime;
    }

    public String getExperimentPlace() {
        return ExperimentPlace;
    }

    public void setExperimentPlace(String experimentPlace) {
        ExperimentPlace = experimentPlace;
    }

    public String getExperimentTeacher() {
        return ExperimentTeacher;
    }

    public void setExperimentTeacher(String experimentTeacher) {
        ExperimentTeacher = experimentTeacher;
    }

    public String getExperimentRemarks() {
        return ExperimentRemarks;
    }

    public void setExperimentRemarks(String experimentRemarks) {
        ExperimentRemarks = experimentRemarks;
    }

    public String getAssignmentRemarks() {
        return AssignmentRemarks;
    }

    public void setAssignmentRemarks(String assignmentRemarks) {
        AssignmentRemarks = assignmentRemarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(semester);
        parcel.writeString(classWeek);
        parcel.writeString(classTime);
        parcel.writeString(classPlace);
        parcel.writeString(classTeacher);
        parcel.writeString(courseRemarks);
        parcel.writeString(ExperimentWeek);
        parcel.writeString(ExperimentTime);
        parcel.writeString(ExperimentPlace);
        parcel.writeString(ExperimentTeacher);
        parcel.writeString(ExperimentRemarks);
        parcel.writeString(AssignmentRemarks);
    }
}
