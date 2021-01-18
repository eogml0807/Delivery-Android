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

    Context context;
    int layout;
    List<OrderVo> orderList;

    public Adapter_OrderList(Context context, int layout, List<OrderVo> orderList) {
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

        TextView txtOrderNo = convertView.findViewById(R.id.txtOrderNo);
        TextView txtOrderLoc = convertView.findViewById(R.id.txtOrderLoc);
        TextView txtOrderDistance = convertView.findViewById(R.id.txtOrderDistance);

        OrderVo orderVo = orderList.get(position);

        int order_no = orderVo.getOrder_no();
        String order_addr = orderVo.getOrder_addr();
        int distance = orderVo.getDistance();

        txtOrderNo.setText(String.valueOf(order_no));
        txtOrderLoc.setText(order_addr);
        txtOrderDistance.setText(distance + "m");

        return convertView;
    }
}
