package com.sumioturk.satomi.domain.user;

/**
 * User object
 */
public class User {

    private String name;

    private String id;

    private boolean isGay;

    /**
     * Instantiate Channel object
     * @param name name of the user
     * @param id identity of the user
     * @param gay sexual orientation of the user
     */
    public User(String name, String id, boolean gay) {
        this.name = name;
        this.id = id;
        isGay = gay;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGay(boolean gay) {
        isGay = gay;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isGay() {
        return isGay;
    }
}
