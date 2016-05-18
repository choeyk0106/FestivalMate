package com.festival.tacademy.festivalmate.FestivalInfo;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.festival.tacademy.festivalmate.Data.User;
import com.festival.tacademy.festivalmate.R;

import org.w3c.dom.Text;

/**
 * Created by J.K.Lee on 2016-05-16.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    ImageView photoView;
    TextView idView;

    public UserViewHolder(View itemView) {
        super(itemView);
        photoView = (ImageView)itemView.findViewById(R.id.image_photo);
        idView = (TextView)itemView.findViewById(R.id.text_id);
    }

    public void setUser(User user) {
        photoView.setImageResource(user.getPhoto());
        idView.setText(user.getId());
    }
}
