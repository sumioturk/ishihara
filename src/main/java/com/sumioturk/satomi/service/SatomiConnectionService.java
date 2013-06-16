package com.sumioturk.satomi.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.sumioturk.satomi.R;
import com.sumioturk.satomi.activity.MainActivity;
import com.sumioturk.satomi.domain.event.Event;
import com.sumioturk.satomi.infrastructure.converter.event.EventJsonConverter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by sumioturk on 6/15/13.
 */
public class SatomiConnectionService extends Service {

    private Executor executor;

    private Executor executor2;

    private HttpURLConnection conn;

    private volatile int count = 0;

    private NotificationManager notificationManager;

    public static final int SET_LISTENER = 0;

    public static final int UNSET_LISTENER = 1;

    public static final int CONNECT_STREAM = 2;

    private List<Messenger> serviceListeners = new ArrayList<Messenger>();

    private Servicehandler servicehandler;

    private Messenger serviceMessenger;

    private EventJsonConverter converter;

    public class Servicehandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SET_LISTENER:
                    Log.e("SatomiConnectionService", "Set Listener");
                    serviceListeners.add(msg.replyTo);
                    break;
                case UNSET_LISTENER:
                    Log.e("SatomiConnectionService", "Unset Listener");
                    serviceListeners.remove(msg.replyTo);
                    break;
                case CONNECT_STREAM:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        converter = new EventJsonConverter();

        executor = Executors.newSingleThreadExecutor();
        executor2 = Executors.newSingleThreadExecutor();

        Runnable connectRunnable = new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                try {
                    conn = ((HttpURLConnection) new URL("http://sashimiquality.com:9000/stream/connect/5176bdfae4b0e56350837a3c?key=secret").openConnection());
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(Integer.MAX_VALUE);
                    conn.connect();
                    Log.e("SatomiConnectionService", "connected!");
                    Message rmessage = Message.obtain(null, MainActivity.SERVICE_RECONNECT);
                    for(Messenger messenger: serviceListeners){
                        messenger.send(rmessage);
                    }
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        Log.e("SatomiConnectionService", line);
                        List<Event> events = converter.fromJsonArray(new JSONArray(line));
                        for (Event event : events) {
                            Bundle data = new Bundle();
                            Message message = Message.obtain(null, MainActivity.SERVICE_EVENT);
                            data.putSerializable("event", event);
                            message.setData(data);
                            if (serviceListeners.size() == 0) {
                                Log.e("SatomiConnectionService", "Notification has fired!");
                                Notification noti = new Notification.Builder(getBaseContext())
                                        .setContentTitle("Message from " + event.getInvokerId())
                                        .setContentText(((Event<com.sumioturk.satomi.domain.message.Message>)event).getBody().getText())
                                        .setSmallIcon(R.drawable.ic_launcher)
                                        .setOngoing(false)
                                        .setTicker("satomi: you've got a message.")
                                        .setContentIntent(
                                                PendingIntent.getActivity(
                                                        getApplicationContext(),
                                                        0,
                                                        new Intent(getBaseContext(), MainActivity.class),
                                                        PendingIntent.FLAG_UPDATE_CURRENT))
                                        .build();
                                noti.flags |= Notification.FLAG_SHOW_LIGHTS;
                                noti.defaults |= Notification.DEFAULT_SOUND;
                                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                                noti.defaults |= Notification.DEFAULT_VIBRATE;
                                notificationManager.notify(++count, noti);
                            }
                            for (Messenger messenger : serviceListeners) {
                                Log.e("SatomiConnectionService", String.format("message fired! %d", serviceListeners.size()));
                                messenger.send(message);
                            }
                        }
                    }
                }catch (IOException e){
                    Log.e("HTTP CONNECTION", "LOST! RECONNECTING!!!!! :(");
                    executor.execute(this);
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("HTTP CONNECTION", "LOST!!!! UNRECOVERABLE :P");
                    e.printStackTrace();
                }

            }
        };

        executor.execute(connectRunnable);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        servicehandler = new Servicehandler();
        serviceMessenger = new Messenger(servicehandler);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("Service", "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Service", "onBind");
        return serviceMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Service", "onUnbind");
        return super.onUnbind(intent);
    }
}
