package com.kh.delivery_project.activities;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.util.ConvertUtil;

import java.util.Map;

public class Activity_FindAccount extends AppCompatActivity implements View.OnClickListener {

    Gson gson = new Gson();

    Button btnFindId, btnFindPw;
    TextView txtFindAccResult;
    EditText edtFindAccId, edtFindAccName, edtFindAccEmail, edtFindAccPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findaccount);
        setViews();
        setListeners();
    }

    private void setViews() {
        btnFindId = findViewById(R.id.btnFindId);
        btnFindPw = findViewById(R.id.btnFindPw);
        txtFindAccResult = findViewById(R.id.txtFindAccResult);
        edtFindAccId = findViewById(R.id.edtFindAccId);
        edtFindAccName = findViewById(R.id.edtFindAccName);
        edtFindAccEmail = findViewById(R.id.edtFindAccEmail);
        edtFindAccPhone = findViewById(R.id.edtFindAccPhone);
    }

    private void setListeners() {
        btnFindId.setOnClickListener(this);
        btnFindPw.setOnClickListener(this);
    }

    private void findAccountId() {
        String dlvr_name = edtFindAccName.getText().toString();
        String dlvr_email = edtFindAccEmail.getText().toString();
        String dlvr_phone = edtFindAccPhone.getText().toString();

        if((dlvr_name != null && !dlvr_name.equals("")) &&
                (dlvr_email != null && !dlvr_email.equals("")) &&
                (dlvr_phone != null && !dlvr_phone.equals(""))) {
            String url = "/account/findAccountId";
            ContentValues params = new ContentValues();
            params.put("acc_name", dlvr_name);
            params.put("acc_email", dlvr_email);
            params.put("acc_phone", dlvr_phone);
            String dlvr_id = gson.fromJson(ConnectServer.getData(url, params), String.class);
            if(dlvr_id != null) {
                String hide_dlvr_id = dlvr_id.substring(0, dlvr_id.length() - 3) + "***";
                String text = "아이디는 " + hide_dlvr_id + " 입니다. \n 뒤 3자리는 가림";
                txtFindAccResult.setText(text);
                txtFindAccResult.setTextColor(Color.BLUE);

                edtFindAccId.setVisibility(View.VISIBLE);
                btnFindPw.setVisibility(View.VISIBLE);
                btnFindId.setVisibility(View.GONE);
            } else {
                txtFindAccResult.setText("일치하는 정보가 없습니다.");
                txtFindAccResult.setTextColor(Color.RED);
            }
        } else {
            txtFindAccResult.setText("빈 값이 있습니다.");
            txtFindAccResult.setTextColor(Color.RED);
        }
    }

    private void findAccountPw() {
        String dlvr_id = edtFindAccId.getText().toString();
        String dlvr_name = edtFindAccName.getText().toString();
        String dlvr_email = edtFindAccEmail.getText().toString();
        String dlvr_phone = edtFindAccPhone.getText().toString();

        if((dlvr_id != null && !dlvr_id.equals("")) &&
                (dlvr_name != null && !dlvr_name.equals("")) &&
                (dlvr_email != null && !dlvr_email.equals("")) &&
                (dlvr_phone != null && !dlvr_phone.equals(""))) {
            String url = "/account/sendAccountPw";
            ContentValues params = new ContentValues();
            params.put("acc_id", dlvr_id);
            params.put("acc_name", dlvr_name);
            params.put("acc_email", dlvr_email);
            params.put("acc_phone", dlvr_phone);
            String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
            if(result.equals("sendAccountPw_success")) {
                Toast.makeText(this, "이메일로 비밀번호 전송", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                txtFindAccResult.setText("일치하는 정보가 없습니다.");
                txtFindAccResult.setTextColor(Color.RED);
            }
        } else {
            txtFindAccResult.setText("빈 값이 있습니다.");
            txtFindAccResult.setTextColor(Color.RED);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnFindId:
                findAccountId();
                break;
            case R.id.btnFindPw:
                findAccountPw();
                break;
        }
    }
}
