package com.kh.delivery_project.domain;

import java.sql.Timestamp;

public class MessageVo {
    // 테이블 컬럼
    private int msg_no;
    private int order_no;
    private int sender_no;
    private int receiver_no;
    private String msg_content;
    private String msg_img;
    private Timestamp msg_date;

    // 조인 컬럼
    private String sender_name;
    private String sender_img;

    @Override
    public String toString() {
        return "MessageVo{" +
                "msg_no=" + msg_no +
                ", order_no=" + order_no +
                ", sender_no=" + sender_no +
                ", receiver_no=" + receiver_no +
                ", msg_content='" + msg_content + '\'' +
                ", msg_img='" + msg_img + '\'' +
                ", msg_date=" + msg_date +
                ", sender_name='" + sender_name + '\'' +
                ", sender_img='" + sender_img + '\'' +
                '}';
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getSender_img() {
        return sender_img;
    }

    public void setSender_img(String sender_img) {
        this.sender_img = sender_img;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getMsg_no() {
        return msg_no;
    }

    public void setMsg_no(int msg_no) {
        this.msg_no = msg_no;
    }

    public int getSender_no() {
        return sender_no;
    }

    public void setSender_no(int sender_no) {
        this.sender_no = sender_no;
    }

    public int getReceiver_no() {
        return receiver_no;
    }

    public void setReceiver_no(int receiver_no) {
        this.receiver_no = receiver_no;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getMsg_img() {
        return msg_img;
    }

    public void setMsg_img(String msg_img) {
        this.msg_img = msg_img;
    }

    public Timestamp getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(Timestamp msg_date) {
        this.msg_date = msg_date;
    }

}
