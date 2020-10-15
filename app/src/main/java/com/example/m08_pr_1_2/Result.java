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
    public String[] getData (){
        String items[] = new String[3];
        items[0] = this.nick;
        items[1] = String.valueOf(this.tries);
        items[2] = String.valueOf(this.time);
        return items;
    }
}
