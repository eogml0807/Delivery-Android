package com.kh.delivery_project.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.OrderVo;
import com.kh.delivery_project.domain.UserVo;
import com.kh.delivery_project.util.ConvertUtil;

import java.util.Map;

public class Dialog_PickedOrder extends Activity {

    Gson gson = new Gson();

    TextView txtOrderNo, txtOrderAddr, txtUserName, txtOrderReq;
    Button btnOrderPick;

    OrderVo orderVo;
    DeliverVo deliverVo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pickedorder);
        setTitle("주문 정보");
        getIntents();
        setViews();
        setListeners();
    }

    private void setListeners() {
        btnOrderPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "/order/android/pickOrder";
                ContentValues params = new ContentValues();
                params.put("order_no", orderVo.getOrder_no());
                params.put("dlvr_no", deliverVo.getDlvr_no());
                String result = gson.fromJson(ConnectServer.getData(url, params), String.class);

                Intent intent = new Intent(getApplicationContext(), Activity_KakaoMap.class);
                if(result.equals("pickOrder_success")) {
                    orderVo.setDlvr_no(deliverVo.getDlvr_no());
                    orderVo.setOrder_state("3-002");
                    intent.putExtra("orderVo", orderVo);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });
    }

    private void setViews() {
        txtOrderNo = findViewById(R.id.txtOrderNo);
        txtUserName = findViewById(R.id.txtUserName);
        txtOrderAddr = findViewById(R.id.txtOrderAddr);
        txtOrderReq = findViewById(R.id.txtOrderReq);
        btnOrderPick = findViewById(R.id.btnOrderPick);

        txtOrderNo.setText(String.valueOf(orderVo.getOrder_no()));
        txtUserName.setText(orderVo.getUser_name());
        txtOrderAddr.setText(orderVo.getOrder_addr());
        txtOrderReq.setText(orderVo.getOrder_req());
    }

    private void getIntents() {
        Intent intent = getIntent();
        this.orderVo = intent.getParcelableExtra("orderVo");
        this.deliverVo = intent.getParcelableExtra("deliverVo");
    }
}
