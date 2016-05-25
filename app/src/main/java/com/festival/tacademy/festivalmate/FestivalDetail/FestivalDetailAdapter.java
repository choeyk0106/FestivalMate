package com.festival.tacademy.festivalmate.FestivalDetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.festival.tacademy.festivalmate.Data.Festival;
import com.festival.tacademy.festivalmate.R;

public class FestivalDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_FESTIVAL_PHOTO = 1;
    public static final int VIEW_TYPE_FESTIVAL_LINEUP = 2;
    public static final int VIEW_TYPE_LETSGO = 3;
    public static final int VIEW_TYPE_MAPVIEW = 4;
    public static final int VIEW_TYPE_TITLE = 5;

    Festival festival;

    public void setFestval(Festival festival) {
        this.festival = festival;
        notifyDataSetChanged();
    }


    UserViewHolder.OnItemClickListener mListener;

    public void setOnItemClickListener(UserViewHolder.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        if( position == 0 ) {
            return VIEW_TYPE_FESTIVAL_PHOTO;
        }
        position--;
        if ( festival.getLineups().size() > 0 ) {
            if( position == 0 ) {
                return VIEW_TYPE_TITLE;
            }
            position--;
            if( position < festival.getLineups().size() ) {
                return VIEW_TYPE_FESTIVAL_LINEUP;
            }
            position -= festival.getLineups().size();
        }

        if( position == 0 ) {
            return VIEW_TYPE_TITLE;
        }
        position--;
        if( position == 0 ) {
            return VIEW_TYPE_LETSGO;
        }
        position--;
        if( position == 0 ) {
            return VIEW_TYPE_MAPVIEW;
        }
        position--;
       throw new IllegalArgumentException("Invalid Position");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FESTIVAL_PHOTO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_festival_photo, null);
                return new PhotoViewHolder(view);
            }
            case VIEW_TYPE_TITLE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_title, null);
                return new TitleViewHolder(view);
            }
            case VIEW_TYPE_FESTIVAL_LINEUP: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_lineup, null);
                return new LineupViewHolder(view);
            }
            case VIEW_TYPE_LETSGO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_letsgo, null);
                return new LetsgoViewHolder(view);
            }
            case VIEW_TYPE_MAPVIEW: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_map, null);
                return new MapViewHolder(view);
            }
        }
        throw new IllegalArgumentException("Invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if( position == 0 ) {
            PhotoViewHolder h = (PhotoViewHolder)holder;
            h.setPhoto(festival);
            return;
        }
        position--;
        if( festival.getLineups().size() > 0 ) {
            if( position == 0 ) {
                TitleViewHolder h = (TitleViewHolder)holder;
                h.setData("Line up");
                return;
            }
            position--;
            if( position < festival.getLineups().size() ) {
                LineupViewHolder h = (LineupViewHolder) holder;
                h.setText(festival.getLineups().get(position));
                return;
            }
            position -= festival.getLineups().size();
        }

        if( position == 0 ) {
            TitleViewHolder h = (TitleViewHolder)holder;
            h.setData("Lets Go");
            return;
        }
        position--;
        if( position == 0 ) {
            LetsgoViewHolder h = (LetsgoViewHolder)holder;
            h.setList(festival.getFestival_going_mem(), mListener);
            return;
        }
        position--;
        if( position == 0 ) {
            MapViewHolder h = (MapViewHolder)holder;
            h.setLocation(festival);
            return;
        }

        throw new IllegalArgumentException("Invalid Position");
    }

    @Override
    public int getItemCount() {
        if( festival == null ) {
            return 0;
        }
        int size = 1;
        if( festival.getLineups().size() > 0 ) {
            size++;
            size += festival.getLineups().size();
        }
        size += 3;

        return size;
    }
}
