package com.kh.delivery_project.connection;

import android.content.ContentValues;
import android.util.Log;

public class ConnectServer {

    public static String getData(String url, ContentValues params) {
        try {
            Log.d("ConnectServer.params", params.toString());
            Task task = new Task();
            task.setUrl(url);
            String result = task.execute(params).get();
            Log.d("ConnectServer.params", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getData(String url) {
        try {
            Task task = new Task();
            task.setUrl(url);
            String result = task.execute().get();
            Log.d("ConnectServer.noparams", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
