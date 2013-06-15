package com.sumioturk.satomi.domain.message;

import java.io.Serializable;

/**
 * Message object
 */
public class Message implements Serializable {

    private String text;

    /**
     * Instantiate Message
     * @param text message
     */
    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
