package com.example.walkrally;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String id;
    public String team;
    public String score;
    public String name;

    public User(){

    }
    public User(String id,String email,String name,String team,String score){
        this.id = id;
        this.email = email;
        this.name = name;
        this.team = team;
        this.score = score ;
    }
    public User(String id,String email){
        this.id = id;
        this.email = email;
        this.name = "NULL";
        this.team = "NULL";
        this.score = "0";
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", id);
        result.put("email", email);


        return result;
    }
}
