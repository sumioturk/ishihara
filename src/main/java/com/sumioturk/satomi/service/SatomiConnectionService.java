package com.sumioturk.satomi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.sumioturk.satomi.domain.event.Event;
import com.sumioturk.satomi.infrastructure.converter.event.EventJsonConverter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by sumioturk on 6/15/13.
 */
public class SatomiConnectionService extends Service {

    private Executor executor;

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
                    serviceListeners.add(msg.replyTo);
                    break;
                case UNSET_LISTENER:
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

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("SatomiConnectionService", "Streaming started!");
                    HttpURLConnection conn = ((HttpURLConnection) new URL("http://sashimiquality.com:9000/stream/connect/5176bdfae4b0e56350837a3c?key=secret").openConnection());
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(Integer.MAX_VALUE);
                    conn.setReadTimeout(Integer.MAX_VALUE);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = reader.readLine()) != null){
                        Log.e("SatomiConnectionService", line);
                        List<Event> events = converter.fromJsonArray(new JSONArray(line));
                        for(Event event: events){
                            Bundle data = new Bundle();
                            data.putSerializable("event", event);
                            Message message = new Message();
                            message.setData(data);
                            for(Messenger messenger: serviceListeners){
                                messenger.send(message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        Toast.makeText(this, "Service created", Toast.LENGTH_SHORT).show();

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
