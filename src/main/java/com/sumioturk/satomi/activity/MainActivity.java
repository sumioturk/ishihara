package com.sumioturk.satomi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.sumioturk.satomi.domain.channel.Channel;

import java.util.List;

/**
 * Created by sumioturk on 6/15/13.
 */
public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {

        super.onStart();

        List<Channel> channels;

        new ChannelAsyncTask(this, new ChannelAsyncTask.AsyncTaskCallback() {
            @Override
            public void onSuccess(List<Channel> ch) {
                Log.e("MainActivity", String.format("%s and %s - %d", ch.get(0).getName(), ch.get(1).getName(), ch.size()));
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("MainActivity", "Something Nasty Happened.");
            }
        }).execute();





    }
}