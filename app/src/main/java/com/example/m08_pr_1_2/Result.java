package com.example.m08_pr_1_2;

public class Result {
    public int tries;
    public String nick;
    public int time;

    public Result(String nick, int tries, int ms){
        this.tries = tries;
        this.nick = nick;
        this.time = ms;
    }
}
