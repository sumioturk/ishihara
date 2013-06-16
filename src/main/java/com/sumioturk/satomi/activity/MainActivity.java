package com.sumioturk.satomi.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumioturk.satomi.ChannelDetailFragment;
import com.sumioturk.satomi.R;
import com.sumioturk.satomi.domain.AsyncRepository;
import com.sumioturk.satomi.domain.event.Event;
import com.sumioturk.satomi.domain.message.MessageRepository;
import com.sumioturk.satomi.service.SatomiConnectionService;
import com.sumioturk.satomi.ui.ConversationAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumioturk on 6/15/13.
 */
public class MainActivity extends Activity implements ServiceConnection {

    private ListView listView;

    private ConversationAdapter adapter;

    private MessageHandler messageHandler;

    private Messenger messenger;

    private Messenger serviceMessenger;

    private ImageButton send;

    private EditText textToSend;


    private ServiceConnection serviceConnection;

    public class MessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Event<com.sumioturk.satomi.domain.message.Message> event = ((Event<com.sumioturk.satomi.domain.message.Message>) msg.getData().getSerializable("event"));
            adapter.insert(event, 0);
            super.handleMessage(msg);
        }
    }

    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);

        textToSend = (EditText) findViewById(R.id.edit_text);
        send = (ImageButton) findViewById(R.id.send);
        send.setBackgroundResource(R.drawable.aoki);


        textToSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                textView.setTextColor(Color.BLACK);
                return true;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textToSend.getText().length() != 0) {
                    try {
                        String text = URLEncoder.encode(textToSend.getText().toString(), "UTF-8");
                        new MessageRepository().store(text, new AsyncRepository.RepositoryAsyncCallback<String>() {
                            @Override
                            public void onEntity(String entity) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textToSend.setTextColor(Color.BLACK);
                                        textToSend.setText("");
                                    }
                                });
                            }

                            @Override
                            public void onError(Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textToSend.setText("failed to send");
                                        textToSend.setTextColor(Color.RED);
                                    }
                                });

                            }
                        });

                    } catch (Exception e) {
                        textToSend.setText("failed to send");
                        textToSend.setTextColor(Color.RED);
                    }
                }
            }
        });

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        adapter = new ConversationAdapter(this, new Handler());
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setDivider(null);

        super.onCreate(savedInstanceState);
        messageHandler = new MessageHandler();
        messenger = new Messenger(messageHandler);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (serviceMessenger != null) {
            Message message = Message.obtain(null, SatomiConnectionService.SET_LISTENER);
            message.replyTo = messenger;
            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (serviceMessenger != null) {
            Message message = Message.obtain(null, SatomiConnectionService.UNSET_LISTENER);
            message.replyTo = messenger;
            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
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


        serviceMessenger = new Messenger(iBinder);

        Message message = Message.obtain(null, SatomiConnectionService.SET_LISTENER);
        message.replyTo = messenger;

        try {

            serviceMessenger.send(message);
            Toast.makeText(this, "Connected to StreamService", Toast.LENGTH_LONG);

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