package com.example.walkrally;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String id;
    public String age;
    public String telephone;
    public String name;
    public String img;

    public User(){

    }
    public User(String id,String email,String name,String telephone,String age,String img){
        this.id = id;
        this.email = email;
        this.name = name;
        this.telephone = telephone;
        this.age = age ;
        this.img = img;
    }
    public User(String id,String email){
        this.id = id;
        this.email = email;
        this.name = "";
        this.telephone = "";
        this.age = "0";
        this.img = "";

    }
    public User(String name, String age, String telephone){
        this.name = name;
        this.age = age;
        this.telephone = telephone;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", id);
        result.put("email", email);


        return result;
    }
}
