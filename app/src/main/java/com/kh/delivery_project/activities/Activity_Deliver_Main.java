package com.kh.delivery_project.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.Keys;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Deliver_Main extends AppCompatActivity implements Codes, Keys, View.OnClickListener, DrawerLayout.DrawerListener {

    Toolbar toolbar;
    private DeliverVo deliverVo;

    DrawerLayout drawerLayout;
    View drawerView;
    Button btnStartDeliver, btnDMainToBoard;
    TextView drawerDlvrName;
    ImageView drawerDlvrImg;
    RelativeLayout relDlvrInfo, relModDlvrInfo, relHelp, relLogout;

//    private Socket socket;
//    {
//        try {
//            socket= IO.socket(IP);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_main);
        getIntents();
        setViews();
        setListeners();

//        socket.on("service", onNewMessage);
//        socket.connect();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }
                    // 메시지를 받으면 data에 담고,
                    // username와 message라는 키값으로 들어왔다는 가정으로 작성된 코드다.
                    // addMessage(username, message); 이런 식으로 코드를 실행시키면 addMessage 쪽으로 인자를 담아 보내니 화면에 노출하게 만들면 될 것이다.
                }
            });
        }
    };

    private Activity getActivity() {
        return this;
    }

    private void getIntents() {
        Intent intent = getIntent();
        DeliverVo deliverVo = intent.getParcelableExtra("deliverVo");
        this.deliverVo = deliverVo;
    }

    private void setViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerView = (View) findViewById(R.id.drawerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnStartDeliver = findViewById(R.id.btnStartDeliver);
        btnDMainToBoard = findViewById(R.id.btnDMainToBoard);
        drawerDlvrName = findViewById(R.id.drawerDlvrName);
        drawerDlvrImg = findViewById(R.id.drawerDlvrImg);
        relDlvrInfo = findViewById(R.id.relDlvrInfo);
        relModDlvrInfo = findViewById(R.id.relModDlvrInfo);
        relHelp = findViewById(R.id.relHelp);
        relLogout = findViewById(R.id.relLogout);

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
        btnStartDeliver.setOnClickListener(this);
        btnDMainToBoard.setOnClickListener(this);
        relDlvrInfo.setOnClickListener(this);
        relModDlvrInfo.setOnClickListener(this);
        relHelp.setOnClickListener(this);
        relLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        switch (id) {
            case R.id.btnStartDeliver:
                intent = new Intent(getApplicationContext(), Activity_KakaoMap.class);
                intent.putExtra("deliverVo", deliverVo);
                startActivity(intent);
                break;
            case R.id.btnDMainToBoard:
                intent = new Intent(getApplicationContext(), Activity_Timeline.class);
                intent.putExtra("deliverVo", deliverVo);
                startActivity(intent);
                break;
            case R.id.relDlvrInfo:
                Toast.makeText(this, "내 정보 보기", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), Activity_Deliver_MyPage.class);
                intent.putExtra("deliverVo", deliverVo);
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
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}
