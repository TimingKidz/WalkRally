package com.example.walkrally;

import java.lang.reflect.Array;

public class Team {
    public String id;
    public String score;
    public String name;
    public Member member;
    public Team(){}
    public Team(String id, String score, String name) {
        this.id = id;
        this.score = score;
        this.name = name;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
