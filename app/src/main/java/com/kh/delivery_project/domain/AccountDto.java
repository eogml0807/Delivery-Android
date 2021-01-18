package com.kh.delivery_project.domain;

public class AccountDto {
    // 조인 컬럼
    private int acc_no;
    private String acc_id;
    private String acc_pw;
    private String acc_name;
    private String acc_email;
    private String acc_phone;
    private String acc_state;
    private int acc_point;
    private int point_rank;

    public AccountDto() {
    }

    public int getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(int acc_no) {
        this.acc_no = acc_no;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getAcc_pw() {
        return acc_pw;
    }

    public void setAcc_pw(String acc_pw) {
        this.acc_pw = acc_pw;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public String getAcc_email() {
        return acc_email;
    }

    public void setAcc_email(String acc_email) {
        this.acc_email = acc_email;
    }

    public String getAcc_phone() {
        return acc_phone;
    }

    public void setAcc_phone(String acc_phone) {
        this.acc_phone = acc_phone;
    }

    public String getAcc_state() {
        return acc_state;
    }

    public void setAcc_state(String acc_state) {
        this.acc_state = acc_state;
    }

    public int getAcc_point() {
        return acc_point;
    }

    public void setAcc_point(int acc_point) {
        this.acc_point = acc_point;
    }

    public int getPoint_rank() {
        return point_rank;
    }

    public void setPoint_rank(int point_rank) {
        this.point_rank = point_rank;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "acc_no=" + acc_no +
                ", acc_id='" + acc_id + '\'' +
                ", acc_pw='" + acc_pw + '\'' +
                ", acc_name='" + acc_name + '\'' +
                ", acc_email='" + acc_email + '\'' +
                ", acc_phone='" + acc_phone + '\'' +
                ", acc_state='" + acc_state + '\'' +
                ", acc_point=" + acc_point +
                ", point_rank=" + point_rank +
                '}';
    }
}
