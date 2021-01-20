package com.kh.delivery_project.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.adapters.Adapter_Message;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.adapters.Adapter_OrderList;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.MessageVo;
import com.kh.delivery_project.domain.OrderVo;
import com.kh.delivery_project.util.AddressUtil;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.DeliveryUtil;
import com.kh.delivery_project.util.FileUploadUtil;
import com.kh.delivery_project.util.PreferenceManager;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_Delivery extends AppCompatActivity
        implements View.OnClickListener, MapView.POIItemEventListener, MapView.MapViewEventListener,
        MapView.CurrentLocationEventListener, Codes, View.OnKeyListener {

    Gson gson = new Gson();

    MapView mapView;
    FrameLayout frmOrder;
    LinearLayout linOrderList, linMap, linOrderInfo, linMessage;
    Button btnShowOrderList, btnShowMap, btnDeliveryComplete, btnDeliveryCancel, btnShowMessage, btnMessageImg, btnSendMessage;
    ListView lvOrderList;
    TextView txtOrderList, txtPickedOrderNo, txtPickedUserName, txtPickedOrderLoc, txtPickedOrderReq,
            txtDialogOrderNo, txtDialogUserName, txtDialogOrderLoc, txtDialogOrderReq;
    ViewGroup mapViewContainer;
    EditText edtMessage;
    ListView lvMessage;

    DeliverVo deliverVo;
    OrderVo pickedOrderVo;
    List<OrderVo> orderList = new ArrayList<>();
    List<MessageVo> messageList;
    Adapter_Message adapter;
    MessageThread thread = new MessageThread();

    File msg_img;
    String str_msg_img;
    int lastMsgNo;
    int range = 1;
    int start = 0;
    boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        mapView = new MapView(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        deliverVo = PreferenceManager.getDeliverVo(this);
        setViews();
        setListeners();
        getPickedOrderVo();
        setMaps();
        checkRunTimePermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop = true;
    }

    private void setViews() {
        btnShowOrderList = findViewById(R.id.btnShowOrderList);
        btnShowMap = findViewById(R.id.btnShowMap);
        btnDeliveryComplete = findViewById(R.id.btnDeliveryComplete);
        btnDeliveryCancel = findViewById(R.id.btnDeliveryCancel);
        btnShowMessage = findViewById(R.id.btnShowMessage);
        btnMessageImg = findViewById(R.id.btnMessageImg);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        txtOrderList = findViewById(R.id.txtOrderList);
        txtPickedOrderNo = findViewById(R.id.txtPickedOrderNo);
        txtPickedUserName = findViewById(R.id.txtPickedUserName);
        txtPickedOrderLoc = findViewById(R.id.txtPickedOrderLoc);
        txtPickedOrderReq = findViewById(R.id.txtPickedOrderReq);
        lvOrderList = findViewById(R.id.lvOrderList);
        frmOrder = findViewById(R.id.frmOrder);
        linMap = findViewById(R.id.linMap);
        linOrderList = findViewById(R.id.linOrderList);
        linOrderInfo = findViewById(R.id.linOrderInfo);
        linMessage = findViewById(R.id.linMessage);
        edtMessage = findViewById(R.id.edtMessage);
        lvMessage = findViewById(R.id.lvMessage);
        mapViewContainer = (ViewGroup) findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
    }

    private void setListeners() {
        btnShowOrderList.setOnClickListener(this);
        btnShowMap.setOnClickListener(this);
        btnDeliveryComplete.setOnClickListener(this);
        btnDeliveryCancel.setOnClickListener(this);
        btnMessageImg.setOnClickListener(this);
        btnShowMessage.setOnClickListener(this);
        btnMessageImg.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        edtMessage.setOnKeyListener(this);
        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderVo orderVo = orderList.get(position);
                openDialog(orderVo);
            }
        });
    }

    private void getPickedOrderVo() {
        String url = "/order/android/getPickedOrder";
        ContentValues params = new ContentValues();
        params.put("dlvr_no", deliverVo.getDlvr_no());

        Map<String, Object> map = gson.fromJson(ConnectServer.getData(url, params), Map.class);
        if (map != null) {
            pickedOrderVo = ConvertUtil.getOrderVo(map);
            showOrderInfo();
            setLvMessage();
        } else {
            showOrderList();
        }
    }

    private void setMaps() {
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.56682, 126.97865), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(3, true);
    }

    private void checkRunTimePermission() {
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }
    }

    private void setLvMessage() {
        String url = "/message/getMessageList";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        List<Map<String, Object>> list = gson.fromJson(ConnectServer.getData(url, params), List.class);
        messageList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            MessageVo messageVo = ConvertUtil.getMessageVo(map);
            messageList.add(messageVo);

            if (i == list.size() - 1) {
                lastMsgNo = messageVo.getMsg_no();
            }
        }

        adapter = new Adapter_Message(messageList, deliverVo, getLayoutInflater());
        lvMessage.setAdapter(adapter);
        lvMessage.setSelection(messageList.size() - 1);
        if (start++ == 0) {
            thread.start();
        }
    }

    public void getCurrentMessage() {
        String url = "/message/getCurrentMessage";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        params.put("msg_no", lastMsgNo);
        List<Map<String, Object>> list = gson.fromJson(ConnectServer.getData(url, params), List.class);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                MessageVo messageVo = ConvertUtil.getMessageVo(map);
                messageList.add(messageVo);

                if (i == list.size() - 1) {
                    lastMsgNo = messageVo.getMsg_no();
                }
            }
            adapter.notifyDataSetChanged();
            lvMessage.setSelection(messageList.size() - 1);
        }
    }

    private void openDialog(OrderVo orderVo) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_pickedorder, null, false);
        txtDialogOrderNo = dialogView.findViewById(R.id.txtDialogOrderNo);
        txtDialogUserName = dialogView.findViewById(R.id.txtDialogUserName);
        txtDialogOrderLoc = dialogView.findViewById(R.id.txtDialogOrderLoc);
        txtDialogOrderReq = dialogView.findViewById(R.id.txtDialogOrderReq);

        txtDialogOrderNo.setText(String.valueOf(orderVo.getOrder_no()));
        txtDialogUserName.setText(orderVo.getUser_name());
        txtDialogOrderLoc.setText(orderVo.getOrder_addr());
        txtDialogOrderReq.setText(orderVo.getOrder_req());

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(Activity_Delivery.this);
        materialAlertDialogBuilder.setView(dialogView)
                .setTitle("주문 정보")
                .setPositiveButton("배달하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int order_no = Integer.parseInt(txtDialogOrderNo.getText().toString());
                        int dlvr_no = deliverVo.getDlvr_no();
                        String result = DeliveryUtil.pickOrder(order_no, dlvr_no);
                        if (result.equals("pickOrder_success")) {
                            getPickedOrderVo();
                            Toast.makeText(Activity_Delivery.this, "주문 접수 완료", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_Delivery.this, "주문이 취소되거나 다른 배달원이 접수했습니다", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("닫기", null)
                .show();
    }

    private void setOrderMarker() {
        if (pickedOrderVo == null) {
            for (int i = 0; i < orderList.size(); i++) {
                OrderVo orderVo = orderList.get(i);
                double order_lat = orderVo.getOrder_lat();
                double order_lng = orderVo.getOrder_lng();
                MapPOIItem marker = new MapPOIItem();
                MapPoint orderPoint = MapPoint.mapPointWithGeoCoord(order_lat, order_lng);
                marker.setMapPoint(orderPoint);
                marker.setItemName("주문번호:" + orderVo.getOrder_no());
                marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
                mapView.addPOIItem(marker);
            }
        } else {
            double order_lat = pickedOrderVo.getOrder_lat();
            double order_lng = pickedOrderVo.getOrder_lng();
            MapPOIItem marker = new MapPOIItem();
            MapPoint orderPoint = MapPoint.mapPointWithGeoCoord(order_lat, order_lng);
            marker.setMapPoint(orderPoint);
            marker.setItemName("주문번호:" + pickedOrderVo.getOrder_no());
            marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
            mapView.addPOIItem(marker);
        }
    }

    private void setOrderList(MapPoint mapPoint) {
        List<Map<String, Object>> data = null;
        double dlvr_lat = mapPoint.getMapPointGeoCoord().latitude;
        double dlvr_lng = mapPoint.getMapPointGeoCoord().longitude;

        Location deliver_loc = new Location("deliver_loc");
        deliver_loc.setLatitude(dlvr_lat);
        deliver_loc.setLongitude(dlvr_lng);

        try {
            String url = "/order/android/getOrderList";
            ContentValues params = new ContentValues();
            params.put("order_lat", dlvr_lat);
            params.put("order_lng", dlvr_lng);
            params.put("range", range);
            data = gson.fromJson(ConnectServer.getData(url, params), List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        orderList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            double order_lat = (double) map.get("order_lat");
            double order_lng = (double) map.get("order_lng");

            Location order_loc = new Location("o_loc");
            order_loc.setLatitude(order_lat);
            order_loc.setLongitude(order_lng);

            int distance = Math.round(order_loc.distanceTo(deliver_loc));
            if (distance <= (range * 1000)) {
                OrderVo orderVo = ConvertUtil.getOrderVo(map);
                String order_addr = AddressUtil.getAddress(this, order_lat, order_lng);
                orderVo.setOrder_addr(order_addr.substring(order_addr.indexOf("국") + 2));
                orderVo.setDistance(distance);
                orderList.add(orderVo);
            }
        }
    }

    private void setLvOrderList() {
        lvOrderList.setAdapter(null);
        Adapter_OrderList adapter = new Adapter_OrderList(this, R.layout.view_orderlist, this.orderList);
        lvOrderList.setAdapter(adapter);
    }

    private void showOrderInfo() {
        linOrderList.setVisibility(View.GONE);
        linOrderInfo.setVisibility(View.VISIBLE);
        btnShowMessage.setVisibility(View.VISIBLE);
        btnShowOrderList.setText("주문 정보 보기");

        stop = false;
        txtPickedOrderNo.setText(String.valueOf(pickedOrderVo.getOrder_no()));
        txtPickedUserName.setText(pickedOrderVo.getUser_name());
        double order_lat = pickedOrderVo.getOrder_lat();
        double order_lng = pickedOrderVo.getOrder_lng();
        String order_addr = AddressUtil.getAddress(this, order_lat, order_lng);
        pickedOrderVo.setOrder_addr(order_addr);
        txtPickedOrderLoc.setText(pickedOrderVo.getOrder_addr());
        txtPickedOrderReq.setText(pickedOrderVo.getOrder_req());
    }

    private void showOrderList() {
        linOrderList.setVisibility(View.VISIBLE);
        linOrderInfo.setVisibility(View.GONE);
        btnShowMessage.setVisibility(View.GONE);
        btnShowOrderList.setText("주문 리스트 보기");

        stop = true;
        pickedOrderVo = null;
        messageList = null;
        lvMessage.setAdapter(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MESSAGE_IMAGE:
                    msg_img = FileUploadUtil.getFile(this, data.getData());
                    str_msg_img = MESSAGE_IMG + deliverVo.getDlvr_id() + msg_img.getName();
                    if (FileUploadUtil.isImage(msg_img)) {
                        String result = DeliveryUtil.sendMsgImg(str_msg_img, pickedOrderVo);
                        if (result.equals("sendMsgImg_success")) {
                            FileUploadUtil.upload(this, msg_img, str_msg_img);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String result;
        switch (id) {
            case R.id.btnShowOrderList:
                linMap.setVisibility(View.GONE);
                frmOrder.setVisibility(View.VISIBLE);
                linMessage.setVisibility(View.GONE);
                break;
            case R.id.btnShowMap:
                linMap.setVisibility(View.VISIBLE);
                frmOrder.setVisibility(View.GONE);
                linMessage.setVisibility(View.GONE);
                break;
            case R.id.btnShowMessage:
                linMap.setVisibility(View.GONE);
                frmOrder.setVisibility(View.GONE);
                linMessage.setVisibility(View.VISIBLE);
                break;
            case R.id.btnDeliveryComplete:
                result = DeliveryUtil.deliveryComplete(pickedOrderVo);
                if (result.equals("delivery_completed")) {
                    Toast.makeText(this, "배달 완료!", Toast.LENGTH_SHORT).show();
                    showOrderList();
                } else {
                    Toast.makeText(this, "What's wrong?", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDeliveryCancel:
                result = DeliveryUtil.deliveryCancel(pickedOrderVo);
                if (result.equals("cancelDelivery_success")) {
                    showOrderList();
                } else {
                    Toast.makeText(this, "What's wrong?", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSendMessage:
                String msg_content = edtMessage.getText().toString();
                if (!msg_content.equals("") && edtMessage != null) {
                    result = DeliveryUtil.sendMsgContent(msg_content, pickedOrderVo);
                    if (result.equals("sendMsgContent_success")) {
                        edtMessage.setText("");
                    }
                } else {
                    Toast.makeText(this, "메시지를 작성해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnMessageImg:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, MESSAGE_IMAGE);
                break;
        }
    }

    // MapView 이벤트
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    // POIItem 이벤트
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        String str_order_no = mapPOIItem.getItemName();
        int order_no = Integer.parseInt(str_order_no.substring(str_order_no.lastIndexOf(":") + 1));
        for (int i = 0; i < orderList.size(); i++) {
            OrderVo orderVo = orderList.get(i);
            if (order_no == orderVo.getOrder_no()) {
                openDialog(orderVo);
                break;
            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    // Location 이벤트
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        // 이 위치로 검색을 해야함
        mapView.removeAllPOIItems();
        setOrderList(mapPoint);
        setLvOrderList();
        setOrderMarker();
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
        Log.d("current", "heading");
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.d("current", "failed");
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.d("current", "canceled");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (v == edtMessage) {
                String msg_content = edtMessage.getText().toString();
                if (!msg_content.equals("") && edtMessage != null) {
                    String result = DeliveryUtil.sendMsgContent(msg_content, pickedOrderVo);
                    if (result.equals("sendMsgContent_success")) {
                        edtMessage.setText("");
                    }
                } else {
                    Toast.makeText(this, "글 입력을 하세요", Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
            return true;
        }
        return false;
    }

    class MessageThread extends Thread {
        @Override
        public void run() {
            while (!stop) {
                try {
                    Log.d("스레드 실행중", "...");
                    getCurrentMessage();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
