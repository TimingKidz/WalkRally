package com.example.walkrally;

import java.lang.reflect.Array;

public class Team {
    public String id;
    public String score;
    public String name;
    public String mcount;
    public String checkpoint;
    public String event;
    public Team(){}
    public Team(String id, String score, String name, String mcount, String checkpoint, String event) {
        this.id = id;
        this.score = score;
        this.name = name;
        this.mcount = mcount;
        this.checkpoint = checkpoint;
        this.event = event;
    }


}
