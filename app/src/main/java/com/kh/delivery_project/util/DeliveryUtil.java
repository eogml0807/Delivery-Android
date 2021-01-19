package com.kh.delivery_project.util;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.OrderVo;

import java.util.Map;

public class DeliveryUtil {

    private static Gson gson = new Gson();

    public static String pickOrder(int order_no, int dlvr_no) {
        String url = "/order/android/pickOrder";
        ContentValues params = new ContentValues();
        params.put("order_no", order_no);
        params.put("dlvr_no", dlvr_no);
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        return result;
    }

    public static String deliveryComplete(OrderVo pickedOrderVo) {
        String url = "/order/android/deliveryCompleted";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        params.put("dlvr_no", pickedOrderVo.getDlvr_no());
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        return result;
    }

    public static String deliveryCancel(OrderVo pickedOrderVo) {
        String url = "/order/android/cancelDelivery";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        params.put("dlvr_no", pickedOrderVo.getDlvr_no());
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        return result;
    }

    public static String sendMsgContent(String msg_content, OrderVo pickedOrderVo) {
        if (msg_content != null && !msg_content.equals("")) {
            String url = "/message/sendMsgContent";
            ContentValues params = new ContentValues();
            params.put("order_no", pickedOrderVo.getOrder_no());
            params.put("sender_no", pickedOrderVo.getDlvr_no());
            params.put("receiver_no", pickedOrderVo.getUser_no());
            params.put("msg_content", msg_content);
            String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
            return result;
        } else {
            return "msg_content is null";
        }
    }

    public static String sendMsgImg(String str_msg_img, OrderVo pickedOrderVo) {
        Log.d("이미지센드", str_msg_img);
        String url = "/message/android/sendMsgImg";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        params.put("sender_no", pickedOrderVo.getDlvr_no());
        params.put("receiver_no", pickedOrderVo.getUser_no());
        params.put("msg_img", str_msg_img);
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        return result;
    }
}
