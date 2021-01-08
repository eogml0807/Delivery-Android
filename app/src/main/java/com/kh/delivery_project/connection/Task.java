package com.kh.delivery_project.connection;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.kh.delivery_project.util.Keys;

public class Task extends AsyncTask<ContentValues, Void, String> implements Keys {
    private String url = "";
    ContentValues values;

    public void setUrl(String link) {
        this.url = IP + link;
    }

    @Override
    protected String doInBackground(ContentValues... contentValues) {
        String result = "";

        HTTPClient httpClient =new HTTPClient();

        if(contentValues.length > 0) {
            result = httpClient.requset(url, contentValues[0]);
        } else {
            result = httpClient.requset(url, values);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
