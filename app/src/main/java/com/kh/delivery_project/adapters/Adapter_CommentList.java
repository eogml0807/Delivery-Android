package com.kh.delivery_project.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kh.delivery_project.domain.CommentVo;

import java.util.List;

public class Adapter_CommentList extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CommentVo> commentList;

    public Adapter_CommentList(Context context, int layout, List<CommentVo> commentList) {
        this.context = context;
        this.layout = layout;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
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



        return convertView;
    }
}
