package com.android.mycourse.reminder;

public class Todos {
    private String title;
    private String desc;
    private String date;
    private String time;
    private long remindTime;
    private int id,isRepeat,imgId;

    Todos(){}

    public void setId(int id){
        this.id = id;
    }

    void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    void setDate(String date){
        this.date = date;
    }

    void setTime(String time){
        this.time = time;
    }

    void setRemindTime(long remindTime){
        this.remindTime = remindTime;
    }

    void setIsRepeat(int isRepeat){
        this.isRepeat = isRepeat;
    }

    void setImgId(int imgId){
        this.imgId = imgId;
    }

    public int getId(){
        return id;
    }

    String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    String getDate(){
        return date;
    }

    String getTime(){
        return time;
    }

    long getRemindTime(){
        return remindTime;
    }

    int getIsRepeat(){
        return isRepeat;
    }

    int getImgId(){
        return imgId;
    }
}
