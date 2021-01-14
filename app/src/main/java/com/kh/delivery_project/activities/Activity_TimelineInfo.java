package com.kh.delivery_project.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.adapters.Adapter_CommentList;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.CommentVo;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_TimelineInfo extends AppCompatActivity implements Codes, View.OnClickListener, AdapterView.OnItemClickListener, View.OnKeyListener {

    Gson gson = new Gson();

    ImageView civWriterImg, ivTimeLike, ivTimeImg;
    TextView txtLikeCount, txtWriterName, txtTimeDate, txtTimeContent;
    EditText edtComment;
    Button btnInsertComment;
    RatingBar rbTimeStar;
    ListView lvCommentList;

    DeliverVo deliverVo;
    TimelineVo timelineVo;
    List<CommentVo> commentList = new ArrayList<>();

    Adapter_CommentList adapter;

    boolean isLike = false;
    int lastCommentNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelineinfo);
        deliverVo = PreferenceManager.getDeliverVo(this);
        setTitle("게시글 정보");
        setTimelineVo();
        setCommentList();
        isLike();
        setViews();
        setListeners();
        setListView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void setTimelineVo() {
        Intent intent = getIntent();
        int time_no = intent.getIntExtra("time_no", 0);
        String url = "/timeline/android/getTimelineInfo";
        ContentValues params = new ContentValues();
        params.put("time_no", time_no);
        Map<String, Object> map = gson.fromJson(ConnectServer.getData(url, params), Map.class);
        timelineVo = ConvertUtil.getTimelineVo(map);
    }

    private void setCommentList() {
        String url = "/comment/getCommentList";
        url += "/" + timelineVo.getTime_no();
        List<Map<String, Object>> list = gson.fromJson(ConnectServer.getData(url), List.class);
        for(int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            CommentVo commentVo = ConvertUtil.getCommentVo(map);
            commentList.add(commentVo);
            if (i == list.size()-1) {
                lastCommentNo = commentVo.getC_no();
            }
        }
    }

    private void isLike() {
        String url = "/like/isLike";
        ContentValues params = new ContentValues();
        params.put("time_no", timelineVo.getTime_no());
        params.put("account_no", deliverVo.getDlvr_no());
        Boolean result = gson.fromJson(ConnectServer.getData(url, params), Boolean.class);
        isLike = result;
    }

    private void setViews() {
        civWriterImg = findViewById(R.id.civWriterImg);
        ivTimeLike = findViewById(R.id.ivTimeLike);
        ivTimeImg = findViewById(R.id.ivTimeImg);
        txtLikeCount = findViewById(R.id.txtLikeCount);
        txtWriterName = findViewById(R.id.txtWriterName);
        txtTimeDate = findViewById(R.id.txtTimeDate);
        txtTimeContent = findViewById(R.id.txtTimeContent);
        edtComment = findViewById(R.id.edtComment);
        btnInsertComment = findViewById(R.id.btnInsertComment);
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
        btnInsertComment.setOnClickListener(this);
        lvCommentList.setOnItemClickListener(this);
        edtComment.setOnKeyListener(this);
    }

    private void setListView() {
        adapter = new Adapter_CommentList(this, R.layout.view_commentlist, commentList);
        lvCommentList.setAdapter(adapter);
//        registerForContextMenu(lvCommentList);
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

    private String insertComment() {
        String url = "/comment/insertComment";
        ContentValues params = new ContentValues();
        params.put("c_content", edtComment.getText().toString());
        params.put("time_no", timelineVo.getTime_no());
        params.put("writer_no", deliverVo.getDlvr_no());
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        return result;
    }

    private void getCurrentComment() {
        String url = "/comment/getCurrentComment";
        ContentValues params = new ContentValues();
        params.put("time_no", timelineVo.getTime_no());
        params.put("c_no", lastCommentNo);
        List<Map<String, Object>> list = gson.fromJson(ConnectServer.getData(url, params), List.class);
        if(list.size() > 0) {
            for(int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                CommentVo commentVo = ConvertUtil.getCommentVo(map);
                commentList.add(commentVo);
                if(i == list.size()-1) {
                    lastCommentNo = commentVo.getC_no();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    // 댓글 수정 삭제
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        getMenuInflater().inflate(R.menu.menu_comment, menu);
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int index = info.position;
//        CommentVo commentVo = commentList.get(info.position);
//
//        MenuItem menuModComment = findViewById(R.id.menuModComment);
//        MenuItem menuDelComment = findViewById(R.id.menuDelComment);
//
//        if(commentVo.getWriter_no() != deliverVo.getDlvr_no()) {
//            menuModComment.setVisible(false);
//            menuDelComment.setVisible(false);
//        }
//
//        switch (index) {
//            case R.id.menuModComment:
//                Toast.makeText(this, "메뉴수정코멘트", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menuDelComment:
//                Toast.makeText(this, "메뉴삭제코멘트", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int likeCount = Integer.parseInt(txtLikeCount.getText().toString());
        String result;

        switch (id) {
            case R.id.ivTimeLike:
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
            case R.id.btnInsertComment:
                result = insertComment();
                if(result.equals("insertComment_success")) {
                    edtComment.setText("");
                    getCurrentComment();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "아이템클릭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (v == edtComment) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                String result = insertComment();
                if(result.equals("insertComment_success")) {
                    edtComment.setText("");
                    getCurrentComment();
                }
            }
            return true;
        }
        return false;
    }
}
