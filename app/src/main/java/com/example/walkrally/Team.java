package com.example.walkrally;

import java.lang.reflect.Array;

public class Team {
    public String id;
    public String score;
    public String name;
    public String member;

    public Team(){

    }
    public Team(String id,String score, String name, String member){
        this.id = id;
        this.score = score;
        this.name = name;
        this.member = member;
    }
}
