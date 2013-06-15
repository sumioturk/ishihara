package com.sumioturk.satomi.infrastructure.converter.channel;

import com.sumioturk.satomi.domain.channel.Channel;
import com.sumioturk.satomi.domain.user.User;
import com.sumioturk.satomi.infrastructure.converter.JsonConverter;
import com.sumioturk.satomi.infrastructure.converter.user.UserJsonConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Json converter for {@link Channel} object
 */
public class ChannelJsonConverter implements JsonConverter<Channel> {

    private UserJsonConverter userConverter = new UserJsonConverter();

    @Override
    public JSONObject toJson(Channel obj) {
        if (obj == null) {
            return null;
        }
        try {
            JSONObject json = new JSONObject();
            json.put("id", obj.getId());
            json.put("name", obj.getName());
            JSONArray jarray = new JSONArray();
            for (User user : obj.getUsers()) {
                jarray.put(userConverter.toJson(user));
            }
            json.put("users", jarray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Channel fromJson(JSONObject json) {
        try {
            String name = (String) json.get("name");
            String id = (String) json.get("id");
            List<User> users = new ArrayList<User>();
            JSONArray jarray = json.getJSONArray("users");
            for (int i = 0; i < jarray.length(); i++) {
                users.add(userConverter.fromJson(jarray.getJSONObject(i)));
            }
            return new Channel(id, name, users);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJsonString(Channel obj) {
        return toJson(obj).toString();
    }

    @Override
    public Channel fromJsonString(String string) {
        try {
            return fromJson(new JSONObject(string));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray toJsonArray(List<Channel> obj) {
        JSONArray jsonArray = new JSONArray();
        for(Channel channel: obj){
            jsonArray.put(toJson(channel));
        }
        return jsonArray;
    }

    @Override
    public List<Channel> fromJsonArray(JSONArray jsonArray) {
        try {
            List<Channel> channels = new ArrayList<Channel>();
            for (int i = 0; i < jsonArray.length(); i++) {
                channels.add(fromJson(jsonArray.getJSONObject(i)));
            }
            return channels;
        } catch (Exception e) {
            return null;
        }
    }
}
