package com.kh.delivery_project.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.CommentVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.UrlImageUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class Adapter_CommentList extends BaseAdapter implements Codes {

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

        ImageView civWriterImg;
        TextView txtWriterName, txtCommentDate, txtCommentContent;

        civWriterImg = convertView.findViewById(R.id.civWriterImg);
        txtWriterName = convertView.findViewById(R.id.txtWriterName);
        txtCommentDate = convertView.findViewById(R.id.txtCommentDate);
        txtCommentContent = convertView.findViewById(R.id.txtCommentContent);

        CommentVo commentVo = commentList.get(position);

        String imgUrl = IMAGE_ADDRESS + commentVo.getWriter_img();
        String writer_name = commentVo.getWriter_name();
        String c_date = new SimpleDateFormat("MM/dd HH:mm").format(commentVo.getC_date());
        String c_content = commentVo.getC_content();

        UrlImageUtil urlImageUtil = new UrlImageUtil(imgUrl, civWriterImg);
        urlImageUtil.execute();
        txtWriterName.setText(writer_name);
        txtCommentDate.setText(c_date);
        txtCommentContent.setText(c_content);

        return convertView;
    }
}
