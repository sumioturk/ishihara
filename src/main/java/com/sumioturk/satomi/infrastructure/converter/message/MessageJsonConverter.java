package com.sumioturk.satomi.infrastructure.converter.message;

import com.sumioturk.satomi.domain.message.Message;
import com.sumioturk.satomi.infrastructure.converter.JsonConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumioturk on 6/15/13.
 */
public class MessageJsonConverter implements JsonConverter<Message> {

    @Override
    public JSONObject toJson(Message obj) {
        try {
            JSONObject json = new JSONObject();
            json.put("text", obj.getText());
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message fromJson(JSONObject json) {
        try {
            String text = (String) json.get("text");
            return new Message(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJsonString(Message obj) {
        return toJson(obj).toString();
    }

    @Override
    public Message fromJsonString(String string) {
        try {
            return fromJson(new JSONObject(string));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray toJsonArray(List<Message> obj) {
        JSONArray jsonArray = new JSONArray();
        for (Message message : obj) {
            jsonArray.put(toJson(message));
        }
        return jsonArray;
    }

    @Override
    public List<Message> fromJsonArray(JSONArray jsonArray) {
        try {
            List<Message> messages = new ArrayList<Message>();
            for (int i =0; i < jsonArray.length(); i++){
                messages.add(fromJson(jsonArray.getJSONObject(i)));
            }
            return messages;
        } catch (Exception e) {
            return null;
        }
    }
}
