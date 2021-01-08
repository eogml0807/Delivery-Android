package com.kh.delivery_project.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.OrderVo;

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

        TextView txtMyOrderNo, txtMyOrderCa, txtMyOrderDate, txtMyOrderCost;
        txtMyOrderNo = convertView.findViewById(R.id.txtMyOrderNo);
        txtMyOrderCa = convertView.findViewById(R.id.txtMyOrderCa);
        txtMyOrderDate = convertView.findViewById(R.id.txtMyOrderDate);
        txtMyOrderCost = convertView.findViewById(R.id.txtMyOrderCost);

        OrderVo orderVo = orderList.get(position);

        txtMyOrderNo.setText(String.valueOf(orderVo.getOrder_no()));
        txtMyOrderCa.setText(orderVo.getOrder_ca());
        txtMyOrderDate.setText(orderVo.getOrder_date().toString());

        return convertView;
    }
}
