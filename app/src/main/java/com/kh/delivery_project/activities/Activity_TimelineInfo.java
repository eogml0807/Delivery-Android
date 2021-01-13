package com.kh.delivery_project.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Activity_TimelineInfo extends AppCompatActivity implements Codes, View.OnClickListener {

    Gson gson = new Gson();

    ImageView civWriterImg, ivTimeLike, ivTimeImg;
    TextView txtLikeCount, txtWriterName, txtTimeDate, txtTimeContent;
    RatingBar rbTimeStar;
    ListView lvCommentList;

    DeliverVo deliverVo;
    TimelineVo timelineVo;

    boolean isLike = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelineinfo);
        deliverVo = PreferenceManager.getDeliverVo(this);
        setTitle("게시글 정보");
        setTimelineVo();
        isLike();
        setViews();
        setListeners();
    }

    private void setTimelineVo() {
        Intent intent = getIntent();
        int time_no = intent.getIntExtra("time_no", 0);
        String url = "/timeline/android/getTimelineInfo";
        ContentValues params = new ContentValues();
        params.put("time_no", time_no);
        Map<String, Object> map = gson.fromJson(ConnectServer.getData(url, params), Map.class);
        timelineVo = ConvertUtil.getTimelineVo(map);
        Log.d("타임라인셋", timelineVo.toString());
    }

    private void setViews() {
        civWriterImg = findViewById(R.id.civWriterImg);
        ivTimeLike = findViewById(R.id.ivTimeLike);
        ivTimeImg = findViewById(R.id.ivTimeImg);
        txtLikeCount = findViewById(R.id.txtLikeCount);
        txtWriterName = findViewById(R.id.txtWriterName);
        txtTimeDate = findViewById(R.id.txtTimeDate);
        txtTimeContent = findViewById(R.id.txtTimeContent);
        rbTimeStar = findViewById(R.id.rbTimeStar);
        lvCommentList = findViewById(R.id.lvCommentList);

        int likeCount = timelineVo.getTime_like();
        txtLikeCount.setText(String.valueOf(likeCount));
        String imgUrl = IMAGE_ADDRESS + timelineVo.getWriter_img();
        UrlImageUtil urlImageUtil = new UrlImageUtil(imgUrl, civWriterImg);
        urlImageUtil.execute();
        if(isLike) {
            ivTimeLike.setImageResource(R.drawable.ic_favorite);
        }
        String time_img = timelineVo.getTime_img();
        if(time_img != null) {
            imgUrl = IMAGE_ADDRESS + time_img;
            urlImageUtil = new UrlImageUtil(imgUrl, ivTimeImg);
            urlImageUtil.execute();
        }
        txtWriterName.setText(timelineVo.getWriter_name());
        Timestamp time_date = timelineVo.getTime_date();
        DateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm");
        String str_time_date = df.format(time_date);
        txtTimeDate.setText(str_time_date);
        txtTimeContent.setText(timelineVo.getTime_content());
        if(timelineVo.getTime_state().equals("2-002")) {
            rbTimeStar.setVisibility(View.VISIBLE);
            rbTimeStar.setRating((float) timelineVo.getTime_star());
        }
    }

    private void setListeners() {
        ivTimeLike.setOnClickListener(this);
    }

    private void isLike() {
        String url = "/like/isLike";
        ContentValues params = new ContentValues();
        params.put("time_no", timelineVo.getTime_no());
        params.put("account_no", deliverVo.getDlvr_no());
        Boolean result = gson.fromJson(ConnectServer.getData(url, params), Boolean.class);
        Log.d("좋아요?", String.valueOf(result));
        isLike = result;
    }

    private String insertLike() {
        String url = "/like/insertLike";
        url += "/" + timelineVo.getTime_no();
        url += "/" + deliverVo.getDlvr_no();
        String result = gson.fromJson(ConnectServer.getData(url), String.class);
        return result;
    }

    private String deleteLike() {
        String url = "/like/deleteLike";
        url += "/" + timelineVo.getTime_no();
        url += "/" + deliverVo.getDlvr_no();
        String result = gson.fromJson(ConnectServer.getData(url), String.class);
        return result;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int likeCount = Integer.parseInt(txtLikeCount.getText().toString());

        switch (id) {
            case R.id.ivTimeLike:
                String result;
                if(!isLike) {
                    result = insertLike();
                    if(result.equals("insertLike_success")) {
                        ivTimeLike.setImageResource(R.drawable.ic_favorite);
                        isLike = true;
                        likeCount++;
                    }
                } else {
                    result = deleteLike();
                    if(result.equals("deleteLike_success")) {
                        ivTimeLike.setImageResource(R.drawable.ic_favorite_borde);
                        isLike = false;
                        likeCount--;
                    }
                }
                txtLikeCount.setText(String.valueOf(likeCount));
                break;
        }
    }
}
