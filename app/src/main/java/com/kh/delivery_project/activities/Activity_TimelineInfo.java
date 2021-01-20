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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.kh.delivery_project.util.FileUploadUtil;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_TimelineInfo extends AppCompatActivity implements Codes, View.OnClickListener, AdapterView.OnItemClickListener, View.OnKeyListener, MenuItem.OnMenuItemClickListener {

    Gson gson = new Gson();

    LinearLayout linModButtons;
    ImageView civWriterImg, ivTimeLike, ivTimeImg;
    TextView txtLikeCount, txtWriterName, txtTimeDate, txtTimeContent;
    EditText edtComment, edtTimeContent;
    Button btnInsertComment, btnModTimelineOk, btnCancelModTimeline;
    RatingBar rbTimeStar;
    ListView lvCommentList;

    MenuItem menuModTimeline, menuDelTimeline;
    Adapter_CommentList adapter;

    DeliverVo deliverVo;
    TimelineVo timelineVo;
    List<CommentVo> commentList = new ArrayList<>();

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
        setViews();
        setListeners();
        setListView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timelineinfo, menu);
        menuModTimeline = menu.findItem(R.id.menuModTimeline);
        menuDelTimeline = menu.findItem(R.id.menuDelTimeline);
        if(deliverVo.getDlvr_no() != timelineVo.getWriter_no()) {
            menuModTimeline.setVisible(false);
            menuDelTimeline.setVisible(false);
        } else {
            menuModTimeline.setOnMenuItemClickListener(this);
            menuDelTimeline.setOnMenuItemClickListener(this);
        }
        return true;
    }

    private void setTimelineVo() {
        Intent intent = getIntent();
        int time_no = intent.getIntExtra("time_no", 0);
        String url = "/timeline/android/getTimelineInfo";
        ContentValues params = new ContentValues();
        params.put("time_no", time_no);
        params.put("account_no", deliverVo.getDlvr_no());
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

    private void setViews() {
        linModButtons = findViewById(R.id.linModButtons);
        civWriterImg = findViewById(R.id.civWriterImg);
        ivTimeLike = findViewById(R.id.ivTimeLike);
        ivTimeImg = findViewById(R.id.ivTimeImg);
        txtLikeCount = findViewById(R.id.txtLikeCount);
        txtWriterName = findViewById(R.id.txtWriterName);
        txtTimeDate = findViewById(R.id.txtTimeDate);
        txtTimeContent = findViewById(R.id.txtTimeContent);
        edtComment = findViewById(R.id.edtComment);
        edtTimeContent = findViewById(R.id.edtTimeContent);
        btnModTimelineOk = findViewById(R.id.btnModTimelineOk);
        btnInsertComment = findViewById(R.id.btnInsertComment);
        btnCancelModTimeline = findViewById(R.id.btnCancelModTimeline);
        rbTimeStar = findViewById(R.id.rbTimeStar);
        lvCommentList = findViewById(R.id.lvCommentList);

        int likeCount = timelineVo.getTime_like();
        txtLikeCount.setText(String.valueOf(likeCount));
        String imgUrl = IMAGE_ADDRESS + timelineVo.getWriter_img();
        UrlImageUtil urlImageUtil = new UrlImageUtil(imgUrl, civWriterImg);
        urlImageUtil.execute();
        if(timelineVo.getLiked_no() != -1) {
            ivTimeLike.setImageResource(R.drawable.ic_favorite);
            isLike = true;
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
        btnModTimelineOk.setOnClickListener(this);
        btnCancelModTimeline.setOnClickListener(this);
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
        if(edtComment.getText().toString() != null && !edtComment.equals("")) {
            String url = "/comment/insertComment";
            ContentValues params = new ContentValues();
            params.put("c_content", edtComment.getText().toString());
            params.put("time_no", timelineVo.getTime_no());
            params.put("writer_no", deliverVo.getDlvr_no());
            String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
            return result;
        } else {
            return "fail";
        }
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

    private String deleteTimeline() {
        String url = "/timeline/android/deleteArticle";
        ContentValues params = new ContentValues();
        params.put("time_no", timelineVo.getTime_no());
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        return result;
    }

    private String updateTimeline() {
        if(edtTimeContent.getText().toString() != null && !edtTimeContent.getText().toString().equals("")) {
            String url = "/timeline/android/updateArticle";
            ContentValues params = new ContentValues();
            params.put("time_no", timelineVo.getTime_no());
            params.put("time_content", edtTimeContent.getText().toString());
            String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
            return result;
        } else {
            return "fail";
        }
    }

    private void toggleButtons() {
        if(linModButtons.getVisibility() == View.GONE) {
            txtTimeContent.setVisibility(View.GONE);
            edtTimeContent.setVisibility(View.VISIBLE);
            linModButtons.setVisibility(View.VISIBLE);
        } else {
            txtTimeContent.setVisibility(View.VISIBLE);
            edtTimeContent.setVisibility(View.INVISIBLE);
            linModButtons.setVisibility(View.GONE);
        }
    }

    private void refresh() {
        Intent intent = new Intent(getApplicationContext(), Activity_TimelineInfo.class);
        intent.putExtra("time_no", timelineVo.getTime_no());
        finish();
        startActivity(intent);
    }

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
                        txtLikeCount.setText(String.valueOf(++likeCount));
                    }
                    isLike = true;
                } else {
                    result = deleteLike();
                    if(result.equals("deleteLike_success")) {
                        ivTimeLike.setImageResource(R.drawable.ic_favorite_borde);
                        txtLikeCount.setText(String.valueOf(--likeCount));
                    }
                    isLike = false;
                }
                break;
            case R.id.btnInsertComment:
                if(insertComment().equals("insertComment_success")) {
                    edtComment.setText("");
                    getCurrentComment();
                } else {
                    Toast.makeText(this, "댓글 내용을 작성해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnModTimelineOk:
                if(updateTimeline().equals("updateArticle_success")) {
                    Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
                    refresh();
                } else {
                    Toast.makeText(this, "수정 내용을 작성해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancelModTimeline:
                toggleButtons();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menuDelTimeline:
                if(deleteTimeline().equals("deleteArticle_success")) {
                    Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "삭제 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menuModTimeline:
                toggleButtons();
                break;
        }
        return false;
    }
}
