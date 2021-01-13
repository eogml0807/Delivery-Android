package com.kh.delivery_project.domain;

import java.sql.Timestamp;

public class CommentVo {
    // 테이블 컬럼
    private int c_no;
    private String c_content;
    private Timestamp c_date;
    private int time_no;
    private int writer_no;
    //조인 컬럼
    private String writer_name;
    private String writer_img;

    public int getC_no() {
        return c_no;
    }

    public void setC_no(int c_no) {
        this.c_no = c_no;
    }

    public String getC_content() {
        return c_content;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }

    public Timestamp getC_date() {
        return c_date;
    }

    public void setC_date(Timestamp c_date) {
        this.c_date = c_date;
    }

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

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getWriter_img() {
        return writer_img;
    }

    public void setWriter_img(String writer_img) {
        this.writer_img = writer_img;
    }

    @Override
    public String toString() {
        return "CommentVo{" +
                "c_no=" + c_no +
                ", c_content='" + c_content + '\'' +
                ", c_date=" + c_date +
                ", time_no=" + time_no +
                ", writer_no=" + writer_no +
                ", writer_name='" + writer_name + '\'' +
                ", writer_img='" + writer_img + '\'' +
                '}';
    }
}
