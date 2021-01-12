package com.kh.delivery_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kh.delivery_project.R;
import com.kh.delivery_project.domain.DeliverVo;
import com.kh.delivery_project.domain.MessageVo;
import com.kh.delivery_project.util.Codes;
import com.kh.delivery_project.util.PreferenceManager;
import com.kh.delivery_project.util.UrlImageUtil;

import java.sql.Timestamp;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Message extends BaseAdapter implements Codes {

    private List<MessageVo> messageList;
    private DeliverVo deliverVo;
    private LayoutInflater layoutInflater;

    public Adapter_Message(List<MessageVo> messageList, DeliverVo deliverVo, LayoutInflater layoutInflater) {
        this.messageList = messageList;
        this.deliverVo = deliverVo;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return messageList.size();
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
        MessageVo messageVo = messageList.get(position);

        View messageView = null;

        if (messageVo.getSender_no() == deliverVo.getDlvr_no()) {
            if(messageVo.getMsg_img() == null) {
                messageView = layoutInflater.inflate(R.layout.message_deliver, parent, false);
            } else {
                messageView = layoutInflater.inflate(R.layout.message_deliver_img, parent, false);
            }
        } else {
            if(messageVo.getMsg_img() == null) {
                messageView = layoutInflater.inflate(R.layout.message_user, parent, false);
            } else {
                messageView = layoutInflater.inflate(R.layout.message_user_img, parent, false);
            }
        }

        CircleImageView civSenderImg = messageView.findViewById(R.id.civSenderImg);
        String urlStr = IMAGE_ADDRESS + messageVo.getSender_img();
        UrlImageUtil urlImageUtil = new UrlImageUtil(urlStr, civSenderImg);
        urlImageUtil.execute();

        TextView txtSenderName = messageView.findViewById(R.id.txtSenderName);
        txtSenderName.setText(messageVo.getSender_name());

        TextView txtSenderMsg;
        ImageView ivMsgImg;
        if(messageVo.getMsg_img() == null) {
            txtSenderMsg = messageView.findViewById(R.id.txtSenderMsg);
            txtSenderMsg.setText(messageVo.getMsg_content());
        } else {
            ivMsgImg = messageView.findViewById(R.id.ivMsgImg);
            urlStr = IMAGE_ADDRESS + messageVo.getMsg_img();
            urlImageUtil = new UrlImageUtil(urlStr, ivMsgImg);
            urlImageUtil.execute();
        }

        TextView txtMsgDate = messageView.findViewById(R.id.txtMsgDate);
        Timestamp msg_date = messageVo.getMsg_date();
        String str_msg_date = "";
        int hours = msg_date.getHours();
        if(hours > 9) {
            str_msg_date += hours + ":";
        } else {
            str_msg_date += "0" + hours + ":";
        }
        int minutes = msg_date.getMinutes();
        if(minutes > 9) {
            str_msg_date += minutes;
        } else {
            str_msg_date += "0" + minutes;
        }
        txtMsgDate.setText(str_msg_date);

        return messageView;
    }
}
