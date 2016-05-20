package com.festival.tacademy.festivalmate.FestivalDetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.festival.tacademy.festivalmate.Data.User;
import com.festival.tacademy.festivalmate.R;

/**
 * Created by J.K.Lee on 2016-05-16.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    ImageView photoView;
    TextView idView;

    public interface OnItemClickListener {
        public void onItemClick(View view);
    }

    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public UserViewHolder(View itemView) {
        super(itemView);
        photoView = (ImageView)itemView.findViewById(R.id.image_photo);
        idView = (TextView)itemView.findViewById(R.id.text_id);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v);
                }
            }
        });
    }

    public void setUser(User user) {
        photoView.setImageResource(user.getPhoto());
        idView.setText(user.getId());
    }
}