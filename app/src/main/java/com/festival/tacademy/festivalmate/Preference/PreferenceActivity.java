package com.festival.tacademy.festivalmate.Preference;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.festival.tacademy.festivalmate.Data.Artist;
import com.festival.tacademy.festivalmate.Data.PreferenceArtist;
import com.festival.tacademy.festivalmate.Data.ShowArtistSurveyResult;
import com.festival.tacademy.festivalmate.HomeActivity;
import com.festival.tacademy.festivalmate.Manager.NetworkManager;
import com.festival.tacademy.festivalmate.Manager.PropertyManager;
import com.festival.tacademy.festivalmate.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class PreferenceActivity extends AppCompatActivity {

    RecyclerView listView;
    PreferenceAdapter mAdapter;
    Toolbar toolbar;
    EditText editSearch;
    List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        mAdapter = new PreferenceAdapter();
        artistList = new ArrayList<Artist>();
        mAdapter.setOnItemClickListener(new PreferenceViewHolder.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Artist artist) {

                if(artist.isCheck()==1){
                    artistList.add(artist);
                }

                else if(artist.isCheck()==0){
                    artistList.remove(artist);
                }

                if(artistList.size()>=10){
                    item.setTitle("완료");
                }
//                Toast.makeText(PreferenceActivity.this, artist.isCheck() + artist.getName(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(PreferenceActivity.this, artistList.size()+"", Toast.LENGTH_SHORT).show();
            }
        });

        listView = (RecyclerView)findViewById(R.id.rv_list);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new GridLayoutManager(this,3));

        editSearch= (EditText)findViewById(R.id.edit_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        setData();

        Button btn = (Button)findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() { // 검색
            @Override
            public void onClick(View v) {
                String name =editSearch.getText().toString();
                int memNo = PropertyManager.getInstance().getNo();
                if(!TextUtils.isEmpty(name)){
                    NetworkManager.getInstance().searchArtistSurvey(PreferenceActivity.this, memNo, name, new NetworkManager.OnResultListener<ShowArtistSurveyResult>() {
                        @Override
                        public void onSuccess(Request request, ShowArtistSurveyResult result) {
                            Toast.makeText(PreferenceActivity.this,"성공",Toast.LENGTH_SHORT).show();
                            mAdapter.clear();
                            mAdapter.addAll(result.result);

                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                            Toast.makeText(PreferenceActivity.this,"실패"+exception.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prefer, menu);
         item = menu.findItem(R.id.complete);
      //  item.setTitle(getResources().getString(R.string.complete));
        return true;
    }

    private void setData() {
        int memNo = PropertyManager.getInstance().getNo();

        NetworkManager.getInstance().showArtistSurvey(PreferenceActivity.this, memNo, new NetworkManager.OnResultListener<ShowArtistSurveyResult>() {
            @Override
            public void onSuccess(Request request, ShowArtistSurveyResult result) {
                Toast.makeText(PreferenceActivity.this,"성공",Toast.LENGTH_SHORT).show();
                mAdapter.clear();
                mAdapter.addAll(result.result);
            }

            @Override
            public void onFail(Request request, IOException exception) {

                Toast.makeText(PreferenceActivity.this,"실패"+exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

//           for (int i = 0; i < 20; i++) {
//                Artist artist = new Artist("name " + i,"http://sitehomebos.kocca.kr/knowledge/abroad/deep/__icsFiles/artimage/2012/03/26/2_1.jpg");
//               artist.setName("name " + i);
//            //  artist.setImage(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
//               artist.setPhoto("http://sitehomebos.kocca.kr/knowledge/abroad/deep/__icsFiles/artimage/2012/03/26/2_1.jpg");
//             mAdapter.add(artist); // 값 추가
//         }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.complete) {
            int memNo = PropertyManager.getInstance().getNo();
            NetworkManager.getInstance().saveArtistSurvey(PreferenceActivity.this, memNo, artistList, new NetworkManager.OnResultListener<ShowArtistSurveyResult>() {
                @Override
                public void onSuccess(Request request, ShowArtistSurveyResult result) {
                    Intent intent = new Intent(PreferenceActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFail(Request request, IOException exception) {

                    Toast.makeText(PreferenceActivity.this,"실패"+exception.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
