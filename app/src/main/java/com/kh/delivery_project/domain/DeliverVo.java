package com.kh.delivery_project.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Timestamp;

public class DeliverVo implements Parcelable {
    // 테이블 컬럼
    private int dlvr_no;
    private String dlvr_id;
    private String dlvr_pw;
    private String dlvr_name;
    private String dlvr_phone;
    private String dlvr_email;
    private String dlvr_addr;
    private String dlvr_img;
    private String dlvr_idcard;
    private Date dlvr_birth;
    private Timestamp dlvr_date;
    private String dlvr_state;
    private int dlvr_point;
    private String account_state;

    // 조인 컬럼
    private int order_count;
    private int dlvr_rank;

    public DeliverVo() {
    }

    protected DeliverVo(Parcel in) {
        dlvr_no = in.readInt();
        dlvr_id = in.readString();
        dlvr_pw = in.readString();
        dlvr_name = in.readString();
        dlvr_phone = in.readString();
        dlvr_email = in.readString();
        dlvr_addr = in.readString();
        dlvr_img = in.readString();
        dlvr_idcard = in.readString();
        dlvr_state = in.readString();
        dlvr_point = in.readInt();
        account_state = in.readString();
        order_count = in.readInt();
        dlvr_rank = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dlvr_no);
        dest.writeString(dlvr_id);
        dest.writeString(dlvr_pw);
        dest.writeString(dlvr_name);
        dest.writeString(dlvr_phone);
        dest.writeString(dlvr_email);
        dest.writeString(dlvr_addr);
        dest.writeString(dlvr_img);
        dest.writeString(dlvr_idcard);
        dest.writeString(dlvr_state);
        dest.writeInt(dlvr_point);
        dest.writeString(account_state);
        dest.writeInt(order_count);
        dest.writeInt(dlvr_rank);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliverVo> CREATOR = new Creator<DeliverVo>() {
        @Override
        public DeliverVo createFromParcel(Parcel in) {
            return new DeliverVo(in);
        }

        @Override
        public DeliverVo[] newArray(int size) {
            return new DeliverVo[size];
        }
    };

    public int getDlvr_no() {
        return dlvr_no;
    }

    public void setDlvr_no(int dlvr_no) {
        this.dlvr_no = dlvr_no;
    }

    public String getDlvr_id() {
        return dlvr_id;
    }

    public void setDlvr_id(String dlvr_id) {
        this.dlvr_id = dlvr_id;
    }

    public String getDlvr_pw() {
        return dlvr_pw;
    }

    public void setDlvr_pw(String dlvr_pw) {
        this.dlvr_pw = dlvr_pw;
    }

    public String getDlvr_name() {
        return dlvr_name;
    }

    public void setDlvr_name(String dlvr_name) {
        this.dlvr_name = dlvr_name;
    }

    public String getDlvr_phone() {
        return dlvr_phone;
    }

    public void setDlvr_phone(String dlvr_phone) {
        this.dlvr_phone = dlvr_phone;
    }

    public String getDlvr_email() {
        return dlvr_email;
    }

    public void setDlvr_email(String dlvr_email) {
        this.dlvr_email = dlvr_email;
    }

    public String getDlvr_addr() {
        return dlvr_addr;
    }

    public void setDlvr_addr(String dlvr_addr) {
        this.dlvr_addr = dlvr_addr;
    }

    public String getDlvr_img() {
        return dlvr_img;
    }

    public void setDlvr_img(String dlvr_img) {
        this.dlvr_img = dlvr_img;
    }

    public String getDlvr_idcard() {
        return dlvr_idcard;
    }

    public void setDlvr_idcard(String dlvr_idcard) {
        this.dlvr_idcard = dlvr_idcard;
    }

    public Date getDlvr_birth() {
        return dlvr_birth;
    }

    public void setDlvr_birth(Date dlvr_birth) {
        this.dlvr_birth = dlvr_birth;
    }

    public Timestamp getDlvr_date() {
        return dlvr_date;
    }

    public void setDlvr_date(Timestamp dlvr_date) {
        this.dlvr_date = dlvr_date;
    }

    public String getDlvr_state() {
        return dlvr_state;
    }

    public void setDlvr_state(String dlvr_state) {
        this.dlvr_state = dlvr_state;
    }

    public int getDlvr_point() {
        return dlvr_point;
    }

    public void setDlvr_point(int dlvr_point) {
        this.dlvr_point = dlvr_point;
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

    public int getDlvr_rank() {
        return dlvr_rank;
    }

    public void setDlvr_rank(int dlvr_rank) {
        this.dlvr_rank = dlvr_rank;
    }

    @Override
    public String toString() {
        return "DeliverVo{" +
                "dlvr_no=" + dlvr_no +
                ", dlvr_id='" + dlvr_id + '\'' +
                ", dlvr_pw='" + dlvr_pw + '\'' +
                ", dlvr_name='" + dlvr_name + '\'' +
                ", dlvr_phone='" + dlvr_phone + '\'' +
                ", dlvr_email='" + dlvr_email + '\'' +
                ", dlvr_addr='" + dlvr_addr + '\'' +
                ", dlvr_img='" + dlvr_img + '\'' +
                ", dlvr_idcard='" + dlvr_idcard + '\'' +
                ", dlvr_birth=" + dlvr_birth +
                ", dlvr_date=" + dlvr_date +
                ", dlvr_state='" + dlvr_state + '\'' +
                ", dlvr_point=" + dlvr_point +
                ", account_state='" + account_state + '\'' +
                ", order_count=" + order_count +
                ", dlvr_rank=" + dlvr_rank +
                '}';
    }
}
