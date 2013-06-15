package com.sumioturk.satomi.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sumioturk.satomi.ChannelDetailFragment;
import com.sumioturk.satomi.R;
import com.sumioturk.satomi.domain.event.Event;
import com.sumioturk.satomi.service.SatomiConnectionService;

/**
 * Created by sumioturk on 6/15/13.
 */
public class MainActivity extends Activity implements ServiceConnection {

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private MessageHandler messageHandler;

    private Messenger messenger;

    private ServiceConnection serviceConnection;

    public class MessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            String text = ((Event<com.sumioturk.satomi.domain.message.Message>) msg.getData().getSerializable("event")).getCreateTime().toString();
            adapter.insert(text, 0);
            super.handleMessage(msg);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        super.onCreate(savedInstanceState);
        messageHandler = new MessageHandler();
        messenger = new Messenger(messageHandler);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, SatomiConnectionService.class), this, Context.BIND_AUTO_CREATE);
        //startService(new Intent(this, SatomiConnectionService.class));
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        if (iBinder == null) {
            return;
        }

        Toast.makeText(this, "Connecting to Service", Toast.LENGTH_LONG);

        Messenger serviceMessenger = new Messenger(iBinder);

        Message message = Message.obtain(null, SatomiConnectionService.SET_LISTENER);
        message.replyTo = messenger;

        try {

            serviceMessenger.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

        Toast.makeText(this, "Disconnected from the Service", Toast.LENGTH_LONG);

        messenger = null;

    }
}