package com.sumioturk.satomi.domain.channel;

import com.sumioturk.satomi.domain.user.User;

import java.util.List;

/**
 * Channel object
 */
public class Channel {

    private String id;

    private String name;

    private List<User> users;


    /**
     * Instantiate Channel object
     * @param id identity
     * @param name name of a channel
     * @param users list of users of the channel
     */
    public Channel(String id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
