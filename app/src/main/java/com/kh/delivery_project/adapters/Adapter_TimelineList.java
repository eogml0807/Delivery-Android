package com.kh.delivery_project.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.TimelineVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.UrlImageUtil;

import java.util.List;

public class Adapter_TimelineList extends BaseAdapter implements Codes {

    private Context context;
    private int layout;
    private List<TimelineVo> list;

    public Adapter_TimelineList(Context context, int layout, List<TimelineVo> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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

        TextView boardWriterName = convertView.findViewById(R.id.boardWriterName);
        TextView boardContent = convertView.findViewById(R.id.boardContent);
        TextView boardStars = convertView.findViewById(R.id.boardStars);
        RatingBar boardRatingbar = convertView.findViewById(R.id.boardRatingbar);
        LinearLayout boardLinStar = convertView.findViewById(R.id.boardLinStar);

        TimelineVo timelineVo = list.get(position);

        boardWriterName.setText(timelineVo.getWriter_name());
        boardContent.setText(timelineVo.getTime_content());
        if(timelineVo.getTime_state().equals("2-002")) {
            Log.d("position", "" + position);
            boardLinStar.setVisibility(View.VISIBLE);
            boardStars.setText(String.valueOf(timelineVo.getTime_star()));
            boardRatingbar.setRating((int) timelineVo.getTime_star());
        }

        return convertView;
    }
}
