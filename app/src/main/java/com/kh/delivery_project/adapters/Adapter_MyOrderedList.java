package com.kh.delivery_project.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.OrderVo;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Adapter_MyOrderedList extends BaseAdapter {

    Context context;
    int layout;
    List<OrderVo> orderList;

    public Adapter_MyOrderedList(Context context, int layout, List<OrderVo> orderList) {
        this.context = context;
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
            convertView = View.inflate(context, layout, null);
        }

        TextView txtMyOrderNo, txtMyOrderReq, txtMyOrderDate, txtMyOrderState;
        txtMyOrderNo = convertView.findViewById(R.id.txtMyOrderNo);
        txtMyOrderReq = convertView.findViewById(R.id.txtMyOrderReq);
        txtMyOrderDate = convertView.findViewById(R.id.txtMyOrderDate);
        txtMyOrderState = convertView.findViewById(R.id.txtMyOrderState);

        OrderVo orderVo = orderList.get(position);

        txtMyOrderNo.setText(String.valueOf(orderVo.getOrder_no()));
        txtMyOrderReq.setText(orderVo.getOrder_req());
        Timestamp order_date = orderVo.getOrder_date();
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
        String str_order_date = df.format(order_date);
        txtMyOrderDate.setText(str_order_date);
        txtMyOrderState.setText(orderVo.getCode_detail());

        return convertView;
    }
}
