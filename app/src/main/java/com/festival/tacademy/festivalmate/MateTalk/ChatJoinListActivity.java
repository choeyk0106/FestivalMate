package com.festival.tacademy.festivalmate.MateTalk;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.festival.tacademy.festivalmate.Data.ChatroomMemListResult;
import com.festival.tacademy.festivalmate.Data.MateTalkRoom;
import com.festival.tacademy.festivalmate.Data.MateTalkWaitJoinList;
import com.festival.tacademy.festivalmate.Data.chatroom_member;
import com.festival.tacademy.festivalmate.Data.chatroom_waiting;
import com.festival.tacademy.festivalmate.Manager.NetworkManager;
import com.festival.tacademy.festivalmate.Manager.PropertyManager;
import com.festival.tacademy.festivalmate.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class ChatJoinListActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarTitle;
    RecyclerView listView;
    ChatJoinListAdapter mAdapter;
    MateTalkRoom mateTalkRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_join_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mateTalkRoom = (MateTalkRoom) intent.getExtras().getSerializable("chatting");
        toolbarTitle.setText(mateTalkRoom.getChatroom_name());

        listView = (RecyclerView)findViewById(R.id.rv_list);
        mAdapter= new ChatJoinListAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    setData();
    }

    private void setData(){
        int memNo = PropertyManager.getInstance().getNo();
        int chatNo = mateTalkRoom.getChatroom_no();

        NetworkManager.getInstance().chatroom_mem_list(ChatJoinListActivity.this, memNo, chatNo, new NetworkManager.OnResultListener<ChatroomMemListResult>() {
            @Override
            public void onSuccess(Request request, ChatroomMemListResult result) {
              //  Toast.makeText(ChatJoinListActivity.this, result.result.getChatroom_waitings().get(0).getMem_name()+"", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChatJoinListActivity.this, result.result.getChatroom_members().size()+"", Toast.LENGTH_SHORT).show();

                //mAdapter.setMateTalkWaitJoinList(result.result);
            }

            @Override
            public void onFail(Request request, IOException exception) {
                Toast.makeText(ChatJoinListActivity.this, "실패"+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        MateTalkWaitJoinList list = new MateTalkWaitJoinList();
//
//        List<chatroom_waiting> waitingList = new ArrayList<>();
//        for(int i=0; i < 2; i++){
//
//            chatroom_waiting waiting = new chatroom_waiting();
//            waiting.setMem_name("waitName" + i);
//            waiting.setMem_img("http://imgnews.naver.com/image/079/2016/03/25/20160325180048791857_99_20160325180307.jpg");
//            waitingList.add(waiting);
//        }
//        list.setChatroom_waitings(waitingList);
//
//        List<chatroom_member> memberList = new ArrayList<>();
//        for(int i=0; i < 3; i++){
//
//            chatroom_member member = new chatroom_member();
//            member.setMem_name("joinName" + i);
//            member.setMem_img("http://imgnews.naver.com/image/079/2016/03/25/20160325180048791857_99_20160325180307.jpg");
//            memberList.add(member);
//        }
//
//        list.setChatroom_members(memberList);
//        list.setName(toolbarTitle.getText().toString());
//
//        mAdapter.setMateTalkWaitJoinList(list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
