package com.festival.tacademy.festivalmate.GCM;

/**
 * Created by Tacademy on 2016-05-30.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.festival.tacademy.festivalmate.Data.RequestNewChatResult;
import com.festival.tacademy.festivalmate.Data.User;
import com.festival.tacademy.festivalmate.MainActivity;
import com.festival.tacademy.festivalmate.Manager.NetworkManager;
import com.festival.tacademy.festivalmate.Manager.PropertyManager;
import com.festival.tacademy.festivalmate.MateTalk.ChattingActivity;
import com.festival.tacademy.festivalmate.MyApplication;
import com.festival.tacademy.festivalmate.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by saltfactory on 6/8/15.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Request;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    public static final String ACTION_CHAT = "com.festival.tacademy.festivalmate.action.chat";
    public static final String EXTRA_SENDER_ID = "senderid";
    public static final String EXTRA_RESULT = "result";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]

    @Override
    public void onMessageReceived(String from, Bundle data) {

//        String type = data.getString("type");
//        String senderid = data.getString("sender");
         String message = data.getString("chat_content");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "data: " + data);

        //Log.d(TAG, "ChatMessage: " + message);


//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        } else {
//            // normal downstream message.
//            if (type.equals("chat")) {
//                long serverid = Long.parseLong(senderid);
//                String lastDate = DataManager.getInstance().getLastDate(serverid);
//                try {
//                    MyResult<List<ChatMessage>> result = NetworkManager.getInstance().getMessageSync(lastDate);
//                    String notiMessage = null;
//                    User u = null;
//                    for (ChatMessage m : result.result) {
//                        long id = DataManager.getInstance().getUserTableId(m.sender);
//                        DataManager.getInstance().addChatMessage(id, DataConstant.ChatTable.TYPE_RECEIVE, m.message, m.date);
//                        notiMessage = m.sender.userName + ":" + m.message;
//                        u = m.sender;
//                    }
//                    Intent intent = new Intent(ACTION_CHAT);
//                    intent.putExtra(EXTRA_SENDER_ID, serverid);
//                    LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent);
//                    boolean isProcessed = intent.getBooleanExtra(EXTRA_RESULT, false);
//                    if (!isProcessed) {
//                        sendNotification(notiMessage, u);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        final String[] message1 = new String[1];

                if (from.startsWith("/topics/")) {
            // message received from some topic.
                 } else {
            // normal downstream message.
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.KOREA);

                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                   String date = sdf.format(new Date(0));
                    NetworkManager.getInstance().request_new_chat(MyApplication.getContext(), PropertyManager.getInstance().getNo(), 1, date, new NetworkManager.OnResultListener<RequestNewChatResult>() {
                        @Override
                        public void onSuccess(Request request, RequestNewChatResult result) {
                            message1[0] = result.result.get(0).chat_content;
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {

                        }
                    });
                  }

       sendNotification(message1[0]);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */

    private void sendNotification(String message) {
        Intent intent = new Intent(this, ChattingActivity.class);
        //intent.putExtra(ChattingActivity.EXTRA_USER, user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setTicker("GCM message")
                .setSmallIcon(R.drawable.festival_icon)
                .setContentTitle("메시지가 도착했습니다.")
                .setContentText("")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}