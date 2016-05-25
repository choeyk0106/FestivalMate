package com.festival.tacademy.festivalmate.FestivalInfo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.festival.tacademy.festivalmate.Data.Festival;
import com.festival.tacademy.festivalmate.Data.FestivalDetailResult;
import com.festival.tacademy.festivalmate.Manager.NetworkManager;
import com.festival.tacademy.festivalmate.Manager.PropertyManager;
import com.festival.tacademy.festivalmate.MateMatching.MateMatchingStartActivity;
import com.festival.tacademy.festivalmate.R;

import java.io.IOException;

import okhttp3.Request;

public class FestivalDetailActivity extends AppCompatActivity {

    TextView toolbarTitle;
    Toolbar toolbar;
    RecyclerView listView;
    FestivalDetailAdapter mAdapter;
    LinearLayoutManager mManager;
    Festival festival;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_detail);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        //  fab = (FloatingActionButton)findViewById(R.id.btn_matching_start);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (RecyclerView)findViewById(R.id.rv_list);
        mAdapter = new FestivalDetailAdapter();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(mManager);

        Intent intent = getIntent();
        festival = (Festival)intent.getExtras().getSerializable("festival");
        toolbarTitle.setText(festival.getFestival_name());

        setData();

        Button btn = (Button)findViewById(R.id.btn_matching);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FestivalDetailActivity.this, MateMatchingStartActivity.class);
                intent.putExtra("festival", festival);
                startActivity(intent);
            }
        });
        Toast.makeText(FestivalDetailActivity.this, festival.getFestival_name() + "+ " + festival.getDate() + "+ " + festival.getFestival_location(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData(){

        mAdapter.setFestval(festival);
//        NetworkManager.getInstance().show_festival_detail(this, festival.getFestival_no(), 1,
//                new NetworkManager.OnResultListener<FestivalDetailResult>() {
//                    @Override
//                    public void onSuccess(Request request, FestivalDetailResult result) {
//                        if(result.success==1) {
//                            Toast.makeText(FestivalDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                            mAdapter.setFestval(result.getResult());
//                        }
//                    }
//
//                    @Override
//                    public void onFail(Request request, IOException exception) {
//                        Toast.makeText(FestivalDetailActivity.this, "Fail", Toast.LENGTH_SHORT).show();
//                    }
//                });

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
