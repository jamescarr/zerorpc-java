package org.zerorpc;

import org.msgpack.annotation.Message;

/**
 * Created by jamescarr on 3/3/14.
 */
@Message
public class Person {
    private int id;
    private String name;
    private String email;
    private String twitter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
