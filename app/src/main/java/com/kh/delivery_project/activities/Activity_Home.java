package com.kh.delivery_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.PreferenceManager;

import java.util.Date;
import java.util.Map;


public class Activity_Home extends AppCompatActivity implements Codes, View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnKeyListener {

    Gson gson = new Gson();

    Button btnLogin, btnRegistDeliver, btnFindAccount;
    EditText edtLoginDlvrId, edtLoginDlvrPw;
    CheckBox chkLoginAuto;

    boolean autoLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PreferenceManager.removeDeliverVo(this);
        setViews();
        setListeners();
        setPermission();
        isAutoLogin();

    }

    private void setViews() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistDeliver = findViewById(R.id.btnRegistDeliver);
        btnFindAccount = findViewById(R.id.btnFindAccount);
        edtLoginDlvrId = findViewById(R.id.edtLoginDlvrId);
        edtLoginDlvrPw = findViewById(R.id.edtLoginDlvrPw);
        chkLoginAuto = findViewById(R.id.chkLoginAuto);

        btnRegistDeliver.setPaintFlags(btnRegistDeliver.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnFindAccount.setPaintFlags(btnFindAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edtLoginDlvrId.setText("dlvr500");
        edtLoginDlvrPw.setText("1q2w3e4r");
    }

    private void setListeners() {
        btnLogin.setOnClickListener(this);
        btnRegistDeliver.setOnClickListener(this);
        btnFindAccount.setOnClickListener(this);
        edtLoginDlvrId.setOnKeyListener(this);
        edtLoginDlvrPw.setOnKeyListener(this);
        chkLoginAuto.setOnCheckedChangeListener(this);
    }

    private void setPermission() {
        // 위치 권한 확인
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            // 권한 없으므로 권한 부여
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // 권한 있음
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // 권한 있음
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // 권한 있음
        }
    }

    private void isAutoLogin() {
        boolean auto_login = PreferenceManager.getBoolean(this, "auto_login");
        if (auto_login) {
            String dlvr_id = PreferenceManager.getString(this, "auto_dlvr_id");
            String dlvr_pw = PreferenceManager.getString(this, "auto_dlvr_pw");
            DeliverVo deliverVo = login(dlvr_id, dlvr_pw);
            if (deliverVo != null) {
                Intent intent = new Intent(getApplicationContext(), Activity_Deliver_Main.class);
                intent.putExtra("deliverVo", deliverVo);
                finish();
                startActivity(intent);
            } else {
                PreferenceManager.removeKey(this, "auto_login");
                PreferenceManager.removeKey(this, "auto_dlvr_id");
                PreferenceManager.removeKey(this, "auto_dlvr_pw");

                Toast.makeText(this, "계정 없어짐", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private DeliverVo login(String dlvr_id, String dlvr_pw) {
        if ((dlvr_id.equals("") || dlvr_id == null) || (dlvr_pw.equals("") || dlvr_pw == null)) {
            Toast.makeText(this, "아이디나 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            String url = "/deliver/android/login";
            ContentValues params = new ContentValues();
            params.put("dlvr_id", dlvr_id);
            params.put("dlvr_pw", dlvr_pw);
            Map<String, Object> map = gson.fromJson(ConnectServer.getData(url, params), Map.class);
            DeliverVo deliverVo = ConvertUtil.getDeliverVo(map);
            PreferenceManager.setDeliverVo(this, deliverVo);
            return deliverVo;
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 권한 허용
        } else {
            // 권한 거부
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.btnLogin:
                String dlvr_id = edtLoginDlvrId.getText().toString();
                String dlvr_pw = edtLoginDlvrPw.getText().toString();
                DeliverVo deliverVo = login(dlvr_id, dlvr_pw);
                if (deliverVo != null) {
                    if (autoLogin) {
                        PreferenceManager.setString(this, "auto_dlvr_id", deliverVo.getDlvr_id());
                        PreferenceManager.setString(this, "auto_dlvr_pw", deliverVo.getDlvr_pw());
                        PreferenceManager.setBoolean(this, "auto_login", true);
                    }
                    intent = new Intent(getApplicationContext(), Activity_Deliver_Main.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegistDeliver:
                intent = new Intent(getApplicationContext(), Activity_Deliver_Regist.class);
                startActivity(intent);
                break;
            case R.id.btnFindAccount:
                intent = new Intent(getApplicationContext(), Activity_FindAccount.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.autoLogin = true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (v == edtLoginDlvrPw) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
            return true;
        }
        return false;
    }
}