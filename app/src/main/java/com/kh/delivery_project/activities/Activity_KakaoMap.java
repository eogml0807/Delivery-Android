package com.kh.delivery_project.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.kh.delivery_project.R;
import com.kh.delivery_project.connection.ConnectServer;
import com.kh.delivery_project.adapters.Adapter_OrderList;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.OrderVo;
import com.kh.delivery_project.domain.UserVo;
import com.kh.delivery_project.util.AddressUtil;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.ConvertUtil;
import com.kh.delivery_project.util.PreferenceManager;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_KakaoMap extends AppCompatActivity
        implements View.OnClickListener, MapView.POIItemEventListener, MapView.MapViewEventListener,
                    MapView.CurrentLocationEventListener, Codes {

    Gson gson = new Gson();

    MapView mapView;
    FrameLayout frm_order;
    LinearLayout frm_order_list, frm_map, frm_order_info;
    Button btnKakaoMapToHome, btnShowOrderList, btnShowMap, btn_dlvr_success, btn_dlvr_cancel;
    ListView lvOrderList;
    TextView txtOrderList, txtCurrentLocation, txt_d_OrderNo, txt_d_UserName, txt_d_OrderAddr, txt_d_OrderReq;
    ViewGroup mapViewContainer;

    DeliverVo deliverVo;
    OrderVo pickedOrderVo;
    List<OrderVo> orderList = new ArrayList<>();

    private int range = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaomap);
        deliverVo = PreferenceManager.getDeliverVo(this);
        setViews();
        setListeners();
        getPickedOrderVo();
        setMaps();
        checkRunTimePermission();
    }

    private void getPickedOrderVo() {
        String url = "/order/android/getPickedOrder";
        ContentValues params = new ContentValues();
        params.put("dlvr_no", deliverVo.getDlvr_no());

        Map<String, Object> map = gson.fromJson(ConnectServer.getData(url, params), Map.class);
        if(map != null) {
            this.pickedOrderVo = ConvertUtil.getOrderVo(map);
        }
    }

    private void setMaps() {
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.56682, 126.97865), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(3, true);
        /*
        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.41, 126.52), 9, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);
        */
    }

    private void setViews() {
        mapView = new MapView(this);
        btnKakaoMapToHome = findViewById(R.id.btnKakaoMapToHome);
        btnShowOrderList = findViewById(R.id.btnShowOrderList);
        btnShowMap = findViewById(R.id.btnShowMap);
        btn_dlvr_success = findViewById(R.id.btn_dlvr_success);
        btn_dlvr_cancel = findViewById(R.id.btn_dlvr_cancel);
        txtOrderList = findViewById(R.id.txtOrderList);
        txtCurrentLocation = findViewById(R.id.txtCurrentLocation);
        txt_d_OrderNo = findViewById(R.id.txt_d_OrderNo);
        txt_d_UserName = findViewById(R.id.txt_d_UserName);
        txt_d_OrderAddr = findViewById(R.id.txt_d_OrderAddr);
        txt_d_OrderReq = findViewById(R.id.txt_d_OrderReq);
        lvOrderList = findViewById(R.id.lvOrderList);
        frm_order = findViewById(R.id.frm_order);
        frm_map = findViewById(R.id.frm_map);
        frm_order_list = findViewById(R.id.frm_order_list);
        frm_order_info = findViewById(R.id.frm_order_info);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

    private void setListeners() {
        btnKakaoMapToHome.setOnClickListener(this);
        btnShowOrderList.setOnClickListener(this);
        btnShowMap.setOnClickListener(this);
        btn_dlvr_success.setOnClickListener(this);
        btn_dlvr_cancel.setOnClickListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderVo orderVo = orderList.get(position);
                openDialog(orderVo);
            }
        });
    }

    private void openDialog(OrderVo orderVo) {
        Intent intent = new Intent(getApplicationContext(), Dialog_PickedOrder.class);
        intent.putExtra("orderVo", orderVo);
        intent.putExtra("deliverVo", deliverVo);
        startActivityForResult(intent, PICK_ORDER);
    }

    private void setDeliverMarker(MapPoint mapPoint) {
        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(mapPoint);
        marker.setItemName("배달원");
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);
    }

    private void setOrderMarker() {
        if(pickedOrderVo == null) {
            for(int i = 0; i < orderList.size(); i++) {
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
        for(int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            double order_lat = (double) map.get("order_lat");
            double order_lng = (double) map.get("order_lng");

            Location order_loc = new Location("o_loc");
            order_loc.setLatitude(order_lat);
            order_loc.setLongitude(order_lng);

            int distance = Math.round(order_loc.distanceTo(deliver_loc));
            if(distance <= (range * 1000)) {
                OrderVo orderVo = ConvertUtil.getOrderVo(map);
                String order_addr = AddressUtil.getAddress(this, order_lat, order_lng);
                orderVo.setOrder_addr(order_addr.substring(order_addr.indexOf("국")+2));
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

    private void checkRunTimePermission() {
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermission == PackageManager.PERMISSION_GRANTED) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }
    }

    private void showOrderInfo() {
        frm_order_list.setVisibility(View.INVISIBLE);
        frm_order_info.setVisibility(View.VISIBLE);
        btnShowOrderList.setText("주문 정보 보기");

        txt_d_OrderNo.setText("" + pickedOrderVo.getOrder_no());
        txt_d_UserName.setText(pickedOrderVo.getUser_name());
        double order_lat = pickedOrderVo.getOrder_lat();
        double order_lng = pickedOrderVo.getOrder_lng();
        String order_addr = AddressUtil.getAddress(this, order_lat, order_lng);
        pickedOrderVo.setOrder_addr(order_addr);
        txt_d_OrderAddr.setText(pickedOrderVo.getOrder_addr());
        txt_d_OrderReq.setText(pickedOrderVo.getOrder_req());
    }

    private boolean cancelOrder() {
        String url = "/order/android/cancelDelivery";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        params.put("dlvr_no", pickedOrderVo.getDlvr_no());
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        if(result.equals("cancelDelivery_success")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean deliveryComplete() {
        String url = "/order/android/deliveryCompleted";
        ContentValues params = new ContentValues();
        params.put("order_no", pickedOrderVo.getOrder_no());
        params.put("dlvr_no", pickedOrderVo.getDlvr_no());
        String result = gson.fromJson(ConnectServer.getData(url, params), String.class);
        if(result.equals("delivery_completed")) {
            return true;
        } else {
            return false;
        }
    }

    private void showOrderList() {
        pickedOrderVo = null;
        frm_order_list.setVisibility(View.VISIBLE);
        frm_order_info.setVisibility(View.INVISIBLE);
        btnShowOrderList.setText("주문 리스트 보기");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == PICK_ORDER) {
                if(data != null) {
                    pickedOrderVo = data.getParcelableExtra("orderVo");
                    Log.d("orderVo", pickedOrderVo.toString());
                    showOrderInfo();
                }
            }
        } else if(requestCode == RESULT_CANCELED) {
            Toast.makeText(this, "다른 사람이 주문 픽업", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnKakaoMapToHome:
                finish();
                break;
            case R.id.btnShowOrderList:
                frm_map.setVisibility(View.INVISIBLE);
                frm_order.setVisibility(View.VISIBLE);
                break;
            case R.id.btnShowMap:
                frm_order.setVisibility(View.INVISIBLE);
                frm_map.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_dlvr_success:
                if(deliveryComplete()) {
                    Toast.makeText(this, "배달 완료!!!!!!", Toast.LENGTH_SHORT).show();
                    showOrderList();
                } else {
                    Toast.makeText(this, "배달 실패......", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_dlvr_cancel:
                if(cancelOrder()) {
                    showOrderList();
                } else {
                    Toast.makeText(this, "What's wrong?", Toast.LENGTH_SHORT).show();
                }
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
//        mapView.removeAllPOIItems();
//        setOrderList(mapPoint);
//        setDeliverMarker(mapPoint);
//        setOrderMarker();
//        setLvOrderList();
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
        int order_no = Integer.parseInt(str_order_no.substring(str_order_no.lastIndexOf(":")+1));
        for(int i = 0; i < orderList.size(); i++) {
            OrderVo orderVo = orderList.get(i);
            if(order_no == orderVo.getOrder_no()) {
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
        MapPoint.GeoCoordinate currentLocation = mapPoint.getMapPointGeoCoord();
        double cur_lat = currentLocation.latitude;
        double cur_lng = currentLocation.longitude;
        txtCurrentLocation.setText("현재 위치 lat=" + cur_lat + ", lng=" + cur_lng);
        mapView.removeAllPOIItems();
        setOrderList(mapPoint);
        setLvOrderList();
//        setDeliverMarker(mapPoint);
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

}
