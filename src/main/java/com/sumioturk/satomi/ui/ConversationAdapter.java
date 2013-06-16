package com.sumioturk.satomi.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.sumioturk.satomi.R;
import com.sumioturk.satomi.domain.AsyncRepository;
import com.sumioturk.satomi.domain.event.Event;
import com.sumioturk.satomi.domain.message.Message;
import com.sumioturk.satomi.domain.user.User;
import com.sumioturk.satomi.domain.user.UserRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by sumioturk on 6/16/13.
 */
public class ConversationAdapter extends ArrayAdapter<Event<Message>> {

    private LayoutInflater mInflater;

    private Handler handler;

    private Map<String, String> names = new HashMap<String, String>();

    public ConversationAdapter(Context context, Handler handler) {
        super(context, R.id.message);
        this.handler = handler;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.conversation_row, null);
        }


        Event<Message> event = getItem(i);

        final TextView name = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView msg = (TextView) view.findViewById(R.id.message);
        final TextView cap = (TextView) view.findViewById(R.id.capital_letter);

        name.setText("");
        cap.setText("");


        SimpleDateFormat df = new SimpleDateFormat("G yyyy HH:mm MMM d");
        df.setTimeZone(TimeZone.getDefault());
        date.setText(df.format(new Date(event.getBroadcastTime())));
        msg.setText(event.getBody().getText());
        date.setSelected(true);

        if(names.containsKey(event.getInvokerId())){
            name.setText(names.get(event.getInvokerId()));
            cap.setText(String.format("%c", names.get(event.getInvokerId()).charAt(0)));
            return view;
        }

        new UserRepository().resolve(event.getInvokerId(), new AsyncRepository.RepositoryAsyncCallback<User>() {
            @Override
            public void onEntity(final User entity) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(entity == null){
                            onError(new Exception());
                            return;
                        }
                        names.put(entity.getId(), entity.getName());
                        name.setText(entity.getName());
                        cap.setText(String.format("%c", entity.getName().charAt(0)));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        name.setText("unknown");
                    }
                });
            }
        });



        return view;

    }

}

