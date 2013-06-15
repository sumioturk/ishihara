package com.sumioturk.satomi.infrastructure.converter.user;

import com.sumioturk.satomi.domain.user.User;
import com.sumioturk.satomi.infrastructure.converter.JsonConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Json Converter for {@link User} object.
 */
public class UserJsonConverter implements JsonConverter<User> {

    @Override
    public JSONObject toJson(User obj) {
        JSONObject json = new JSONObject();
        try {
            json.putOpt("name", obj.getName());
            json.putOpt("id", obj.getId());
            json.putOpt("isGay", obj.isGay());
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User fromJson(JSONObject json) {
        try {
            String name = (String) json.get("name");
            String id = (String) json.get("id");
            Boolean isGay = (Boolean) json.get("isGay");
            return new User(name, id, isGay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toJsonString(User obj) {
        return toJson(obj).toString();
    }

    @Override
    public User fromJsonString(String string) {
        try {
            return fromJson(new JSONObject(string));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONArray toJsonArray(List<User> obj) {
        JSONArray jsonArray = new JSONArray();
        for (User user : obj) {
            jsonArray.put(toJson(user));
        }
        return jsonArray;
    }

    @Override
    public List<User> fromJsonArray(JSONArray jsonArray) {
        try {
            List<User> users = new ArrayList<User>();
            for (int i = 0; i < jsonArray.length(); i++) {
                users.add(fromJson(jsonArray.getJSONObject(i)));
            }
            return users;
        } catch (Exception e) {
            return null;
        }
    }
}
