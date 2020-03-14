package com.example.walkrally;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String id;
    public User(){
    }
    public User(String id,String email) {
        this.id = id;
        this.email = email;

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", id);
        result.put("email", email);


        return result;
    }
}
