package com.kh.delivery_project.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.adapters.Adapter_MyOrderedList;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.OrderVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.DeliveryUtil;
import com.kh.delivery_project.util.FileUploadUtil;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_Deliver_MyPage extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher, Codes {

    Gson gson = new Gson();
    Context context = this;

    LinearLayout linMyInfo, linModifyInfo, linMyDelivery, MyLinMod01, MyLinMod02;
    Button btnMyInfo, btnModMyInfo, btnModDlvrImg, btnModOk, btnDeleteAccount, btnMyOrderedList, btnModPrev, btnModNext, btnSeacrhModAddr, btnClearAddr, btnClearImg;
    EditText edtModDlvrPw, edtModDlvrPwDupl, edtModDlvrPhone, edtModDlvrEmail, edtModDlvrAddr, edtModDlvrDetailAddr;
    TextView txtMyDlvrId, txtMyDlvrName, txtMyDlvrPhone, txtMyDlvrEmail, txtMyDlvrAddr, txtMyDlvrPoint, txtMyDlvrRank, txtChkModDlvrPwForm, txtChkModDlvrPwDupl, txtChkModDlvrEmailForm, txtChkModDlvrPhoneForm;
    ImageView ivMyDlvrImg, ivModDlvrImg;
    ListView lvMyOrderedList;

    DeliverVo deliverVo;
    File modImg;
    List<OrderVo> orderList = new ArrayList<>();
    int focusedId;
    String[] modValNames = {"dlvr_pw", "dlvr_phone", "dlvr_email", "dlvr_addr", "dlvr_img"};
    String[] modVals;
    ContentValues params = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_mypage);
        deliverVo = PreferenceManager.getDeliverVo(this);
        modVals = new String[]{deliverVo.getDlvr_pw(), deliverVo.getDlvr_phone(), deliverVo.getDlvr_email(), deliverVo.getDlvr_addr(), deliverVo.getDlvr_img()};
        setViews();
        setListeners();
        getCompletedOrder();
        setTexts();
        setListView();
    }

    private void setViews() {
        linMyInfo = findViewById(R.id.linMyInfo);
        linModifyInfo = findViewById(R.id.linModifyInfo);
        linMyDelivery = findViewById(R.id.linMyDelivery);
        MyLinMod01 = findViewById(R.id.MyLinMod01);
        MyLinMod02 = findViewById(R.id.MyLinMod02);
        btnMyInfo = findViewById(R.id.btnMyInfo);
        btnModMyInfo = findViewById(R.id.btnModMyInfo);
        btnModDlvrImg = findViewById(R.id.btnModDlvrImg);
        btnModOk = findViewById(R.id.btnModOk);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnMyOrderedList = findViewById(R.id.btnMyOrderedList);
        btnModPrev = findViewById(R.id.btnModPrev);
        btnModNext = findViewById(R.id.btnModNext);
        btnClearAddr = findViewById(R.id.btnClearAddr);
        btnSeacrhModAddr = findViewById(R.id.btnSeacrhModAddr);
        btnClearImg = findViewById(R.id.btnClearImg);
        edtModDlvrPw = findViewById(R.id.edtModDlvrPw);
        edtModDlvrPwDupl = findViewById(R.id.edtModDlvrPwDupl);
        edtModDlvrPhone = findViewById(R.id.edtModDlvrPhone);
        edtModDlvrEmail = findViewById(R.id.edtModDlvrEmail);
        edtModDlvrAddr = findViewById(R.id.edtModDlvrAddr);
        edtModDlvrDetailAddr = findViewById(R.id.edtModDlvrDetailAddr);
        txtMyDlvrId = findViewById(R.id.txtMyDlvrId);
        txtMyDlvrName = findViewById(R.id.txtMyDlvrName);
        txtMyDlvrPhone = findViewById(R.id.txtMyDlvrPhone);
        txtMyDlvrEmail = findViewById(R.id.txtMyDlvrEmail);
        txtMyDlvrAddr = findViewById(R.id.txtMyDlvrAddr);
        txtMyDlvrPoint = findViewById(R.id.txtMyDlvrPoint);
        txtMyDlvrRank = findViewById(R.id.txtMyDlvrRank);
        txtChkModDlvrPwForm = findViewById(R.id.txtChkModDlvrPwForm);
        txtChkModDlvrPwDupl = findViewById(R.id.txtChkModDlvrPwDupl);
        txtChkModDlvrEmailForm = findViewById(R.id.txtChkModDlvrEmailForm);
        txtChkModDlvrPhoneForm = findViewById(R.id.txtChkModDlvrPhoneForm);
        ivMyDlvrImg = findViewById(R.id.ivMyDlvrImg);
        ivModDlvrImg = findViewById(R.id.ivModDlvrImg);
        lvMyOrderedList = findViewById(R.id.lvMyOrderedList);

        String imgUrl = IMAGE_ADDRESS + deliverVo.getDlvr_img();
        UrlImageUtil urlImageUtil = new UrlImageUtil(imgUrl, ivMyDlvrImg);
        urlImageUtil.execute();

        Intent intent = getIntent();
        int page = intent.getIntExtra("page", 0);
        switch (page) {
            case SHOW_DLVR_INFO:
                setFrame();
                linMyInfo.setVisibility(View.VISIBLE);
                break;
            case SHOW_MOD_DLVR:
                setFrame();
                linModifyInfo.setVisibility(View.VISIBLE);
                break;
            case SHOW_DELIVERY:
                setFrame();
                linMyDelivery.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setListeners() {
        btnMyInfo.setOnClickListener(this);
        btnModMyInfo.setOnClickListener(this);
        btnModDlvrImg.setOnClickListener(this);
        btnModOk.setOnClickListener(this);
        btnDeleteAccount.setOnClickListener(this);
        btnMyOrderedList.setOnClickListener(this);
        btnModPrev.setOnClickListener(this);
        btnModNext.setOnClickListener(this);
        btnSeacrhModAddr.setOnClickListener(this);
        btnClearAddr.setOnClickListener(this);
        btnClearImg.setOnClickListener(this);
        edtModDlvrPw.setOnFocusChangeListener(this);
        edtModDlvrPwDupl.setOnFocusChangeListener(this);
        edtModDlvrPhone.setOnFocusChangeListener(this);
        edtModDlvrEmail.setOnFocusChangeListener(this);
        edtModDlvrPw.addTextChangedListener(this);
        edtModDlvrPwDupl.addTextChangedListener(this);
        edtModDlvrPhone.addTextChangedListener(this);
        edtModDlvrEmail.addTextChangedListener(this);
//        lvMyOrderedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                OrderVo orderVo = orderList.get(position);
//            }
//        });
    }

    private void getCompletedOrder() {
        String url = "/order/android/getCompletedOrder";
        ContentValues params = new ContentValues();
        params.put("dlvr_no", deliverVo.getDlvr_no());
        List<Map<String, Object>> list = gson.fromJson(ConnectServer.getData(url, params), List.class);
        for(int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            OrderVo orderVo = ConvertUtil.getOrderVo(map);
            orderList.add(orderVo);
        }
    }

    private void setTexts() {
        txtMyDlvrId.setText(deliverVo.getDlvr_id());
        txtMyDlvrName.setText(deliverVo.getDlvr_name());
        txtMyDlvrPhone.setText(deliverVo.getDlvr_phone());
        txtMyDlvrEmail.setText(deliverVo.getDlvr_email());
        txtMyDlvrAddr.setText(deliverVo.getDlvr_addr());
        txtMyDlvrPoint.setText(String.valueOf(deliverVo.getDlvr_point()));
        txtMyDlvrRank.setText(deliverVo.getDlvr_rank());
    }

    private void setListView() {
        if(orderList != null) {
            Adapter_MyOrderedList adapter = new Adapter_MyOrderedList(this, R.layout.view_myorderedlist, orderList);
            lvMyOrderedList.setAdapter(adapter);
        }
    }

    private void setFrame() {
        linMyInfo.setVisibility(View.GONE);
        linModifyInfo.setVisibility(View.GONE);
        linMyDelivery.setVisibility(View.GONE);
    }

    private boolean showImage(Uri uri, File file, ImageView iv) {
        if(FileUploadUtil.isImage(file)) {
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                Bitmap image = BitmapFactory.decodeStream(is);
                iv.setImageBitmap(image);
                is.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "이미지 파일만", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean checkModValues() {
        boolean b = true;
        int red = Color.RED;
        int chkPwForm = txtChkModDlvrPwForm.getCurrentTextColor();
        int chkPhoneForm = txtChkModDlvrPhoneForm.getCurrentTextColor();
        int chkEmailForm = txtChkModDlvrEmailForm.getCurrentTextColor();

        if(red == chkPwForm || red == chkPhoneForm || red == chkEmailForm) {
            b = false;
        }

        return b;
    }

    private void modifyDeliver() {
        String url = "/deliver/android/modifyDeliver";
        params.put("dlvr_no", deliverVo.getDlvr_no());
        for(int i = 0; i < modValNames.length; i++) {
            params.put(modValNames[i], modVals[i]);
        }
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        if(result.equals("modify_deliver_success")) {
            if(modImg != null) {
                FileUploadUtil.modify(this, modImg, deliverVo.getDlvr_img(), modVals[4]);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "수정 성공", Toast.LENGTH_SHORT).show();
                    deliverVo.setDlvr_pw(modVals[0]);
                    deliverVo.setDlvr_phone(modVals[1]);
                    deliverVo.setDlvr_email(modVals[2]);
                    deliverVo.setDlvr_addr(modVals[3]);
                    deliverVo.setDlvr_img(modVals[4]);
                    PreferenceManager.setDeliverVo(context, deliverVo);
                    Intent intent = new Intent(getApplicationContext(), Activity_Deliver_Main.class);
                    intent.putExtra("result", "modify");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }, 3000);
        } else {
            Toast.makeText(this, "수정 실패", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case MOD_DLVR_IMG:
                    File file = FileUploadUtil.getFile(this, data.getData());
                    if(showImage(data.getData(), file, ivModDlvrImg)) {
                        this.modImg = file;
                        modVals[4] = DLVR_IMG + deliverVo.getDlvr_id() + "_" + modImg.getName();
                    }
                    break;
                case MOD_DLVR_ADDR:
                    String addr = data.getExtras().getString("data");
                    if(addr != null) {
                        edtModDlvrAddr.setText(addr);
                        modVals[3] = addr;
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
            case R.id.btnMyInfo:
                setFrame();
                linMyInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.btnModMyInfo:
                setFrame();
                linModifyInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.btnMyOrderedList:
                setFrame();
                linMyDelivery.setVisibility(View.VISIBLE);
                break;
            case R.id.btnModDlvrImg:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, MOD_DLVR_IMG);
                break;
            case R.id.btnModOk:
                if(checkModValues()) {
                    modifyDeliver();
                } else {
                    Toast.makeText(this, "수정 사항 확인", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDeleteAccount:
                break;
            case R.id.btnModPrev:
                MyLinMod01.setVisibility(View.VISIBLE);
                MyLinMod02.setVisibility(View.INVISIBLE);
                btnModNext.setVisibility(View.VISIBLE);
                btnModPrev.setVisibility(View.GONE);
                break;
            case R.id.btnModNext:
                MyLinMod01.setVisibility(View.INVISIBLE);
                MyLinMod02.setVisibility(View.VISIBLE);
                btnModNext.setVisibility(View.GONE);
                btnModPrev.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSeacrhModAddr:
                intent = new Intent(getApplicationContext(), Activity_WebView.class);
                startActivityForResult(intent, MOD_DLVR_ADDR);
                break;
            case R.id.btnClearAddr:
                edtModDlvrAddr.setText("");
                edtModDlvrDetailAddr.setText("");
                modVals[3] = deliverVo.getDlvr_addr();
                break;
            case R.id.btnClearImg:
                ivModDlvrImg.setImageBitmap(null);
                modImg = null;
                modVals[4] = deliverVo.getDlvr_img();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString();

        switch (focusedId) {
            case R.id.edtModDlvrPw:
                if(string.length() == 0) {
                    txtChkModDlvrPwForm.setText("");
                    txtChkModDlvrPwForm.setTextColor(Color.BLACK);
                } else if(string.length() < 8) {
                    txtChkModDlvrPwForm.setText("비밀번호는 8자리 이상");
                    txtChkModDlvrPwForm.setTextColor(Color.RED);
                } else if(string.equals(deliverVo.getDlvr_pw())) {
                    txtChkModDlvrPwForm.setText("기존 비밀번호와 동일함");
                    txtChkModDlvrPwForm.setTextColor(Color.RED);
                } else {
                    for(int i = 0; i < string.length(); i++) {
                        char code = string.charAt(i);
                        if((code >= 48 && code <= 57) || (code >= 65 && code <= 90) || (code >= 97 && code <= 122)) {
                            txtChkModDlvrPwForm.setText("옳은 비밀번호");
                            txtChkModDlvrPwForm.setTextColor(Color.BLUE);
                        } else {
                            txtChkModDlvrPwForm.setText("옳지 않은 비밀번호");
                            txtChkModDlvrPwForm.setTextColor(Color.RED);
                            break;
                        }
                    }
                }
                break;
            case R.id.edtModDlvrPwDupl:
                if(txtChkModDlvrPwForm.getText().toString().equals("옳은 비밀번호")) {
                    if(string.length() == 0) {
                        txtChkModDlvrPwDupl.setText("");
                        txtChkModDlvrPwDupl.setTextColor(Color.BLACK);
                        modVals[0] = deliverVo.getDlvr_pw();
                    } else {
                        String password = edtModDlvrPw.getText().toString();
                        if(string.equals(password)) {
                            txtChkModDlvrPwDupl.setText("비밀번호 일치");
                            txtChkModDlvrPwDupl.setTextColor(Color.BLUE);
                            modVals[0] = string;
                        } else {
                            txtChkModDlvrPwDupl.setText("비밀번호 불일치");
                            txtChkModDlvrPwDupl.setTextColor(Color.RED);
                        }
                    }
                } else {
                    txtChkModDlvrPwDupl.setText("비밀번호부터 맞게 입력해라");
                    txtChkModDlvrPwDupl.setTextColor(Color.RED);
                }
                break;
            case R.id.edtModDlvrPhone:
                if(string.length() == 0) {
                    txtChkModDlvrPhoneForm.setText("");
                    txtChkModDlvrPhoneForm.setTextColor(Color.BLACK);
                    modVals[1] = deliverVo.getDlvr_phone();
                } else if(string.length() >= 9) {
                    txtChkModDlvrPhoneForm.setText("올바른 전화번호 형식");
                    txtChkModDlvrPhoneForm.setTextColor(Color.BLUE);
                    modVals[1] = string;
                } else {
                    txtChkModDlvrPhoneForm.setText("올바르지 않은 전화번호 형식");
                    txtChkModDlvrPhoneForm.setTextColor(Color.RED);
                }
                break;
            case R.id.edtModDlvrEmail:
                if(string.length() == 0) {
                    txtChkModDlvrEmailForm.setText("");
                    txtChkModDlvrEmailForm.setTextColor(Color.BLACK);
                    modVals[2] = deliverVo.getDlvr_email();
                } else if(string.contains("@") && string.contains(".") &&
                        (string.indexOf("@") != 0) &&
                        (string.indexOf("@")+1 < string.indexOf(".")) &&
                        (string.indexOf(".")+1 < string.length())) {
                    txtChkModDlvrEmailForm.setText("옳은 이메일");
                    txtChkModDlvrEmailForm.setTextColor(Color.BLUE);
                    modVals[2] = string;
                } else {
                    txtChkModDlvrEmailForm.setText("옳지 않은 이메일");
                    txtChkModDlvrEmailForm.setTextColor(Color.RED);
                }
                break;
            case R.id.edtModDlvrDetailAddr:
                String modAddr = edtModDlvrAddr.getText().toString();
                if(modAddr.equals("") || modAddr == null) {
                    edtModDlvrDetailAddr.setText("");
                    modVals[3] = deliverVo.getDlvr_addr();
                } else {
                    if(string.length() == 0) {
                        modVals[3] = modAddr;
                    } else {
                        modVals[3] = modAddr + " " + string;
                    }
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        focusedId = v.getId();
    }
}
