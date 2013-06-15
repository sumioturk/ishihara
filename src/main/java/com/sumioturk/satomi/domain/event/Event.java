package com.sumioturk.satomi.domain.event;

import java.io.Serializable;

/**
 * Event Object
 *
 * @param <T> {@link EventBodyType} a type of an event
 */
public class Event<T> implements Serializable {

    private String id;

    private Long createTime;

    private Long broadcastTime;

    private String invokerId;

    private String toChannelId;

    private EventBodyType bodyType;

    private T body;


    /**
     * Instantiate Event object
     *
     * @param id            identity of the event
     * @param createTime    unix time of event creation
     * @param broadcastTime unix time of event broadcast
     * @param invokerId     event invoker identity
     * @param toChannelId   event distination channel id
     * @param bodyType      event body type
     * @param body          event body
     */
    public Event(String id, Long createTime, Long broadcastTime, String invokerId, String toChannelId, EventBodyType bodyType, T body) {
        this.id = id;
        this.createTime = createTime;
        this.broadcastTime = broadcastTime;
        this.invokerId = invokerId;
        this.toChannelId = toChannelId;
        this.bodyType = bodyType;
        this.body = body;
    }


    public enum EventBodyType {

        Message("message");

        private String code;

        EventBodyType(String code) {
            this.code = code;
        }

        public static EventBodyType resolve(String string) {
            if (string == null) {
                throw new IllegalArgumentException();
            }

            for (EventBodyType type : EventBodyType.values()) {
                if (type.getCode().equals(string)) {
                    return type;
                }
            }
            return null;
        }

        public String getCode() {
            return this.code;
        }

    }


    public String getId() {
        return id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Long getBroadcastTime() {
        return broadcastTime;
    }

    public String getInvokerId() {
        return invokerId;
    }

    public String getToChannelId() {
        return toChannelId;
    }

    public EventBodyType getBodyType() {
        return bodyType;
    }

    public T getBody() {
        return body;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setBroadcastTime(Long broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    public void setInvokerId(String invokerId) {
        this.invokerId = invokerId;
    }

    public void setToChannelId(String toChannelId) {
        this.toChannelId = toChannelId;
    }

    public void setBodyType(EventBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public void setBody(T body) {
        this.body = body;
    }


}
