package com.kh.delivery_project.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class TimelineVo implements Parcelable {
    // 테이블 컬럼
    private int time_no;
    private int writer_no;
    private String writer_state;
    private String time_content;
    private String time_img;
    private Timestamp time_date;
    private String time_state;
    private double time_star;
    private int dlvr_no;
    private String time_location;

    // 조인 컬럼
    private int time_like;
    private String writer_name;
    private String dlvr_name;
    private String writer_img;
    private int liked_no;

    public TimelineVo() {
    }

    protected TimelineVo(Parcel in) {
        time_no = in.readInt();
        writer_no = in.readInt();
        writer_state = in.readString();
        time_content = in.readString();
        time_img = in.readString();
        time_state = in.readString();
        time_star = in.readDouble();
        dlvr_no = in.readInt();
        time_location = in.readString();
        time_like = in.readInt();
        writer_name = in.readString();
        dlvr_name = in.readString();
        writer_img = in.readString();
        liked_no = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(time_no);
        dest.writeInt(writer_no);
        dest.writeString(writer_state);
        dest.writeString(time_content);
        dest.writeString(time_img);
        dest.writeString(time_state);
        dest.writeDouble(time_star);
        dest.writeInt(dlvr_no);
        dest.writeString(time_location);
        dest.writeInt(time_like);
        dest.writeString(writer_name);
        dest.writeString(dlvr_name);
        dest.writeString(writer_img);
        dest.writeInt(liked_no);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimelineVo> CREATOR = new Creator<TimelineVo>() {
        @Override
        public TimelineVo createFromParcel(Parcel in) {
            return new TimelineVo(in);
        }

        @Override
        public TimelineVo[] newArray(int size) {
            return new TimelineVo[size];
        }
    };

    public int getTime_no() {
        return time_no;
    }

    public void setTime_no(int time_no) {
        this.time_no = time_no;
    }

    public int getWriter_no() {
        return writer_no;
    }

    public void setWriter_no(int writer_no) {
        this.writer_no = writer_no;
    }

    public String getWriter_state() {
        return writer_state;
    }

    public void setWriter_state(String writer_state) {
        this.writer_state = writer_state;
    }

    public String getTime_content() {
        return time_content;
    }

    public void setTime_content(String time_content) {
        this.time_content = time_content;
    }

    public String getTime_img() {
        return time_img;
    }

    public void setTime_img(String time_img) {
        this.time_img = time_img;
    }

    public Timestamp getTime_date() {
        return time_date;
    }

    public void setTime_date(Timestamp time_date) {
        this.time_date = time_date;
    }

    public String getTime_state() {
        return time_state;
    }

    public void setTime_state(String time_state) {
        this.time_state = time_state;
    }

    public double getTime_star() {
        return time_star;
    }

    public void setTime_star(double time_star) {
        this.time_star = time_star;
    }

    public int getDlvr_no() {
        return dlvr_no;
    }

    public void setDlvr_no(int dlvr_no) {
        this.dlvr_no = dlvr_no;
    }

    public String getTime_location() {
        return time_location;
    }

    public void setTime_location(String time_location) {
        this.time_location = time_location;
    }

    public int getTime_like() {
        return time_like;
    }

    public void setTime_like(int time_like) {
        this.time_like = time_like;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getDlvr_name() {
        return dlvr_name;
    }

    public void setDlvr_name(String dlvr_name) {
        this.dlvr_name = dlvr_name;
    }

    public String getWriter_img() {
        return writer_img;
    }

    public void setWriter_img(String writer_img) {
        this.writer_img = writer_img;
    }

    public int getLiked_no() {
        return liked_no;
    }

    public void setLiked_no(int liked_no) {
        this.liked_no = liked_no;
    }

    @Override
    public String toString() {
        return "TimelineVo{" +
                "time_no=" + time_no +
                ", writer_no=" + writer_no +
                ", writer_state='" + writer_state + '\'' +
                ", time_content='" + time_content + '\'' +
                ", time_img='" + time_img + '\'' +
                ", time_date=" + time_date +
                ", time_state='" + time_state + '\'' +
                ", time_star=" + time_star +
                ", dlvr_no=" + dlvr_no +
                ", time_location='" + time_location + '\'' +
                ", time_like=" + time_like +
                ", writer_name='" + writer_name + '\'' +
                ", dlvr_name='" + dlvr_name + '\'' +
                ", writer_img='" + writer_img + '\'' +
                ", liked_no=" + liked_no +
                '}';
    }
}
