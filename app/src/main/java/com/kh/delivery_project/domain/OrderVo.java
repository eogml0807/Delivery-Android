package com.kh.delivery_project.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class OrderVo implements Parcelable {
    // 테이블 컬럼
    private int order_no;
    private String order_ca;
    private String order_req;
    private double order_lat;
    private double order_lng;
    private int user_no;
    private int dlvr_no;
    private String order_state;
    private Timestamp order_date;
    private int distance;
    private String order_addr;

    // 조인 컬럼
    private String user_name;
    private String code_detail;
    private String dlvr_name;
    private int time_star;

    public OrderVo() {
    }

    protected OrderVo(Parcel in) {
        order_no = in.readInt();
        order_ca = in.readString();
        order_req = in.readString();
        order_lat = in.readDouble();
        order_lng = in.readDouble();
        user_no = in.readInt();
        dlvr_no = in.readInt();
        order_state = in.readString();
        distance = in.readInt();
        order_addr = in.readString();
        user_name = in.readString();
        code_detail = in.readString();
        dlvr_name = in.readString();
        time_star = in.readInt();
    }

    public static final Creator<OrderVo> CREATOR = new Creator<OrderVo>() {
        @Override
        public OrderVo createFromParcel(Parcel in) {
            return new OrderVo(in);
        }

        @Override
        public OrderVo[] newArray(int size) {
            return new OrderVo[size];
        }
    };

    public int getTime_star() {
        return time_star;
    }

    public void setTime_star(int time_star) {
        this.time_star = time_star;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getOrder_ca() {
        return order_ca;
    }

    public void setOrder_ca(String order_ca) {
        this.order_ca = order_ca;
    }

    public String getOrder_req() {
        return order_req;
    }

    public void setOrder_req(String order_req) {
        this.order_req = order_req;
    }

    public double getOrder_lat() {
        return order_lat;
    }

    public void setOrder_lat(double order_lat) {
        this.order_lat = order_lat;
    }

    public double getOrder_lng() {
        return order_lng;
    }

    public void setOrder_lng(double order_lng) {
        this.order_lng = order_lng;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public int getDlvr_no() {
        return dlvr_no;
    }

    public void setDlvr_no(int dlvr_no) {
        this.dlvr_no = dlvr_no;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public Timestamp getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Timestamp order_date) {
        this.order_date = order_date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getOrder_addr() {
        return order_addr;
    }

    public void setOrder_addr(String order_addr) {
        this.order_addr = order_addr;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCode_detail() {
        return code_detail;
    }

    public void setCode_detail(String code_detail) {
        this.code_detail = code_detail;
    }

    public String getDlvr_name() {
        return dlvr_name;
    }

    public void setDlvr_name(String dlvr_name) {
        this.dlvr_name = dlvr_name;
    }

    @Override
    public String toString() {
        return "OrderVo{" +
                "order_no=" + order_no +
                ", order_ca='" + order_ca + '\'' +
                ", order_req='" + order_req + '\'' +
                ", order_lat=" + order_lat +
                ", order_lng=" + order_lng +
                ", user_no=" + user_no +
                ", dlvr_no=" + dlvr_no +
                ", order_state='" + order_state + '\'' +
                ", order_date=" + order_date +
                ", distance=" + distance +
                ", order_addr='" + order_addr + '\'' +
                ", user_name='" + user_name + '\'' +
                ", code_detail='" + code_detail + '\'' +
                ", dlvr_name='" + dlvr_name + '\'' +
                ", time_star=" + time_star +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(order_no);
        dest.writeString(order_ca);
        dest.writeString(order_req);
        dest.writeDouble(order_lat);
        dest.writeDouble(order_lng);
        dest.writeInt(user_no);
        dest.writeInt(dlvr_no);
        dest.writeString(order_state);
        dest.writeInt(distance);
        dest.writeString(order_addr);
        dest.writeString(user_name);
        dest.writeString(code_detail);
        dest.writeString(dlvr_name);
        dest.writeInt(time_star);
    }
}
