package com.festival.tacademy.festivalmate.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.festival.tacademy.festivalmate.Data.Artist;
import com.festival.tacademy.festivalmate.Data.MySignInResult;
import com.festival.tacademy.festivalmate.Data.MySignUpResult;
import com.festival.tacademy.festivalmate.Data.ShowArtistSurveyResult;
import com.festival.tacademy.festivalmate.MyApplication;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Tacademy on 2016-05-23.
 */

public class NetworkManager {

    private static NetworkManager instance;
    public static NetworkManager getInstance(){
        if(instance == null){
            instance = new NetworkManager();
        }
        return instance;
    }
    private static final int DEFAULT_CACHE_SIZE = 50*1024*1024;
    private static final String DEFAULT_CACHE_DIR="miniapp";
    OkHttpClient mClient;

    private NetworkManager(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Context context = MyApplication.getContext();
        CookieManager cookieManager = new CookieManager();
        builder.cookieJar(new JavaNetCookieJar(cookieManager)); //메모리 저장하는 쿠키

        File dir = new File(context.getExternalCacheDir(),DEFAULT_CACHE_DIR);
        if(!dir.exists()){
            dir.mkdir();
        }

        builder.cache(new Cache(dir, DEFAULT_CACHE_SIZE));

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        mClient = builder.build(); // 외장메모리 저장하는 캐시

    }

    public interface OnResultListener<T>{
        public  void onSuccess(Request request, T result);
        public void onFail(Request request, IOException exception);
    }

    private static final  int MESSAGE_SUCCESS= 1;
    private static final int MESSAGE_FAIL = 0;

    class NetworkHandler extends Handler {
        public NetworkHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NetworkResult result  = (NetworkResult)msg.obj;

            switch (msg.what){
                case  MESSAGE_SUCCESS:
                    result.listener.onSuccess(result.request, result.result);
                    break;

                case MESSAGE_FAIL:
                    result.listener.onFail(result.request, result.exception);
                    break;
            }
        }
    }

    NetworkHandler mHandler = new NetworkHandler(Looper.getMainLooper());
    static class NetworkResult<T>{
        Request request;
        OnResultListener<T> listener;
        IOException exception;
        T result;

    }

    Gson gson = new Gson();

    private static final String MY_SERVER = "http://52.79.159.248:3000";  //회원가입
    private static final String URL_SIGN_UP = MY_SERVER + "/signup";
    public Request signup(Object tag, String mem_name,
                          String mem_id,
                          String mem_pwd,
                          String mem_img,
                          OnResultListener<MySignUpResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_name", mem_name)
                .add("mem_pwd", mem_pwd)
                .add("mem_id", mem_id)
                .add("mem_img",mem_img)
                .build();

        Request request = new Request.Builder()
                .url(URL_SIGN_UP)
                .post(body)
                .build();

        final NetworkResult<MySignUpResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.exception = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    MySignUpResult data = gson.fromJson(text, MySignUpResult.class);
                    if (data.success == 1) {
                        result.result = data;
                        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                    } else {
                        result.exception = new IOException(data.message);
                        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                    }

                } else {
                    result.exception = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }

    private static final String URL_SIGN_IN = MY_SERVER + "/login"; // 로그인
    public Request signin(Object tag,
                          String mem_id,
                          String mem_pwd,
                          OnResultListener<MySignInResult> listener) {
        RequestBody body = new FormBody.Builder()
                .add("mem_id", mem_id)
                .add("mem_pwd", mem_pwd)
                .build();

        Request request = new Request.Builder()
                .url(URL_SIGN_IN)
                .post(body)
                .build();

        final NetworkResult<MySignInResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.exception = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    MySignInResult data = gson.fromJson(text, MySignInResult.class);
                    if (data.success == 1) {
                        result.result = data;
                        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                    } else {
                        result.exception = new IOException(data.message);
                        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                    }

                } else {
                    result.exception = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }



    private static final String URL_SHOW_ARTIST_SURVEY = MY_SERVER + "/show_artist_survey"; // 선호가수 조사
    public Request showArtistSurvey(Object tag,
                          int mem_no,
                          OnResultListener<ShowArtistSurveyResult> listener) {
        RequestBody body = new FormBody.Builder()
                .add("mem_no", mem_no+"")
                .build();

        Request request = new Request.Builder()
                .url(URL_SHOW_ARTIST_SURVEY)
                .post(body)
                .build();

        final NetworkResult<ShowArtistSurveyResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.exception = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    ShowArtistSurveyResult data = gson.fromJson(text, ShowArtistSurveyResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));

                } else {
                    result.exception = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }


    private static final String URL_SEARCH_ARTIST_SURVEY = MY_SERVER + "/search_artist_survey"; // 선호가수 조사
    public Request searchArtistSurvey(Object tag,
                                    int mem_no,
                                      String artist_name,
                                    OnResultListener<ShowArtistSurveyResult> listener) {
        RequestBody body = new FormBody.Builder()
                .add("mem_no", mem_no+"")
                .build();

        Request request = new Request.Builder()
                .url(URL_SEARCH_ARTIST_SURVEY)
                .post(body)
                .build();

        final NetworkResult<ShowArtistSurveyResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.exception = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    ShowArtistSurveyResult data = gson.fromJson(text, ShowArtistSurveyResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));

                } else {
                    result.exception = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }

    private static final String URL_SAVE_ARTIST_SURVEY = MY_SERVER + "/save_artist_survey"; // 선호가수 조사
    public Request saveArtistSurvey(Object tag,
                                      int mem_no,
                                      List<Artist> artist_no,
                                      OnResultListener<ShowArtistSurveyResult> listener) {

        RequestBody body = new FormBody.Builder()
                .add("mem_no", mem_no+"")
                .add("artist_no[0]",artist_no.get(0).getArtist_no()+"")
                .build();


        Request request = new Request.Builder()
                .url(URL_SAVE_ARTIST_SURVEY)
                .post(body)
                .build();

        final NetworkResult<ShowArtistSurveyResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.exception = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    ShowArtistSurveyResult data = gson.fromJson(text, ShowArtistSurveyResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));

                } else {
                    result.exception = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }
}
