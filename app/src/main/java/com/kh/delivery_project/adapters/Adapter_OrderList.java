package com.kh.delivery_project.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.OrderVo;

import java.sql.Timestamp;
import java.util.List;

public class Adapter_OrderList extends BaseAdapter {

    Context contex;
    int layout;
    List<OrderVo> orderList;

    public Adapter_OrderList(Context contex, int layout, List<OrderVo> orderList) {
        this.contex = contex;
        this.layout = layout;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(contex, layout, null);
        }

        TextView txtOrderInfo = convertView.findViewById(R.id.txtOrderInfo);

        OrderVo orderVo = orderList.get(position);

        int order_no = orderVo.getOrder_no();
        String order_addr = orderVo.getOrder_addr();
        int distance = orderVo.getDistance();

        String text = "주문번호 : " + order_no + " | 주소 : " + order_addr + " | 거리 : " + distance + "m";
        txtOrderInfo.setText(text);

        return convertView;
    }
}
