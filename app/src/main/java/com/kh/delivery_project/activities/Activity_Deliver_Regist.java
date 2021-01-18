package com.kh.delivery_project.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.FileUploadUtil;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Activity_Deliver_Regist extends AppCompatActivity implements Codes, View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    Gson gson = new Gson();

    ImageView ivRegDlvrImg, ivRegDlvrIdCard;
    Button btnRegPrev, btnRegNext, btnCheckIdDupl, btnSelectBirth, btnSeacrhAddr, btnRegDlvrImg, btnRegDlvrIdCard;
    TextView txtChkDlvrIdForm, txtChkDlvrPwForm, txtChkDlvrPwDupl, txtChkDlvrNameForm, txtChkDlvrPhoneForm, txtChkEmailForm;
    EditText edtRegDlvrId, edtRegDlvrPw, edtRegDlvrPwDupl, edtRegDlvrName, edtRegDlvrPhone, edtRegDlvrEmail, edtRegDlvrAddr, edtRegDlvrDetailAddr, edtRegDlvrBirth;
    LinearLayout[] linRegs = new LinearLayout[5];
    int[] lin_ids = {R.id.linReg1, R.id.linReg2, R.id.linReg3, R.id.linReg4, R.id.linReg5};

    File idCard;
    File img;
    int focusedId;
    int index = 0;
    boolean[] checkValues = new boolean[11];
    int[][] checkIndexs = {{0, 3},{4, 6},{7, 8},{9},{10}};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_regist);
        setViews();
        setListeners();
    }

    private void setViews() {
        btnRegPrev = findViewById(R.id.btnRegPrev);
        btnRegNext = findViewById(R.id.btnRegNext);
        btnCheckIdDupl = findViewById(R.id.btnCheckIdDupl);
        btnSelectBirth = findViewById(R.id.btnSelectBirth);
        btnRegDlvrIdCard = findViewById(R.id.btnRegDlvrIdCard);
        btnRegDlvrImg = findViewById(R.id.btnRegDlvrImg);
        btnSeacrhAddr = findViewById(R.id.btnSeacrhAddr);
        txtChkDlvrIdForm = findViewById(R.id.txtChkDlvrIdForm);
        txtChkDlvrPwForm = findViewById(R.id.txtChkDlvrPwForm);
        txtChkDlvrPwDupl = findViewById(R.id.txtChkDlvrPwDupl);
        txtChkDlvrNameForm = findViewById(R.id.txtChkDlvrNameForm);
        txtChkEmailForm = findViewById(R.id.txtChkEmailForm);
        edtRegDlvrBirth = findViewById(R.id.edtRegDlvrBirth);
        txtChkDlvrPhoneForm = findViewById(R.id.txtChkDlvrPhoneForm);
        edtRegDlvrId = findViewById(R.id.edtRegDlvrId);
        edtRegDlvrPw = findViewById(R.id.edtRegDlvrPw);
        edtRegDlvrPwDupl = findViewById(R.id.edtRegDlvrPwDupl);
        edtRegDlvrName = findViewById(R.id.edtRegDlvrName);
        edtRegDlvrPhone = findViewById(R.id.edtRegDlvrPhone);
        edtRegDlvrEmail = findViewById(R.id.edtRegDlvrEmail);
        edtRegDlvrAddr = findViewById(R.id.edtRegDlvrAddr);
        edtRegDlvrDetailAddr = findViewById(R.id.edtRegDlvrDetailAddr);
        ivRegDlvrImg = findViewById(R.id.ivRegDlvrImg);
        ivRegDlvrIdCard = findViewById(R.id.ivRegDlvrIdCard);
        for(int i = 0; i < linRegs.length; i++) {
            linRegs[i] = findViewById(lin_ids[i]);
        }

        edtRegDlvrBirth.setEnabled(false);
        edtRegDlvrAddr.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListeners() {
        btnRegDlvrImg.setOnClickListener(this);
        btnRegDlvrIdCard.setOnClickListener(this);
        btnRegPrev.setOnClickListener(this);
        btnRegNext.setOnClickListener(this);
        btnCheckIdDupl.setOnClickListener(this);
        btnSelectBirth.setOnClickListener(this);
        btnSeacrhAddr.setOnClickListener(this);
        edtRegDlvrId.addTextChangedListener(this);
        edtRegDlvrId.setOnFocusChangeListener(this);
        edtRegDlvrPw.addTextChangedListener(this);
        edtRegDlvrPw.setOnFocusChangeListener(this);
        edtRegDlvrPwDupl.addTextChangedListener(this);
        edtRegDlvrPwDupl.setOnFocusChangeListener(this);
        edtRegDlvrName.addTextChangedListener(this);
        edtRegDlvrName.setOnFocusChangeListener(this);
        edtRegDlvrEmail.addTextChangedListener(this);
        edtRegDlvrEmail.setOnFocusChangeListener(this);
        edtRegDlvrPhone.addTextChangedListener(this);
        edtRegDlvrPhone.setOnFocusChangeListener(this);
    }

    // 회원가입 폼 페이지 변경
    private void setRegistForm() {
        if(index >= 4) {
            index = 4;
            btnRegNext.setText("작성 완료");
        } else if(index > 0) {
            btnRegNext.setText("다음");
            btnRegPrev.setVisibility(View.VISIBLE);
        } else if(index <= 0) {
            index = 0;
            btnRegNext.setText("다음");
            btnRegPrev.setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i < linRegs.length; i++) {
            linRegs[i].setVisibility(View.INVISIBLE);
        }
        linRegs[index].setVisibility(View.VISIBLE);
    }

    // 회원 가입
    private String registDeliver() {
        String dlvr_id = edtRegDlvrId.getText().toString();
        String dlvr_img = DLVR_IMG + dlvr_id + "_" + img.getName();
        String dlvr_idcard = DLVR_IDCARD + dlvr_id + "_" + idCard.getName();

        String url = "/deliver/android/registDeliver";
        ContentValues params = new ContentValues();
        params.put("dlvr_id", dlvr_id);
        params.put("dlvr_pw", edtRegDlvrPw.getText().toString());
        params.put("dlvr_name", edtRegDlvrName.getText().toString());
        params.put("dlvr_phone", edtRegDlvrPhone.getText().toString());
        params.put("dlvr_email", edtRegDlvrEmail.getText().toString());
        params.put("dlvr_addr", edtRegDlvrAddr.getText().toString() + " " + edtRegDlvrDetailAddr.getText().toString());
        params.put("str_dlvr_birth", edtRegDlvrBirth.getText().toString());
        params.put("dlvr_img", dlvr_img);
        params.put("dlvr_idcard", dlvr_idcard);
        
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        if(result.equals("registSuccess")) {
            FileUploadUtil.upload(this, img, dlvr_img);
            FileUploadUtil.upload(this, idCard, dlvr_idcard);
        }
        return result;
    }

    // 아이디 중복 검사
    private void checkIdDupl() {
        String url = "/account/checkIdDupl";
        ContentValues params = new ContentValues();
        params.put("acc_id", edtRegDlvrId.getText().toString());
        Boolean result = gson.fromJson(ConnectServer.getData(url, params), Boolean.class);
        if(result) {
            Toast.makeText(this, "사용 가능한 아이디", Toast.LENGTH_SHORT).show();
            checkValues[1] = true;
        } else {
            Toast.makeText(this, "사용 불가능한 아이디", Toast.LENGTH_SHORT).show();
            checkValues[1] = false;
        }
    }

    // 프로필, 신분증 이미지 출력
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
            Toast.makeText(this, "이미지 파일만 올려주세요", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // 페이지 별 값 유효성 확인
    private boolean checkValuesOk() {
        boolean b = true;
        Log.d("checkValues", checkValues.toString());
        if(checkIndexs[index].length > 1) {
            int startIndex = checkIndexs[index][0];
            int endIndex = checkIndexs[index][1];
            for(int i = startIndex; i <= endIndex; i++) {
                if(!checkValues[i]) {
                    b = false;
                    break;
                }
            }
        } else {
            int checkIndex = checkIndexs[index][0];
            if(!checkValues[checkIndex]) {
                b = false;
            }
        }
        return b;
    }

    // 달력 다이얼로그
    private void showDatePickerDialog() {
        DateFormat str_year = new SimpleDateFormat("yyyy");
        DateFormat str_month = new SimpleDateFormat("MM");
        DateFormat str_day = new SimpleDateFormat("dd");
        int y = Integer.parseInt(str_year.format(System.currentTimeMillis()));
        int m = Integer.parseInt(str_month.format(System.currentTimeMillis())) - 1;
        int d = Integer.parseInt(str_day.format(System.currentTimeMillis()));
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dlvr_birth = year + "-";
                if(month + 1 > 9) {
                    dlvr_birth += (month + 1) + "-";
                } else {
                    dlvr_birth += "0" + (month + 1) + "-";
                }
                if(dayOfMonth > 9) {
                    dlvr_birth += dayOfMonth;
                } else {
                    dlvr_birth += "0" + dayOfMonth;
                }
                edtRegDlvrBirth.setText(dlvr_birth);
                checkValues[7] = true;
            }
        }, y, m, d);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        switch(id) {
            case R.id.btnRegPrev:
                index--;
                setRegistForm();
                break;
            case R.id.btnRegNext:
                if(btnRegNext.getText().toString().equals("작성 완료")) {
                    if(checkValuesOk()) {
                        if(registDeliver().equals("regist_success")) {
                            Toast.makeText(this, "배달원 가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "배달원 가입 실패", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(this, "누락된 부분이 있습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(checkValuesOk()) {
                        index++;
                        setRegistForm();
                    } else {
                        Toast.makeText(this, "누락된 부분이 있습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnCheckIdDupl:
                checkIdDupl();
                break;
            case R.id.btnSelectBirth:
                showDatePickerDialog();
                break;
            case R.id.btnSeacrhAddr:
                intent = new Intent(getApplicationContext(), Activity_WebView.class);
                startActivityForResult(intent, REGIST_DLVR_ADDR);
                break;
            case R.id.btnRegDlvrImg:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REGIST_DLVR_IMG);
                break;
            case R.id.btnRegDlvrIdCard:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REGIST_DLVR_IDCARD);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REGIST_DLVR_BIRTH:
                    String str_dlvr_birth = data.getStringExtra("str_dlvr_birth");
                    edtRegDlvrBirth.setText(str_dlvr_birth);
                    checkValues[7] = true;
                    break;
                case REGIST_DLVR_ADDR:
                    String addr = data.getExtras().getString("data");
                    if(addr != null) {
                        edtRegDlvrAddr.setText(addr);
                        checkValues[8] = true;
                    } else {
                        checkValues[8] = false;
                    }
                    break;
                case REGIST_DLVR_IMG:
                    img = FileUploadUtil.getFile(this, data.getData());
                    checkValues[9] = showImage(data.getData(), img, ivRegDlvrImg);
                    break;
                case REGIST_DLVR_IDCARD:
                    idCard = FileUploadUtil.getFile(this, data.getData());
                    checkValues[10] = showImage(data.getData(), idCard, ivRegDlvrIdCard);
                    break;
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    // 값 유효성 검사
    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString();
        switch (focusedId) {
            case R.id.edtRegDlvrId:
                if(string.length() < 6) {
                    txtChkDlvrIdForm.setText("아이디는 6글자 이상, 영어 소문자, 숫자만 가능합니다");
                    txtChkDlvrIdForm.setTextColor(Color.RED);
                    checkValues[0] = false;
                } else {
                    for(int i = 0; i < string.length(); i++) {
                        char code = string.charAt(i);
                        if((code >= 48 && code <= 57) || (code >= 97 && code <= 122)) {
                            txtChkDlvrIdForm.setText("옳은 아이디");
                            txtChkDlvrIdForm.setTextColor(Color.BLUE);
                            checkValues[0] = true;
                        } else {
                            txtChkDlvrIdForm.setText("옳지 않은 아이디");
                            txtChkDlvrIdForm.setTextColor(Color.RED);
                            checkValues[0] = false;
                            break;
                        }
                    }
                }
                break;
            case R.id.edtRegDlvrPw:
                if(string.length() < 8) {
                    txtChkDlvrPwForm.setText("비밀번호는 8자리 이상, 영어 소문자, 숫자만 가능합니다");
                    txtChkDlvrPwForm.setTextColor(Color.RED);
                    checkValues[2] = false;
                } else {
                    for(int i = 0; i < string.length(); i++) {
                        char code = string.charAt(i);
                        if((code >= 48 && code <= 57) || (code >= 65 && code <= 90) || (code >= 97 && code <= 122)) {
                            txtChkDlvrPwForm.setText("옳은 비밀번호");
                            txtChkDlvrPwForm.setTextColor(Color.BLUE);
                            checkValues[2] = true;
                        } else {
                            txtChkDlvrPwForm.setText("옳지 않은 비밀번호");
                            txtChkDlvrPwForm.setTextColor(Color.RED);
                            checkValues[2] = false;
                            break;
                        }
                    }
                }
                break;
            case R.id.edtRegDlvrPwDupl:
                if(txtChkDlvrPwForm.getText().toString().equals("옳은 비밀번호")) {
                    String password = edtRegDlvrPw.getText().toString();
                    if(string.equals(password)) {
                        txtChkDlvrPwDupl.setText("비밀번호 일치");
                        txtChkDlvrPwDupl.setTextColor(Color.BLUE);
                        checkValues[3] = true;
                    } else {
                        txtChkDlvrPwDupl.setText("비밀번호 불일치");
                        txtChkDlvrPwDupl.setTextColor(Color.RED);
                        checkValues[3] = false;
                    }
                } else {
                    txtChkDlvrPwDupl.setText("비밀번호를 맞게 입력해주세요");
                    txtChkDlvrPwDupl.setTextColor(Color.RED);
                    checkValues[3] = false;
                }
                break;
            case R.id.edtRegDlvrName:
                if(string.length() < 2) {
                    txtChkDlvrNameForm.setText("제대로 입력해주세요");
                    txtChkDlvrNameForm.setTextColor(Color.RED);
                    checkValues[4] = false;
                } else {
                    for(int i = 0; i < string.length(); i++) {
                        char code = string.charAt(i);
                        if(code >= 44032 && code <= 55203) {
                            txtChkDlvrNameForm.setText("");
                            txtChkDlvrNameForm.setTextColor(Color.BLUE);
                            checkValues[4] = true;
                        } else {
                            txtChkDlvrNameForm.setText("한글만 입력가능합니다");
                            txtChkDlvrNameForm.setTextColor(Color.RED);
                            checkValues[4] = false;
                        }
                    }
                }
                break;
            case R.id.edtRegDlvrEmail:
                if(string.contains("@") && string.contains(".") &&
                        (string.indexOf("@") != 0) &&
                        (string.indexOf("@")+1 < string.indexOf(".")) &&
                        (string.indexOf(".")+1 < string.length())) {
                    txtChkEmailForm.setText("");
                    txtChkEmailForm.setTextColor(Color.BLUE);
                    checkValues[5] = true;
                } else {
                    txtChkEmailForm.setText("옳지 않은 이메일 형식입니다");
                    txtChkEmailForm.setTextColor(Color.RED);
                    checkValues[5] = false;
                }
                break;
            case R.id.edtRegDlvrPhone:
                if(string.length() >= 9) {
                    txtChkDlvrPhoneForm.setText("");
                    txtChkDlvrPhoneForm.setTextColor(Color.BLUE);
                    checkValues[6] = true;
                } else {
                    txtChkDlvrPhoneForm.setText("전화번호 형식에 맞지 않습니다");
                    txtChkDlvrPhoneForm.setTextColor(Color.RED);
                    checkValues[6] = false;
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.focusedId = v.getId();
    }

}
