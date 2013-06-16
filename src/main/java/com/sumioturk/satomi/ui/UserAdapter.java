package com.sumioturk.satomi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sumioturk.satomi.R;
import com.sumioturk.satomi.domain.user.User;

/**
 * Created by sumioturk on 6/16/13.
 */
public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user_list_row, null);
        }
        User user = getItem(position);

        TextView userName = (TextView) convertView.findViewById(R.id.user_name);
        userName.setText(user.getName());

        return convertView;
    }
}
