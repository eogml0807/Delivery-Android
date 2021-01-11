package com.kh.delivery_project.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.FileUploadUtil;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.io.File;
import java.io.InputStream;

public class Dialog_Timeline extends Activity implements View.OnClickListener, Codes {

    Gson gson = new Gson();

    TextView txtWriterName, txtTimeContent;
    EditText edtTimeContent;
    Button btnModTimeline, btnDelTimeline, btnCloseTimeline, btnModTimeImg, btnModTimelineOk;
    ImageView ivTimeImg;
    RatingBar rbTimeStar;

    DeliverVo deliverVo;
    TimelineVo timelineVo;
    File modImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_timeline);
        deliverVo = PreferenceManager.getDeliverVo(this);
        getIntents();
        setViews();
        setListeners();
        setTimeline();
    }

    private void setTimeline() {
        if(deliverVo.getDlvr_no() != timelineVo.getWriter_no()) {
            btnModTimeline.setVisibility(View.INVISIBLE);
            btnDelTimeline.setVisibility(View.INVISIBLE);
        }
        if(timelineVo.getTime_state().equals("2-002")) {
            rbTimeStar.setVisibility(View.VISIBLE);
            rbTimeStar.setRating((int) timelineVo.getTime_star());
        }
        txtWriterName.setText(timelineVo.getWriter_name());
        txtTimeContent.setText(timelineVo.getTime_content());
        edtTimeContent.setText(timelineVo.getTime_content());
        if(timelineVo.getTime_img() != null) {
            String url = IMAGE_ADDRESS + timelineVo.getTime_img();
            UrlImageUtil imgUtil = new UrlImageUtil(url, ivTimeImg);
            imgUtil.execute();
        } else {
            ivTimeImg.setVisibility(View.INVISIBLE);
        }
    }

    private void getIntents() {
        Intent intent = getIntent();
        this.timelineVo = intent.getParcelableExtra("timelineVo");
    }

    private void setViews() {
        txtWriterName = findViewById(R.id.txtWriterName);
        txtTimeContent = findViewById(R.id.txtTimeContent);
        edtTimeContent = findViewById(R.id.edtTimeContent);
        btnModTimeline = findViewById(R.id.btnModTimeline);
        btnDelTimeline = findViewById(R.id.btnDelTimeline);
        btnCloseTimeline = findViewById(R.id.btnCloseTimeline);
        btnModTimeImg = findViewById(R.id.btnModTimeImg);
        btnModTimelineOk = findViewById(R.id.btnModTimelineOk);
        ivTimeImg = findViewById(R.id.ivTimeImg);
        rbTimeStar = findViewById(R.id.rbTimeStar);
    }

    private void setListeners() {
        btnModTimeline.setOnClickListener(this);
        btnDelTimeline.setOnClickListener(this);
        btnCloseTimeline.setOnClickListener(this);
        btnModTimeImg.setOnClickListener(this);
        btnModTimelineOk.setOnClickListener(this);
    }

    private void showImage(Uri uri) {
        if(FileUploadUtil.isImage(modImg)) {
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ivTimeImg.setImageBitmap(bitmap);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "이미지 파일만", Toast.LENGTH_SHORT).show();
            modImg = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == MOD_TIME_IMG) {
                modImg = FileUploadUtil.getFile(this, data.getData());
                showImage(data.getData());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnModTimeline:
                setModTimeline();
                break;
            case R.id.btnDelTimeline:
                deleteTimeline();
                break;
            case R.id.btnCloseTimeline:
                finish();
                break;
            case R.id.btnModTimeImg:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, MOD_TIME_IMG);
                break;
            case R.id.btnModTimelineOk:
                modifyTimeline();
                break;
        }
    }

    private void deleteTimeline() {
        String url = "/timeline/android/deleteTimeline";
        ContentValues params = new ContentValues();
        params.put("time_no", timelineVo.getTime_no());
        Log.d("params", params.toString());
//        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
//        Log.d("result is ", result);
    }

    private void modifyTimeline() {
        String url = "/timeline/android/updateTimeline";
        ContentValues params = new ContentValues();
        params.put("time_no", timelineVo.getTime_no());
        params.put("time_content", edtTimeContent.getText().toString());
        if(modImg != null) {
            String time_img = TIMELINE_IMG + deliverVo.getDlvr_id() + "_" + modImg.getName();
            params.put("time_img", time_img);
        }
        Log.d("params is ", params.toString());
//        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
//        Log.d("result is ", result);
    }

    private void setModTimeline() {
        if(btnModTimeline.getText().toString().equals("수정")) {
            txtTimeContent.setVisibility(View.GONE);
            edtTimeContent.setVisibility(View.VISIBLE);
            btnModTimeImg.setVisibility(View.VISIBLE);
            btnModTimeline.setText("취소");
        } else {
            txtTimeContent.setVisibility(View.VISIBLE);
            edtTimeContent.setVisibility(View.GONE);
            btnModTimeImg.setVisibility(View.GONE);
            setTimeline();
            btnModTimeline.setText("수정");
        }
    }
}
