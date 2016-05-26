package com.festival.tacademy.festivalmate.MateMatching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.festival.tacademy.festivalmate.Data.Artist;
import com.festival.tacademy.festivalmate.Data.Festival;
import com.festival.tacademy.festivalmate.Data.MateTalkRoom;
import com.festival.tacademy.festivalmate.Data.ShowMatchingResult;
import com.festival.tacademy.festivalmate.Data.ShowMemProfileResult;
import com.festival.tacademy.festivalmate.Data.User;
import com.festival.tacademy.festivalmate.FestivalInfo.UserViewHolder;
import com.festival.tacademy.festivalmate.Manager.NetworkManager;
import com.festival.tacademy.festivalmate.Manager.PropertyManager;
import com.festival.tacademy.festivalmate.Profile.ProfileDialogFragment;
import com.festival.tacademy.festivalmate.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MateMatchingActivity extends AppCompatActivity {

    Toolbar toolbar;
    boolean isClicked = false;
    RecyclerView listView;
    MatetalkDetailAdapter mAdapter;
    LinearLayoutManager mManager;
    boolean isClick = false;
    ShowMatchingResult result;

    List<Artist> artists = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<MateTalkRoom> chatinfoes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mate_matching);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        result = (ShowMatchingResult)intent.getExtras().getSerializable("ShowMatchingResult");

        listView = (RecyclerView)findViewById(R.id.rv_list);
        mAdapter = new MatetalkDetailAdapter();
        mAdapter.setOnItemClickListener(new MatetalkDetailViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MateTalkRoom room) {

                int memNo = PropertyManager.getInstance().getNo();
                int roomNo = room.getChatroom_no();


                NetworkManager.getInstance().show_chatroom_detail(MateMatchingActivity.this, memNo, roomNo, new NetworkManager.OnResultListener<ShowMatchingResult>() {
                    @Override
                    public void onSuccess(Request request, ShowMatchingResult result) {
                        Toast.makeText(MateMatchingActivity.this, "성공",Toast.LENGTH_SHORT).show();
                        mAdapter.clear();
                        mAdapter.addAll(result.result);

                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                        Toast.makeText(MateMatchingActivity.this, "실패"+exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                View v = view.findViewById(R.id.detail_view);

                if(isClick == false) {
                    v.setVisibility(View.VISIBLE);
                    isClick = true;
                } else {
                    v.setVisibility(View.GONE);
                    isClick = false;
                }
            }
        });

        mAdapter.setOnItemClickListener(new ChatUserViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User user) {

                NetworkManager.getInstance().show_mem_profile(MateMatchingActivity.this, user.getMem_no(), new NetworkManager.OnResultListener<ShowMemProfileResult>() {
                    @Override
                    public void onSuccess(Request request, ShowMemProfileResult result) {
                        Toast.makeText(MateMatchingActivity.this, "성공",Toast.LENGTH_SHORT).show();
                        ProfileDialogFragment f = new ProfileDialogFragment();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", result.result);
                        f.setArguments(bundle);
                        f.show(getSupportFragmentManager(), "aaaa");
                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                        Toast.makeText(MateMatchingActivity.this, "실패"+exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


//                ProfileDialogFragment f = new ProfileDialogFragment();
//
//                List<Artist> artists = new ArrayList<>();
//                List<Festival> letsgo = new ArrayList<>();
//
//                for(int i=0; i<3; i++) {
//                    letsgo.add(new Festival("Festival "+i));
//                }
//
//                for(int i=0; i<10; i++) {
//                    artists.add(new Artist("Artist: " + i, "http://sitehomebos.kocca.kr/knowledge/abroad/deep/__icsFiles/artimage/2012/03/26/2_1.jpg"));
//                }
//                User user = new User("ID " + 1, "http://sitehomebos.kocca.kr/knowledge/abroad/deep/__icsFiles/artimage/2012/03/26/2_1.jpg","Name " + 1, letsgo, artists);
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("user", user);
//                f.setArguments(bundle);
//                f.show(getSupportFragmentManager(), "aaaa");
            }
        });

        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listView.setAdapter(mAdapter);
        listView.setLayoutManager(mManager);

        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_make_talk, menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.make_talk){
            startActivity(new Intent(MateMatchingActivity.this, MakeMateTalkActivity.class));

            return true;
        }
       else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void initData() {

//        mAdapter.clear();
//        mAdapter.addAll(result.result);

        for (int i = 0; i < 10; i++) {

            Artist artist = new Artist();
            artist.setMatched_artist_name("matchedName : "+ i);
            artist.setMatched_artist_no(i);
            artists.add(artist);

            User user = new User();
            user.setMem_no(1);
            user.setChatroom_mem_name("chatName : "+ i);
            user.setChatroom_mem_img("http://sitehomebos.kocca.kr/knowledge/abroad/deep/__icsFiles/artimage/2012/03/26/2_1.jpg");
            user.setChatroom_mems_no(i);
            users.add(user);

        }

        for( int i=0; i<10; i++ ) {
            chatinfoes.add(new MateTalkRoom("모두모두 대환영 " + i,"http://sitehomebos.kocca.kr/knowledge/abroad/deep/__icsFiles/artimage/2012/03/26/2_1.jpg" , "Festival " + i, artists, 1, 1, users));
        }
        mAdapter.addAll(chatinfoes);

    }

}
