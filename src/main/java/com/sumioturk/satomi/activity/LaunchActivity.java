package com.sumioturk.satomi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sumioturk.satomi.R;
import com.sumioturk.satomi.domain.AsyncRepository;
import com.sumioturk.satomi.domain.user.User;
import com.sumioturk.satomi.domain.user.UserRepository;
import com.sumioturk.satomi.ui.UserAdapter;

import java.util.List;

public class LaunchActivity extends Activity {

    private ListView listView;

    private UserAdapter adapter;

    private UserRepository repo;

    private boolean isUserListLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        listView = (ListView) findViewById(R.id.user_list_view);
        adapter = new UserAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               User user = adapter.getItem(i);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("user", user.getId());
                startActivity(intent);
                finish();
            }
        });
        repo = new UserRepository();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isUserListLoaded) {
            repo.resolveAll(new AsyncRepository.RepositoryAsyncCallback<List<User>>() {
                @Override
                public void onEntity(List<User> entity) {
                    isUserListLoaded = true;
                    adapter.addAll(entity);
                }

                @Override
                public void onError(Exception e) {
                    isUserListLoaded = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Unable to fetch user list", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launch, menu);
        return true;
    }

}
