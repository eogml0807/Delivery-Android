package com.kh.delivery_project.util;

import android.location.Address;
import android.util.Log;

import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.OrderVo;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.domain.UserVo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ConvertUtil {

    private static Date getBirth(String str_birth) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(df.parse(str_birth).getTime());
        return date;
    }

    private static Timestamp getDate(String str_date) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(df.parse(str_date).getTime());
        return timestamp;
    }

    public static DeliverVo getDeliverVo(Map<String, Object> map) {
        try {
            DeliverVo deliverVo = new DeliverVo();
            deliverVo.setDlvr_no((int)(double) map.get("dlvr_no"));
            deliverVo.setDlvr_id((String) map.get("dlvr_id"));
            deliverVo.setDlvr_pw((String) map.get("dlvr_pw"));
            deliverVo.setDlvr_name((String) map.get("dlvr_name"));
            deliverVo.setDlvr_phone((String) map.get("dlvr_phone"));
            deliverVo.setDlvr_email((String) map.get("dlvr_email"));
            deliverVo.setDlvr_addr((String) map.get("dlvr_addr"));
            deliverVo.setDlvr_img((String) map.get("dlvr_img"));
            deliverVo.setDlvr_idcard((String) map.get("dlvr_idcard"));
            deliverVo.setDlvr_birth(getBirth((String) map.get("dlvr_birth")));
            deliverVo.setDlvr_date(getDate((String) map.get("dlvr_date")));
            deliverVo.setDlvr_point((int)(double) map.get("dlvr_point"));
            deliverVo.setDlvr_rank((String) map.get("dlvr_rank"));
            return deliverVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OrderVo getOrderVo(Map<String, Object> map) {
        try {
            OrderVo orderVo = new OrderVo();
            orderVo.setOrder_no((int) (double) map.get("order_no"));
            orderVo.setOrder_ca((String) map.get("order_ca"));
            orderVo.setOrder_req((String) map.get("order_req"));
            orderVo.setOrder_lat((double) map.get("order_lat"));
            orderVo.setOrder_lng((double) map.get("order_lng"));
            orderVo.setUser_no((int) (double) map.get("user_no"));
            orderVo.setDlvr_no((int) (double) map.get("dlvr_no"));
            orderVo.setOrder_state((String) map.get("order_state"));
            orderVo.setOrder_date(getDate((String) map.get("order_date")));
            if(map.get("user_name") != null) {
                orderVo.setUser_name((String) map.get("user_name"));
            }
            return orderVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserVo getUserVo(Map<String, Object> map) {
        try {
            UserVo userVo = new UserVo();
            userVo.setUser_no((int) (double) map.get("user_no"));
            userVo.setUser_id((String) map.get("user_id"));
            userVo.setUser_pw((String) map.get("user_pw"));
            userVo.setUser_name((String) map.get("user_name"));
            userVo.setUser_phone((String) map.get("user_phone"));
            userVo.setUser_email((String) map.get("user_email"));
            userVo.setUser_addr((String) map.get("user_addr"));
            userVo.setUser_point((int) (double) map.get("user_point"));
            userVo.setUser_rank((String) map.get("user_rank"));
            userVo.setUser_birth(getBirth((String) map.get("user_birth")));
            userVo.setUser_date(getDate((String) map.get("user_date")));
            return userVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TimelineVo getTimelineVo(Map<String, Object> map) {
        try {
            TimelineVo timelineVo = new TimelineVo();
            timelineVo.setTime_no((int) (double) map.get("time_no"));
            timelineVo.setWriter_no((int) (double) map.get("writer_no"));
            timelineVo.setWriter_state((String) map.get("writer_state"));
            timelineVo.setTime_content((String) map.get("time_content"));
            timelineVo.setTime_img((String) map.get("time_img"));
            timelineVo.setTime_date(getDate((String) map.get("time_date")));
            timelineVo.setTime_state((String) map.get("time_state"));
            timelineVo.setTime_star((double) map.get("time_star"));
            timelineVo.setDlvr_no((int) (double) map.get("dlvr_no"));
            timelineVo.setWriter_name((String) map.get("writer_name"));
            timelineVo.setDlvr_name((String) map.get("dlvr_name"));
            return timelineVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}