package com.kh.delivery_project.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.adapters.Adapter_TimelineList;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.util.AddressUtil;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.FileUploadUtil;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Activity_Timeline extends AppCompatActivity implements Codes, View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    Gson gson = new Gson();
    Adapter_TimelineList adapter;
    LocationManager lm;

    LinearLayout boardWriteForm;
    ProgressBar boardProgressbar;
    ListView boardListView;
    Button btnToggleWriteForm, btnInsertArticle, btnSelectImg;
    EditText edtBoard;
    ImageView ivWriteImg, ivDialogTimeImg;
    Spinner boardSpinner;

    boolean lockListView = false;
    boolean islastItem = false;
    boolean showEnd = false;
    boolean firstSelect = false;
    List<TimelineVo> timelineList = new ArrayList<>();
    List<TimelineVo> showList = new ArrayList<>();

    boolean isStart = true;
    DeliverVo deliverVo;
    int page = 0;
    int offset = 10;
    File writeTimeImg, modTimeImg;
    String time_img;
    String[] select = {"전체", "공지", "리뷰", "자유"};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        deliverVo = PreferenceManager.getDeliverVo(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setViews();
        setSpinner();
        setListeners();
        setTimelineList(ALL_TIMELINE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isStart) {
            refresh();
        }
    }

    private void setViews() {
        btnToggleWriteForm = findViewById(R.id.btnToggleWriteForm);
        btnInsertArticle = findViewById(R.id.btnInsertArticle);
        btnSelectImg = findViewById(R.id.btnSelectImg);
        boardListView = findViewById(R.id.boardListView);
        boardProgressbar = findViewById(R.id.boardProgressbar);
        edtBoard = findViewById(R.id.edtBoard);
        ivWriteImg = findViewById(R.id.ivWriteImg);
        boardWriteForm = findViewById(R.id.boardWriteForm);
        boardSpinner = findViewById(R.id.boardSpinner);
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, select);
        boardSpinner.setAdapter(adapter);
    }

    private void setListeners() {
        btnToggleWriteForm.setOnClickListener(this);
        btnInsertArticle.setOnClickListener(this);
        btnSelectImg.setOnClickListener(this);
        boardListView.setOnScrollListener(this);
        boardListView.setOnItemClickListener(this);
        boardSpinner.setOnItemSelectedListener(this);
    }

    private void setTimelineList(int searchType) {
        String url = "/timeline/getTimelineList";
        ContentValues params = new ContentValues();
        switch (searchType) {
            case NOTICE_TIMELINE:
                params.put("searchType", "2-001");
                break;
            case REVIEW_TIMELINE:
                params.put("searchType", "2-002");
                break;
            case FREE_TIMELINE:
                params.put("searchType", "2-003");
                break;
        }
        params.put("account_no", deliverVo.getDlvr_no());
        List<Map<String, Object>> list = gson.fromJson(ConnectServer.getData(url, params), List.class);
        Log.d("list is", list.toString());
        timelineList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            TimelineVo timelineVo = ConvertUtil.getTimelineVo(map);
            timelineList.add(timelineVo);
        }
        setListview();
        getItem();
    }

    private void setListview() {
        boardListView.setAdapter(null);
        adapter = new Adapter_TimelineList(this, R.layout.view_timelinelist, showList);
        boardListView.setAdapter(adapter);
    }

    private void getItem() {
        lockListView = true;

        int startIndex = page * offset;
        if (startIndex <= showList.size()) {
            int endIndex = startIndex + offset;
            if (endIndex > timelineList.size()) {
                endIndex = timelineList.size();
            }

            for (int i = startIndex; i < endIndex; i++) {
                TimelineVo timelineVo = timelineList.get(i);
                showList.add(timelineVo);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    page++;
                    adapter.notifyDataSetChanged();
                    boardProgressbar.setVisibility(View.GONE);
                    lockListView = false;
                }
            }, 1000);
        } else {
            if (!showEnd) {
                Toast.makeText(this, "마지막 글입니다", Toast.LENGTH_SHORT).show();
                showEnd = true;
            }
            boardProgressbar.setVisibility(View.GONE);
            lockListView = false;
        }
    }

    private void showImage(Uri uri, ImageView iv) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bitmap);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String insertArticle() {
        String url = "/timeline/android/insertArticle";
        ContentValues params = new ContentValues();
        String time_content = edtBoard.getText().toString();
        if((time_content != null && !time_content.equals("")) || writeTimeImg != null) {

            int writer_no = deliverVo.getDlvr_no();
            params.put("writer_no", writer_no);
            params.put("time_content", time_content);
            params.put("writer_state", "2-013");
            params.put("time_state", "2-003");
            if (writeTimeImg != null) {
                time_img = TIMELINE_IMG + deliverVo.getDlvr_id() + "_" + writeTimeImg.getName();
                params.put("time_img", time_img);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("주소 실패", "fail");
            } else {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double latitude = location.getLatitude();
                double longtitude = location.getLongitude();
                String address = AddressUtil.getAddress(this, latitude, longtitude);
                Log.d("주소", address);
                String[] addrList = address.split(" ");
                Log.d("addrList", Arrays.toString(addrList));
                for (int i = 0; i < addrList.length; i++) {
                    String time_location = addrList[i].substring(addrList[i].length() - 1);
                    Log.d("동찾기", time_location);
                    if (time_location.equals("동")) {
                        params.put("time_location", addrList[i]);
                        break;
                    }
                }
            }
            Log.d("글쓰기", params.toString());
            String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
            return result;
        } else {
            return "fail";
        }
    }

    private void refresh() {
        Intent intent = new Intent(getApplicationContext(), Activity_Timeline.class);
        finish();
        startActivity(intent);
    }

    private void resetTimeline() {
        boardListView.setAdapter(null);
        showList = new ArrayList<>();
        page = 0;
        showEnd = false;
        islastItem = false;
        lockListView = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case WRITE_TIME_IMG:
                    writeTimeImg = FileUploadUtil.getFile(this, data.getData());
                    if (FileUploadUtil.isImage(writeTimeImg)) {
                        showImage(data.getData(), ivWriteImg);
                    }
                    break;
                case MOD_TIME_IMG:
                    modTimeImg = FileUploadUtil.getFile(this, data.getData());
                    if (FileUploadUtil.isImage(modTimeImg)) {
                        showImage(data.getData(), ivDialogTimeImg);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.btnToggleWriteForm:
                String text = btnToggleWriteForm.getText().toString();
                if (text.equals("글 작성")) {
                    boardWriteForm.setVisibility(View.VISIBLE);
                    btnToggleWriteForm.setText("닫기");
                } else {
                    boardWriteForm.setVisibility(View.GONE);
                    btnToggleWriteForm.setText("글 작성");
                    edtBoard.setText("");
                    ivWriteImg.setImageBitmap(null);
                    writeTimeImg = null;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                break;
            case R.id.btnInsertArticle:
                if (insertArticle().equals("insertArticle_success")) {
                    if (time_img != null) {
                        FileUploadUtil.upload(this, writeTimeImg, time_img);
                    }
                    boardWriteForm.setVisibility(View.GONE);
                    btnToggleWriteForm.setText("글 작성");
                    ivWriteImg.setImageBitmap(null);
                    edtBoard.setText("");
                    Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show();
                    refresh();
                } else {
                    Toast.makeText(this, "글 작성을 하지 않았거나, 사진을 등록하지 않았습니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSelectImg:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, WRITE_TIME_IMG);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && islastItem && !lockListView) {
            boardProgressbar.setVisibility(View.VISIBLE);
            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        islastItem = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final TimelineVo timelineVo = timelineList.get(position);
        Intent intent = new Intent(getApplicationContext(), Activity_TimelineInfo.class);
        intent.putExtra("time_no", timelineVo.getTime_no());
        isStart = false;
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (firstSelect) {
            int eventCode = position + 10300;
            resetTimeline();
            switch (eventCode) {
                case ALL_TIMELINE:
                    setTimelineList(ALL_TIMELINE);
                    break;
                case NOTICE_TIMELINE:
                    setTimelineList(NOTICE_TIMELINE);
                    break;
                case REVIEW_TIMELINE:
                    setTimelineList(REVIEW_TIMELINE);
                    break;
                case FREE_TIMELINE:
                    setTimelineList(FREE_TIMELINE);
                    break;
            }
        } else {
            firstSelect = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
