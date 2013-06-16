package com.sumioturk.satomi.infrastructure.converter.event;

import com.sumioturk.satomi.domain.event.Event;
import com.sumioturk.satomi.domain.message.Message;
import com.sumioturk.satomi.infrastructure.converter.JsonConverter;
import com.sumioturk.satomi.infrastructure.converter.message.MessageJsonConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON converter for an object {@link Event}
 */
public class EventJsonConverter implements JsonConverter<Event> {

    @Override
    public JSONObject toJson(Event obj) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", obj.getId());
            json.put("createTime", obj.getCreateTime());
            json.put("broadcastTime", obj.getBroadcastTime());
            json.put("toChannelId", obj.getToChannelId());
            json.put("invokerId", obj.getInvokerId());
            json.put("bodyType", obj.getBodyType().getCode());

            switch (obj.getBodyType()) {
                case Message:
                    MessageJsonConverter converter = new MessageJsonConverter();
                    JSONObject jsonObject = converter.toJson((Message) obj.getBody());
                    json.put("body", jsonObject);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return json;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Event fromJson(JSONObject json) {
        try {
            String id = (String) json.get("id");
            Long createTime = (Long) json.get("createTime");
            Long broadcastTime = (Long) json.get("broadcastTime");
            String toChannelId = (String) json.get("toChannelId");
            String invokerId = (String) json.get("invokerId");
            Event.EventBodyType bodyType = Event.EventBodyType.resolve((String) json.get("bodyType"));
            switch (bodyType) {
                case Message:
                    MessageJsonConverter converter = new MessageJsonConverter();
                    Message message = converter.fromJson(json.getJSONObject("body"));
                    return new Event<Message>(id, createTime, broadcastTime, invokerId, toChannelId, bodyType, message);
                default:
                    throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJsonString(Event obj) {
        return toJson(obj).toString();
    }

    @Override
    public Event fromJsonString(String string) {
        try {
            return fromJson(new JSONObject(string));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray toJsonArray(List<Event> obj) {
        JSONArray jsonArray = new JSONArray();
        for (Event event : obj) {
            jsonArray.put(toJson(event));
        }
        return jsonArray;
    }

    @Override
    public List<Event> fromJsonArray(JSONArray jsonArray) {
        try {
            List<Event> events = new ArrayList<Event>();
            for(int i =0; i < jsonArray.length(); i++){
                events.add(fromJson(jsonArray.getJSONObject(i)));
            }
            return events;
        } catch (Exception e) {
            return null;
        }
    }
}
