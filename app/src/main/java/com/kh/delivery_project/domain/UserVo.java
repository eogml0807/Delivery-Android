package com.kh.delivery_project.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Timestamp;

public class UserVo implements Parcelable {
    // 테이블 컬럼
    private int user_no;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_phone;
    private String user_email;
    private String user_addr; // 주소
    private Date user_birth;
    private Timestamp user_date;
    private String user_state;
    private int user_point;
    private String account_state;

    // 조인 컬럼
    private int order_count;
    private int user_rank;

    public UserVo() {
    }

    protected UserVo(Parcel in) {
        user_no = in.readInt();
        user_id = in.readString();
        user_pw = in.readString();
        user_name = in.readString();
        user_phone = in.readString();
        user_email = in.readString();
        user_addr = in.readString();
        user_state = in.readString();
        user_point = in.readInt();
        account_state = in.readString();
        order_count = in.readInt();
        user_rank = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_no);
        dest.writeString(user_id);
        dest.writeString(user_pw);
        dest.writeString(user_name);
        dest.writeString(user_phone);
        dest.writeString(user_email);
        dest.writeString(user_addr);
        dest.writeString(user_state);
        dest.writeInt(user_point);
        dest.writeString(account_state);
        dest.writeInt(order_count);
        dest.writeInt(user_rank);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserVo> CREATOR = new Creator<UserVo>() {
        @Override
        public UserVo createFromParcel(Parcel in) {
            return new UserVo(in);
        }

        @Override
        public UserVo[] newArray(int size) {
            return new UserVo[size];
        }
    };

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_addr() {
        return user_addr;
    }

    public void setUser_addr(String user_addr) {
        this.user_addr = user_addr;
    }

    public Date getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(Date user_birth) {
        this.user_birth = user_birth;
    }

    public Timestamp getUser_date() {
        return user_date;
    }

    public void setUser_date(Timestamp user_date) {
        this.user_date = user_date;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public String getAccount_state() {
        return account_state;
    }

    public void setAccount_state(String account_state) {
        this.account_state = account_state;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getUser_rank() {
        return user_rank;
    }

    public void setUser_rank(int user_rank) {
        this.user_rank = user_rank;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "user_no=" + user_no +
                ", user_id='" + user_id + '\'' +
                ", user_pw='" + user_pw + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_addr='" + user_addr + '\'' +
                ", user_birth=" + user_birth +
                ", user_date=" + user_date +
                ", user_state='" + user_state + '\'' +
                ", user_point=" + user_point +
                ", account_state='" + account_state + '\'' +
                ", order_count=" + order_count +
                ", user_rank=" + user_rank +
                '}';
    }
}
