package com.kh.delivery_project.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.OrderVo;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.util.AddressUtil;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.Keys;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.util.Map;

public class Activity_Deliver_Main extends AppCompatActivity implements Codes, Keys, View.OnClickListener, DrawerLayout.DrawerListener {

    Gson gson = new Gson();
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    View drawerView;
    Button btnMainToTimeline, btnMainToDelivery;
    RelativeLayout relDlvrInfo, relModDlvrInfo, relHelp, relLogout;
    LinearLayout linTimeline, linDelivery, linMainOrder, linMainNoOrder, linLastTimeStar;
    TextView drawerDlvrName, txtLastWriterName, txtLastTimeContent, txtLastTimeStar, txtMainOrderCa, txtMainOrderReq, txtMainOrderLoc;
    ImageView drawerDlvrImg;
    RatingBar rbLastTimeStar;

    TimelineVo timelineVo;
    DeliverVo deliverVo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_main);
        deliverVo = PreferenceManager.getDeliverVo(this);
        setViews();
        setListeners();
        setLastTimeline();
        setMyOrder();
    }

    private void setViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerView = (View) findViewById(R.id.drawerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnMainToTimeline = findViewById(R.id.btnMainToTimeline);
        btnMainToDelivery = findViewById(R.id.btnMainToDelivery);
        linTimeline = findViewById(R.id.linTimeline);
        linDelivery = findViewById(R.id.linDelivery);
        linMainNoOrder = findViewById(R.id.linMainNoOrder);
        linMainOrder = findViewById(R.id.linMainOrder);
        linLastTimeStar = findViewById(R.id.linLastTimeStar);
        txtLastWriterName = findViewById(R.id.txtLastWriterName);
        txtLastTimeContent = findViewById(R.id.txtLastTimeContent);
        txtLastTimeStar = findViewById(R.id.txtLastTimeStar);
        txtMainOrderCa = findViewById(R.id.txtMainOrderCa);
        txtMainOrderReq = findViewById(R.id.txtMainOrderReq);
        txtMainOrderLoc = findViewById(R.id.txtMainOrderLoc);
        drawerDlvrName = findViewById(R.id.drawerDlvrName);
        drawerDlvrImg = findViewById(R.id.drawerDlvrImg);
        relDlvrInfo = findViewById(R.id.relDlvrInfo);
        relModDlvrInfo = findViewById(R.id.relModDlvrInfo);
        relHelp = findViewById(R.id.relHelp);
        relLogout = findViewById(R.id.relLogout);
        rbLastTimeStar = findViewById(R.id.rbLastTimeStar);

        toolbar.setTitle(R.string.deliver_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_stat_name);

        String dlvr_name = deliverVo.getDlvr_name();
        drawerDlvrName.setText(dlvr_name);
        String imgUrl = IMAGE_ADDRESS + deliverVo.getDlvr_img();
        UrlImageUtil urlImageUtil = new UrlImageUtil(imgUrl, drawerDlvrImg);
        urlImageUtil.execute();
    }

    private void setListeners() {
        drawerLayout.setDrawerListener(this);
        btnMainToTimeline.setOnClickListener(this);
        btnMainToDelivery.setOnClickListener(this);
        relDlvrInfo.setOnClickListener(this);
        relModDlvrInfo.setOnClickListener(this);
        relHelp.setOnClickListener(this);
        relLogout.setOnClickListener(this);
        linTimeline.setOnClickListener(this);
        linDelivery.setOnClickListener(this);
    }

    private void setLastTimeline() {
        String url = "/timeline/android/getLastTimeline";
        Map<String, Object> map = gson.fromJson(ConnectServer.getData(url), Map.class);
        if(map != null) {
            timelineVo = ConvertUtil.getTimelineVo(map);
            txtLastWriterName.setText(timelineVo.getWriter_name());
            txtLastTimeContent.setText(timelineVo.getTime_content());
            String time_state = timelineVo.getTime_state();
            if(time_state.equals("2-002")) {
                linLastTimeStar.setVisibility(View.VISIBLE);
                txtLastTimeStar.setText(String.valueOf(timelineVo.getTime_star()));
                rbLastTimeStar.setRating((float) timelineVo.getTime_star());
            }
        }
    }

    private void setMyOrder() {
        String url = "/order/android/getPickedOrder";
        ContentValues params = new ContentValues();
        params.put("dlvr_no", deliverVo.getDlvr_no());
        Map<String, Object> map = gson.fromJson(ConnectServer.getData(url, params), Map.class);
        if(map != null) {
            linMainOrder.setVisibility(View.VISIBLE);
            linMainNoOrder.setVisibility(View.GONE);
            OrderVo orderVo = ConvertUtil.getOrderVo(map);
            txtMainOrderCa.setText(orderVo.getOrder_ca());
            txtMainOrderReq.setText(orderVo.getOrder_req());
            double order_lat = orderVo.getOrder_lat();
            double order_lng = orderVo.getOrder_lng();
            String order_addr = AddressUtil.getAddress(this, order_lat, order_lng);
            txtMainOrderLoc.setText(order_addr);
        }
    }

    private void setListenersOnDraw(View.OnClickListener listener) {
        linTimeline.setOnClickListener(listener);
        linDelivery.setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        switch (id) {
            case R.id.relDlvrInfo:
                Toast.makeText(this, "내 정보 보기", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), Activity_Deliver_MyPage.class);
                startActivity(intent);
                break;
            case R.id.relModDlvrInfo:
                Toast.makeText(this, "정보 수정", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relHelp:
                Toast.makeText(this, "도움 액티비티", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relLogout:
                intent = new Intent(getApplicationContext(), Activity_Home.class);
                PreferenceManager.clear(this);
                finish();
                startActivity(intent);
                break;
            case R.id.linDelivery:
                intent = new Intent(getApplicationContext(), Activity_Delivery.class);
                startActivity(intent);
                break;
            case R.id.btnMainToDelivery:
                intent = new Intent(getApplicationContext(), Activity_Delivery.class);
                startActivity(intent);
                break;
            case R.id.linTimeline:
                intent = new Intent(getApplicationContext(), Activity_Timeline.class);
                startActivity(intent);
                break;
            case R.id.btnMainToTimeline:
                intent = new Intent(getApplicationContext(), Activity_Timeline.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("menu id", "" + id);
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(drawerView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        setListenersOnDraw(null);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        setListenersOnDraw(this);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}
